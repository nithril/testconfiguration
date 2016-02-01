package com.example;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

/**
 * Created by nlabrot on 28/11/14.
 */
@Service
public class ActionMappingPostProcessor implements BeanPostProcessor, DestructionAwareBeanPostProcessor, Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(ActionMappingPostProcessor.class);



    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        Class<?> targetClass = bean.getClass();

        Set<Method> resources = ReflectionUtils.getAllMethods(bean.getClass());

        for (Method method : resources) {
           // AnnotationAttributes attribute =



        }
        return bean;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
