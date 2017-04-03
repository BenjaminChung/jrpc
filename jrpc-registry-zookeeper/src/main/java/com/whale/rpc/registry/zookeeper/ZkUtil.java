package com.whale.rpc.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * zookeeper工具类
 * Created by benjaminchung on 2017/4/3.
 */
public class ZkUtil {

    public static void createPath(ZkClient zkClient, String path, CreateMode createMode){
        if(!zkClient.exists(path)){
            if(CreateMode.PERSISTENT.equals(createMode)) {
                zkClient.createPersistent(path);
            }else if(CreateMode.EPHEMERAL.equals(createMode)){
                zkClient.createEphemeral(path);
            }
        }
    }
}
