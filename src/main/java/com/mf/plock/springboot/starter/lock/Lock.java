package com.mf.plock.springboot.starter.lock;

public interface Lock {

    /**
     *  acquire lock
     * @return
     */
    boolean acquire();

    /**
     *  release lock
     * @return
     */
    boolean release();
}
