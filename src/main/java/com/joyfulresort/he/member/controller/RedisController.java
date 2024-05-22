package com.joyfulresort.he.member.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //@RestController 相當於@Controller搭配@ResponseBody
@RequestMapping("/redis")
public class RedisController {
	
	@Autowired
	StringRedisTemplate redis;
	
	@GetMapping("/getAuthCode")
	public String getAuthCode() {
		String code = returnAuthCode();
		redis.opsForValue().set("AuthCode", code, 1,TimeUnit.MINUTES );
		return redis.opsForValue().get("AuthCode");
	}
	
	
	
	//驗證碼生產器
	 String returnAuthCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 8; i++) {
			int condition = (int) (Math.random() * 3) + 1;
			switch (condition) {
			case 1:
				char c1 = (char) ((int) (Math.random() * 26) + 65);
				sb.append(c1);
				break;
			case 2:
				char c2 = (char) ((int) (Math.random() * 26) + 97);
				sb.append(c2);
				break;
			case 3:
				sb.append((int) (Math.random() * 10));
			}
		}
		return sb.toString();
	}
}
