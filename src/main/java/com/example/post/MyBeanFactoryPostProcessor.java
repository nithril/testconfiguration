package com.example.post;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;



/**
 * Created by nlabrot on 28/01/16.
 */
@Service
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {




    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

            MyConfigurationClassEnhancer enhancer = new MyConfigurationClassEnhancer();

            AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) configurableListableBeanFactory.getBeanDefinition("fooBean");

            Class<?> beanClass = beanDefinition.resolveBeanClass(beanClassLoader);


            Class<?> enhance = enhancer.enhance(beanClass, beanClassLoader);

           // beanDefinition.setBeanClass(enhance);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
