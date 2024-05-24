package com.joyfulresort.he.member.controller;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class TestSecurity {
	public static void main(String[] args) {

		// 生成公私鑰
		KeyPairGenerator keyPairGenerator = null;

		try { //創建密鑰生成器 算法有DiffieHellman, DSA, RSA 三種
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		//初始化 長度需大於加密內容
		keyPairGenerator.initialize(2048);
		
		//生成密鑰對
		KeyPair pair = keyPairGenerator.generateKeyPair();
		
		//取得公鑰
		PublicKey publicKey = pair.getPublic();
		System.out.println(publicKey);
		System.out.println("--------------------");
		//取得私鑰
		PrivateKey privateKey = pair.getPrivate();
		
		
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE,privateKey);//私鑰加密
			byte[] b = cipher.doFinal("鄭禾昇".getBytes());//對"鄭禾昇"加密
			System.out.println(new String(b));
			System.out.println("--------------------");
			
			cipher.init(Cipher.DECRYPT_MODE, publicKey);//公鑰解密
			b = cipher.doFinal(b);
			System.out.println(new String(b));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

}
