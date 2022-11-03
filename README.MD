# plock-spring-boot-starter

plock-spring-boot-starter is a spring starter about Distributed lock, use redis mutually exclusive properties.

Quick start

1. have build this project

``` shell
    maven isnatll -DskipTests=true
```
2. add dependence to your project

``` properties
<dependency>
    <groupId>com.mf</groupId>
    <artifactId>plock-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

3. use @Plock annotation in the method

```java
    @Override
    @Plock(name = "name", keys = {"#user.id", "#user.name"})
    public String getOrder(User user) {
        return "abc";
    }
```
4. add config in your application.yaml

```properties
spring:
  plock:
    address: xxx:6379
```

## Parameter Description

```properties
spring.plock.name=${lock_name} // lock name
spring.plock.waitTime=${lock_wait_time} // the max acquire lock time
spring.plock.leaseTime=${lock_lease_time} // the max  release lock  time
spring.plock.type=${lock_type} // the type of lock
spring.plock.keys=${lock_name_keys} // combine the keys and lock name as full lock name 
spring.plock.lockTimeoutStrategy=${strategy} // get lock timeout, use this strategy
spring.plock.releaseTimeoutStrategy=${strategy} // release lock timeout, use this strategy
```
### lock type
```properties
ReentrantLock
ReadLock
WriteLock
FairLock
```

### lock timeout strategy 

````properties
NO_OPERATION
FAIL_FAST
KEEP_ACQUIRE
````

### release lock timeout strategy

````properties
NO_OPERATION,
FAIL_FAST
````