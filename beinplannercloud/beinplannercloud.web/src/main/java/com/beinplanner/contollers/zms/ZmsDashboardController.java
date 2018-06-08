package com.beinplanner.contollers.zms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ZmsStatuUtil;
import tr.com.beinplanner.zms.dao.Custom_ZmsStockMonth;
import tr.com.beinplanner.zms.dao.Custom_ZmsStockOutOrder;
import tr.com.beinplanner.zms.dao.ZmsStockOut;
import tr.com.beinplanner.zms.service.ZmsStockOutService;
import tr.com.beinplanner.zms.service.ZmsStockService;

@RestController
@RequestMapping("/bein/zms/dashboard")
public class ZmsDashboardController {

	@Autowired
	ZmsStockService zmsStockService;
	
	@Autowired
	ZmsStockOutService zmsStockOutService;
	
	@Autowired
	LoginSession loginSession;
	
	
	@PostMapping(value="/findStockByMonths/{year}")
	public  @ResponseBody HmiResultObj findStockByMonths(@PathVariable("year") int year,HttpServletRequest request ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		List<Custom_ZmsStockMonth> custom_ZmsStockMonths=new ArrayList<Custom_ZmsStockMonth>();
		for (int i=1;i<13;i++) {
			
			Custom_ZmsStockMonth custom_ZmsStockMonth= zmsStockService.findStocksAndOrdersForMonth(loginSession.getUser().getFirmId(), year, i);
			custom_ZmsStockMonths.add(custom_ZmsStockMonth);
		}
	
		hmiResultObj.setResultObj(custom_ZmsStockMonths);
		return hmiResultObj;
	}
	
	@PostMapping(value="/findStockStatusByYear/{year}")
	public  @ResponseBody HmiResultObj findStockStatusByYear(@PathVariable("year") int year,HttpServletRequest request ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		Custom_ZmsStockOutOrder custom_ZmsStockOutOrder= zmsStockService.findOrdersByStatusForYear(loginSession.getUser().getFirmId(), year);
	    hmiResultObj.setResultObj(custom_ZmsStockOutOrder);
		return hmiResultObj;
	}
	
	@PostMapping(value="/findZmsStockOutByStatusAndDate/{year}/{month}/{statu}")
	public  @ResponseBody HmiResultObj findZmsStockOutByStatusAndDate(@PathVariable("year") int year,@PathVariable("month") int month,@PathVariable("statu") int statu,HttpServletRequest request ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		List<ZmsStockOut> zmsStockOuts = zmsStockService.findZmsStockOutByStatusAndDate(loginSession.getUser().getFirmId(),year, month, statu);
	    hmiResultObj.setResultObj(zmsStockOuts);
		return hmiResultObj;
	}
	
	@PostMapping(value="/findStockOutForDeptors")
	public  @ResponseBody 	List<ZmsStockOut> findStockOutForDeptors( ) {
		return zmsStockOutService.findStockOutForDeptors(loginSession.getUser().getFirmId(),ZmsStatuUtil.ZMS_STATU_DONE);
	}
	
	
	
}
