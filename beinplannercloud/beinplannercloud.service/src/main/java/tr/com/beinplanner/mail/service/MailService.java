package tr.com.beinplanner.mail.service;

import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.mail.CustomMailProperties;
import tr.com.beinplanner.mail.MailObj;
import tr.com.beinplanner.mail.SmtpAuthenticator;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.util.MailUtil;

@Component
@Scope("prototype")
@Qualifier(value="mailService")
public class MailService implements Runnable {

	@Autowired
	LoginSession loginSession;
	
	@Autowired
	CustomMailProperties customMailProperties;
	
	private MailObj mailObj;
	private List<User> users;
	
	
	


	@Override
	public void run() {
		Properties props=customMailProperties.getCustomMailProperties();
		
		SmtpAuthenticator authentication = new SmtpAuthenticator();
		Session session = Session.getDefaultInstance(props, authentication);
		
		//String from=MailUtil.generateFirmMail(loginSession.getPtGlobal().)
		
		try {
		
			
			Transport transport = session.getTransport("smtp");
			transport.connect(props.get("mail.smtp.host").toString() ,props.get("mail.smtp.user").toString(), null);
		
				users.forEach(u->{
						try {
							MimeMessage message = new MimeMessage(session);
							message.setFrom(mailObj.getToFrom());
							message.addRecipient(RecipientType.TO, new InternetAddress(u.getUserEmail()));
							
							
							
							message.setSubject(mailObj.getSubject());
							message.setContent(mailObj.getMultipartMessage());
							
							transport.sendMessage(message, message.getAllRecipients());
							
						} catch (AddressException e) {
							e.printStackTrace();
						} catch (MessagingException e) {
							e.printStackTrace();
						}		   
				});	
				transport.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		
		
	}



	public MailObj getMailObj() {
		return mailObj;
	}



	public void setMailObj(MailObj mailObj) {
		this.mailObj = mailObj;
	}



	public List<User> getUsers() {
		return users;
	}



	public void setUsers(List<User> users) {
		this.users = users;
	}

	
	
}
