package com.bridgelabz.micro.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bridgelabz.micro.dto.LoginDto;
import com.bridgelabz.micro.model.User;


public class EncryptUtil {

	public String encryptPassword(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	public static boolean isPassword(LoginDto loginDto, User user) {

		return new BCryptPasswordEncoder().matches(loginDto.getPassword(), user.getPassword());
	}

}
