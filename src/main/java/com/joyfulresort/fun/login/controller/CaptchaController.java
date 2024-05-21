package com.joyfulresort.fun.login.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequestMapping("/captcha")
public class CaptchaController {

    @GetMapping("/generate")
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int width = 160;
        int height = 40;
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.BOLD, 30));

        // 干擾線
        for (int i = 0; i < 10; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawLine(x1, y1, x2, y2);
        }

        StringBuilder captchaText = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            char c = chars[random.nextInt(chars.length)];
            captchaText.append(c);
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            
            // 字符扭曲
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 8 * random.nextDouble() * (random.nextBoolean() ? 1 : -1), (20 * i + 10) + 15 / 2.0, 30 / 2.0);
            g.setTransform(affine);
            g.drawString(String.valueOf(c), 20 * i + 10, 30);
        }

        HttpSession session = request.getSession();
        session.setAttribute("captcha", captchaText.toString());

        response.setContentType("image/jpeg");
        ImageIO.write(image, "jpeg", response.getOutputStream());
    }
}
