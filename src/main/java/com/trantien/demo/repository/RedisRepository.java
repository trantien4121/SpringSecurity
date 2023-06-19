package com.trantien.demo.repository;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisRepository {
    private RedisTemplate<String, Object> redisTemplate;

    public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    public Object find(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

}
