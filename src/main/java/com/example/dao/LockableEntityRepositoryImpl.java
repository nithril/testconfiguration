package com.example.dao;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import java.io.Serializable;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.example.dom.State;

/**
 * Created by nlabrot on 30/01/16.
 */
public class LockableEntityRepositoryImpl<T extends State, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements LockableEntityRepository<T,ID> {

    private EntityManager entityManager;

    public LockableEntityRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityManager = entityManager;
    }


    @Override
    public T lock(ID id) {
        return entityManager.find(getDomainClass() , id , LockModeType.PESSIMISTIC_WRITE);
    }

    @Override
    public void lock(ID id, long timeout) {

    }

    @Override
    public void lockNoWait(ID id) {

    }

}
