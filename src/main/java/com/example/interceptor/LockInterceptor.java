package com.example.interceptor;

import java.io.Serializable;
import java.util.function.Predicate;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.dom.State;
import com.example.exception.UnexpectedCurrentStateException;

/**
 * Created by nlabrot on 29/01/16.
 */
@Component
public class LockInterceptor implements MethodInterceptor {

    private final LockAttributeSource lockAttributeSource;

    @Autowired
    public LockInterceptor(LockAttributeSource lockAttributeSource) {
        this.lockAttributeSource = lockAttributeSource;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        LockAttribute lockAttribute = lockAttributeSource.getLockAttribute(invocation.getThis().getClass(), invocation.getMethod());

        if (lockAttribute != null) {

            for (EntityLockAttribute attribute : lockAttribute.getEntityLockAttributes()) {
                State state = lockEntity(attribute, invocation);

                for (Predicate predicate : attribute.getPredicates()) {
                    predicate.test(state);
                }
            }
        }

        return invocation.proceed();

    }

    public State lockEntity(EntityLockAttribute attribute, MethodInvocation invocation) {
        Serializable entityId = (Serializable) invocation.getArguments()[attribute.getArgumentIndex()];
        return attribute.getLockableEntityRepository().lock(entityId);
    }

}
