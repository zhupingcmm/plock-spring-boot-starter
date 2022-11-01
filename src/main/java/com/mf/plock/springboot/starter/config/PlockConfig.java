package com.mf.plock.springboot.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.plock")
public class PlockConfig {

    /**
     * host: port
     */
    private String address;

    /**
     * redis password
     */
    private String password;


    private int database = 15;

    /**
     *  the time about wait to acquire lock
     */
    private long waitTime = 30;

    /**
     *
     *the time about wait to lease lock
     */
    private long leaseTime = 30;

    /**
     *  the redis Serializable method
     */
    private String codec = "org.redisson.codec.JsonJacksonCodec";

    private ClusterServer clusterServer;

    @Data
    public static class ClusterServer {
        private String [] nodeAddress;
    }
}
