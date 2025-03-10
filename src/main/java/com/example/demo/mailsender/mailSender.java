package com.example.demo.mailsender;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class mailSender {
	 public int sendOtp(String recipientEmail) {
	        final String username = "yourmail@gmail.com";
	        final String password = "your-app-password";

	        // Generate 6-digit OTP
	        int otp = UniqueSixDigitNumberGenerator.generateUniqueNumber();

	        Properties props = new Properties();
	        props.put("mail.smtp.auth", true);
	        props.put("mail.smtp.starttls.enable", true);
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        Session session = Session.getInstance(props, new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	            }
	        });

	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(username));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(recipientEmail)
	            );
	            message.setSubject("SSTREAM OTP");

	            // Include the OTP in the email body
	            message.setText("Your OTP for password reset is: " + otp);

	            Transport.send(message);

	            System.out.println("Email sent successfully!");

	        } catch (MessagingException e) {
	            throw new RuntimeException(e);
	        }
	        return otp;
	    }
}
