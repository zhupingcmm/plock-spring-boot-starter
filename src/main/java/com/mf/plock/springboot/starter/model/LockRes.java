package com.mf.plock.springboot.starter.model;

import com.mf.plock.springboot.starter.lock.Lock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LockRes {

    /**
     * lock information
     */
    private LockInfo lockInfo;

    /**
     * lock instance
     */
    private Lock lock;

}
