package com.beinplanner.contollers.zms;

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
import tr.com.beinplanner.util.PaymentConfirmUtil;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.zms.dao.ZmsPayment;
import tr.com.beinplanner.zms.dao.ZmsPaymentDetail;
import tr.com.beinplanner.zms.dao.ZmsProduct;
import tr.com.beinplanner.zms.service.ZmsPaymentService;

@RestController
@RequestMapping("/bein/zms/payment")
public class ZmsPaymentController {

	
	@Autowired
	ZmsPaymentService zmsPaymentService;

	@Autowired
	LoginSession loginSession;
	
	@PostMapping(value="/findPayment/{stkIdx}")
	public @ResponseBody ZmsPayment findPaymentByStkIdx(@PathVariable("stkIdx") long stkIdx) {
		
		ZmsPayment zmsPayment=zmsPaymentService.findPaymentByStkIdx(stkIdx);
		if(zmsPayment!=null) {
		List<ZmsPaymentDetail> zmsPaymentDetails=zmsPaymentService.findPaymentDetailByPayIdx(zmsPayment.getPayIdx());
		zmsPayment.setZmsPaymentDetails(zmsPaymentDetails);
		}
		return zmsPayment;
		
	}
	
	@PostMapping(value="/create") 
	public @ResponseBody HmiResultObj create(@RequestBody ZmsPayment zmsPayment ){
		zmsPayment.setPayConfirm(PaymentConfirmUtil.PAYMENT_UNCONFIRM);
		zmsPayment.setStaffId(loginSession.getUser().getUserId());
		
		double payAmount=zmsPayment.getPayAmount();
		String payComment=zmsPayment.getPayComment();
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		
		if(zmsPayment.getPayIdx()!=0) {
			ZmsPayment dbZmzP=zmsPaymentService.findPaymentById(zmsPayment.getPayIdx());
			zmsPayment.setPayAmount(zmsPayment.getPayAmount()+dbZmzP.getPayAmount());
			zmsPayment=((ZmsPayment)((HmiResultObj)zmsPaymentService.createPayment(zmsPayment)).getResultObj());
		
		}else {
			zmsPayment=((ZmsPayment)((HmiResultObj)zmsPaymentService.createPayment(zmsPayment)).getResultObj());
		}

		ZmsPaymentDetail zmsPaymentDetail=new ZmsPaymentDetail();
		zmsPaymentDetail.setPayIdx(zmsPayment.getPayIdx());
		zmsPaymentDetail.setPayAmount(payAmount);
		zmsPaymentDetail.setPayComment(payComment);
		zmsPaymentDetail.setPayConfirm(PaymentConfirmUtil.PAYMENT_UNCONFIRM);
		zmsPaymentDetail.setPayDate(zmsPayment.getPayDate());
		zmsPaymentDetail.setPayType(zmsPayment.getPayType());
		zmsPaymentDetail.setStaffId(loginSession.getUser().getUserId());
		
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		
		zmsPaymentService.createPaymentDetail(zmsPaymentDetail);
		
		return hmiResultObj;
	}
	
	@PostMapping(value="/delete") 
	public @ResponseBody HmiResultObj delete(@RequestBody ZmsPayment zmsPayment ){
		return zmsPaymentService.deletePayment(zmsPayment);
	}
	
	@PostMapping(value="/deleteDetail") 
	public @ResponseBody HmiResultObj deleteDetail(@RequestBody ZmsPaymentDetail zmsPaymentDetail ){
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		ZmsPayment zmsPayment=zmsPaymentService.findPaymentById(zmsPaymentDetail.getPayIdx());
		List<ZmsPaymentDetail> zmsPaymentDetails=zmsPaymentService.findPaymentDetailByPayIdx(zmsPaymentDetail.getPayIdx());
		if(zmsPaymentDetails.size()==1) {
			hmiResultObj=zmsPaymentService.deletePayment(zmsPayment);
		}else {
			zmsPayment.setPayAmount(zmsPayment.getPayAmount()-zmsPaymentDetail.getPayAmount());
			zmsPaymentService.createPayment(zmsPayment);
		}
		
		zmsPaymentService.deletePaymentDetail(zmsPaymentDetail);
		
		return hmiResultObj;
	}
	
}
