package com.example.precondition;

import java.util.function.Predicate;

import com.example.dom.State;
import com.example.exception.UnexpectedCurrentStateException;

/**
 * Created by nlabrot on 30/01/16.
 */
public class ExpectedStatePrecondition implements Predicate<State> {

    private final String expectedState;

    public ExpectedStatePrecondition(String expectedState) {
        this.expectedState = expectedState;
    }

    @Override
    public boolean test(State o) {
        if (!expectedState.equals(o.getState())){
            throw new UnexpectedCurrentStateException(o.getState() , expectedState);
        }
        return true;
    }

    public String getExpectedState() {
        return expectedState;
    }
}
