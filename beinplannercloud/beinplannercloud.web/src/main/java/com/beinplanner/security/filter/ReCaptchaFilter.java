package com.beinplanner.security.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;


public class ReCaptchaFilter extends GenericFilterBean {

	private static final String RECAPTCHA_SECRET = "6Leuk1UUAAAAAKG5x3BJcKra_DINuutRZvATXkZR";

    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String RECAPTCHA_RESPONSE_PARAM = "g-recaptcha-response";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        /*
		chain.doFilter(req, res);
		return;
		*/
		
		if (
                !(req instanceof HttpServletRequest) ||
                !("POST".equalsIgnoreCase(((HttpServletRequest)req).getMethod()))
            ) {
                chain.doFilter(req, res);
                return;
            }

        
    
        String url = ((HttpServletRequest) req).getRequestURI();
        
       
		if (!url.equals("/login")) {
			chain.doFilter(req, res);
		}else {
        
        
			
			
			
			
            PostMethod method = new PostMethod(RECAPTCHA_URL);
            method.addParameter("secret", RECAPTCHA_SECRET);
            method.addParameter("response", req.getParameter(RECAPTCHA_RESPONSE_PARAM));
            method.addParameter("remoteip", req.getRemoteAddr());

            HttpClient client = new HttpClient();
            client.executeMethod(method);
            BufferedReader br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
            String readLine;
            StringBuffer response = new StringBuffer();
            while(((readLine = br.readLine()) != null)) {
                response.append(readLine);
            }

            JSONParser parser = new JSONParser();
            
            Object obj=null;
			try {
				obj = parser.parse(response.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            JSONObject jsonObject=(JSONObject) obj;
            
            System.out.println(response.toString()+"  "+jsonObject.get("success"));
            if((Boolean)jsonObject.get("success"))
               chain.doFilter(req, res);
            else
            	 ((HttpServletResponse)res).sendError(HttpStatus.BAD_REQUEST.value(), "Bad ReCaptcha, Please confirm that you are human");
            
		}   
          
	}

	

	
	

	

}