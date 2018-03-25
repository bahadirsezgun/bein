package com.beinplanner.test.mail;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import tr.com.beinplanner.mail.MailObj;
import tr.com.beinplanner.mail.MailSenderThread;
import tr.com.beinplanner.mail.SmtpAuthenticator;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.service.PacketSaleService;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.beinplanner","tr.com.beinplanner"})
@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@RunWith(SpringRunner.class)
@SpringBootTest 
public class MailTest {

	@Configuration
    static class ContextConfiguration {
      
		@Bean
        public PacketPaymentService packetPaymentService() {
			PacketPaymentService packetPaymentService = new PacketPaymentService();
            // set properties, etc.
            return packetPaymentService;
        }
		
        @Bean
        public PacketSaleService packetSaleService() {
        	PacketSaleService packetSaleService = new PacketSaleService();
            // set properties, etc.
            return packetSaleService;
        }
   }
	
	@Test
	public void sendMail() throws MessagingException {
		
		    MailObj mailObj=new MailObj();
		 
		    
		    mailObj.setToPerson("bbcsezgun@gmail.com");
			String content="MERHABA dünya";
			
			String htmlContent="<strong>denemePass</strong>";
			
			mailObj.setContent(content);
			mailObj.setHtmlContent(htmlContent);
			
			String[] toWho=new String[]{"bbcsezgun@gmail.com"};
			
			
			mailObj.setToWho(toWho);
			
			
			MimeMultipart mcontent = new MimeMultipart();
			MimeBodyPart mainPart = new MimeBodyPart();
			  
		    mainPart.setText(mailObj.getContent(),"UTF-8", "plain");
		    mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
		    
		    mcontent.addBodyPart(mainPart);
		    
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent( mailObj.getHtmlContent(), "text/html; charset=utf-8" );
			
			mcontent.addBodyPart(htmlPart);
			
			mailObj.setMultipartMessage(mcontent);
			
			sendMailTo(mailObj);
		
			
	}
	
	
	private void sendMailTo(MailObj mailObj) {
		try {
			
	    	  
			
			
			String host = "mail.beinplanner.com";
  				String from="info@beinplanner.com";
	
  /*
  				String host = "smtp.yandex.com.tr";
			String from="bsezgun@yandex.com";//"info@beinplanner.com";
*/			
			String port ="465"; 
			String auth="true";
			
			
			Properties props = System.getProperties();
			//props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", from);
		    props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", auth);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			
			
			SmtpAuthenticator authentication = new SmtpAuthenticator();
			Session session = Session.getDefaultInstance(props, authentication);
			
			
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[1];
			toAddress[0]=new InternetAddress(mailObj.getToPerson());
 
			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(RecipientType.TO, toAddress[i]);
			}
			// başlık
			message.setSubject(mailObj.getSubject());
			// içerik
			if(mailObj==null)
			    message.setText("demenemen sdsdf");
			else
				message.setContent(mailObj.getMultipartMessage());
			
			
			
			Transport transport = session.getTransport("smtp");
			transport.connect(host ,from, null);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
	
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	
	}
	
}
