package com.example.interceptor;

import java.util.List;

/**
 * Created by nlabrot on 29/01/16.
 */
public class LockAttribute {

    private final List<EntityLockAttribute> entityLockAttributes;

    public LockAttribute(List<EntityLockAttribute> entityLockAttributes) {
        this.entityLockAttributes = entityLockAttributes;
    }

    public List<EntityLockAttribute> getEntityLockAttributes() {
        return entityLockAttributes;
    }
}
