package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan(basePackages = "com.filters.LoginFilter, com.filters.LoginStateFilter")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
