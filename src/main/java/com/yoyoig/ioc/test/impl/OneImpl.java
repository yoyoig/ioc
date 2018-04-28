package com.yoyoig.ioc.test.impl;

import com.yoyoig.ioc.annotation.Bean;
import com.yoyoig.ioc.annotation.Inject;
import com.yoyoig.ioc.annotation.Value;
import com.yoyoig.ioc.test.One;
import com.yoyoig.ioc.test.Three;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/27
 */
@Bean("one")
public class OneImpl implements One {

    @Inject
    private Three three;

    @Value("{test")
    private String test;

    @Override
    public void say(String message) {
        System.out.println("这里是one:"+message);
        System.out.println("test:"+test);
    }

    public void sayTwo(){
        three.say("通过类型注入这里是one");
    }
}