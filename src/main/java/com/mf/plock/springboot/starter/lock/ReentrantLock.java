package com.mf.plock.springboot.starter.lock;

import com.mf.plock.springboot.starter.model.LockInfo;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class ReentrantLock implements Lock{

    private RLock rLock;
    private RedissonClient redissonClient;

    private LockInfo lockInfo;

    public ReentrantLock(RedissonClient redissonClient, LockInfo lockInfo){
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }
    @Override
    public boolean acquire() {
        try {
            rLock = redissonClient.getLock(lockInfo.getName());
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (rLock.isHeldByCurrentThread()) {
            try {
                return rLock.forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }
}
