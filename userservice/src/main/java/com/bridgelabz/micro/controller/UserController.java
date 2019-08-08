package com.bridgelabz.micro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.micro.filter.SimpleFilter;

@RestController
@RequestMapping("/userservice")
public class UserController {

	@Autowired
	private RestTemplate rest;
	@Autowired
	private SimpleFilter simpleFilter;

	@GetMapping("/user")
	public String getUser() {
		String url = "http://NOTE-SERVICE/";
		String name = rest.getForObject(url, String.class);
		
		return name;
	}
}
