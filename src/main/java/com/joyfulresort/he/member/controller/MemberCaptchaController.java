package com.joyfulresort.he.member.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberCaptchaController {
	
	@Autowired 
	StringRedisTemplate redis; //Spring boot redis
	
	//驗證碼字元 2-9, A-Z排除ILO, a-z排除ilo;
	private static final String BASE_CHAR = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
	private static final Integer BASE_CHAR_LEN = BASE_CHAR.length();
	
	//驗證碼長度
	private static final Integer CAPTCHA_LEN = 4;

	// 圖片寬度,高度
	private static final Integer WIDTH = 150;
	private static final Integer HEIGHT = 50;
	
	@GetMapping("/getCode")
	public void memberCaptcha(HttpServletRequest req, HttpServletResponse res) {
		// 創建一張圖片 指定寬和高
		BufferedImage captchaImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		// Graphics 類別是所有圖形上下文的抽象基礎類別，允許應用程序在元件（已經在各種設備上實作）以及閉屏圖像上進行繪製。
		Graphics graphics = captchaImage.getGraphics();
		Graphics2D g = (Graphics2D) graphics;

		g.setColor(Color.WHITE); // 背景色
		g.fillRect(0, 0, WIDTH, HEIGHT); // 繪製範圍(坐標系原點在左上角)

		g.setColor(Color.BLACK); // 圖片邊框顏色
		g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);

		// 透過 graphics 繪製干擾線
		drawNoiseLine(g);

		// 圖片上的隨機數
		String code = drawCode(g);
		
		//將隨機數存入redis中
		redis.opsForValue().set("memberCaptcha", code, 1,TimeUnit.MINUTES );
		
		//圖片顯示到預覽器上
		responseImage(res, captchaImage, g);
	}

	// 干擾線
	private void drawNoiseLine(Graphics2D g) {
		Random random = new Random();		
		for (int i = 0; i < 12; i++) {// 干擾線的數量
			int x1 = random.nextInt(WIDTH);
			int y1 = random.nextInt(HEIGHT);
			int x2 = random.nextInt(WIDTH);
			int y2 = random.nextInt(HEIGHT);
			g.setColor(new Color(random.nextInt(225), random.nextInt(225), random.nextInt(225)));
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	//隨機數(驗證碼)
	private String drawCode(Graphics2D g) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		
		Font font = new Font(g.getFont().getName(), Font.ITALIC, 40); //字體設定
		g.setFont(font);//寫入設定
		
		int x = 20; // 繪製起始範圍
		int y = 40; // 繪製起始範圍
		//控制字數
		for(int i = 0; i < CAPTCHA_LEN; i++) {
			g.setColor(new Color(random.nextInt(225), random.nextInt(225), random.nextInt(225))); //畫筆(驗證碼)顏色
			String ch = String.valueOf(BASE_CHAR.charAt(random.nextInt(BASE_CHAR_LEN)));
			sb.append(ch);
			//設置字體旋轉角度
			int degree = random.nextInt() % 30; //偏移角度(旋轉字體)
			g.rotate(degree * Math.PI / 180, x, 25);
			g.drawString(ch, x, y);
			//反向角度(字體轉回)
			g.rotate(-degree * Math.PI / 180, x, 25);
			x += 25; //下一個字的開始起點
		}
		return sb.toString();
	}
	//將圖片給預覽器
	private void responseImage(HttpServletResponse res, BufferedImage bi, Graphics2D g) {
		res.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		//設置預覽器禁用緩存
		res.setDateHeader(HttpHeaders.EXPIRES, 0);
		res.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
		res.setHeader(HttpHeaders.PRAGMA, "no-cache");
		
		try(ServletOutputStream outputStream = res.getOutputStream()){
			ImageIO.write(bi, "jpeg", outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			g.dispose();
		}
	}
}
