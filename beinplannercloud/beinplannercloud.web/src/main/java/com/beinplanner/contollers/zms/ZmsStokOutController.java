package com.beinplanner.contollers.zms;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.util.OhbeUtil;
import tr.com.beinplanner.util.ZmsStatuUtil;
import tr.com.beinplanner.zms.dao.ZmsPayment;
import tr.com.beinplanner.zms.dao.ZmsPaymentDetail;
import tr.com.beinplanner.zms.dao.ZmsStockOut;
import tr.com.beinplanner.zms.service.ZmsPaymentService;
import tr.com.beinplanner.zms.service.ZmsProductService;
import tr.com.beinplanner.zms.service.ZmsStockOutService;

@RestController
@RequestMapping("/bein/zms/stockout")
public class ZmsStokOutController {

	@Autowired
	ZmsStockOutService zmsStockOutService;
	
	
	@Autowired
	ZmsProductService zmsProductService;
	
	@Autowired
	ZmsPaymentService zmsPaymentService;
	
	
	@Autowired
	LoginSession loginSession;
	
	
	
	@PostMapping(value="/create") 
	public @ResponseBody HmiResultObj create(@RequestBody ZmsStockOut zmsStockOut ){
		zmsStockOut.setStaffId(loginSession.getUser().getUserId());
		zmsStockOut.setFirmId(loginSession.getUser().getFirmId());
		return zmsStockOutService.createStockOut(zmsStockOut);
	}
	
	@PostMapping(value="/delete") 
	public @ResponseBody HmiResultObj delete(@RequestBody  ZmsStockOut zmsStockOut ){
		zmsStockOut.setFirmId(loginSession.getUser().getFirmId());
		return zmsStockOutService.deleteStockOut(zmsStockOut);
	}
	
	
	@PostMapping(value="/findByName") 
	public @ResponseBody List<ZmsStockOut> findByName(@RequestBody User user ){
		return zmsStockOutService.findZmsStockOutByUsernameAndSurname(loginSession.getUser().getFirmId(), user.getUserName(), user.getUserSurname());
	}
	
	@PostMapping(value="/findStockOutForDeptors") 
	public @ResponseBody List<ZmsStockOut> findStockOutForDeptors(){
		return zmsStockOutService.findStockOutForDeptors(loginSession.getUser().getFirmId(), ZmsStatuUtil.ZMS_STATU_DONE);
	}
	
	@PostMapping(value="/findAllStockOuts/{year}") 
	public @ResponseBody List<ZmsStockOut> findStockOuts(@PathVariable("year") int year ){
		String startDateStr="01/01/"+year;
		Date startDate=OhbeUtil.getThatDateForNight(startDateStr,"dd/MM/yyyy"); 
		
		String endDateStr="01/01/"+(year+1);
		Date endDate=OhbeUtil.getThatDateForNight(endDateStr,"dd/MM/yyyy"); 
		
		
		List<ZmsStockOut> zmsStockOuts=zmsStockOutService.findStockOutByDate(loginSession.getUser().getFirmId(), startDate, endDate);
		
		zmsStockOuts.stream().forEach(zso->{
			ZmsPayment zmsPayment= zmsPaymentService.findPaymentByStkIdx(zso.getStkIdx());
			if(zmsPayment!=null) {
				List<ZmsPaymentDetail> zmsPaymentDetails=zmsPaymentService.findPaymentDetailByPayIdx(zmsPayment.getPayIdx());
				zmsPayment.setZmsPaymentDetails(zmsPaymentDetails);
			 }
			zso.setZmsPayment(zmsPayment);
		});
		
		
		
		return zmsStockOuts;
	}
	
	@PostMapping(value="/findAllStockOutsByProduct/{year}/{productId}") 
	public @ResponseBody List<ZmsStockOut> findStockOuts(@PathVariable("year") int year,@PathVariable("productId") long productId ){
		String startDateStr="01/01/"+year;
		Date startDate=OhbeUtil.getThatDateForNight(startDateStr,"dd/MM/yyyy"); 
		
		String endDateStr="01/01/"+(year+1);
		Date endDate=OhbeUtil.getThatDateForNight(endDateStr,"dd/MM/yyyy"); 
		
		
		List<ZmsStockOut> zmsStockOuts=zmsStockOutService.findStockOutByProductIdAndDate(productId, startDate, endDate);
		
		zmsStockOuts.stream().forEach(zso->{
			ZmsPayment zmsPayment= zmsPaymentService.findPaymentByStkIdx(zso.getStkIdx());
			if(zmsPayment!=null) {
				List<ZmsPaymentDetail> zmsPaymentDetails=zmsPaymentService.findPaymentDetailByPayIdx(zmsPayment.getPayIdx());
				zmsPayment.setZmsPaymentDetails(zmsPaymentDetails);
			 }
			zso.setZmsPayment(zmsPayment);
		});
		
		
		
		return zmsStockOuts;
	}
	
}
