package com.beinplanner.contollers.stripe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
@RestController
@RequestMapping("/stripe")
public class StripePaymentController {

	
	
	@RequestMapping(value="/charge", method = RequestMethod.POST) 
	public void chargeFromCard(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException, IOException {
		// Set your secret key: remember to change this to your live secret key in production
		// See your keys here: https://dashboard.stripe.com/account/apikeys
//		Stripe.apiKey = "sk_test_zRhW35HHPrDAaF1LZzBUsrap";
		Stripe.apiKey = "pk_test_qQ2zkFV4Ht8CMNp9EHPJ7D4f";
		// Token is created using Checkout or Elements!
		// Get the payment token ID submitted by the form:
		String token = request.getParameter("stripeToken");
		String dataAmount = request.getParameter("dataAmount");

		// Charge the user's card:
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", 999);
		params.put("currency", "usd");
		params.put("description", "Example charge");
		params.put("source", token);

		//Charge charge = Charge.create(params);
		

		String firmName = request.getParameter("firmName");
		String firmEmail = request.getParameter("firmEmail");
		String firmCityName = request.getParameter("firmCityName");

		System.out.println("dataAmount :"+dataAmount);
		System.out.println("firmName :"+firmName);
		System.out.println("firmEmail :"+firmEmail);
		System.out.println("firmCityName :"+firmCityName);
		
		
		response.sendRedirect("/login");
		//System.out.println(charge.getCustomer());
	}
}
