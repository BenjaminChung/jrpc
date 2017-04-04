package com.whale.jrpc.sample.model;

import java.io.Serializable;

/**
 * Created by benjaminchung on 2017/4/3.
 */
public class Person implements Serializable{

    private String name;
    private int  age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
