package com.vv.framework.spring.starter.annotation;

import java.lang.annotation.*;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/28-18:01
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VRpcReference {

    String url() default "";

    String group() default "default";

    String serviceToken() default "";

    int timeOut() default 3000;

    int retry() default 1;

    boolean async() default false;

}
