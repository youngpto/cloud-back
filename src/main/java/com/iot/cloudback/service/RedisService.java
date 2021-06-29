package com.iot.cloudback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.service
 * @ClassName: RedisService
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 21:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 21:52
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public long getExpire(String key, TimeUnit timeUnit) {
        return stringRedisTemplate.getExpire(key, timeUnit);
    }

    public long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    public boolean setExpire(String key, long timeout, TimeUnit timeUnit) {
        return stringRedisTemplate.expire(key, timeout, timeUnit);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }
}
