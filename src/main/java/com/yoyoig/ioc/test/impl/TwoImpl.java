package com.yoyoig.ioc.test.impl;

import com.yoyoig.ioc.annotation.Bean;
import com.yoyoig.ioc.annotation.Inject;
import com.yoyoig.ioc.test.Two;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/27
 */
@Bean
public class TwoImpl implements Two {

    @Inject("one")
    private OneImpl one;

    @Override
    public void say(String message) {
        System.out.println("这里是two:"+message);
    }

    public void sayOne(){
        one.say("通过beanName注入,这里是two");
    }
}