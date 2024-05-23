package com.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

@WebFilter
public class LoginStateFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		System.out.println("檢查預覽器紀錄登入狀態的cookie");
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession session = req.getSession();
		
		Cookie[] cookies = req.getCookies();
		JSONObject obj = new JSONObject();
		//取得cookie 並裝入JSON物件內
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
//				System.out.println(cookie.getName() + " " + cookie.getValue()); 
				obj.put(cookie.getName(), cookie.getValue());
			}
		}
		
		try {
//			System.out.println(obj.get("LogInState"));
			if(obj.get("LogInState").equals("200") && obj.get("MemberID") != null) {
//				System.out.println("預覽器有紀錄登入狀態");
				//寫入登入資訊
				
				session.setAttribute("memberID", obj.get("MemberID"));
				
//				System.out.println("寫入成功");
				
			} else {
//				System.out.println("預覽器紀錄登入狀態錯誤");
			}
		} catch (JSONException e) {
//			System.out.println("查無cookie 預覽器沒有紀錄登入狀態"); 
			
			
			// 移除登入狀態的Cookie
			Cookie cookie = new Cookie("LogInState", "200");
			cookie.setMaxAge(0);  //設定 cookie 存活時間 0-->立刻失效
			cookie.setPath("/");
			res.addCookie(cookie);
					
			Cookie id = new Cookie("MemberID", "0");
			id.setMaxAge(0);  //設定 cookie 存活時間 0-->立刻失效
			id.setPath("/");
			res.addCookie(id);
		}
		
		//當後台Spring重啟後 紀錄登入狀態的 session 會消失 造成前後台登入狀態不一致
		//不管如何都會放行 只是檢查預覽器端有無 cookie紀錄登入狀態 有就寫入登入狀態的session
		chain.doFilter(request, response);
	}

}
