# 加解密


# [深入理解Spring循环依赖](https://mrbird.cc/%E6%B7%B1%E5%85%A5%E7%90%86%E8%A7%A3Spring%E5%BE%AA%E7%8E%AF%E4%BE%9D%E8%B5%96.html)

## Bean创建源码
我们先通过源码熟悉下Bean创建过程（源码仅贴出相关部分）。

IOC容器获取Bean的入口为AbstractBeanFactory类的getBean方法：
```java
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null, null, false);
    }

}
```

该方法是一个空壳方法，具体逻辑都在doGetBean方法内：
```java
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    protected <T> T doGetBean(
            String name, @Nullable Class<T> requiredType, @Nullable Object[] args, boolean typeCheckOnly)
            throws BeansException {

        // 获取Bean名称
        String beanName = transformedBeanName(name);
        Object bean;

        // 从三级缓存中获取目标Bean实例
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null && args == null) {
            // 不为空，则进行后续处理并返回
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
        } else {
            try {
                RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                // 从三级缓存中没有获取到Bean实例，并且目标Bean是单实例Bean的话
                if (mbd.isSingleton()) {
                    // 通过getSingleton(String beanName, ObjectFactory<?> singletonFactory)方法创建Bean实例
                    sharedInstance = getSingleton(beanName, () -> {
                        try {
                            // 创建Bean实例
                            return createBean(beanName, mbd, args);
                        }
                        catch (BeansException ex) {
                            //......
                        }
                    });
                    // 后续处理，并返回
                    bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
                }
                //......
            }
            catch (BeansException ex) {
                //......
            }
            finally {
                //......
            }
        }
        //......
        return (T) bean;
    }
}
```