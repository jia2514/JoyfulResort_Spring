package com.joyfulresort.reserveorder.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResTokenService {

	
	public void addReserveorder(Integer ResId,int token) {
		
	}
	
	
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String TOKEN_PREFIX = "FrontReserve:";

    public String generateToken() {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(TOKEN_PREFIX + token, token, 5, TimeUnit.MINUTES);
        return token;
        
    }

    public boolean validateToken(String token) {
        String key = TOKEN_PREFIX + token;
        String redisToken = redisTemplate.opsForValue().get(key);
        if (redisToken != null && redisToken.equals(token)) {
            redisTemplate.delete(key);
            return true;
        }
        
        return false;
    }
}