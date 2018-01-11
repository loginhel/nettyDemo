package com.hhp.netty.protocol.netty.codec;

import com.hhp.netty.protocol.netty.struct.Header;
import com.hhp.netty.protocol.netty.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hhp
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    private NettyMarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                               int lengthAdjustment, int initialBytesToStrip)throws IOException{
        super(maxFrameLength,lengthFieldOffset,lengthFieldLength,lengthAdjustment,initialBytesToStrip);
        marshallingDecoder = MarshallerCodecFactory.buildMarshallingDecoder();
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //如果还不是完整的消息
        ByteBuf frame = (ByteBuf)super.decode(ctx, in);
        if(frame==null){
            return null;
        }
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if(size>0){
            Map<String,Object> attach = new HashMap<>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for(int i=0; i< size;i++){
                keySize = frame.readInt();
                keyArray = new byte[keySize];
                frame.readBytes(keyArray);
                key = new String(keyArray,"UTF-8");
                attach.put(key,marshallingDecoder.decode(ctx,frame));
            }
            keyArray = null;
            key = null;
            header.setAttachment(attach);
        }
        if(frame.readableBytes()>0){
            message.setBody(marshallingDecoder.decode(ctx, frame));
        }
        message.setHeader(header);
        return message;
    }
}
