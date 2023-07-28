package com.vv.core.common.annotation;

import java.lang.annotation.*;

/**
 * @author vv
 * @Description 注解，标识前后过滤器
 * @date 2023/7/28-16:41
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SPI {

    String value() default "";
}
