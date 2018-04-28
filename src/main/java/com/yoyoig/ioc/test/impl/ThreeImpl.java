package com.yoyoig.ioc.test.impl;

import com.yoyoig.ioc.annotation.Bean;
import com.yoyoig.ioc.test.Three;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/27
 */
@Bean
public class ThreeImpl implements Three {


    @Override
    public void say(String message) {
        System.out.println("这里是three:"+message);
    }
}