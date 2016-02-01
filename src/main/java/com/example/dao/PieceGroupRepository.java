package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dom.PieceGroup;
import com.example.dom.State;


/**
 * Created by nlabrot on 30/01/16.
 */
public interface PieceGroupRepository extends JpaRepository<PieceGroup, Long>, LockableEntityRepository<State, Long> {
}
