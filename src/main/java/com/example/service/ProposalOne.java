package com.example.service;

import com.example.annotation.DataSet;
import com.example.annotation.PieceGroup;
import com.example.annotation.ServiceWithLock;


/**
 * Created by nlabrot on 28/01/16.
 */
@ServiceWithLock
public class ProposalOne {

    public void dataSet(@DataSet("STATE") Long dataSetId) {
        System.out.println("ok");
    }

    public void pieceGroup(@PieceGroup("STATE") Long pieceGroupId) {
        System.out.println("ok");
    }

}
