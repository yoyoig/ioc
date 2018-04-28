package com.yoyoig.ioc;

import com.yoyoig.ioc.annotation.Bean;
import com.yoyoig.ioc.bean.BeanDefine;
import com.yoyoig.ioc.bean.ClassDefine;
import com.yoyoig.ioc.bean.SimpleBeanFactory;
import com.yoyoig.ioc.config.SummerConstant;
import com.yoyoig.ioc.exception.BeanNotFoundException;
import com.yoyoig.ioc.resolver.Resolver;
import com.yoyoig.ioc.scanner.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/26
 */
@Slf4j
public class SummerContext {

    /**
     * 所有实例化后的bean放在context中
     */
    private Map<String,Object> context = new HashMap<>(128);

    /**
     * 配置文件
     */
    private  Properties properties;

    /**
     * 准备被实例化的bean
     */
    private Set<ClassDefine> prepareBean;

    public SummerContext(String propertiesPath) throws ClassNotFoundException, IOException, URISyntaxException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        init(propertiesPath);
    }

    /**
     * 初始化bean
     * @param propertiesPath
     * @throws IOException
     * @throws URISyntaxException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void init(String propertiesPath) throws IOException, URISyntaxException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        SimpleBeanFactory factory = new SimpleBeanFactory();
        Resolver resolver = new Resolver();
        properties = resolver.loadProperties(propertiesPath);
        String packagePath = properties.getProperty(SummerConstant.PACKAGE_PATH);
        Scanner scanner = new Scanner();
        // 扫描bean到prepareBean
        prepareBean = scanner.scannerBeanByAnnotation(packagePath,true, Bean.class);
        // 注册bean到context
        for (ClassDefine classDefine:prepareBean){
            factory.registerBean(classDefine,this);
        }
    }

    /**
     * 将bean设置到context
     * @param beanDefine
     */
    public void setBean(BeanDefine beanDefine){
        this.context.put(beanDefine.getClassName(),beanDefine.getBean());
    }

    /**
     * 判断context是否存在该bean
     * @param beanName
     * @return
     */
    public boolean isExist(String beanName){
        return this.context.containsKey(beanName);
    }

    /**
     * 通过beanName获取bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        return this.context.get(beanName);
    }

    /**
     * 包下是否存在该beanName
     * @param beanName
     * @return
     */
    public boolean isPrepareBeanContain(String beanName){
        return prepareBean.stream().anyMatch( e -> StringUtils.equals(beanName,e.getBeanName()));
    }

    /**
     * 通过beanName获取bean类型
     * @param beanName
     * @return
     */
    public ClassDefine getClassByBeanName(String beanName){
        for (ClassDefine classDefine:prepareBean){
            if (StringUtils.equals(classDefine.getBeanName(),beanName)){
                return classDefine;
            }
        }
        // 不存在则报错
        log.error("class not found");
        throw new BeanNotFoundException("bean not found");
    }

    /**
     * context中是否包含改bean
     * @param beanName
     * @return
     */
    public boolean hasBean(String beanName) {
        return this.context.containsKey(beanName);
    }

    /**
     * 通过类型获取bean类型
     * @param type
     * @return
     */
    public ClassDefine getClassDefineByType(Type type){
        for (ClassDefine classDefine:prepareBean){
            for (Class superClass:classDefine.getSuperClazz()){
                if(StringUtils.equals(superClass.getTypeName(),type.getTypeName())){
                    return classDefine;
                }
            }
        }
        return null;
    }

    public String getByProperties(String key){
        return this.properties.getProperty(key);
    }

}
