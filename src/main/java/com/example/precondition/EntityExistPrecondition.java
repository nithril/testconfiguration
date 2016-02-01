package com.example.precondition;

import java.util.function.Predicate;

import com.example.dom.State;
import com.example.exception.EntityNotFoundException;
import com.example.exception.UnexpectedCurrentStateException;

/**
 * Created by nlabrot on 30/01/16.
 */
public class EntityExistPrecondition implements Predicate<State> {

    @Override
    public boolean test(State o) {
        if (o ==null){
            throw new EntityNotFoundException("");
        }
        return true;
    }
}
