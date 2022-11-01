package com.mf.plock.springboot.starter.hanlder.lock.impl;

import com.mf.plock.springboot.starter.exception.PlockExeception;
import com.mf.plock.springboot.starter.hanlder.lock.LockTimeoutHandler;
import com.mf.plock.springboot.starter.lock.Lock;
import com.mf.plock.springboot.starter.model.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

import java.util.concurrent.TimeUnit;

@Slf4j
public enum LockTimeoutStrategy implements LockTimeoutHandler {
    NO_OPERATION() {
        @Override
        public void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {
            // do nothing
        }
    },
    FAIL_FAST(){
        @Override
        public void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {
            String errorMessage = String.format("Failed to acquire lock (%s) with %s s time", lockInfo.getName(), lockInfo.getWaitTime());
            throw new PlockExeception(errorMessage);
        }
    },
    KEEP_ACQUIRE(){
        private static final long DEFAULT_INTERVAL = 100l;

        private static final long DEFAULT_MAX_INTERVAL = 3 * 60 * 1000l;
        @Override
        public void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {

            long internal = DEFAULT_INTERVAL;


            while (!lock.acquire()) {
                if (internal > DEFAULT_MAX_INTERVAL) {
                    throw new PlockExeception(String.format("Failed to acquire Lock(%s) after too many times, this may because dead lock occurs.",
                            lockInfo.getName()));
                }

                try {
                    TimeUnit.MICROSECONDS.sleep(DEFAULT_INTERVAL);
                    internal <<= 1;
                } catch (InterruptedException e) {
                    throw new PlockExeception("Failed to acquire lock",e);
                }
            }
        }
    }
}
