package com.example.interceptor;

import java.lang.reflect.Method;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nlabrot on 29/01/16.
 */
@Component
public class LockAttributeSourcePointcut extends StaticMethodMatcherPointcut {

    private final LockAttributeSource lockAttributeSource;

    @Autowired
    public LockAttributeSourcePointcut(LockAttributeSource lockAttributeSource) {
        this.lockAttributeSource = lockAttributeSource;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return lockAttributeSource.hasLockAttribute(targetClass , method);
    }
}
