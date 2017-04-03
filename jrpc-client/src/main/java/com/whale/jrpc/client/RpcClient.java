package com.whale.jrpc.client;

import codec.RpcEncoder;
import com.whale.jrpc.common.bean.RpcRequest;
import com.whale.jrpc.common.bean.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by benjaminchung on 2017/4/3.
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>{
    private final static Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    private  final String host;
    private  final int port;

    private RpcResponse response;


    public RpcClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        this.response = msg;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOGGER.error("api caught exception",cause);
        ctx.close();
    }

    /**
     * 发送请求
     * @param rpcRequest
     * @return
     * @throws InterruptedException
     */
    public RpcResponse send(RpcRequest rpcRequest) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline channelPipeline = ch.pipeline();
                    channelPipeline.addLast(new RpcEncoder(RpcRequest.class));
                    channelPipeline.addLast(new RpcEncoder(RpcResponse.class));
                    channelPipeline.addLast(RpcClient.this);
                }

            });
            bootstrap.option(ChannelOption.TCP_NODELAY,true);
            ChannelFuture future = bootstrap.connect(host,port).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(rpcRequest).sync();
            channel.closeFuture().sync();
            return response;
        }finally {
            group.shutdownGracefully();
        }
    }
}
