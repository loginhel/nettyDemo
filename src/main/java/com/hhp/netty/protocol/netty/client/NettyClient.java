package com.hhp.netty.protocol.netty.client;

import com.hhp.netty.protocol.netty.NettyConstant;
import com.hhp.netty.protocol.netty.codec.NettyMessageDecoder;
import com.hhp.netty.protocol.netty.codec.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author hhp
 */
public class NettyClient {

    private static final Log LOG = LogFactory.getLog(NettyClient.class);
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws Exception {
        //配置NIO线程组
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("MessageDecoder",
                                    new NettyMessageDecoder(1024 * 1024,
                                            4, 4,-8,0));
                            ch.pipeline().addLast("MessageEncoder",
                                    new NettyMessageEncoder());
                            //心跳超时机制50s关闭连接
                            ch.pipeline().addLast("readTimeoutHandler",
                                    new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("LoginAuthHandler",
                                    new LoginAuthReqHandler());
                            ch.pipeline().addLast("HeartBeatHandler",
                                    new HeartBeatReqHandler());
                        }
                    });
            //发起异步连接操作
            ChannelFuture future = b.connect(new InetSocketAddress(host, port),
                    new InetSocketAddress(NettyConstant.LOCALIP,NettyConstant.LOCAL_PORT)).sync();
            System.out.println("Netty client start ok: " + (NettyConstant.LOCALIP + ":" + NettyConstant.LOCAL_PORT));

            //future.channel().writeAndFlush(LoginAuthReqHandler.buildLoginReq());

            future.channel().closeFuture().sync();
        } finally {
            //所有资源释放完成后，清空资源，再次发起重连操作
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    try {
                        //发起重连操作
                        connect(port, host);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String... args) throws Exception {
        new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
    }
}
