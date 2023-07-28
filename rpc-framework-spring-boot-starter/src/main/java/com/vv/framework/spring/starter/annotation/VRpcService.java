package com.vv.framework.spring.starter.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/28-18:02
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface VRpcService {

    int limit() default 0;

    String group() default "default";

    String serviceToken() default "";

}