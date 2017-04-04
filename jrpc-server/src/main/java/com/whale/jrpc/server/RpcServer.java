package com.whale.jrpc.server;

import com.whale.jrpc.common.codec.RpcDecoder;
import com.whale.jrpc.common.codec.RpcEncoder;
import com.whale.jrpc.common.bean.RpcRequest;
import com.whale.jrpc.common.bean.RpcResponse;
import com.whale.jrpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;


import java.util.HashMap;
import java.util.Map;

/**
 * RPC 服务器
 * Created by benjaminchung on 2017/4/3.
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

    public static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private String serviceAddress;

    private ServiceRegistry serviceRegistry;

    private Map<String, Object> handleMap = new HashMap<String, Object>();

    public RpcServer(String serviceAddress,ServiceRegistry serviceRegistry){
        this.serviceAddress = serviceAddress;
        this.serviceRegistry = serviceRegistry;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);

        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String serivceVersion = rpcService.version();
                if (StringUtils.hasText(serivceVersion)) {
                    serviceName += "-" + serivceVersion;
                }
                handleMap.put(serviceName,serviceBean);
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup wokerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,wokerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new RpcDecoder(RpcRequest.class));
                    pipeline.addLast(new RpcEncoder(RpcResponse.class));
                    pipeline.addLast(new RpcServerHandler(handleMap));
                }
            });
            //最大允许连接的客户端数
            serverBootstrap.option(ChannelOption.SO_BACKLOG,1024);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            //获取rpc服务器ip地址与端口号
            String[] addressArray = StringUtils.split(serviceAddress,":");
            String ip = addressArray[0];
            int port = Integer.parseInt(addressArray[1]);
            //启动
            ChannelFuture future = serverBootstrap.bind(ip,port).sync();
            if(serviceRegistry != null){
                for(String interfaceName:handleMap.keySet()){
                    serviceRegistry.register(interfaceName,serviceAddress);
                }
            }
            LOGGER.debug("server started on port {}",port);
            //关闭RPC服务器
            future.channel().closeFuture().sync();

        }finally {
            wokerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }


}
