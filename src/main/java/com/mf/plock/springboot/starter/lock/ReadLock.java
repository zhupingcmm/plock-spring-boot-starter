package com.mf.plock.springboot.starter.lock;

import com.mf.plock.springboot.starter.model.LockInfo;
import lombok.AllArgsConstructor;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class ReadLock implements Lock{
    private RReadWriteLock rLock;
    private RedissonClient redissonClient;

    private LockInfo lockInfo;
    public ReadLock(RedissonClient redissonClient, LockInfo lockInfo){
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }

    @Override
    public boolean acquire() {
        try {
            rLock = redissonClient.getReadWriteLock(lockInfo.getName());
            return rLock.readLock().tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (rLock.writeLock().isHeldByCurrentThread()) {
            try {
                return rLock.readLock().forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }
}
