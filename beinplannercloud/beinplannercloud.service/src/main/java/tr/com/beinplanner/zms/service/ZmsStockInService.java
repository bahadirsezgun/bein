package tr.com.beinplanner.zms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.zms.dao.ZmsProduct;
import tr.com.beinplanner.zms.dao.ZmsStockIn;
import tr.com.beinplanner.zms.repository.ZmsProductRepository;
import tr.com.beinplanner.zms.repository.ZmsStockInRepository;

@Service
@Qualifier("zmsStockInService")
public class ZmsStockInService {

	
	@Autowired
	ZmsStockInRepository zmsStockInRepository;
	
	@Autowired
	ZmsProductRepository zmsProductRepository;
	
	
	public synchronized List<ZmsStockIn> findStockInByProductId(long stkIdx) {
		return zmsStockInRepository.findByProductId(stkIdx);
	}
	
	
	public synchronized HmiResultObj createStockIn(ZmsStockIn zmsStockIn){
		HmiResultObj hmiResultObj=new HmiResultObj();
		try {
			zmsStockIn= zmsStockInRepository.save(zmsStockIn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public synchronized List<ZmsStockIn> findAllZmsStockInGroupByProduct(int firmId){
		 
		
		
		List<ZmsStockIn> zmsStockIns= zmsStockInRepository.findAllZmsStockInGroupByProduct(firmId);
		 zmsStockIns.stream().forEach(zmsSI->{
			 ZmsProduct zmsProduct=zmsProductRepository.findOne(zmsSI.getProductId());
			 zmsSI.setProductUnit(zmsProduct.getProductUnit());
			 zmsSI.setProductName(zmsProduct.getProductName());
		 });
		 
		 
		 Map<Long,List<ZmsStockIn>> zmsStockInsGroup=zmsStockIns.stream().collect(Collectors.groupingBy(ZmsStockIn::getProductId));
			
		 
		 List<ZmsStockIn> zstin=new ArrayList<ZmsStockIn>();
		 
		 zmsStockInsGroup.forEach((k,zls)->{
			 int stockCount=zls.stream().mapToInt(z->z.getStockCount()).sum() ;
			 double stockPrice=zls.stream().mapToDouble(z->z.getStockCount()).sum() ;
			 ZmsStockIn zmsStockIn=new ZmsStockIn();
			 zmsStockIn.setStockCount(stockCount);
			 zmsStockIn.setStockPrice(stockPrice);
			 zstin.add(zmsStockIn);
		 });
		
		 return zstin;
	}
	
}
