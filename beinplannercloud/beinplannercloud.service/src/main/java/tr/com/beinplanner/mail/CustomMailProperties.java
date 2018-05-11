package tr.com.beinplanner.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Qualifier("customMailProperties")
public class CustomMailProperties {

	public Properties getCustomMailProperties() {
		String host = "mail.abasus.com.tr";
  		String from="info@abasus.com.tr";
	
  /*
  				String host = "smtp.yandex.com.tr";
			String from="bsezgun@yandex.com";//"info@beinplanner.com";
*/			
			String port ="465"; 
			String auth="true";
			
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
	    props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		return props;
	}
}
