package tr.com.beinplanner.mail;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;

@Component
@Scope("prototype")
@Qualifier("mailSenderThread")
public class MailSenderThread {

	
	@Autowired
	CustomMailProperties customMailProperties;
	
	public HmiResultObj sendMail(MailObj mailObj) {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
      try {
    	  		
    	  		Properties props=customMailProperties.getCustomMailProperties();
			
			SmtpAuthenticator authentication = new SmtpAuthenticator();
			Session session = Session.getDefaultInstance(props, authentication);
			
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(props.get("mail.smtp.user").toString()));
			
			InternetAddress[] toAddress = new InternetAddress[mailObj.getToWho().length];
			
			int i=0;
			for (String email : mailObj.getToWho()) {
				toAddress[i]=new InternetAddress(email);
				message.addRecipient(RecipientType.TO, toAddress[i]);
				i++;
			}
			
			message.setSubject(mailObj.getSubject());
			message.setContent(mailObj.getMultipartMessage());
			
			Transport transport = session.getTransport("smtp");
			transport.connect(props.get("mail.smtp.host").toString() ,props.get("mail.smtp.user").toString(), null);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();		   
			
		} catch (Exception e) {
			e.printStackTrace();
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		return hmiResultObj;
	}

	
}
