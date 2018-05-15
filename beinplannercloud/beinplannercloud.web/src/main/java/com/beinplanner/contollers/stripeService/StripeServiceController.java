package com.beinplanner.contollers.stripeService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;
import com.stripe.model.Subscription;

import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.FirmApprovedUtil;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.util.StripePlanUtil;

@RestController
@RequestMapping("/stripeService")
public class StripeServiceController {

	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	StripePlanService stripePlanService;
	
	@Autowired
	SubscriptionService subscriptionService;
	
	
	@RequestMapping(value="/unFreeze", method = RequestMethod.POST) 
	public void unFreeze(HttpServletRequest request,HttpServletResponse response) throws IOException  {
		Stripe.apiKey = StripePlanUtil.API_KEY;
		
		String token = request.getParameter("stripeToken");
		String email = request.getParameter("stripeEmail");
		
		DefFirm defFirm=definitionService.findFirm(loginSession.getUser().getFirmId());
		
		//String dataAmount = request.getParameter("dataAmount");
		
		String restriction = request.getParameter("restriction");
		String plan = request.getParameter("plan");
		
		
		
			
			   boolean isSubscriptionDone=false;
				
				String stripeCustId=defFirm.getStripeCustId();
				try {
					
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("plan", plan);
		
					Map<String, Object> items = new HashMap<String, Object>();
					items.put("0", item);
		
					Map<String, Object> paramPlans = new HashMap<String, Object>();
					paramPlans.put("customer", stripeCustId);
					paramPlans.put("items", items);
					
					Subscription subscription=  Subscription.create(paramPlans);
					
					defFirm.setFirmApproved(FirmApprovedUtil.FIRM_APPROVED_YES);
					defFirm.setFirmRestriction(Integer.parseInt(restriction));
					defFirm.setStripeCustId(stripeCustId);
					defFirm.setStripePlanId(plan);
					definitionService.createFirm(defFirm);
					
					
					isSubscriptionDone=true;
				} catch (AuthenticationException e) {
					e.printStackTrace();
				} catch (InvalidRequestException e) {
					e.printStackTrace();
				} catch (APIConnectionException e) {
					e.printStackTrace();
				} catch (CardException e) {
					e.printStackTrace();
				} catch (APIException e) {
					e.printStackTrace();
				}
				
			    if(isSubscriptionDone )
					response.sendRedirect("/login");
				else
					response.sendRedirect("/firmCreatedBeforeException");
			

		
	}
	
	/*
	@RequestMapping(value="/unFreeze", method = RequestMethod.POST) 
	public HmiResultObj unFreezeAccount() throws IOException  {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		DefFirm defFirm=definitionService.findFirm(loginSession.getUser().getFirmId());
		String customerId=defFirm.getStripeCustId();
		String planId=defFirm.getStripePlanId();
		try {
			Plan plan=stripePlanService.retrieveProductById(planId);
			
			if(plan!=null) {
				subscriptionService.createSubscription(planId, customerId);
			}
			
			defFirm.setFirmApproved(FirmApprovedUtil.FIRM_APPROVED_YES);
			definitionService.createFirm(defFirm);
			hmiResultObj.setResultMessage("pleaseReLoginToSystem");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hmiResultObj;
	}
	*/
	
	@RequestMapping(value="/freeze", method = RequestMethod.POST) 
	public HmiResultObj freezeAccount() throws IOException  {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		DefFirm defFirm=definitionService.findFirm(loginSession.getUser().getFirmId());
		String customerId=defFirm.getStripeCustId();
		try {
			
			Customer customer=Customer.retrieve(customerId);
			
			CustomerSubscriptionCollection customerSubscriptionCollection= customer.getSubscriptions();
			
			List<Subscription> subscriptions= customerSubscriptionCollection.getData();
			
			for (Subscription subscription : subscriptions) {
				String subscriptionId=subscription.getId();
				Subscription canceledSub= subscriptionService.cancelSubscription(subscriptionId);
				if(canceledSub.getStatus().equals("canceled")) {
					System.out.println("SUBSCRIPTION CANCELED ");
				}
			}
			
			
			
			defFirm.setFirmApproved(FirmApprovedUtil.FIRM_APPROVED_NO);
			definitionService.createFirm(defFirm);
			hmiResultObj.setResultMessage("pleaseReLoginToSystem");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hmiResultObj;
	}
	
	
	
	@RequestMapping(value="/updatePaymentMethod", method = RequestMethod.POST) 
	public void updatePaymentMethod(HttpServletRequest request,HttpServletResponse response) throws IOException  {
		
		
		
		Stripe.apiKey = StripePlanUtil.API_KEY;
		
		String token = request.getParameter("stripeToken");
		
		DefFirm defFirm=definitionService.findFirm(loginSession.getUser().getFirmId());
		String customerId=defFirm.getStripeCustId();
		
		
		try {
			Customer cu = Customer.retrieve(customerId);
			Map<String, Object> updateParams = new HashMap<String, Object>();
			updateParams.put("source", token);
			cu.update(updateParams);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.sendRedirect("/beincloud#/stripe/changepayment");
		
		
	}
	
}
