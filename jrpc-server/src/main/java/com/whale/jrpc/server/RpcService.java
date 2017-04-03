package com.whale.jrpc.server;

import org.springframework.stereotype.Component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by benjaminchung on 2017/4/3.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    /**
     * 服务接口类
     * @return
     */
    Class<?> value();

    /**
     * 服务版本号
     * @return
     */
    String version() default "";
}
