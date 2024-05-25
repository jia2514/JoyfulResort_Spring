package com.joyfulresort.fun.empmail.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service("tokenStore")
public class TokenStore {

    private final Map<String, TokenInfo> tokenMap = new HashMap<>();

    public void storeToken(String token, Integer empAccount) {
        long expirationTime = System.currentTimeMillis() + 15 * 60 * 1000; // 15 分鐘後過期
        tokenMap.put(token, new TokenInfo(empAccount, expirationTime));
    }

    public Integer getEmpAccountByToken(String token) {
        TokenInfo tokenInfo = tokenMap.get(token);
        if (tokenInfo != null && tokenInfo.getExpirationTime() > System.currentTimeMillis()) {
            return tokenInfo.getEmpAccount();
        }
        return null;
    }

    public void removeToken(String token) {
        tokenMap.remove(token);
    }

    private static class TokenInfo {
        private final Integer empAccount;
        private final long expirationTime;

        public TokenInfo(Integer empAccount, long expirationTime) {
            this.empAccount = empAccount;
            this.expirationTime = expirationTime;
        }

        public Integer getEmpAccount() {
            return empAccount;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }
}

