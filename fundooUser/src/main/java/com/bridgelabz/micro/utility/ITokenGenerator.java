package com.bridgelabz.micro.utility;

import java.io.UnsupportedEncodingException;

public interface ITokenGenerator {

	String generateToken(String id) throws IllegalArgumentException, UnsupportedEncodingException;

	String verifyToken(String token);
}
