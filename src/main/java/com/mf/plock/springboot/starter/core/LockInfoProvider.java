package com.mf.plock.springboot.starter.core;

import com.mf.plock.springboot.starter.annotation.Plock;
import com.mf.plock.springboot.starter.config.PlockConfig;
import com.mf.plock.springboot.starter.model.LockInfo;
import com.mf.plock.springboot.starter.model.LockType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;


@AllArgsConstructor
@NoArgsConstructor
public class LockInfoProvider {

    private static final String LOCK_NAME_PREFIX = "lock";
    private static final String LOCK_NAME_SEPARATOR = "-";

    @Autowired
    private PlockConfig plockConfig;

    @Autowired
    private BusinessKeyProvider businessKeyProvider;


    /**
     * @param joinPoint
     * @param plock
     * @return lock info
     */
    public LockInfo get(ProceedingJoinPoint joinPoint, Plock plock) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LockType lockType = plock.type();
        String businessKeyName = businessKeyProvider.getKeyName(joinPoint, plock);
        String lockName = LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR + getName(plock.name(), methodSignature) + businessKeyName;
        return new LockInfo(lockType,lockName,getWaitTime(plock), getLeaseTime(plock));
    }

    private String getName(String annotationLockName, MethodSignature methodSignature) {
        if (annotationLockName == null) {
            return String.format("%s.%s", methodSignature.getDeclaringTypeName(), methodSignature.getMethod().getName());
        } else {
            return annotationLockName;
        }
    }

    private long getWaitTime(Plock plock) {
        return plock.waitTime() == Long.MIN_VALUE ?  plockConfig.getWaitTime() : plock.waitTime();
    }

    private long getLeaseTime(Plock plock) {
        return plock.leaseTime() == Long.MIN_VALUE ? plockConfig.getLeaseTime() : plock.leaseTime();
    }
}
