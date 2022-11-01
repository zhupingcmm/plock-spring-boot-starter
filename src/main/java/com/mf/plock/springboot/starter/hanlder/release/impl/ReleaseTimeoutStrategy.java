package com.mf.plock.springboot.starter.hanlder.release.impl;

import com.mf.plock.springboot.starter.exception.PlockExeception;
import com.mf.plock.springboot.starter.hanlder.release.ReleaseTimeoutHandler;
import com.mf.plock.springboot.starter.model.LockInfo;

public enum ReleaseTimeoutStrategy implements ReleaseTimeoutHandler<LockInfo> {

    NO_OPERATION() {
        @Override
        public void handle(LockInfo lockInfo) {
            // nothing todo
        }
    },
    FAIL_FAST() {
        @Override
        public void handle(LockInfo lockInfo) {
            String errorMsg = String.format("Found Lock(%s) already been released while lock lease time is %d s", lockInfo.getName(), lockInfo.getLeaseTime());
            throw new PlockExeception(errorMsg);
        }
    }

}
