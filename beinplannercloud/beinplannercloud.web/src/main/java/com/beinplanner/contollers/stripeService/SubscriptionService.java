package com.beinplanner.contollers.stripeService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.beinplanner.contollers.stripe.dao.PlanDao;
import com.beinplanner.contollers.stripeService.dao.CouponDao;
import com.beinplanner.contollers.stripeService.dao.SubscriptionDao;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Coupon;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;

import tr.com.beinplanner.util.StripePlanUtil;

@Service
@Qualifier("subscriptionService")
public class SubscriptionService {

	public SubscriptionDao retrieveSubscription(String id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = StripePlanUtil.API_KEY;
		
		Subscription subscription=Subscription.retrieve(id);
		SubscriptionDao subscriptionDao=new SubscriptionDao();
		subscriptionDao.setBilling(subscription.getBilling());
		subscriptionDao.setBilling_cycle_anchor(subscription.getBillingCycleAnchor());
		subscriptionDao.setCancel_at_period_end(subscription.getCancelAtPeriodEnd());
		subscriptionDao.setCreated(subscription.getCreated());
		subscriptionDao.setCreatedDate(new Date(subscriptionDao.getCreated()*1000));
		subscriptionDao.setCurrentPeriodEnd(subscription.getCurrentPeriodEnd());
		subscriptionDao.setCurrentPeriodEndDate(new Date(subscriptionDao.getCurrentPeriodEnd()*1000));
		subscriptionDao.setCurrentPeriodStart(subscription.getCurrentPeriodStart());
		subscriptionDao.setCurrentPeriodStartDate(new Date(subscriptionDao.getCurrentPeriodStart()*1000));
		subscriptionDao.setCustomer(subscription.getCustomer());
		subscriptionDao.setId(subscription.getId());
		
		subscriptionDao.setQuantity(subscription.getQuantity());
		subscriptionDao.setStart(subscription.getStart());
		subscriptionDao.setTrialEnd(subscription.getTrialEnd());
		subscriptionDao.setTrialStart(subscription.getTrialStart());
		
		subscriptionDao.setStartDate(new Date(subscription.getStart()*1000));
		subscriptionDao.setTrialEndDate(new Date(subscription.getTrialEnd()*1000));
		subscriptionDao.setTrialStartDate(new Date(subscription.getTrialStart()*1000));
		subscriptionDao.setStatus(subscription.getStatus());
		
		if(subscription.getDiscount()!=null) {
			Coupon cp=subscription.getDiscount().getCoupon();
			CouponDao couponDao=new CouponDao();
			couponDao.setDuration(cp.getDuration());
			couponDao.setId(cp.getId());
			couponDao.setPercentageOf(cp.getPercentOff());
			
			subscriptionDao.setCouponDao(couponDao);
		}
		
		PlanDao planDao=new PlanDao();
		planDao.setAmount(subscription.getPlan().getAmount());
		planDao.setCurrency(subscription.getPlan().getCurrency());
		planDao.setId(subscription.getPlan().getId());
		planDao.setInterval(subscription.getPlan().getInterval());
		planDao.setName(subscription.getPlan().getName());
		planDao.setNickName(subscription.getPlan().getNickname());
		
		subscriptionDao.setPlanDao(planDao);
		
		return subscriptionDao;
	}
	
	public List<SubscriptionDao> retrieveSubscriptions(int limit,String status) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		
		Stripe.apiKey = StripePlanUtil.API_KEY;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("status", status);
		
		SubscriptionCollection subscriptionCollection=Subscription.list(params);
		List<Subscription> subscriptions= subscriptionCollection.getData();
		List<SubscriptionDao> subscriptionDaos=new ArrayList<SubscriptionDao>();
		for (Subscription subscription : subscriptions) {
			SubscriptionDao subscriptionDao=new SubscriptionDao();
			subscriptionDao.setBilling(subscription.getBilling());
			subscriptionDao.setBilling_cycle_anchor(subscription.getBillingCycleAnchor());
			subscriptionDao.setCancel_at_period_end(subscription.getCancelAtPeriodEnd());
			subscriptionDao.setCreated(subscription.getCreated());
			subscriptionDao.setCreatedDate(new Date(subscriptionDao.getCreated()*1000));
			subscriptionDao.setCurrentPeriodEnd(subscription.getCurrentPeriodEnd());
			subscriptionDao.setCurrentPeriodEndDate(new Date(subscriptionDao.getCurrentPeriodEnd()*1000));
			subscriptionDao.setCurrentPeriodStart(subscription.getCurrentPeriodStart());
			subscriptionDao.setCurrentPeriodStartDate(new Date(subscriptionDao.getCurrentPeriodStart()*1000));
			subscriptionDao.setCustomer(subscription.getCustomer());
			subscriptionDao.setId(subscription.getId());
			
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
			if(subscription.getTrialEnd()!=null)
			subscriptionDao.setTrialEnd(subscription.getTrialEnd());
			
			if(subscription.getTrialStart()!=null)
			subscriptionDao.setTrialStart(subscription.getTrialStart());
			
			subscriptionDao.setStartDate(new Date(subscription.getStart()*1000));
			
			if(subscription.getTrialStart()!=null)
				subscriptionDao.setTrialEndDate(new Date(subscription.getTrialEnd()*1000));
			
			if(subscription.getTrialStart()!=null)
				subscriptionDao.setTrialStartDate(new Date(subscription.getTrialStart()*1000));
			subscriptionDao.setStatus(subscription.getStatus());
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
		
		
		return subscriptionDaos;
	}
	
	public void createSubscription(String plan,String stripeCustId) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = StripePlanUtil.API_KEY;
		
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("plan", plan);

		Map<String, Object> items = new HashMap<String, Object>();
		items.put("0", item);

		Map<String, Object> paramPlans = new HashMap<String, Object>();
		paramPlans.put("customer", stripeCustId);
		paramPlans.put("items", items);
		//paramPlans.put("trial_period_days", 15);
		
		Date d= getDateForNextDate(new Date(), 14);
		
		paramPlans.put("trial_end", d.getTime()/ 1000L);
		
		
		Subscription subscription=  Subscription.create(paramPlans);
		
	}
	
	public String addCouponToSubscription(String id,String coupon) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = StripePlanUtil.API_KEY;
		
		
		Subscription sub = Subscription.retrieve(id);
		Map<String, Object> updateParams = new HashMap<String, Object>();
		updateParams.put("coupon", coupon);
		sub.update(updateParams);
		
		return "success";
	}
	
	
	public static Date getDateForNextDate(Date date, int dayDuration) {
		if (date == null)
			return new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(((Date)(date.clone())).getTime());
		
		cal.set(Calendar.DATE, cal.get(Calendar.DATE)+dayDuration);
		
		Date returnDate=new Date(cal.getTimeInMillis());
		
		return returnDate;
	}
	
	public Subscription cancelSubscription(String id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = StripePlanUtil.API_KEY;
		Subscription sub= Subscription.retrieve(id);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("at_period_end", false);
		
		return sub.cancel(params);
		
	}
}
