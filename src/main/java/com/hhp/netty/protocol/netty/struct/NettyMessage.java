package com.hhp.netty.protocol.netty.struct;

/**
 * @author hhp
 */
public final class NettyMessage {
    private Header header;
    private Object body;

    /**
     * 获取消息头
     * @return 消息头
     */
    public final Header getHeader(){
        return header;
    }

    public final void setHeader(Header header){
        this.header = header;
    }

    public final Object getBody(){
        return body;
    }

    public final void setBody(Object body){
        this.body = body;
    }

    @Override
    public String toString(){
        return "NettyMessage [header="+header+"]";
    }
}
