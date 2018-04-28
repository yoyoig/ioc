package com.yoyoig.ioc.bean;

import com.yoyoig.ioc.SummerContext;

import java.lang.reflect.InvocationTargetException;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/27
 */
public interface BeanFactory {


    /**
     * 注册一个bean到context
     * @param context
     */
    void registerBean(ClassDefine classDefine, SummerContext context) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;




}