package tr.com.beinplanner.zms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.zms.dao.ZmsStockIn;
import tr.com.beinplanner.zms.repository.ZmsStockInRepository;

@Service
@Qualifier("zmsStockInService")
public class ZmsStockInService {

	
	@Autowired
	ZmsStockInRepository zmsStockInRepository;
	
	
	public synchronized List<ZmsStockIn> findStockInByProductId(long stkIdx) {
		return zmsStockInRepository.findByProductId(stkIdx);
	}
	
	
	public synchronized HmiResultObj createStockIn(ZmsStockIn zmsStockIn){
		HmiResultObj hmiResultObj=new HmiResultObj();
		zmsStockIn= zmsStockInRepository.save(zmsStockIn);
		hmiResultObj.setResultObj(zmsStockIn);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
		
	}
	
	public synchronized HmiResultObj deleteStockIn(ZmsStockIn zmsStockIn){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		try {
			zmsStockInRepository.delete(zmsStockIn);
		} catch (Exception e) {
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		return hmiResultObj;
	}
	
}
