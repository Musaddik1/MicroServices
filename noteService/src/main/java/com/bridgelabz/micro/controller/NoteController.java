package com.bridgelabz.micro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("noteservice")
public class NoteController {

	@GetMapping("/data")
	public String getData()
	{
		return "hello";
	}
	@GetMapping("/data1")
	public String getData1()
	{
		return "hi";
	}
}
