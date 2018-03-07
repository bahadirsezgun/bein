package com.beinplanner.contollers.bonus;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.bonus.businessDao.UserBonusSearchObj;
import tr.com.beinplanner.bonus.businessDao.UserPaymentObj;
import tr.com.beinplanner.bonus.dao.UserBonusPaymentFactory;
import tr.com.beinplanner.bonus.service.IUserBonusPayment;
import tr.com.beinplanner.bonus.service.UserBonusPaymentClassService;
import tr.com.beinplanner.bonus.service.UserBonusPaymentPersonalService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.BonusTypes;

@RestController
@RequestMapping(value="/bonus/payment")
public class BonusPaymentController {

	
	@Autowired
	UserBonusPaymentPersonalService userBonusPaymentPersonalService;
	
	@Autowired
	UserBonusPaymentClassService userBonusPaymentClassService;
	
	
	
	@RequestMapping(value="/findUserPaymentDetail/{bonType}/{schtId}", method = RequestMethod.POST) 
	public @ResponseBody UserPaymentObj findUserPaymentDetail(@PathVariable("bonType") String bonType,@PathVariable("schtId") long schtId , HttpServletRequest request ) {
		
		IUserBonusPayment iUserBonusPayment;
		if(bonType.equals(BonusTypes.BONUS_PAYMENT_TYPE_PERSONAL)){
			iUserBonusPayment=userBonusPaymentPersonalService;
		}else{
			iUserBonusPayment=userBonusPaymentClassService;
		}
		
		return iUserBonusPayment.findUserPayment(schtId);
	}
	
	
	@RequestMapping(value="/saveBonusPayment/{bonType}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj saveBonusPayment(@RequestBody UserBonusPaymentFactory userBonusPaymentFactory, @PathVariable("bonType") String bonType , HttpServletRequest request ) {
		
		IUserBonusPayment iUserBonusPayment;
		if(bonType.equals(BonusTypes.BONUS_PAYMENT_TYPE_PERSONAL)){
			iUserBonusPayment=userBonusPaymentPersonalService;
		}else{
			iUserBonusPayment=userBonusPaymentClassService;
		}
		/*
      if(userBonusPaymentFactory.getBonQueryType()==1){ // AYLIK SORGULAMA
			
			int year=userBonusPaymentFactory.getBonYear();
			int month=userBonusPaymentFactory.getBonMonth();
			String monthStr=""+month;
			if(month<10)
				 monthStr="0"+month;
			
			String startDateStr="01/"+monthStr+"/"+year;
			Date startDate=DateTimeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy");
			Date endDate=DateTimeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy");
			endDate=OhbeUtil.getDateForNextMonth(endDate, 1);
			
			userBonusPaymentFactory.setBonStartDate(startDate);
			userBonusPaymentFactory.setBonEndDate(endDate);
			
		}else{
			
			Date startDate=DateTimeUtil.getThatDayFormatNotNull(userBonusPaymentFactory.getBonStartDateStr() , GlobalUtil.global.getPtScrDateFormat());
			Date endDate=DateTimeUtil.getThatDayFormatNotNull(userBonusPaymentFactory.getBonEndDateStr() , GlobalUtil.global.getPtScrDateFormat());
			
			userBonusPaymentFactory.setBonStartDate(startDate);
			userBonusPaymentFactory.setBonEndDate(endDate);
			
			int month=DateTimeUtil.getMonthOfDate(startDate);
			int year=DateTimeUtil.getYearOfDate(startDate);
			
			userBonusPaymentFactory.setBonMonth(month);
			userBonusPaymentFactory.setBonYear(year);
			
			
		}
		
        userBonusPaymentFactory.setBonPaymentDate(DateTimeUtil.getThatDayFormatNotNull(userBonusPaymentFactory.getBonPaymentDateStr() , GlobalUtil.global.getPtScrDateFormat()));
		*/
		
		
		return iUserBonusPayment.saveBonusPayment(userBonusPaymentFactory);
		
		
	}
	
	@RequestMapping(value="/deleteBonusPayment/{bonType}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteBonusPayment(@RequestBody UserBonusPaymentFactory userBonusPaymentFactory, @PathVariable("bonType") String bonType , HttpServletRequest request ) {
	
		IUserBonusPayment iUserBonusPayment;
		if(bonType.equals(BonusTypes.BONUS_PAYMENT_TYPE_PERSONAL)){
			iUserBonusPayment=userBonusPaymentPersonalService;
		}else{
			iUserBonusPayment=userBonusPaymentClassService;
		}
		
		return iUserBonusPayment.deleteBonusPayment(userBonusPaymentFactory);
		
	}
	
	@RequestMapping(value="/findStaffBonusPayment/{bonType}", method = RequestMethod.POST) 
	public @ResponseBody List<UserBonusPaymentFactory> findStaffBonusPayment(@RequestBody UserBonusSearchObj userBonusSearchObj, @PathVariable("bonType") String bonType , HttpServletRequest request ) {
		
		IUserBonusPayment iUserBonusPayment;
		if(bonType.equals(BonusTypes.BONUS_PAYMENT_TYPE_PERSONAL)){
			iUserBonusPayment=userBonusPaymentPersonalService;
		}else{
			iUserBonusPayment=userBonusPaymentClassService;
		}
		/*
		if(userBonusSearchObj.getQueryType()==1){ // AYLIK SORGULAMA
			
			int year=userBonusSearchObj.getYear();
			int month=userBonusSearchObj.getMonth();
			String monthStr=""+month;
			if(month<10)
				 monthStr="0"+month;
			
			String startDateStr="01/"+monthStr+"/"+year;
			Date startDate=DateTimeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy");
			Date endDate=DateTimeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy");
			endDate=OhbeUtil.getDateForNextMonth(endDate, 1);
			
			userBonusSearchObj.setStartDate(startDate);
			userBonusSearchObj.setEndDate(endDate);
			
		}else{
			
			Date startDate=DateTimeUtil.getThatDayFormatNotNull(userBonusSearchObj.getStartDateStr() , GlobalUtil.global.getPtScrDateFormat());
			Date endDate=DateTimeUtil.getThatDayFormatNotNull(userBonusSearchObj.getEndDateStr() , GlobalUtil.global.getPtScrDateFormat());
			endDate=OhbeUtil.getDateForNextDate(endDate, 1);
			
			userBonusSearchObj.setStartDate(startDate);
			userBonusSearchObj.setEndDate(endDate);
			
		}
		*/
		return iUserBonusPayment.findStaffBonusPayment(userBonusSearchObj);
		
	}
	
	
}
