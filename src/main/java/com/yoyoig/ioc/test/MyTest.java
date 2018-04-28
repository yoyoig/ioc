package com.yoyoig.ioc.test;

import com.yoyoig.ioc.SummerContext;
import com.yoyoig.ioc.test.impl.OneImpl;
import com.yoyoig.ioc.test.impl.TwoImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/27
 */
public class MyTest {

    public static void main(String[] args) throws URISyntaxException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        SummerContext summerContext = new SummerContext("app.properties");
        OneImpl oneImpl = (OneImpl) summerContext.getBean("one");
        TwoImpl twoImpl = (TwoImpl) summerContext.getBean("TwoImpl");
        oneImpl.say("----");
        oneImpl.sayTwo();
        twoImpl.say("----");
        twoImpl.sayOne();
    }
}