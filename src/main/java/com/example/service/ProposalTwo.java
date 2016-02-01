package com.example.service;

import org.springframework.stereotype.Service;

import com.example.annotation.DataSet;
import com.example.annotation.DataSetLock;
import com.example.annotation.Lock;
import com.example.annotation.PieceGroup;
import com.example.annotation.PieceGroupLock;


/**
 * Created by nlabrot on 28/01/16.
 */
@Service
public class ProposalTwo {

    @DataSetLock(paramName = "dataSetId", expectedState = "STATE")
    public void dataSet(Long dataSetId) {
        System.out.println("ok");
    }

    @PieceGroupLock(paramName = "pieceGroupId", expectedState = "STATE")
    public void pieceGroup(Long pieceGroupId) {
        System.out.println("ok");
    }

}
