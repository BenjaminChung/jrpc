package com.whale.jrpc.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import com.whale.jrpc.common.util.SerializationUtil;

import java.util.List;

/**
 * Created by benjaminchung on 2017/4/3.
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int datalen = in.readInt();
        if (in.readableBytes() < datalen) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[datalen];
        in.readBytes(data);
        out.add(SerializationUtil.deserialize(data,genericClass));
    }
}
