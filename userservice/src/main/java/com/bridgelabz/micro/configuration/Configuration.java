package com.bridgelabz.micro.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.micro.filter.SimpleFilter;
@org.springframework.context.annotation.Configuration
public class Configuration {
	

	@Bean
	@LoadBalanced
	public RestTemplate getreRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		
		return restTemplateBuilder.build();
	}
	@Bean
	  public SimpleFilter simpleFilter() {
	     return new SimpleFilter();
	  }
}
