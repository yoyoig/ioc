package com.yoyoig.ioc.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/26
 */
@Data
@EqualsAndHashCode
public class ClassDefine {

    private String beanName;
    private Class<?> clazz;
    private Class<?>[] superClazz;

    public ClassDefine(Class<?> clazz,String beanName){
        this.beanName = beanName;
        this.clazz = clazz;
        this.superClazz = clazz.getInterfaces();
    }

    public ClassDefine(Class<?> clazz){
        this.beanName = clazz.getName();
        this.clazz = clazz;
        this.superClazz = clazz.getInterfaces();
    }

}