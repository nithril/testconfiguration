package com.example.exception;

/**
 * Created by nlabrot on 30/01/16.
 */
public class UnexpectedCurrentStateException extends RuntimeException {

    private final String currentState;
    private final String expectedState;

    public UnexpectedCurrentStateException(String currentState, String expectedState) {
        this.currentState = currentState;
        this.expectedState = expectedState;
    }

    @Override
    public String toString() {
        return "UnexpectedCurrentStateException{" +
                "currentState='" + currentState + '\'' +
                ", expectedState='" + expectedState + '\'' +
                '}';
    }
}
