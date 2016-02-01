package com.example;

import java.lang.reflect.Method;

import org.springframework.util.ObjectUtils;

/**
 * Created by nlabrot on 29/01/16.
 */
public class DefaultCacheKey {

    private final Method method;

    private final Class<?> targetClass;

    public DefaultCacheKey(Method method, Class<?> targetClass) {
        this.method = method;
        this.targetClass = targetClass;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DefaultCacheKey)) {
            return false;
        }
        DefaultCacheKey otherKey = (DefaultCacheKey) other;
        return (this.method.equals(otherKey.method) &&
                ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass));
    }

    @Override
    public int hashCode() {
        return this.method.hashCode() + (this.targetClass != null ? this.targetClass.hashCode() * 29 : 0);
    }
}