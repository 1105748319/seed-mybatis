package com.czy.seed.mybatis.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by panlc on 2017-02-08.
 */
@Component
public class SpringContextHelper implements BeanFactoryAware {

    private static Logger logger = LoggerFactory.getLogger(SpringContextHelper.class);

    private static DefaultListableBeanFactory beanFactory;

    public void setBeanFactory(BeanFactory factory) throws BeansException {
        beanFactory = (DefaultListableBeanFactory) factory;
    }

    /**
     * 根据id获取bean
     *
     * @param id
     * @param <T>
     * @return
     */
    public static <T> T getBeanById(String id) {
        return (T) beanFactory.getBean(id);
    }

    /**
     * 根据类型得到Bean
     * @param t 类型
     * @param <T> 类型
     * @return Bean
     */
    public static <T> T getBeanByType(Class<T> t) {
        return beanFactory.getBean(t);
    }

    /**
     * 销毁spring容器中的bean
     *
     * @param beanName 从容器中移除对应的bean
     */
    public void destroy(String beanName) {
        logger.info("destroy bean " + beanName);
        if (beanFactory.containsBean(beanName)) {
            beanFactory.destroySingleton(beanName);
//            beanFactory.destroyBean(beanName);
            beanFactory.removeBeanDefinition(beanName);
        } else {
            logger.info("No {} defined in bean container.", beanName);
        }
    }

    /**
     * 向spring容器中注入bean
     *
     * @param beanClass         bean的类型
     * @param beanName          bean在spring容器中的id
     * @param autowiredResource 初始化时，要自动注入的其他引用对象， key:要注入的属性的id, value:要注入的对象
     * @param initMethod        初始化方法
     * @param destroyMethod     销毁方法
     */
    public void addBean(Class<?> beanClass, String beanName, Map<String, Object> autowiredResource, String initMethod, String destroyMethod) {
        if (!beanFactory.containsBean(beanName)) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
//            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(beanClass);
            if (autowiredResource != null) {
                for (String key : autowiredResource.keySet()) {
                    beanDefinitionBuilder.addPropertyValue(key, autowiredResource.get(key));
                }
            }
            if (initMethod != null && !"".equals(initMethod)) {
                beanDefinitionBuilder.setInitMethodName(initMethod);
            }
            if (destroyMethod != null && !"".equals(destroyMethod)) {
                beanDefinitionBuilder.setDestroyMethodName(destroyMethod);
            }
            beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
            logger.info("Add {} to bean container.", beanName);
        }
    }

    /**
     * 向spring窗口中注入单例bean
     * 同一类型的对象只能注入一次，否则会报错
     *
     * @param beanName
     * @param bean
     */
    public void addSingleton(String beanName, Object bean) {
        if (!beanFactory.containsBean(beanName)) {
            beanFactory.registerSingleton(beanName, bean);
        } else {
            logger.error("fail to register bean {} to spring, the same beanName had keeped by spring already", beanName);
            throw new RuntimeException("fail to register bean " + beanName +
                    " to spring, the same beanName had keeped by spring already");
        }
    }

}
