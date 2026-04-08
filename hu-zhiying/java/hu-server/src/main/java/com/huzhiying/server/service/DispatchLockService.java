package com.huzhiying.server.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DispatchLockService {

    private final ObjectProvider<StringRedisTemplate> redisTemplateProvider;
    private final boolean redisEnabled;
    private final Map<String, Long> localLocks = new ConcurrentHashMap<>();

    public DispatchLockService(ObjectProvider<StringRedisTemplate> redisTemplateProvider,
                               @Value("${hzy.redis.enabled:true}") boolean redisEnabled) {
        this.redisTemplateProvider = redisTemplateProvider;
        this.redisEnabled = redisEnabled;
    }

    public boolean tryLock(String taskId) {
        if (redisEnabled) {
            try {
                StringRedisTemplate redisTemplate = redisTemplateProvider.getIfAvailable();
                if (redisTemplate != null) {
                    Boolean locked = redisTemplate.opsForValue().setIfAbsent(
                            buildKey(taskId),
                            String.valueOf(System.currentTimeMillis()),
                            Duration.ofSeconds(30)
                    );
                    if (Boolean.TRUE.equals(locked)) {
                        return true;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return localLocks.putIfAbsent(taskId, System.currentTimeMillis()) == null;
    }

    public void release(String taskId) {
        if (redisEnabled) {
            try {
                StringRedisTemplate redisTemplate = redisTemplateProvider.getIfAvailable();
                if (redisTemplate != null) {
                    redisTemplate.delete(buildKey(taskId));
                }
            } catch (Exception ignored) {
            }
        }
        localLocks.remove(taskId);
    }

    private String buildKey(String taskId) {
        return "dispatch:task:" + taskId;
    }
}
