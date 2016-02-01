package com.example.exception;

import java.io.Serializable;

/**
 * Created by nlabrot on 31/01/16.
 */
public class EntityNotFoundException extends RuntimeException {

    private final Serializable id;


    public EntityNotFoundException(Serializable id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EntityNotFoundException{" +
                "id=" + id +
                '}';
    }
}
