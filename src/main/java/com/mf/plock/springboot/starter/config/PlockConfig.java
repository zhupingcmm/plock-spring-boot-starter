package com.mf.plock.springboot.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.plock")
public class PlockConfig {

    private String address;

    private String password;

    private int database = 15;

    private long waitTime = 30;

    private long leaseTime = 30;

    private String codec = "org.redisson.codec.JsonJacksonCodec";

    private ClusterServer clusterServer;

    @Data
    public static class ClusterServer {
        private String [] nodeAddress;
    }
}
