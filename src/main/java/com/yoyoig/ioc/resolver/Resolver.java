package com.yoyoig.ioc.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author mingke.yan@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/4/26
 */
public class Resolver {

    /**
     * 解析properties的值到Map中
     * @param filePath
     * @return
     */
    public Properties loadProperties(String filePath) throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(filePath);
        Properties properties = new Properties();
        properties.load(stream);
        return properties;
    }

}
