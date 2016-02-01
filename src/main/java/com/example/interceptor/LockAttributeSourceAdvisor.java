package com.example.interceptor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.interceptor.LockAttributeSource;
import com.example.interceptor.LockAttributeSourcePointcut;
import com.example.interceptor.LockInterceptor;

/**
 * Created by nlabrot on 29/01/16.
 */
@Component
public class LockAttributeSourceAdvisor extends AbstractPointcutAdvisor {

    private final LockAttributeSourcePointcut lockAttributeSourcePointcut;

    private final LockInterceptor lockInterceptor;

    @Autowired
    public LockAttributeSourceAdvisor(LockAttributeSourcePointcut lockAttributeSourcePointcut , LockInterceptor lockInterceptor) {
        this.lockAttributeSourcePointcut = lockAttributeSourcePointcut;
        this.lockInterceptor = lockInterceptor;
    }

    @Override
    public Pointcut getPointcut() {
        return lockAttributeSourcePointcut;
    }

    @Override
    public Advice getAdvice() {
        return lockInterceptor;
    }
}
