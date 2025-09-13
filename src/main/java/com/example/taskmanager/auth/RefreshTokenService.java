package com.example.taskmanager.auth;

import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    private final StringRedisTemplate redis;
    private final Duration ttl;

    public RefreshTokenService(StringRedisTemplate redis,
                               com.example.taskmanager.security.JwtProperties props) {
        this.redis = redis;
        this.ttl = props.getRefreshTokenTtl();
    }

    public void store(String refreshToken, String username) {
        redis.opsForValue().set(key(refreshToken), username, ttl);
    }

    public boolean exists(String refreshToken) {
        Boolean has = redis.hasKey(key(refreshToken));
        return has != null && has;
    }

    public String consume(String refreshToken) {
        String k = key(refreshToken);
        String username = redis.opsForValue().get(k);
        if (username != null) redis.delete(k); // одноразовый refresh (рекомендуется)
        return username;
    }

    public void revoke(String refreshToken) {
        redis.delete(key(refreshToken));
    }

    private String key(String token) { return "refresh:" + token; }
}
