package com.reset.password.demo.app;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;



/**
 * This Service class implements Email Messaging services to be use by the 'Forgot my password flow'.
 * 
 * @author Oren Hoffman
 * @date 28/6/2017
 *
 */

@Component
//Please create your own 'email.properties' file with your email credentials as mine with 'git ignore'.
@PropertySource("classpath:email.properties")
public class EmailService {
	private static final Logger log = Logger.getLogger(EmailService.class);

	/*
	 * Spring Dependency Injection
	 */
	@Autowired
	private PersonService peesonStub;
	@Autowired
	private AccessTokenService  accessTokenService ;
	
	
	/*
	 * Retrieving Environment Variables for email credentials
	 */
	@Value("${emailUsername}")
	private String emailUsername;
	@Value("${emailPassword}")
	private String emailPassword;
	private static final String RESET_PASS_URL_REPLACEMENT = "inject-reset-password-url" ;



/**
 * Method will send mail by a given {@link MailMessageRequest}  - (Configured for smtp.gmail.com)
 * @param messageRequest
 * @return {@link Boolean}
 */
	public boolean sendMail(MailMessageRequest messageRequest) {

		//Validate the user's email
		if (!ResetPasswordDemoUtil.emailValidator(messageRequest.getSentTo())) {
			return false;
		}

		//Please notice that email @java.util.Properties configured to be used with Gmail account, Configured as you wish.
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.setProperty("mail.transport.protocol", "smtp");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailUsername,emailPassword);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailUsername, "Reset-Password Application"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(messageRequest.getSentTo()));
			message.setSubject(messageRequest.getSubject());
			message.setText("Reset-Password Application");
			message.setContent(messageRequest.getBody(), "text/html");
			message.setReplyTo(messageRequest.getReplayTo());
			Transport.send(message);
			log.info("An e-mail has been sent to " + messageRequest.getSentTo() + " at --> " + new Date());
		} catch (Exception e) {
			log.info("Reset password e-mail to ------> " + messageRequest.getSentTo() + " has faild.", e);
			return false;
		}
		return true;
	}




/**
 * Method will prepare the 'Reset Password email' template to be send
 * @param sendConformationMailTo
 * @return {@link MailMessageRequest}
 */
	public MailMessageRequest prepareResetPasswordMail(String sendConformationMailTo ,HttpServletRequest req) {
		Person temp = peesonStub.findByEmail(sendConformationMailTo);
		if (temp == null) {
			return null;
		}
		//Generate Access-Token
		String token = ResetPasswordDemoUtil.getSaltString();
		String body = createResetPasswordTemplate(req,token,sendConformationMailTo) ;
		//Inject the 'reset.html' end-point and the Access-Token in the URL and Create the template to be sent
		MailMessageRequest message = new MailMessageRequest(sendConformationMailTo,"Reset password for your Reset-Password-Demo account",body,"donotrelplay@reset.com");
		//Register the Access-Token
		accessTokenService.addAccessToken(sendConformationMailTo, token);
		//Send Mail to user
		return message ;

	}
	
	/**
	 * Method will send a 'Reset Password Mail' email by a given {@link MailMessageRequest} 
	 * @param messageRequest
	 * @return {@link Boolean}
	 */
	public boolean sendResetPasswordMail(String sendConformationMailTo ,HttpServletRequest req) {
		MailMessageRequest message = prepareResetPasswordMail(sendConformationMailTo,req) ;
		if (message == null) {
			return false ;
		}
		return sendMail(message);
	}
	
	
	

	
	/**
	 * Method will create the 'Reset Password Mail' email-template
	 * @param req
	 * @param token
	 * @param sendConformationMailTo
	 * @return {@link String}
	 */
private String createResetPasswordTemplate(HttpServletRequest req, String token ,String sendConformationMailTo)   {
		String host = "http"+ResetPasswordDemoUtil.getMachineHostName(req);
		String urlToInject =null;
		String content = null;
	try {
		 urlToInject = host+ "/reset.html?token=" + token+ "&email=" + sendConformationMailTo;
		 InputStream is = getClass().getResourceAsStream("/reset-password-mail.html");
		 content =ResetPasswordDemoUtil.getEmailTemplateFromClasspath(is) ;
	} catch (Exception e) {
		log.error("Faild to inject \"Reset Password URL\" in Rest password Email template" , e);
	}
	   content = content.replace(RESET_PASS_URL_REPLACEMENT , urlToInject) ;
	   return content;
}


}
