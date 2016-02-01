package com.example.interceptor;

import java.util.List;
import java.util.function.Predicate;

import com.example.dao.LockableEntityRepository;

/**
 * Created by nlabrot on 29/01/16.
 */
public class EntityLockAttribute {

    private final int argumentIndex;
    private final LockableEntityRepository lockableEntityRepository;

    private final List<Predicate> predicates;


    public EntityLockAttribute(int argumentIndex, LockableEntityRepository lockableEntityRepository, List<Predicate> predicates) {
        this.argumentIndex = argumentIndex;
        this.lockableEntityRepository = lockableEntityRepository;
        this.predicates = predicates;
    }

    public int getArgumentIndex() {
        return argumentIndex;
    }

    public LockableEntityRepository getLockableEntityRepository() {
        return lockableEntityRepository;
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }
}
