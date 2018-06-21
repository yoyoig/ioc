## 简单IOC容器  

- 通过bean类型自动注入bean
- 通过bean名称自动注入bean
- 将配置文件的值注入到类的属性中

### 相关类
- BeanDefine 已经实例化的bean,以及bean相关信息.
- ClassDefine 将要实例化的bean的类信息.
- BeanFactory 通过ClassDefine集合递归实例化bean
- Resolver 解析配置文件
- Scanner 扫描配置路径下的Bean,生成ClassDefine集合
- SummerContext 初始化并启动容器,存储已经实例化的BeanDefine集合.
