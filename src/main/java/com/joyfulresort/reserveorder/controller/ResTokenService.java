package com.joyfulresort.reserveorder.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import jedis.connectionpool.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class ResTokenService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	private JedisPool pool = JedisUtil.getJedisPool();

	private static final String TOKEN_PREFIX = "FrontReserve:";

	public String generateToken() {
		String token = UUID.randomUUID().toString();
		redisTemplate.opsForValue().set(TOKEN_PREFIX + token, token, 60, TimeUnit.MINUTES);
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

//	public String Token() {
//		String token = UUID.randomUUID().toString();
//		try (Jedis jedis = pool.getResource()) {
//			jedis.select(3);
//			String key = "FrontReserve:" + token;
//			jedis.set(key, token);
//			System.out.println(key);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return token;
//	}
//
//	public boolean valiToken(String token) {
//		boolean isValid = false;
//		try (Jedis jedis = pool.getResource()) {
//			jedis.select(3);
//			String key = "FrontReserve:" + token;
//			String redisToken = jedis.get(key);
//			if (redisToken != null && redisToken.equals(token)) {
//				jedis.del(key);
//				isValid = true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return isValid;
//	}
}