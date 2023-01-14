package com.bootcamp.java.product.config;

import com.bootcamp.java.product.dto.ProductDTO;
import com.bootcamp.java.product.dto.ProductResponseDTO;
import com.bootcamp.java.product.service.product.ProductServiceImplWithRedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class RedisConfig {

    //private static final String KEY = "productresponse";
    //@Value("${KEY_REDIS_NAME:productresponse}")
    public static String key;

    @Value("${KEY_REDIS_NAME:productresponse}")
    public void setKey(String KEY_REDIS_NAME) {
        RedisConfig.key = KEY_REDIS_NAME;
    }

    @Bean
    public ReactiveHashOperations<String, Integer, ProductResponseDTO> hashOperations(ReactiveRedisConnectionFactory redisConnectionFactory){
        var template = new ReactiveRedisTemplate<>(
                redisConnectionFactory,
                RedisSerializationContext.<String, ProductResponseDTO>newSerializationContext(new StringRedisSerializer())
                                         .hashKey(new GenericToStringSerializer<>(Integer.class))
                                         .hashValue(new Jackson2JsonRedisSerializer<>(ProductResponseDTO.class))
                                         .build()
        );

        /*template.expire(KEY, Duration.ofMillis(2000));

        Instant instant = new Date().toInstant();
        log.info("instant: {}", instant);
        log.info("instant.plus: {}", instant.plusSeconds(10));

        template.expireAt(KEY, instant.plusSeconds(10));
        */
        return template.opsForHash();
    }


}
