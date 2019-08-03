package com.bridgelabz.micro.controller;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.micro.dto.LoginDto;
import com.bridgelabz.micro.dto.UserDto;
import com.bridgelabz.micro.response.Response;
import com.bridgelabz.micro.service.UserService;

@RestController
@RequestMapping("/userservice")
@CrossOrigin(origins = "*",allowedHeaders = {"*"})
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RestTemplate restTemplate;
	/*
	 * @Autowired private AmazonService amazonService;
	 */
	@PostMapping("/register")
	public ResponseEntity<Response> registerUser( @RequestBody UserDto userDto, HttpServletRequest request) {
		StringBuffer requestUrl = request.getRequestURL();
		String message = userService.registrationUser(userDto, requestUrl);
		Response response = new Response(200, message, null);
		
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto, HttpServletResponse httpServletResponse)
			throws IllegalArgumentException, UnsupportedEncodingException {
		String token = userService.loginUser(loginDto);
		httpServletResponse.setHeader("Authorization", token);
		String url = "http://localhost:8080/noteservice/notes";
		HttpHeaders headers = new HttpHeaders();
		headers.set("token",token);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		System.out.println("header-->" + headers); // Note noteId =
		//restTemplate.getForObject(url, Note.class);
		ResponseEntity<Object[]> notes = restTemplate.exchange(url,HttpMethod.GET ,entity,Object[].class);
		System.out.println(notes.getBody().toString());
		Response response = new Response(200, "User logged in successfully", token);
		return new ResponseEntity<>(notes.getBody(), HttpStatus.OK);
	}

	@GetMapping("/forget")
	public ResponseEntity<Response> forgotPassword(@RequestParam String emailId, HttpServletRequest request) {
		System.out.println("Forget Paasssssssssssssss="+emailId);
		
		StringBuffer requestUrl = request.getRequestURL();
		// Response response = userService.forgetPassword(emailId, requestUrl);
		String message = userService.forgetPassword(emailId, requestUrl);
		Response response = new Response(200, message, null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PutMapping("/resetpassword")
	public ResponseEntity<Response> resetPassword(@RequestHeader String token, @RequestParam String password) {
		System.err.println("fdslkjas;l");
		String message = userService.restSetPassword(token, password);
		Response response = new Response(HttpStatus.OK.value(), message, token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

	@GetMapping("/verification/{token}")
	public ResponseEntity<Response> mailValidation(@PathVariable String token) {
		String message = userService.validateUser(token);
		Response response = new Response(HttpStatus.OK.value(), message, null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@GetMapping("/geturl")
	public String getUrl(@RequestHeader String token)
	{
		return userService.getUrl(token);
	}
	
	@GetMapping("/checkUser/{token}")
	public boolean checkUser(@PathVariable String token)
	{
		return userService.checkUser(token);
	}

}
