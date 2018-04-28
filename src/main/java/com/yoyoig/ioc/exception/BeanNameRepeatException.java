package com.yoyoig.ioc.exception;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/27
 */
public class BeanNameRepeatException extends RuntimeException{

    public BeanNameRepeatException(String message) {
        super(message);
    }
}