package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.annotation.PieceGroup;
import com.example.dom.DataSet;

/**
 * Created by nlabrot on 30/01/16.
 */
public interface DataSetRepository extends JpaRepository<DataSet, Long>, LockableEntityRepository<DataSet, Long> {
}
