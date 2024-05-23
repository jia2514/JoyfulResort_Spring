package com.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class FilterConfig {
	
	@Bean
	public FilterRegistrationBean<LoginFilter> registerMyFilter(){
		
		FilterRegistrationBean<LoginFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new LoginFilter());
		
		bean.addUrlPatterns("/joyfulresort/member/memberinfo");
		return bean;
	}
	
	@Bean
	public FilterRegistrationBean<LoginStateFilter> LoginStateFilter(){
		
		FilterRegistrationBean<LoginStateFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new LoginStateFilter());
		
		bean.addUrlPatterns("/*");
		return bean;
	}
}
