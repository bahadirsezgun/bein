package tr.com.beinplanner.zms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.zms.dao.ZmsPayment;
import tr.com.beinplanner.zms.dao.ZmsPaymentDetail;
import tr.com.beinplanner.zms.repository.ZmsPaymentDetailRepository;
import tr.com.beinplanner.zms.repository.ZmsPaymentRepository;

@Service
@Qualifier("zmsPaymentService")
public class ZmsPaymentService {

	
	@Autowired
	ZmsPaymentRepository zmsPaymentRepository;
	
	@Autowired
	ZmsPaymentDetailRepository zmsPaymentDetailRepository;
	
	
	public synchronized ZmsPayment findPaymentByStkIdx(long stkIdx) {
		return zmsPaymentRepository.findByStkIdx(stkIdx);
	}
	
	public synchronized ZmsPayment findPaymentById(long payIdx) {
		return zmsPaymentRepository.findOne(payIdx);
	}
	
	public synchronized HmiResultObj createPayment(ZmsPayment zmsPayment){
		HmiResultObj hmiResultObj=new HmiResultObj();
		zmsPayment= zmsPaymentRepository.save(zmsPayment);
		hmiResultObj.setResultObj(zmsPayment);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
		
	}
	
	public synchronized HmiResultObj deletePayment(ZmsPayment zmsPayment){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		try {
			zmsPaymentRepository.delete(zmsPayment);
		} catch (Exception e) {
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		return hmiResultObj;
	}
	
	
	
	
	
	
	
	
	
	public synchronized List<ZmsPaymentDetail> findPaymentDetailByPayIdx(long payIdx) {
		return zmsPaymentDetailRepository.findByPayIdx(payIdx);
	}
	
	
	public synchronized HmiResultObj createPaymentDetail(ZmsPaymentDetail zmsPaymentDetail){
		HmiResultObj hmiResultObj=new HmiResultObj();
		zmsPaymentDetail= zmsPaymentDetailRepository.save(zmsPaymentDetail);
		hmiResultObj.setResultObj(zmsPaymentDetail);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
		
	}
	
	public synchronized HmiResultObj deletePaymentDetail(ZmsPaymentDetail zmsPaymentDetail){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		try {
			zmsPaymentDetailRepository.delete(zmsPaymentDetail);
			
			List<ZmsPaymentDetail> paymentDetails=zmsPaymentDetailRepository.findByPayIdx(zmsPaymentDetail.getPayIdx());
			if(paymentDetails.size()==0) {
				ZmsPayment zmsPayment=new ZmsPayment();
				zmsPayment.setPayIdx(zmsPaymentDetail.getPayIdx());
				zmsPaymentRepository.delete(zmsPayment);
			}
			
		} catch (Exception e) {
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		return hmiResultObj;
	}
}
