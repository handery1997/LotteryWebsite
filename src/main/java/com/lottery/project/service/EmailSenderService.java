package com.lottery.project.service;

import javax.mail.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(String userMail, String password) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("LotteryWebsite");
		simpleMailMessage.setTo(userMail);
		simpleMailMessage.setSubject("Reset password");
		simpleMailMessage.setText("Your new password is: " + password);
		javaMailSender.send(simpleMailMessage);
	}
	
	public void sendEmailAdd(String userMail, String password) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("LotteryWebsite");
		simpleMailMessage.setTo(userMail);
		simpleMailMessage.setSubject("Your new account is created");
		simpleMailMessage.setText("Your password is: " + password + "\nNow your can log in with your email and this password!");
		javaMailSender.send(simpleMailMessage);
	}
}
