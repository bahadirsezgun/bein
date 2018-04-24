package com.beinplanner.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ReCaptchaFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
		System.out.println("LOGIN FILTER WORKS");
		/*
		String response = req.getParameter("recaptcha_response_field");
		String challenge = req.getParameter("recaptcha_challenge_field");
 
		if (challenge != null) {
			String remoteAddr = req.getRemoteAddr();
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			reCaptcha.setPrivateKey(privateKey);
			ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, response);
 
			boolean captchaOK = reCaptchaResponse.isValid();
			userDAO.setCaptchaOK(captchaOK);
			if (!captchaOK) {
				log.debug("CAPTCHA *NOT* OK");
			} else {
				log.debug("CAPTCHA OK");
			}
		}

		chain.doFilter(req, resp); */
		
	}

	
	
}
