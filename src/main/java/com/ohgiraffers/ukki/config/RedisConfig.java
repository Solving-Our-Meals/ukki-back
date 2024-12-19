package com.ohgiraffers.ukki.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // yml에 넘기려고 했는데 yml에서 컴파일 에러가 떠서 config 사용
    @Bean
    public RedisTemplate<String, String> redisTemplate() { // JedisConnectionFactory 설정 -> yml에 하려고했는데 yml에 컴파일에러떠서 넘김(보안상으론 부족할듯..)
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("localhost"); // Redis 서버 호스트 이름 ?
        jedisConnectionFactory.setPort(6379);            // Redis 서버 포트 -> 기본값이 6379
        jedisConnectionFactory.setPassword("ukki");      // Redis 비번

        // 없으면 에러남 (초기화?)
        jedisConnectionFactory.start();

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }
}
