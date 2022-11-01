package com.mf.plock.springboot.starter.hanlder.lock;

import com.mf.plock.springboot.starter.lock.Lock;
import com.mf.plock.springboot.starter.model.LockInfo;
import org.aspectj.lang.JoinPoint;

public interface LockTimeoutHandler {
    void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint);
}
