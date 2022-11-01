package com.mf.plock.springboot.starter.annotation;

import com.mf.plock.springboot.starter.hanlder.lock.impl.LockTimeoutStrategy;
import com.mf.plock.springboot.starter.model.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Plock {

    String name() default "";

    long waitTime() default Long.MIN_VALUE;

    long leaseTime() default Long.MIN_VALUE;

    LockType type() default LockType.Reentrant;

    LockTimeoutStrategy lockTimeoutStrategy() default LockTimeoutStrategy.NO_OPERATION;
}
