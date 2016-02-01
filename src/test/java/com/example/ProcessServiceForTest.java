package com.example;

import org.springframework.stereotype.Service;

import com.example.annotation.DataSet;
import com.example.annotation.Lock;
import com.victorbuckservices.vbackbone.common.utils.lambda.with.CheckedCallable;

/**
 * Created by nlabrot on 30/01/16.
 */
@Service
@Lock
public class ProcessServiceForTest {


    public <T> T dataSet(@DataSet(expectedState = "foo") Long id, CheckedCallable<T> c) {
        return c.call();
    }

    public <T> T noState(@DataSet Long id, CheckedCallable<T> c) {
        return c.call();
    }
}

