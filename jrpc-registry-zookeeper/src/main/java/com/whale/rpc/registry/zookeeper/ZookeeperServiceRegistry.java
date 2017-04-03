package com.whale.rpc.registry.zookeeper;

import com.whale.jrpc.registry.ServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务注册
 * Created by benjaminchung on 2017/4/3.
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);

    private final ZkClient zkClient;

    public ZookeeperServiceRegistry(String zkAddress) {
        zkClient = new ZkClient(zkAddress, Constants.ZK_SESSION_TIMEOUT, Constants.ZK_CONNECTION_TIMEOUT);
    }


    @Override
    public void register(String serviceName, String serviceAddress) {
        String registerPath = Constants.ZK_REGISTRY_PATH;
        //根目录节点
        ZkUtil.createPath(zkClient, registerPath, CreateMode.PERSISTENT);
        LOGGER.debug("create registry node :{}", registerPath);
        //创建服务节点
        String servicePath = registerPath + Constants.SEPARATOR + serviceName;
        ZkUtil.createPath(zkClient, servicePath, CreateMode.PERSISTENT);
        LOGGER.debug("create service node :{}", registerPath);

        String addressPath = servicePath + "/address-";
        //待编号的服务列表
        String addressNode = zkClient.createEphemeralSequential(addressPath,serviceAddress);
        LOGGER.debug("create address node :{}", addressNode);

    }
}
