package com.whale.jrpc.registry;

/**
 * 服务注册接口
 * Created by benjaminchung on 2017/4/3.
 */
public interface ServiceRegistry {

    /**
     * 注册服务的名称与地址
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName,String serviceAddress);
}
