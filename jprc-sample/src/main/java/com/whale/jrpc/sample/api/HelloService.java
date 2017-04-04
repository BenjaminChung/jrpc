package com.whale.jrpc.sample.api;

import com.whale.jrpc.sample.model.Person;

/**
 * Created by benjaminchung on 2017/4/3.
 */
public interface HelloService {

    String hello(String name);

    String hello(Person person);
}
