package com.mf.plock.springboot.starter.core;

import com.mf.plock.springboot.starter.annotation.Plock;
import com.mf.plock.springboot.starter.hanlder.release.impl.ReleaseTimeoutStrategy;
import com.mf.plock.springboot.starter.lock.Lock;
import com.mf.plock.springboot.starter.lock.LockFactory;
import com.mf.plock.springboot.starter.model.LockInfo;
import com.mf.plock.springboot.starter.model.LockRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;





@Slf4j
@Order(0)
@Aspect
@Component
@RequiredArgsConstructor
public class PlockAspectHandler {

    private final LockInfoProvider lockInfoProvider;

    private final LockFactory lockFactory;

    /**
     * store current thread lock info, and clear lock info when return or throw ex
     */
    private ThreadLocal<LockRes> currentThreadLock = new ThreadLocal<>();

    @Around(value = "@annotation(plock)")
    public Object around(ProceedingJoinPoint joinPoint, Plock plock) throws Throwable {

        LockInfo lockInfo = lockInfoProvider.get(joinPoint, plock);
        currentThreadLock.set(LockRes.builder().lockInfo(lockInfo).build());

        Lock lock = lockFactory.getLock(lockInfo);
        boolean acquired = lock.acquire();

        if (!acquired) {
            log.warn("Timeout while acquiring Lock({})", lockInfo.getName());

            plock.lockTimeoutStrategy().handle(lockInfo,lock,joinPoint);
        }
        currentThreadLock.get().setLock(lock);
        return joinPoint.proceed();
    }

    @AfterReturning(value = "@annotation(plock)")
    public void afterReturning(Plock plock) {
        releaseLock(plock);
    }

    @AfterThrowing(value = "@annotation(plock)", throwing = "ex")
    public void afterThrowing(Plock plock, Throwable ex) throws Throwable {
        releaseLock(plock);
        throw ex;
    }
    private void releaseLock(Plock plock){
        boolean release = currentThreadLock.get().getLock().release();
        if (!release) {
            plock.releaseTimeoutStrategy().handle(currentThreadLock.get().getLockInfo());
        }
        currentThreadLock.remove();
    }
}
