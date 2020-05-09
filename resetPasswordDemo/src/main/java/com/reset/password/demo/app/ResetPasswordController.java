package com.reset.password.demo.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * A Collection of {@link RestController} class that will accept HTTP request to interact with the JS client
 * A Collection of REST-API's for validating and authenticating user's login, register, update password and logout 
 * @author Hoffman
 *
 */
@RestController
public class ResetPasswordController {

	@Autowired
	private PersonDAO personStub;
	@Autowired
	private AccessTokenService tokenStub;
	@Autowired
	private EmailService emailStub;

	private static final String USER = "user";
	private static final String GENERAL_ERROR = "Something  Went Wrong";
	private static final String LOGIN_ERROR = "Username or password are incorrect";

	@RequestMapping(path = "/reset-pass-demo/registration", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public Response registration(@RequestBody(required = true) Person person, @Context HttpServletRequest req, @Context HttpSession session) {
		if (!ResetPasswordDemoUtil.emailValidator(person.getEmail())) {
			return Response.status(200).entity(new Message("*Plaese enter a valid email address")).build();
		}
		else if (person.getEmail().equals(person.getPassword())) {
			return Response.status(200).entity(new Message("*Username and password cannot be the same")).build();
		}
		else if (personStub.findByEmail(person.getEmail())!=null) {
			return Response.status(200).entity(new Message("*Username already exist")).build();
		}
		Person newPersonEntry = personStub.signup(person);
		if (null != newPersonEntry) {
			session = req.getSession(true);
			session.setAttribute(USER, person);
			return Response.status(201).entity(newPersonEntry).build();
		}
		return Response.status(400).entity(new Message(GENERAL_ERROR)).build();
	}

	@RequestMapping(path = "/reset-pass-demo/login", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public Response login(@RequestBody(required = true) Person person, @Context HttpServletRequest req, @Context HttpSession session) {
		Person loggedInPerson = personStub.login(person);
		if (null != loggedInPerson) {
			session = req.getSession(true);
			session.setAttribute(USER, person);
			return Response.status(200).entity(loggedInPerson).build();
		}
		return Response.status(400).entity(new Message(LOGIN_ERROR)).build();
	}

	@RequestMapping(path = "/reset-pass-demo/logout", method = RequestMethod.POST)
	public Response logout(@Context HttpSession session) {
		session.removeAttribute(USER);
		session.invalidate();
		if (session != null) {
			session = null;
		}
		return Response.status(200).entity(Status.OK.getReasonPhrase()).build();

	}

	@RequestMapping(path = "/reset-pass-demo/updatePassword", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
	public Response updatePassword(@RequestBody ResetPasswordRequest resetPasswordRequest, @Context HttpServletRequest req, HttpSession session) {
		Person person = personStub.findByEmail(resetPasswordRequest.getEmail());
		if (StringUtils.isEmpty(resetPasswordRequest.getNewPassword())) {
			return Response.status(200).entity(new Message("*A new Password Is Required")).build();
		}
		if (tokenStub.isEligible(person, resetPasswordRequest.getToken())) {
			personStub.updatePassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getNewPassword());
			return Response.status(200).entity(new Message("Your password has been rest successfully")).build();
		}
		return Response.status(401).entity(new Message("*Request not authorized")).build();
	}

	@RequestMapping(value = "/reset-pass-demo/forgotMyPassword", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
	public Response sendConformationMailTo(@RequestBody String sendConformationMailTo, @Context HttpServletResponse res, @Context HttpServletRequest req) {
		if (!ResetPasswordDemoUtil.emailValidator(sendConformationMailTo)) {
			return Response.status(200).entity(new Message("*Plaese enter a valid email address")).build();
		}
		if (emailStub.sendResetPasswordMail(sendConformationMailTo, req)) {
			return Response.status(200).entity(new Message("A confirmation link has been sent to your email address")).build();
		}
		return Response.status(200).entity(new Message("*This email is not registered in our website")).build();
	}

}
