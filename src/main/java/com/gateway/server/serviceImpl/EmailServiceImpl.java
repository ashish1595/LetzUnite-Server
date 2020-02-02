package com.gateway.server.serviceImpl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.gateway.server.exception.UtilityException;

@EnableAsync
@Component
public class EmailServiceImpl {

	public static Properties initParam() {
		Properties props = System.getProperties();
		props.setProperty("mail.transport.protocol", "smtp");     
		props.setProperty("mail.host", "smtp.gmail.com");  
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
		props.put("mail.smtp.socketFactory.fallback", "false");  
		return props;
	}

	@Async
	public Boolean processSendMailOperation(String from, String to, String subject, String bodyText) throws UtilityException {
		String[] toArray = to.split(",");
		Session mailSession = Session.getInstance(initParam(),  
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from, "Airtel@123");  
			}  
		});  
		MimeMessage message = new MimeMessage(mailSession);
		InternetAddress[] addressTo = new InternetAddress[toArray.length];
		for (int i = 0; i < toArray.length; i++) {
			try {
				addressTo[i] = new InternetAddress(toArray[i]);
			} catch (AddressException e) {
				System.out.println("err");
			}
		}
		try {
			message.setRecipients(Message.RecipientType.TO, addressTo);
			message.setSentDate(new java.util.Date());
			message.setSubject(subject);
			message.setFrom(new InternetAddress(from));

			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(bodyText, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message, message.getAllRecipients());
			return Boolean.TRUE;
		} catch (MessagingException e) {
			System.out.println("err");
			return Boolean.FALSE;
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}  

