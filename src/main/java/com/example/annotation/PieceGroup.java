package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.example.dao.LockableEntityRepositoryImpl;
import com.example.dao.PieceGroupRepository;

/**
 * Created by nlabrot on 28/01/16.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@EntityLock(repositoryClass = PieceGroupRepository.class)
@ExpectedStatePrecondition
@EntityExistPrecondition
public @interface PieceGroup {

    @AliasFor("expectedState")
    String value() default "";

    String expectedState() default "";
}
