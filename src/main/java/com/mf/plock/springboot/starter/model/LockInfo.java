package com.mf.plock.springboot.starter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LockInfo {

    private LockType lockType;

    private String name;

    private long waitTime;

    protected long leaseTime;
}
