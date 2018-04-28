package com.yoyoig.ioc.annotation;

import java.lang.annotation.*;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/26
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    /**
     * 自定义beanName
     * @return
     */
    String value() default "";

}
