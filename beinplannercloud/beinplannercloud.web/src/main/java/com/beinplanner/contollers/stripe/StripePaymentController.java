package com.beinplanner.contollers.stripe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beinplanner.contollers.stripe.dao.CouponDao;
import com.beinplanner.contollers.stripe.dao.CustomerDao;
import com.beinplanner.contollers.stripe.dao.PlanDao;
import com.beinplanner.contollers.stripe.dao.SubscriptionDao;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;
import com.stripe.model.Subscription;

import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.menu.dao.MenuRoleEmbededId;
import tr.com.beinplanner.menu.dao.MenuRoleTbl;
import tr.com.beinplanner.menu.service.MenuService;
import tr.com.beinplanner.settings.dao.PtGlobal;
import tr.com.beinplanner.settings.dao.PtLock;
import tr.com.beinplanner.settings.dao.PtRules;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.BonusLockUtil;
import tr.com.beinplanner.util.OhbeUtil;
import tr.com.beinplanner.util.StripePlanUtil;
@RestController
@RequestMapping("/stripe")
public class StripePaymentController {

	@Autowired
	UserService userService;
	
	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	LoginSession loginSession;
	
	
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	SettingsService settingsService;
	
	@RequestMapping(value="/charge", method = RequestMethod.POST) 
	public void chargeFromCard(HttpServletRequest request,HttpServletResponse response) throws IOException  {
		Stripe.apiKey = StripePlanUtil.API_KEY;
		
		String token = request.getParameter("stripeToken");
		String email = request.getParameter("stripeEmail");
		
		//String dataAmount = request.getParameter("dataAmount");
		
		String description = request.getParameter("description");
		String restriction = request.getParameter("restriction");
		String plan = request.getParameter("plan");
		String lang = request.getParameter("lang");
		String coupon = request.getParameter("coupon");

		
		
			
			    boolean isNewCustomerDone=true;
				boolean isSubscriptionDone=false;
				String currency=findCurrency(plan);
				
				String stripeCustId="";
				try {
					Map<String, Object> customerParams = new HashMap<String, Object>();
					customerParams.put("email", email);
					customerParams.put("description", description);
					customerParams.put("source", token);
					// ^ obtained with Stripe.js
					Customer customer= Customer.create(customerParams);
					stripeCustId=customer.getId();
					
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("plan", plan);
		
					Map<String, Object> items = new HashMap<String, Object>();
					items.put("0", item);
		
					Map<String, Object> paramPlans = new HashMap<String, Object>();
					paramPlans.put("customer", stripeCustId);
					paramPlans.put("items", items);
					
					
					
					DefFirm df=definitionService.findFirmByEMail(email);
					if(df!=null) {
						df.setCreateTime(new Date());
						df.setFirmApproved(0);
						df.setFirmRestriction(Integer.parseInt(restriction));
						df.setStripeCustId(stripeCustId);
						df.setStripePlanId(plan);
						updateDefFirm(df);
						isNewCustomerDone=false;
					}else {
						
						Date d= OhbeUtil.getDateForNextDate(new Date(), 15);
						paramPlans.put("trial_end", d.getTime()/ 1000L);
						
						df=createDefFirm(email, restriction, stripeCustId, currency, lang,plan);
					}
					
					
					
					
					if(findCoupon(coupon))
					  paramPlans.put("coupon", coupon);
					
					
					Subscription subscription=  Subscription.create(paramPlans);
					
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
				
				if(isSubscriptionDone && isNewCustomerDone)
				   response.sendRedirect("/register");
				else if(isSubscriptionDone && !isNewCustomerDone)
					response.sendRedirect("/login");
				else
					response.sendRedirect("/firmCreatedBeforeException");
			

		
	}
	
	private DefFirm updateDefFirm(DefFirm defFirm) {
		return definitionService.createFirm(defFirm);
	}
	
	private DefFirm createDefFirm(String email,String restriction,String stripeCustId,String currency,String lang,String planId) {
		DefFirm defFirm=new DefFirm();
		defFirm.setFirmCityName("");
		defFirm.setFirmStateName("");
		defFirm.setCreateTime(new Date());
		defFirm.setFirmAddress("");
		defFirm.setFirmApproved(0);
		defFirm.setFirmAuthPerson("-");
		defFirm.setFirmEmail(email);
		defFirm.setFirmGroupId(0);
		defFirm.setFirmName("");
		defFirm.setFirmPhone("");
		defFirm.setStripePlanId(planId);
		defFirm.setFirmRestriction(Integer.parseInt(restriction));
		defFirm.setStripeCustId(stripeCustId);
		
		defFirm=definitionService.createFirm(defFirm);
		
		
		createDefaultFirmSettings(defFirm, currency, lang);
		createDefaultAdminMenuRole(defFirm);
		
		return defFirm;
	}
	
	private boolean findCoupon(String coupon) {
		
		
		if(coupon==null)
			return false;
		else if(coupon.equals(""))
			return false;
		
		
		Stripe.apiKey = StripePlanUtil.API_KEY;

		try {
			Coupon cp= Coupon.retrieve(coupon.toUpperCase());
			if(cp==null) {
				return false;
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return false;
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			return false;
		} catch (APIConnectionException e) {
			e.printStackTrace();
			return false;
		} catch (CardException e) {
			e.printStackTrace();
			return false;
		} catch (APIException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void closeCustomerAccount(String id)  {
		
		try {
			DefFirm defFirm=definitionService.findFirm(loginSession.getUser().getFirmId());
			Stripe.apiKey = StripePlanUtil.API_KEY;
			Customer cust= Customer.retrieve(defFirm.getStripeCustId());
			cust.delete();
			
			
			defFirm.setFirmApproved(1);
			defFirm.setStripeCustId("");
			
			definitionService.createFirm(defFirm);
			
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
	}
	
	
	public CustomerDao retrieveCustomer(String id) {
		
		Stripe.apiKey = StripePlanUtil.API_KEY;
		CustomerDao customerDao=new CustomerDao();
		Customer customer;
		try {
			customer = Customer.retrieve(id);
		
				
				customerDao.setAccountBalance(customer.getAccountBalance());
				customerDao.setCurrency(customer.getCurrency());
				customerDao.setEmail(customer.getEmail());
				customerDao.setId(customer.getId());
				
				CustomerSubscriptionCollection customerSubscriptionCollection= customer.getSubscriptions();
				
				List<Subscription> subscriptions= customerSubscriptionCollection.getData();
				
				List<SubscriptionDao> subscriptionDaos=new ArrayList<SubscriptionDao>();
				for (Subscription subscription : subscriptions) {
					SubscriptionDao subscriptionDao=new SubscriptionDao();
					subscriptionDao.setBilling(subscription.getBilling());
					subscriptionDao.setBilling_cycle_anchor(subscription.getBillingCycleAnchor());
					subscriptionDao.setCancel_at_period_end(subscription.getCancelAtPeriodEnd());
					subscriptionDao.setCreated(subscription.getCreated());
					subscriptionDao.setCreatedDate(new Date(subscriptionDao.getCreated()*100));
					subscriptionDao.setCurrentPeriodEnd(subscription.getCurrentPeriodEnd());
					subscriptionDao.setCurrentPeriodEndDate(new Date(subscriptionDao.getCurrentPeriodEnd()*100));
					subscriptionDao.setCurrentPeriodStart(subscription.getCurrentPeriodStart());
					subscriptionDao.setCurrentPeriodStartDate(new Date(subscriptionDao.getCurrentPeriodStart()*100));
					subscriptionDao.setCustomer(subscription.getCustomer());
					subscriptionDao.setId(subscription.getId());
					subscriptionDao.setStatus(subscription.getStatus());
					if(subscription.getDiscount()!=null) {
						Coupon cp=subscription.getDiscount().getCoupon();
						CouponDao couponDao=new CouponDao();
						couponDao.setDuration(cp.getDuration());
						couponDao.setId(cp.getId());
						couponDao.setPercentageOf(cp.getPercentOff());
						
						subscriptionDao.setCouponDao(couponDao);
					}
					
					subscriptionDao.setQuantity(subscription.getQuantity());
					subscriptionDao.setStart(subscription.getStart());
					subscriptionDao.setTrialEnd(subscription.getTrialEnd());
					subscriptionDao.setTrialStart(subscription.getTrialStart());
					
					subscriptionDao.setStartDate(new Date(subscription.getStart()*100));
					subscriptionDao.setTrialEndDate(new Date(subscription.getTrialEnd()*100));
					subscriptionDao.setTrialStartDate(new Date(subscription.getTrialStart()*100));
					
					PlanDao planDao=new PlanDao();
					planDao.setAmount(subscription.getPlan().getAmount());
					planDao.setCurrency(subscription.getPlan().getCurrency());
					planDao.setId(subscription.getPlan().getId());
					planDao.setInterval(subscription.getPlan().getInterval());
					planDao.setName(subscription.getPlan().getName());
					planDao.setNickName(subscription.getPlan().getNickname());
					
					subscriptionDao.setPlanDao(planDao);
					subscriptionDaos.add(subscriptionDao);
				}
				
				customerDao.setSubscriptionDaos(subscriptionDaos);
		
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
		return customerDao;
	}
	
	
	/*
	// TEST
	@RequestMapping(value="/charge", method = RequestMethod.POST) 
	public void chargeFromCard(HttpServletRequest request,HttpServletResponse response) throws IOException  {
		Stripe.apiKey = "sk_test_zRhW35HHPrDAaF1LZzBUsrap";
		
		String token = request.getParameter("stripeToken");
		String email = request.getParameter("stripeEmail");
		
		//String dataAmount = request.getParameter("dataAmount");
		
		String description = request.getParameter("description");
		String restriction = request.getParameter("restriction");
		String plan = request.getParameter("plan");
		String lang = request.getParameter("lang");

		if(userService.findUserByUserEmail(email).isPresent()) {
			response.sendRedirect("/firmCreatedBeforeException");
		}else {
		
			DefFirm df=definitionService.findFirmByEMail(email);
			if(df!=null) {
				response.sendRedirect("/firmCreatedBeforeException");
			}else {
			
				boolean isSubscriptionDone=false;
				
				// Charge the user's card:
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", dataAmount);
				params.put("currency", currency);
				params.put("description", description);
				params.put("source", token);
				
				String currency=findCurrency(plan);
				
				String stripeCustId="";
				//Charge charge=null;
			
				//	charge = Charge.create(params);
					
					
					Map<String, Object> customerParams = new HashMap<String, Object>();
					customerParams.put("email", email);
					customerParams.put("description", description);
					customerParams.put("source", token);
					// ^ obtained with Stripe.js
					//Customer customer= Customer.create(customerParams);
					stripeCustId="cus_test3367";//customer.getId();
					
					DefFirm defFirm=new DefFirm();
					defFirm.setFirmCityName("");
					defFirm.setFirmStateName("");
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
					
					defFirm=definitionService.createFirm(defFirm);
					
					
					createDefaultFirmSettings(defFirm, currency, lang);
					createDefaultAdminMenuRole(defFirm);
					
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("plan", plan);
		
					System.out.println("plan is "+plan);
					
					Map<String, Object> items = new HashMap<String, Object>();
					items.put("0", item);
		
					Map<String, Object> paramPlans = new HashMap<String, Object>();
					paramPlans.put("customer", stripeCustId);
					paramPlans.put("items", items);
		
					//Subscription subscription=  Subscription.create(paramPlans);
					
					isSubscriptionDone=true;
				
				
				if(isSubscriptionDone)
				   response.sendRedirect("/register");
				else
					response.sendRedirect("/firmCreatedBeforeException");
			}

		}
		
		
		//System.out.println(charge.getCustomer());
	}*/
	
	
	private String findCurrency(String plan) {
		
		System.out.println("PLAN : "+plan);
		if(plan.contains("usd")) {
			System.out.println("CONTAINS USD  ");
			return "USD";
		}else {
			System.out.println("CONTAINS TRY  ");
			return "TRY";
		}
	}
	
	private void createDefaultAdminMenuRole(DefFirm defFirm) {
		
		MenuRoleTbl m20=new MenuRoleTbl(new MenuRoleEmbededId(6, 20, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m20);
		
		MenuRoleTbl m21=new MenuRoleTbl(new MenuRoleEmbededId(6, 21, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m21);
		
		MenuRoleTbl m22=new MenuRoleTbl(new MenuRoleEmbededId(6, 22, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m22);
		
		MenuRoleTbl m23=new MenuRoleTbl(new MenuRoleEmbededId(6, 23, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m23);
		
		MenuRoleTbl m24=new MenuRoleTbl(new MenuRoleEmbededId(6, 24, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m24);
		
		MenuRoleTbl m25=new MenuRoleTbl(new MenuRoleEmbededId(6, 25, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m25);
		
		MenuRoleTbl m26=new MenuRoleTbl(new MenuRoleEmbededId(6, 26, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m26);
		
		MenuRoleTbl m30=new MenuRoleTbl(new MenuRoleEmbededId(6, 30, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m30);
		
		MenuRoleTbl m31=new MenuRoleTbl(new MenuRoleEmbededId(6, 31, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m31);
		
		MenuRoleTbl m32=new MenuRoleTbl(new MenuRoleEmbededId(6, 32, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m32);
		
		MenuRoleTbl m33=new MenuRoleTbl(new MenuRoleEmbededId(6, 33, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m33);
		
		MenuRoleTbl m35=new MenuRoleTbl(new MenuRoleEmbededId(6, 35, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m35);
		
		MenuRoleTbl m36=new MenuRoleTbl(new MenuRoleEmbededId(6, 36, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m36);
		
		MenuRoleTbl m37=new MenuRoleTbl(new MenuRoleEmbededId(6, 37, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m37);
		
		
		MenuRoleTbl m38=new MenuRoleTbl(new MenuRoleEmbededId(6, 38, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m38);
		
		MenuRoleTbl m39=new MenuRoleTbl(new MenuRoleEmbededId(6, 39, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m39);
		
		MenuRoleTbl m80=new MenuRoleTbl(new MenuRoleEmbededId(6, 80, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m80);
		
		MenuRoleTbl m81=new MenuRoleTbl(new MenuRoleEmbededId(6, 81, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m81);
		
		MenuRoleTbl m82=new MenuRoleTbl(new MenuRoleEmbededId(6, 82, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m82);
		
		MenuRoleTbl m83=new MenuRoleTbl(new MenuRoleEmbededId(6, 83, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m83);
		
		MenuRoleTbl m84=new MenuRoleTbl(new MenuRoleEmbededId(6, 84, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m84);
		
		
		MenuRoleTbl m100=new MenuRoleTbl(new MenuRoleEmbededId(6, 100, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m100);
		
		MenuRoleTbl m101=new MenuRoleTbl(new MenuRoleEmbededId(6, 101, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m101);
		
		MenuRoleTbl m102=new MenuRoleTbl(new MenuRoleEmbededId(6, 102, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m102);
		
		MenuRoleTbl m103=new MenuRoleTbl(new MenuRoleEmbededId(6, 103, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m103);
		
		
		MenuRoleTbl m210=new MenuRoleTbl(new MenuRoleEmbededId(6, 210, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m210);
		
		MenuRoleTbl m212=new MenuRoleTbl(new MenuRoleEmbededId(6, 212, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m212);
		
		MenuRoleTbl m213=new MenuRoleTbl(new MenuRoleEmbededId(6, 213, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m213);
		
		MenuRoleTbl m240=new MenuRoleTbl(new MenuRoleEmbededId(6, 240, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m240);
		
		MenuRoleTbl m241=new MenuRoleTbl(new MenuRoleEmbededId(6, 241, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m241);
		
		MenuRoleTbl m242=new MenuRoleTbl(new MenuRoleEmbededId(6, 242, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m242);
		
		MenuRoleTbl m243=new MenuRoleTbl(new MenuRoleEmbededId(6, 243, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m243);
		
		
		MenuRoleTbl m270=new MenuRoleTbl(new MenuRoleEmbededId(6, 270, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m270);
		
		MenuRoleTbl m271=new MenuRoleTbl(new MenuRoleEmbededId(6, 271, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m271);
		
		MenuRoleTbl m300=new MenuRoleTbl(new MenuRoleEmbededId(6, 300, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m300);
		
		MenuRoleTbl m309=new MenuRoleTbl(new MenuRoleEmbededId(6, 309, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m309);
		
		MenuRoleTbl m312=new MenuRoleTbl(new MenuRoleEmbededId(6, 312, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m312);
		
		/******************************************/
		MenuRoleTbl m313=new MenuRoleTbl(new MenuRoleEmbededId(6, 313, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m313);
		
		MenuRoleTbl m314=new MenuRoleTbl(new MenuRoleEmbededId(6, 314, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m314);
		
		MenuRoleTbl m315=new MenuRoleTbl(new MenuRoleEmbededId(6, 315, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m315);
		/******************************************/
		
		MenuRoleTbl m400=new MenuRoleTbl(new MenuRoleEmbededId(6, 400, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m400);
		
		MenuRoleTbl m401=new MenuRoleTbl(new MenuRoleEmbededId(6, 401, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m401);
		
		MenuRoleTbl m402=new MenuRoleTbl(new MenuRoleEmbededId(6, 402, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m402);
		
		
		MenuRoleTbl m405=new MenuRoleTbl(new MenuRoleEmbededId(6, 405, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m405);
		
		MenuRoleTbl m800=new MenuRoleTbl(new MenuRoleEmbededId(6, 800, defFirm.getFirmId()),"ADMIN");
		menuService.createMenuRoleTbl(m800);
		
	}
		
	
	private void createDefaultFirmSettings(DefFirm defFirm,String currency,String lang) {
		PtGlobal ptGlobal=new PtGlobal();
		ptGlobal.setFirmId(defFirm.getFirmId());
		ptGlobal.setPtCurrency(currency);
		ptGlobal.setPtDateFormat("%d/%m/%Y");
		ptGlobal.setPtStaticIp("127.0.0.1");
		ptGlobal.setPtTz("Europe/Istanbul");
		ptGlobal.setPtLang(lang);
		settingsService.createPtGlobal(ptGlobal);
		
		
		
		PtLock ptLock=new PtLock();
		ptLock.setBonusLock(BonusLockUtil.BONUS_LOCK_FLAG);
		ptLock.setFirmId(defFirm.getFirmId());						
		settingsService.createPtLock(ptLock);
		
		
		PtRules pt1=new PtRules();
		pt1.setRuleId(1);
		pt1.setRuleName("noClassBeforePayment");
		pt1.setRuleValue(0);
		pt1.setFirmId(defFirm.getFirmId());
		try {
			pt1=settingsService.createPtRules(pt1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		PtRules pt2=new PtRules();
		pt2.setRuleId(2);
		pt2.setRuleName("noChangeAfterBonusPayment");
		pt2.setRuleValue(1);
		pt2.setFirmId(defFirm.getFirmId());
		pt2.setPtrId(0);
		try {
			pt2=settingsService.createPtRules(pt2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		PtRules pt3=new PtRules();
		pt3.setRuleId(3);
		pt3.setRuleName("payBonusForConfirmedPayment");
		pt3.setRuleValue(1);
		pt3.setFirmId(defFirm.getFirmId());
		pt3.setPtrId(0);
		pt3=settingsService.createPtRules(pt3);
		System.out.println("pt3 :"+pt3.getRuleName());
		
		PtRules pt4=new PtRules();
		pt4.setRuleId(4);
		pt4.setRuleName("taxRule");
		pt4.setRuleValue(0);
		pt4.setFirmId(defFirm.getFirmId());
		pt4.setPtrId(0);
		settingsService.createPtRules(pt4);
		
		PtRules pt5=new PtRules();
		pt5.setRuleId(5);
		pt5.setRuleName("location");
		pt5.setRuleValue(0);
		pt5.setFirmId(defFirm.getFirmId());
		pt5.setPtrId(0);
		settingsService.createPtRules(pt5);
		
		PtRules pt6=new PtRules();
		pt6.setRuleId(6);
		pt6.setRuleName("notice");
		pt6.setRuleValue(0);
		pt6.setFirmId(defFirm.getFirmId());
		pt6.setPtrId(0);
		settingsService.createPtRules(pt6);
		
		PtRules pt7=new PtRules();
		pt7.setRuleId(7);
		pt7.setRuleName("creditCardCommission");
		pt7.setRuleValue(0);
		pt7.setFirmId(defFirm.getFirmId());
		pt7.setPtrId(0);
		settingsService.createPtRules(pt7);
		
		PtRules pt8=new PtRules();
		pt8.setRuleId(8);
		pt8.setRuleName("creditCardCommissionRate");
		pt8.setRuleValue(0);
		pt8.setFirmId(defFirm.getFirmId());
		pt8.setPtrId(0);
		settingsService.createPtRules(pt8);
		
		PtRules pt9=new PtRules();
		pt9.setRuleId(9);
		pt9.setRuleName("noSaleToPlanning");
		pt9.setRuleValue(1);
		pt9.setFirmId(defFirm.getFirmId());
		pt9.setPtrId(0);
		settingsService.createPtRules(pt9);
		
		PtRules pt10=new PtRules();
		pt10.setRuleId(10);
		pt10.setRuleName("bonusPaymentFullPacket");
		pt10.setRuleValue(0);
		pt10.setFirmId(defFirm.getFirmId());
		pt10.setPtrId(0);
		settingsService.createPtRules(pt10);
		
	}
	
	
	
}
