package com.whale.jrpc.sample.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by benjaminchung on 2017/4/3.
 */
public class RpcBootstrap {

    public static final Logger LOGGER = LoggerFactory.getLogger(RpcBootstrap.class);

    public static void main(String[] args) {
        LOGGER.debug(" start server");
        new ClassPathXmlApplicationContext("server-spring.xml");
    }
}
