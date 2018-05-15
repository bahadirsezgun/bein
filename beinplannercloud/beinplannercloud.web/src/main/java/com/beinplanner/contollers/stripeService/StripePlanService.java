package com.beinplanner.contollers.stripeService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Plan;

import tr.com.beinplanner.util.StripePlanUtil;
@Service
@Qualifier("stripePlanService")
public class StripePlanService {

	
	public Plan retrieveProductById(String id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = StripePlanUtil.API_KEY;
		return Plan.retrieve(id);
	}
	
}
