package com.whale.jrpc.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.whale.jrpc.common.util.SerializationUtil;

/**
 * RPC 编码器
 * Created by benjaminchung on 2017/4/3.
 */
public class RpcEncoder extends MessageToByteEncoder{
    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass){
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if(genericClass.isInstance(msg)){
            byte[] data = SerializationUtil.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
