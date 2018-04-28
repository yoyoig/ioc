package com.yoyoig.ioc.scanner;

import com.yoyoig.ioc.annotation.Bean;
import com.yoyoig.ioc.bean.ClassDefine;
import com.yoyoig.ioc.exception.BeanNameRepeatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/26
 */
@Bean
@Slf4j
public class Scanner {

    /**
     * 设置包路径下的,某注解的所有类信息
     * @param path          包路径
     * @param recursive     是否递归目录
     * @param annotation    注解
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws URISyntaxException
     */
    public Set scannerBeanByAnnotation(String path,boolean recursive,Class<? extends Bean> annotation) throws ClassNotFoundException, IOException, URISyntaxException {
        String packagePath = path.replace(".",File.separator);
        Set<ClassDefine> classes = new HashSet<>();
        this.getFilePath(packagePath,recursive,classes, annotation);
        System.out.println(classes);
        return classes;
    }

    /**
     * 获取包的绝对路径
     * @param packagePath       包路径
     * @param recursive         是否递归目录
     * @param classes           类信息
     * @param annotation        注解
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     */
    private void getFilePath(String packagePath,boolean recursive,Set<ClassDefine> classes,Class<? extends  Bean> annotation) throws IOException, ClassNotFoundException, URISyntaxException {
        Enumeration<URL> dirs = this.getClass().getClassLoader().getResources(packagePath);
        while (dirs.hasMoreElements()){
            URL url = dirs.nextElement();
            String filePath = new URI(url.getFile()).getPath();
            File file = new File(filePath);
            this.setClassDefine(packagePath.replace(File.separatorChar,'.'),file,recursive,classes,annotation);
        }
    }

    /**
     *
     * @param packagePath       包路径
     * @param filePath          文件绝对路径
     * @param recursive         是否递归目录
     * @param classes           类信息
     * @param annotation        注解
     * @throws ClassNotFoundException
     */
    private void setClassDefine(String packagePath, File filePath, boolean recursive, Set<ClassDefine> classes, Class<? extends Bean> annotation) throws ClassNotFoundException {
        if (!filePath.isDirectory()){
            return;
        }
        List<String> beanNames = new ArrayList<>();
        // 过滤不是dir且不是.class的其他文件,recursive用来选择是否递归该目录
        File[] files = filePath.listFiles( e -> (recursive && e.isDirectory()) || e.getName().endsWith(".class"));
        for (File file:files){
            if (file.isDirectory()){
                setClassDefine(packagePath,file.getAbsoluteFile(),recursive,classes,annotation);
            }else {
                String className = file.getPath().substring(file.getPath().indexOf(packagePath.replace(".",File.separator)));
                className = className.substring(0,className.lastIndexOf("."));
                Class<?> clazz = this.getClass().getClassLoader().loadClass(className.replace(File.separator,"."));
                // 排除接口,枚举,注解
                if (clazz.isInterface() || clazz.isEnum() || clazz.isAnnotation()){
                    continue;
                }
                if (clazz.isAnnotationPresent(annotation)){
                    Bean beanAnnotation = clazz.getAnnotation(annotation);
                    String beanName = beanAnnotation.value();
                    if (!StringUtils.isNotEmpty(beanName)){
                        beanName = clazz.getName();
                        beanName = beanName.substring(beanName.lastIndexOf(".")+1);
                    }
                    // 扫描时,beanName相同则报错
                    if (beanNames.contains(beanName)){
                        log.error("register bean [{}] ambiguous,expect bean [{}] only one",beanName,beanName);
                        throw new BeanNameRepeatException("beanName repeat");
                    }else{
                        beanNames.add(beanName);
                    }
                    classes.add(new ClassDefine(clazz,beanName));
                }
            }
        }
    }

}
