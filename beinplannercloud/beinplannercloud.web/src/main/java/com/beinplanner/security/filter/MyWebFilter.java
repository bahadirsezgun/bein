package com.beinplanner.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.util.FirmApprovedUtil;

@WebFilter(urlPatterns = "/bein/*")
public class MyWebFilter implements Filter{

	 @Autowired
	 LoginSession loginSession;
		
	
	@Override
	  public void init (FilterConfig filterConfig) throws ServletException {

	  }

	  @Override
	  public void doFilter (ServletRequest request,
	                        ServletResponse response, FilterChain chain)
	            throws IOException, ServletException {
	      String url = request instanceof HttpServletRequest ?
	                ((HttpServletRequest) request).getRequestURL().toString() : "N/A";
	     
	                System.out.println("from filter, processing url: "+url+"  "+loginSession.getDefFirm().getFirmApproved() );
	      
	       if(request instanceof HttpServletRequest) {
	       if(loginSession.getDefFirm().getFirmApproved()==FirmApprovedUtil.FIRM_APPROVED_NO) {
	    	   ((HttpServletResponse)response).sendRedirect("/appplans");
	    	   return;
	       }
	       }
	           
	       chain.doFilter(request, response);
	      
	  }

	  @Override
	  public void destroy () {

	  }
}
