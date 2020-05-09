package com.reset.password.demo.app;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

/**
 * Application HTTP Filter Provides the HTTP request validation of the user for
 * redirect
 * 
 * @author Hoffman
 *
 */
@Component
public class ResetPassDemoFilter implements Filter {

	private static final String USER = "user";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		String requestUrl = request.getRequestURL().toString();

		if (session != null && null != session.getAttribute(USER)) {
			if (requestUrl.contains("/resources") || requestUrl.endsWith("index.html") || requestUrl.contains("/reset-pass-demo") || requestUrl.contains("/h2")) {
				chain.doFilter(request, response);
				return;
			} else {
				String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/index.html";
				response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				response.setHeader("Location", url);
				return;
			}
		}

		else if (session == null || (session != null && null == session.getAttribute(USER))) {
			if (requestUrl.contains("/resources") || requestUrl.contains("/reset-pass-demo") || requestUrl.contains("/login.html") || requestUrl.contains("signup.html") || requestUrl.contains("reset.html") || requestUrl.contains("/h2")) {
				chain.doFilter(request, response);
				return;
			} else {
				String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/signup.html";
				response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				response.setHeader("Location", url);
				return;
			}

		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}