package com.yoyoig.ioc.bean;


import com.yoyoig.ioc.SummerContext;
import com.yoyoig.ioc.annotation.Bean;
import com.yoyoig.ioc.annotation.Inject;
import com.yoyoig.ioc.annotation.Value;
import com.yoyoig.ioc.exception.BeanNotFoundException;
import com.yoyoig.ioc.exception.ValueEmptyException;
import com.yoyoig.ioc.exception.ValueFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/27
 */
@Bean
@Slf4j
public class SimpleBeanFactory implements BeanFactory {

    @Override
    public void registerBean(ClassDefine classDefine, SummerContext context) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // 如果context中已经存在,则返回
        if (context.hasBean(classDefine.getBeanName())){
            return;
        }
        Class<?> clazz = classDefine.getClazz();
        Object object = clazz.getConstructor().newInstance();
        this.initFields(clazz,object,context);
        // 将bean注入到context
        context.setBean(new BeanDefine(object,classDefine.getBeanName()));
    }

    /**
     * 初始化field
     * @param clazz
     * @param object
     * @param context
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private void initFields(Class<?> clazz, Object object ,SummerContext context) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Field[] injectFields = clazz.getDeclaredFields();
        // 注入filed
        for (Field field:injectFields){
            // 注入配置文件的值
            this.injectValue(object,field,context);
            // 注入bean
            this.injectBean(object,field,context);
        }
    }

    /**
     * 给对象注入bean
     * @param object
     * @param field
     * @param context
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    private void injectBean(Object object,Field field,SummerContext context) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        Inject annotation = field.getAnnotation(Inject.class);
        if (annotation != null) {
            if (StringUtils.isNotEmpty(annotation.value())) {
                this.injectBeanByName(annotation,object,field,context);
            } else {
                this.injectBeanByType(object,field,context);
            }
        }
    }

    /**
     * 给对象注入配置
     * @param object
     * @param field
     * @param context
     * @throws IllegalAccessException
     */
    private void injectValue(Object object,Field field,SummerContext context) throws IllegalAccessException {
        Value valueAnnotation = field.getAnnotation(Value.class);
        if (valueAnnotation!=null){
            String param = valueAnnotation.value();
            if (!StringUtils.isNotEmpty(param)){
                log.error("Value annotation value not empty");
                throw new ValueEmptyException("Value annotation value not empty");
            }
            if (!param.startsWith("{") || !param.endsWith("}")){
                log.error("Value annotation value expect startsWith '{' ,endWith '}'");
                throw new ValueFormatException("Value annotation value expect startsWith '{' ,endWith '}'");
            }
            String value = context.getByProperties(param.substring(1,param.length()-1));
            if (field.getType().getName() != String.class.getTypeName()){
                log.error("field type error");
                throw new ValueFormatException("field type error");
            }
            if (StringUtils.isEmpty(value)){
                log.error("properties {} not null",value);
                throw new ValueEmptyException("properties not null");
            }
            field.setAccessible(true);
            field.set(object, value);
        }
    }

    /**
     * 根据类型注入bean
     * @param object
     * @param field
     * @param context
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    private void injectBeanByType(Object object, Field field, SummerContext context) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        // 没有指定beanName,则按类型注入(接口)
        Type type = field.getGenericType();
        ClassDefine classDefineByType = context.getClassDefineByType(type);
        // prepareBean中有,则需要被实例化,
        if (classDefineByType != null){
            if (context.hasBean(classDefineByType.getBeanName())){
                // context已经存在该bean则注入
                Object bean = context.getBean(classDefineByType.getBeanName());
                this.inject(field,object,bean);
            } else {
                //不存在则创建并注册到context中,注册结束后注入
                this.registerBean(classDefineByType,context);
                Object bean = context.getBean(classDefineByType.getBeanName());
                this.inject(field,object,bean);
            }
        } else {
            // 不存在则报错
            log.error("can not found type [{}] inject to field [{}]",type.getTypeName(),field.getName());
            throw new BeanNotFoundException("type not found");
        }
    }

    /**
     * 根据beanName注入bean
     * @param annotation
     * @param object
     * @param field
     * @param context
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    private void injectBeanByName(Inject annotation, Object object, Field field, SummerContext context) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        String beanName = annotation.value();
        if (context.isExist(beanName)) {
            // 当context中存在该名称的bean,则注入到field中
            Object bean = context.getBean(beanName);
            this.inject(field,object,bean);
        } else {
            // context中不存在时,从classes中查询是否有该名称的bean,存在则注册
            if (context.isPrepareBeanContain(beanName)){
                this.registerBean(context.getClassByBeanName(beanName),context);
                // 注册结束后,注入
                Object bean = context.getBean(beanName);
                this.inject(field,object,bean);
            }else {
                // 不存在则报错
                log.error("can not found bean [{}] inject to field [{}]",beanName,field.getName());
                throw new BeanNotFoundException("bean not found");
            }
        }
    }


    /**
     * 将object注入到bean的field中
     * @param field
     * @param object
     * @param value
     * @throws IllegalAccessException
     */
    private void inject(Field field,Object object,Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object, value);
    }

}