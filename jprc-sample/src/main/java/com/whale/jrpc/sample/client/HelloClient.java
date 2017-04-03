package com.whale.jrpc.sample.client;

import com.whale.jrpc.client.RpcProxy;
import com.whale.jrpc.sample.api.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by benjaminchung on 2017/4/3.
 */
public class HelloClient {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("client-spring.xml");
        RpcProxy rpcProxy = applicationContext.getBean(RpcProxy.class);


        HelloService helloService = rpcProxy.create(HelloService.class);

        String result = helloService.hello("benjamin");

        System.out.println(result);


    }
}
