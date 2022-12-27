package com.mf.plock.springboot.starter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LockInfo {

    /**
     * lock type
     */
    private LockType lockType;

    /**
     * lock name
     */
    private String name;

    /**
     * the time of  try to get lock
     */
    private long waitTime;

    /**
     * the time of try to release lock
     */

    protected long leaseTime;
}
