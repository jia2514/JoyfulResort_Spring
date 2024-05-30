package com.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;


@WebFilter
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		System.out.println("Filter被執行");
//		chain.doFilter(request, response);
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
//		System.out.println(req.getContextPath());
		
		// 取得Session
		HttpSession session = req.getSession();
		
		Object account = session.getAttribute("memberID");
		//判斷有無登入
		if (account == null) { //無 跳轉至首頁 
			session.setAttribute("location", req.getRequestURI());
			res.sendRedirect(req.getContextPath() + "/");
			return;
		} else { //有 放行
			chain.doFilter(request, response);
		}


	}

}
