package com.mf.plock.springboot.starter.lock;

public interface Lock {

    boolean acquire();

    boolean release();
}
