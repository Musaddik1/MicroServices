package com.bridgelabz.micro.utility;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bridgelabz.micro.dto.Email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;

@Component
public class RabbitMqUtil {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${fundoo.rabbitmq.exchange}")
	private String exchange;

	@Value("${fundoo.rabbitmq.routingkey}")
	private String routingKey;

	@RabbitListener(queues = "${fundoo.rabbitmq.queue}")
	public void send(Email email) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

		helper.setTo(email.getTo());
		helper.setText(email.getBody());
		helper.setSubject("verification");
		System.out.println("mail send successfully...l");
		javaMailSender.send(message);
		/*
		 * simpleMailMessage.setTo(email.getTo());
		 * simpleMailMessage.setText(email.getBody());
		 * simpleMailMessage.setSubject("verification");
		 * System.out.println("mail send successfully...l");
		 * javaMailSender.send(simpleMailMessage);
		 */

	}

	@RabbitListener(queues = "${fundoo.rabbitmq.queue}")
	public void sendforget(Email email) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(email.getTo());
		helper.setText(email.getBody());
		helper.setSubject("forget");
		System.out.println("mail send successfully..");
		javaMailSender.send(message);

	}

	public void rabbitSender(Object email) {

		System.out.println("Entering Queue");
		rabbitTemplate.convertAndSend(exchange, routingKey, email);
	}

}
