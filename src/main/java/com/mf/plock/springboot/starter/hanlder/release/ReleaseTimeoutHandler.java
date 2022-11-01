package com.mf.plock.springboot.starter.hanlder.release;

import com.mf.plock.springboot.starter.model.LockInfo;

public interface ReleaseTimeoutHandler <T extends LockInfo>{
    void handle(T t);
}
