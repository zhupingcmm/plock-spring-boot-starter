package com.mf.plock.springboot.starter;

import com.mf.plock.springboot.starter.config.PlockConfig;
import com.mf.plock.springboot.starter.core.BusinessKeyProvider;
import com.mf.plock.springboot.starter.core.LockInfoProvider;
import com.mf.plock.springboot.starter.core.PlockAspectHandler;
import com.mf.plock.springboot.starter.exception.PlockExeception;
import com.mf.plock.springboot.starter.lock.LockFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.plock", name = "enable", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(PlockConfig.class)
@Import({PlockAspectHandler.class})
public class PlockAutoConfiguration {

    @Autowired
    private PlockConfig plockConfig;


    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient () {
        try {
            Config config = new Config();
            if (plockConfig.getClusterServer() != null) {
                // cluster mode
                config.useClusterServers()
                        .setPassword(plockConfig.getPassword())
                        .addNodeAddress(plockConfig.getClusterServer().getNodeAddress());
            } else {
                // standalone mode
                config.useSingleServer()
                        .setAddress(plockConfig.getAddress())
                        .setDatabase(plockConfig.getDatabase())
                        .setPassword(plockConfig.getPassword());
            }
            Codec codec = (Codec) Class.forName(plockConfig.getCodec(), true, this.getClass().getClassLoader()).newInstance();
            config.setCodec(codec);
            config.setEventLoopGroup(new NioEventLoopGroup());
            log.info("connecting redis server");
            return Redisson.create(config);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            log.error("failed to connect redis", e);
            throw new PlockExeception("failed to connect redis", e);
        }
    }

    @Bean
    public LockInfoProvider lockInfoProvider(){
        return new LockInfoProvider();
    }

    @Bean
    public LockFactory lockFactory(){
        return new LockFactory();
    }

    @Bean
    public PlockConfig plockConfig(){
        return new PlockConfig();
    }

    @Bean
    public BusinessKeyProvider businessKeyProvider () {
        return new BusinessKeyProvider();
    }


}
