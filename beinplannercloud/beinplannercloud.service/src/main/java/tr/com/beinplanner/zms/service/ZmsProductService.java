package tr.com.beinplanner.zms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.zms.dao.ZmsProduct;
import tr.com.beinplanner.zms.repository.ZmsProductRepository;

@Service
@Qualifier("zmsProductService")
public class ZmsProductService {

	@Autowired
	ZmsProductRepository zmsProductRepository;
	
	public synchronized List<ZmsProduct> findAllZmsProducts(int firmId) {
		return zmsProductRepository.findByFirmId(firmId);
	}
	
	public synchronized ZmsProduct findById(long productId) {
		return zmsProductRepository.findOne(productId);
	}
	
	
	public synchronized HmiResultObj create(ZmsProduct zmsProduct){
		HmiResultObj hmiResultObj=new HmiResultObj();
		zmsProduct= zmsProductRepository.save(zmsProduct);
		hmiResultObj.setResultObj(zmsProduct);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
		
	}
	
	public synchronized HmiResultObj delete(ZmsProduct zmsProduct){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		try {
			zmsProductRepository.delete(zmsProduct);
		} catch (Exception e) {
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		return hmiResultObj;
	}
	
}
