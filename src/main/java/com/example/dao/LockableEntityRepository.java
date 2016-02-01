package com.example.dao;

import java.io.Serializable;

import com.example.dom.State;

/**
 * Created by nlabrot on 29/01/16.
 */
public interface LockableEntityRepository<T extends State, ID extends Serializable> {

    T lock(ID id);

    void lock(ID id, long timeout);

    void lockNoWait(ID id);

}
