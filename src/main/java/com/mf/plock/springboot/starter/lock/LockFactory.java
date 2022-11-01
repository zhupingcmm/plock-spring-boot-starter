package com.mf.plock.springboot.starter.lock;

import com.mf.plock.springboot.starter.model.LockInfo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;


public class LockFactory {

    @Autowired
    private RedissonClient redissonClient;

    public Lock getLock(LockInfo lockInfo) {
        switch (lockInfo.getLockType()) {
            case Fair:
                return new FairLock(redissonClient,lockInfo);
            case Write:
                return new WriteLock(redissonClient, lockInfo);
            case Read:
                return new ReadLock(redissonClient, lockInfo);
            default:
                return new ReentrantLock(redissonClient, lockInfo);

        }
    }


}
