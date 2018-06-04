package tr.com.beinplanner.zms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.zms.dao.ZmsStockOut;
import tr.com.beinplanner.zms.dao.ZmsStockOutDetail;
import tr.com.beinplanner.zms.repository.ZmsStockOutDetailRepository;
import tr.com.beinplanner.zms.repository.ZmsStockOutRepository;

@Service
@Qualifier("zmsStockOutService")
public class ZmsStockOutService {

	
	@Autowired
	ZmsStockOutRepository zmsStockOutRepository;
	
	@Autowired
	ZmsStockOutDetailRepository zmsStockOutDetailRepository;
	
	
	public synchronized List<ZmsStockOut> findStockOutByProductId(long stkIdx) {
		return zmsStockOutRepository.findByProductId(stkIdx);
	}
	
	
	public synchronized HmiResultObj createStockOut(ZmsStockOut zmsStockOut){
		HmiResultObj hmiResultObj=new HmiResultObj();
		zmsStockOut= zmsStockOutRepository.save(zmsStockOut);
		hmiResultObj.setResultObj(zmsStockOut);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
		
	}
	
	public synchronized HmiResultObj deleteStockOut(ZmsStockOut zmsStockOut){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		try {
			zmsStockOutRepository.delete(zmsStockOut);
		} catch (Exception e) {
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		return hmiResultObj;
	}
	
	
	
	
	public synchronized List<ZmsStockOutDetail> findStockOutDetailBySktIdx(long stkIdx) {
		return zmsStockOutDetailRepository.findByStkIdx(stkIdx);
	}
	
	
	public synchronized HmiResultObj createStockOutDetail(ZmsStockOutDetail zmsStockOutDetail){
		HmiResultObj hmiResultObj=new HmiResultObj();
		zmsStockOutDetail= zmsStockOutDetailRepository.save(zmsStockOutDetail);
		hmiResultObj.setResultObj(zmsStockOutDetail);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
		
	}
	
	public synchronized HmiResultObj deleteStockOutDetail(ZmsStockOutDetail zmsStockOutDetail){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		try {
			zmsStockOutDetailRepository.delete(zmsStockOutDetail);
			
			
			List<ZmsStockOutDetail> zmsPaymentDetails=zmsStockOutDetailRepository.findByStkIdx(zmsStockOutDetail.getStkIdx());
			if(zmsPaymentDetails.size()==0) {
				ZmsStockOut zmsStockOut=new ZmsStockOut();
				zmsStockOut.setSktIdx(zmsStockOutDetail.getStkIdx());
				zmsStockOutRepository.delete(zmsStockOut);
			}
			
		} catch (Exception e) {
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		return hmiResultObj;
	}
	
	
}
