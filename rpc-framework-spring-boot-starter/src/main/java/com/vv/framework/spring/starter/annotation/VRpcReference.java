package com.vv.framework.spring.starter.annotation;

import java.lang.annotation.*;

/**
 * @author vv
 * @Description 远程服务指向
 * @date 2023/7/28-18:01
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VRpcReference {
    //服务地址
    String url() default "";
    //服务分组
    String group() default "default";
    //服务鉴权
    String serviceToken() default "";
    //超时时间
    int timeOut() default 3000;
    //重试次数
    int retry() default 1;
    //是否异步
    boolean async() default false;

}
