package com.beinplanner.contollers.stripe;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;

import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.definition.service.DefinitionService;
@RestController
@RequestMapping("/stripe")
public class StripePaymentController {

	@Autowired
	DefinitionService definitionService;
	
	@RequestMapping(value="/charge", method = RequestMethod.POST) 
	public void chargeFromCard(HttpServletRequest request,HttpServletResponse response) throws IOException  {
		Stripe.apiKey = "sk_test_zRhW35HHPrDAaF1LZzBUsrap";
		
		String token = request.getParameter("stripeToken");
		String email = request.getParameter("stripeEmail");
		
		String dataAmount = request.getParameter("dataAmount");
		String currency = request.getParameter("dataAmount");
		String description = request.getParameter("description");
		String restriction = request.getParameter("restriction");
		String plan = request.getParameter("plan");

		
		
		// Charge the user's card:
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", dataAmount);
		params.put("currency", currency);
		params.put("description", description);
		params.put("source", token);
		String stripeCustId="";
		//Charge charge=null;
		try {
		//	charge = Charge.create(params);
			
			
			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("email", email);
			customerParams.put("description", description);
			customerParams.put("source", token);
			// ^ obtained with Stripe.js
			Customer customer= Customer.create(customerParams);
			stripeCustId=customer.getId();
			
			DefFirm defFirm=new DefFirm();
			defFirm.setCityName("");
			defFirm.setStateName("");
			defFirm.setCreateTime(new Date());
			defFirm.setFirmAddress("");
			defFirm.setFirmApproved(0);
			defFirm.setFirmAuthPerson("-");
			defFirm.setFirmEmail(email);
			defFirm.setFirmGroupId(0);
			defFirm.setFirmName("");
			defFirm.setFirmPhone("");
			defFirm.setFirmRestriction(Integer.parseInt(restriction));
			defFirm.setStripeCustId(stripeCustId);
			
			definitionService.createFirm(defFirm);
			
			
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("plan", plan);

			Map<String, Object> items = new HashMap<String, Object>();
			items.put("0", item);

			Map<String, Object> paramPlans = new HashMap<String, Object>();
			paramPlans.put("customer", stripeCustId);
			paramPlans.put("items", items);

			Subscription.create(params);
			
			
		} catch (AuthenticationException e) {
			response.sendRedirect("/stripeError");
		} catch (InvalidRequestException e) {
			response.sendRedirect("/stripeError");
		} catch (APIConnectionException e) {
			response.sendRedirect("/stripeError");
		} catch (CardException e) {
			response.sendRedirect("/stripeError");
		} catch (APIException e) {
			response.sendRedirect("/stripeError");
		}
		
		

		
		response.sendRedirect("/register?"+stripeCustId);
		//System.out.println(charge.getCustomer());
	}
	
	
	
	
	
	
	
}
