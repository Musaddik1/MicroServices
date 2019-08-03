package com.bridgelabz.micro.service;

import java.io.UnsupportedEncodingException;

import com.bridgelabz.micro.dto.LoginDto;
import com.bridgelabz.micro.dto.UserDto;

public interface UserService {

	String registrationUser(UserDto userDto, StringBuffer requestUrl);

	String loginUser(LoginDto loginDto) throws IllegalArgumentException, UnsupportedEncodingException;

	String forgetPassword(String emailId,StringBuffer requestUrl);

	String restSetPassword(String token,String password);
	
	//String validateUser(String token);
	String validateUser(String token);

	String getUrl(String token);
	
	boolean checkUser(String token);
	
}
