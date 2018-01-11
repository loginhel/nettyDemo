package com.hhp.netty.protocol.netty.server;

import com.hhp.netty.protocol.netty.NettyConstant;
import com.hhp.netty.protocol.netty.codec.NettyMessageDecoder;
import com.hhp.netty.protocol.netty.codec.NettyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hhp
 */
public class NettyServer {

    private static final Log LOG = LogFactory.getLog(NettyServer.class);

    public void bind() throws Exception {
        //Nio线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("MessageDecoder",
                                    new NettyMessageDecoder(1024 * 1024,
                                            4, 4,-8,0));
                            ch.pipeline().addLast("MessageEncoder",
                                    new NettyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler",
                                    new ReadTimeoutHandler(50));
                            ch.pipeline().addLast(new LoginAuthRespHandler());
                            ch.pipeline().addLast(new HeartBeatRespHandler());
                        }
                    });

            //绑定端口，同步等待成功
            ChannelFuture cf = sb.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
            System.out.println("Netty server start ok: " + (NettyConstant.REMOTEIP + ":" + NettyConstant.PORT));

            //等待服务端监听端口关闭
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String... args) throws Exception {
        new NettyServer().bind();
    }
}
