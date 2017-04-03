package com.whale.jrpc.registry.zookeeper;

import com.whale.jrpc.registry.ServiceDiscovery;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 服务发现
 * Created by benjaminchung on 2017/4/3.
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    public static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceDiscovery.class);

    private String zkAddress;

    public ZookeeperServiceDiscovery(String zkAddress){
        this.zkAddress = zkAddress;
    }

    @Override
    public String discover(String serviceName) {
        ZkClient zkClient = new ZkClient(zkAddress, Constants.ZK_SESSION_TIMEOUT, Constants.ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("conneted zookeeper");
        try {
            String servicePath = Constants.ZK_REGISTRY_PATH + Constants.SEPARATOR + serviceName;
            if (zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format(" can not find any service node on path :%s", servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if (CollectionUtils.isEmpty(addressList)) {
                throw new RuntimeException(String.format(" can not find any service node on path :%s", servicePath));
            }

            String address;
            int size = addressList.size();
            if (size == 1) {
                address = addressList.get(0);
                LOGGER.debug("get only one address node :{}", address);
            } else {
                //此处可后续做负载均衡优化
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("get ramdon  address node :{}", address);
            }
            //获取节点的值
            String addressPath = servicePath + Constants.SEPARATOR + address;
            return zkClient.readData(addressPath);


        } finally {
            zkClient.close();
        }
    }
}
