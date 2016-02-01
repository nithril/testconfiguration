package com.example.annotation;

/**
 * Created by nlabrot on 28/01/16.
 */
public @interface PieceGroupLock {
    String expectedState();

    String paramName();
}
