package com.mf.plock.springboot.starter.annotation;

import com.mf.plock.springboot.starter.hanlder.lock.impl.LockTimeoutStrategy;
import com.mf.plock.springboot.starter.hanlder.release.impl.ReleaseTimeoutStrategy;
import com.mf.plock.springboot.starter.model.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Plock {

    /**
     * lock name, but the name is maybe a partition of lock name
     * because the full lock name is name + keys value
     * e.g. @Plock(name="ab", keys={"#user.id", "#user.name} ) id: 1 name:c
     * the name is ab-1-c
     */
    String name() default "";

    /**
     *  the time about wait to acquire lock
     */
    long waitTime() default Long.MIN_VALUE;

    /**
     *
     *the time about wait to lease lock
     */
    long leaseTime() default Long.MIN_VALUE;

    /**
     *
     *  the lock type
     */
    LockType type() default LockType.Reentrant;

    /**
     *
     *  the parameters of method value
     *  e.g.
     *  @Plock(name="ab", keys={"#user.id", "#user.name})
     *  public getUser(User user){}
     */
    String [] keys() default {};

    /**
     *
     * when acquire lock timeout, execute this strategy
     */
    LockTimeoutStrategy lockTimeoutStrategy() default LockTimeoutStrategy.NO_OPERATION;

    /**
     *
     * when release lock timeout, execute this strategy
     */
    ReleaseTimeoutStrategy releaseTimeoutStrategy() default ReleaseTimeoutStrategy.NO_OPERATION;
}
