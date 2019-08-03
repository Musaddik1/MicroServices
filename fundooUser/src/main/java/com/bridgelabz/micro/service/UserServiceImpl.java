package com.bridgelabz.micro.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.micro.dto.Email;
import com.bridgelabz.micro.dto.LoginDto;
import com.bridgelabz.micro.dto.UserDto;
import com.bridgelabz.micro.exception.UserException;
import com.bridgelabz.micro.model.User;
import com.bridgelabz.micro.repository.UserRepository;
import com.bridgelabz.micro.utility.EncryptUtil;
import com.bridgelabz.micro.utility.ITokenGenerator;
import com.bridgelabz.micro.utility.MailUtil;
import com.bridgelabz.micro.utility.RabbitMqUtil;

@Service
public class UserServiceImpl implements UserService {

	/**
	 * 
	 */
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	MailUtil mailsender;

	@Autowired
	private RabbitMqUtil rabbitmq;

	@Autowired
	private ITokenGenerator tokenGenerator;

	@Override
	public String registrationUser(UserDto userDto, StringBuffer requestUrl) {

		boolean ismail = userRepository.findByEmail(userDto.getEmail()).isPresent();
		if (!ismail) {
			User user = mapper.map(userDto, User.class);
			user.setPassword(encoder.encode(userDto.getPassword()));
			// user.setCreationTime(LocalTime.now());
			try {
				user.setCreationTime(LocalDateTime.now());
				user.setUpdateTime(LocalDateTime.now());
				User savedUser = userRepository.save(user);
				String token = tokenGenerator.generateToken(savedUser.getUserId());
				String activationUrl = getLink(requestUrl, "/verification/", token);
				Email email = new Email();
				email.setTo("musaddikshaikh10@gmail.com");
				email.setSubject("Account verification");
				email.setBody("Please verify your email id by using below link \n" + activationUrl);
				mailsender.send(email);
				// rabbitmq.rabbitSender(email);
				return "Verification mail send successfully";
			} catch (Exception e) {
				e.printStackTrace();
				throw new UserException("Something not right");
			}
		} else {
			throw new UserException("User already exist");
		}
	}

	@Override
	public String loginUser(LoginDto loginDto) throws IllegalArgumentException, UnsupportedEncodingException {

		Optional<User> optUser = userRepository.findByEmail(loginDto.getEmail());
		if (optUser.isPresent()) {
			User user = optUser.get();

			if (EncryptUtil.isPassword(loginDto, user)) {
				if (user.isVerified()) {
					return tokenGenerator.generateToken(user.getUserId());
				} else {
					throw new UserException("please verify your email");
				}
			} else {
				throw new UserException("incorrect password");
			}

		} else {
			throw new UserException("User not found");
		}

	}

	@Override
	public String forgetPassword(String emailId, StringBuffer requestUrl) {
		Optional<User> optUser = userRepository.findByEmail(emailId);
		if (optUser.isPresent()) {
			User user = optUser.get();
			String id = user.getUserId();
			try {
				String token = tokenGenerator.generateToken(id);
				String resetUrl = "http://localhost:4200/reset/" + token;
				Email email = new Email();
				email.setTo("musaddikshaikh5@gmail.com");
				email.setSubject("resetPassword");
				email.setBody("reset your password \n" + resetUrl);
				mailsender.send(email);
				// rabbitmq.rabbitSender(email);
				return "Mail sent";

			} catch (Exception e) {

				e.printStackTrace();
				throw new UserException("internal server error");
			}
		} else {
			throw new UserException("User not present..");
		}

	}

	@Override
	public String restSetPassword(String token, String password) {
		String userid = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findByUserId(userid);
		if (optUser.isPresent()) {
			User user = optUser.get();
			user.setPassword(encoder.encode(password));
			// user.setPassword(password);
			user.setUpdateTime(LocalDateTime.now());
			userRepository.save(user);
			// return new Response(200, "password changed successfully..", null);
			return "Password changed successfully..";

		} else {
			throw new UserException("User not verified");
		}
	}

	@Override
	public String validateUser(String token) {
		String id = tokenGenerator.verifyToken(token);
		Optional<User> optionalUser = userRepository.findByUserId(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setVerified(true);
			userRepository.save(user);
			return "User verified";
		} else {
			throw new UserException("User not verified");
		}
	}

	@SuppressWarnings("unused")
	private String getLink(StringBuffer requestUrl, String mappingUrl, String token) {
		String url = requestUrl.substring(0, requestUrl.lastIndexOf("/")) + mappingUrl + token;
		return url;
	}

	@Override
	public String getUrl(String token) {
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findById(userId);
		if (optUser.isPresent()) {
			User user = optUser.get();
			return user.getImageUrl();
		}
		return null;
	}

	@Override
	public boolean checkUser(String token) {

		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findById(userId);
		if (optUser.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

}
