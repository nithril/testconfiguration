package com.example.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.example.DefaultCacheKey;
import com.example.annotation.EntityLock;
import com.example.annotation.Lock;
import com.example.dao.LockableEntityRepository;
import com.example.precondition.EntityExistPrecondition;
import com.example.precondition.ExpectedStatePrecondition;

import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotationAttributes;

/**
 * Created by nlabrot on 29/01/16.
 */
@Component
public class LockAttributeSource {

    private final Map<DefaultCacheKey, LockAttribute> attributeCache = new ConcurrentHashMap<>(1024);

    private final static LockAttribute NULL_TRANSACTION_ATTRIBUTE = new LockAttribute(Collections.emptyList());

    private final ApplicationContext applicationContext;

    @Autowired
    public LockAttributeSource(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public boolean hasLockAttribute(Class<?> targetClass, Method method) {
        return findAttribute(targetClass, method) != null;
    }

    public LockAttribute getLockAttribute(Class<?> targetClass, Method method) {

        DefaultCacheKey cacheKey = getCacheKey(method, targetClass);
        LockAttribute cached = this.attributeCache.get(cacheKey);
        if (cached != null) {
            // Value will either be canonical value indicating there is no transaction attribute,
            // or an actual transaction attribute.
            if (cached == NULL_TRANSACTION_ATTRIBUTE) {
                return null;
            } else {
                return cached;
            }
        } else {
            // We need to work it out.
            LockAttribute txAtt = findAttribute(targetClass, method);
            // Put it in the cache.
            if (txAtt == null) {
                this.attributeCache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
            } else {
                this.attributeCache.put(cacheKey, txAtt);
            }
            return txAtt;
        }
    }


    private LockAttribute findAttribute(Class<?> targetClass, Method method) {
        // Ignore CGLIB subclasses - introspect the actual user class.
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        // If we are dealing with method with generic parameters, find the original method.
        // Not need anymore post java 8
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);


        List<EntityLockAttribute> lockableEntityAttributes = extractArguments(specificMethod);
        if (lockableEntityAttributes.isEmpty()) {
            lockableEntityAttributes = extractArguments(method);
        }

        if (lockableEntityAttributes.isEmpty()) {
            return null;
        }


        // First try is the method in the target class.
        AnnotationAttributes txAtt = getMergedAnnotationAttributes(specificMethod, Lock.class);
        if (txAtt == null) {

            // Second try is the transaction attribute on the target class.
            txAtt = getMergedAnnotationAttributes(specificMethod.getDeclaringClass(), Lock.class);
            if (txAtt == null) {

                if (specificMethod != method) {
                    // Fallback is to look at the original method.
                    txAtt = getMergedAnnotationAttributes(method, Lock.class);
                    if (txAtt == null) {
                        // Last fallback is the class of the original method.
                        txAtt = AnnotatedElementUtils.getMergedAnnotationAttributes(method.getDeclaringClass(), Lock.class);
                    }
                }
            }
        }

        if (txAtt == null) {
            return null;
        }

        return new LockAttribute(lockableEntityAttributes);
    }

    private List<EntityLockAttribute> extractArguments(Method specificMethod) {
        Parameter[] specificMethodParameters = specificMethod.getParameters();

        List<EntityLockAttribute> lockableEntityAttributes = new ArrayList<>();

        for (int index = 0; index < specificMethodParameters.length; index++) {
            AnnotationAttributes attributes = getMergedAnnotationAttributes(specificMethodParameters[index], EntityLock.class);
            if (attributes != null) {
                //applicationContext.getBean(attributes)
                Class<? extends LockableEntityRepository> repositoryClass = (Class<? extends LockableEntityRepository>) attributes.get("repositoryClass");

                List<Predicate> predicates = getPredicates(specificMethodParameters[index]);

                // TODO: could slowdown the spring bootstrap. may implement a deferred resolution
                LockableEntityRepository repository = applicationContext.getBean(repositoryClass);

                lockableEntityAttributes.add(new EntityLockAttribute(index, repository, predicates));
            }
        }
        return lockableEntityAttributes;
    }


    protected DefaultCacheKey getCacheKey(Method method, Class<?> targetClass) {
        return new DefaultCacheKey(method, targetClass);
    }

    protected List<Predicate> getPredicates(Parameter specificMethodParameter) {
        List<Predicate> predicates = new ArrayList<>();

        AnnotationAttributes attributes = getMergedAnnotationAttributes(specificMethodParameter, com.example.annotation.ExpectedStatePrecondition.class);
        if (attributes != null && StringUtils.isNotBlank(attributes.getString("expectedState"))) {
            predicates.add(new ExpectedStatePrecondition(attributes.getString("expectedState")));
        }

        attributes = getMergedAnnotationAttributes(specificMethodParameter, com.example.annotation.EntityExistPrecondition.class);
        if (attributes != null) {
            predicates.add(new EntityExistPrecondition());
        }
        return predicates;

    }
}
