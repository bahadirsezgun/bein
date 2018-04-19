package tr.com.beinplanner.sms.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

@Service
@Qualifier("sendSmsByAWSService")
public class SendSmsByAWSService {

	
	public String sendMessageTo(String phoneNumber,String message) {
		Map<String, MessageAttributeValue> smsAttributes =
		        new HashMap<String, MessageAttributeValue>();
		smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
		        .withStringValue("beinplanner") //The sender ID shown on the device.
		        .withDataType("String"));
		smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
		        .withStringValue("0.10") //Sets the max price to 0.50 USD.
		        .withDataType("Number"));
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
		        .withStringValue("Promotional") //Sets the type to promotional.
		        .withDataType("String"));
		
		
		
		 AmazonSNS snsClient = AmazonSNSClient.builder().build();
	        
	        
	     return   sendSMSMessage(snsClient, message, phoneNumber, smsAttributes);
	}
	
	public String sendSMSMessage(AmazonSNS snsClient, String message, 
			String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
	        PublishResult result = snsClient.publish(new PublishRequest()
	                        .withMessage(message)
	                        .withPhoneNumber(phoneNumber)
	                        .withMessageAttributes(smsAttributes));
	      //  System.out.println(result); // Prints the message ID.
	        return result.getMessageId();
	}
	
}
