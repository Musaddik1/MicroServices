package com.bridgelabz.interceptor.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
public class LoggerInterceptor extends WebMvcConfigurerAdapter {

	@Autowired
	private InterceptorConfig logInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(logInterceptor);
	}

}
