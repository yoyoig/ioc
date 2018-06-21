package com.yoyoig.ioc.bean;

import com.yoyoig.ioc.annotation.Inject;
import lombok.Data;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description 已经实例化的类信息
 * @date 2018/4/26
 */
@Data
public class BeanDefine {

    private Object bean;

    private Class<?> clazz;

    private String className;


    public BeanDefine(Object bean){
        this(bean,bean.getClass());
    }

    public BeanDefine(Object bean,Class<?> clazz){
        this.bean = bean;
        this.clazz = clazz;
    }

    public BeanDefine(Object bean,String className){
        this(bean);
        this.className = className;
    }

}