package com.whale.jrpc.sample.server;

import com.whale.jrpc.sample.api.HelloService;
import com.whale.jrpc.server.RpcService;

/**
 * Created by benjaminchung on 2017/4/3.
 */

@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
