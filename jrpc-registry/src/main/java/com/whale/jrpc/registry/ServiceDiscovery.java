package com.whale.jrpc.registry;

/**
 * 服务发现
 * Created by benjaminchung on 2017/4/3.
 */
public interface ServiceDiscovery {
    /**
     * 根据服务名称查找服务地址
     * @param serviceName 服务名称
     * @return
     */
    String discover(String serviceName);
}
