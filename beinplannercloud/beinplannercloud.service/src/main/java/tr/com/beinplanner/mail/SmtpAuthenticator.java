package tr.com.beinplanner.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator {
	public SmtpAuthenticator() {

	    super();
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		
		
	/*	
	 String username ="bsezgun@yandex.com";//"info@beinplanner.com";
	 String password ="berkin2162";// ""Berkin2162"";
	*/ 
	 String username ="info@beinplanner.com";
	 String password ="Berkin2162";
	 
	    if ((username != null) && (username.length() > 0) && (password != null) 
	      && (password.length   () > 0)) {

	        return new PasswordAuthentication(username, password);
	    }

	    return null;
	}
	}
