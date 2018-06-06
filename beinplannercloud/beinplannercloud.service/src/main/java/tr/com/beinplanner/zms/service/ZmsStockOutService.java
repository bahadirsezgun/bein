package tr.com.beinplanner.zms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.zms.business.StockBusiness;
import tr.com.beinplanner.zms.dao.ZmsProduct;
import tr.com.beinplanner.zms.dao.ZmsStockOut;
import tr.com.beinplanner.zms.dao.ZmsStockOutDetail;
import tr.com.beinplanner.zms.repository.ZmsProductRepository;
import tr.com.beinplanner.zms.repository.ZmsStockOutDetailRepository;
import tr.com.beinplanner.zms.repository.ZmsStockOutRepository;

@Service
@Qualifier("zmsStockOutService")
public class ZmsStockOutService {

	
	@Autowired
	ZmsStockOutRepository zmsStockOutRepository;
	
	@Autowired
	ZmsStockOutDetailRepository zmsStockOutDetailRepository;
	
	@Autowired
	StockBusiness stockBusiness;
	
	@Autowired
	UserService userService;
	
	
	
	@Autowired
	ZmsProductRepository zmsProductRepository;
	
	public synchronized List<ZmsStockOut> findStockOutByProductId(long stkIdx) {
		return zmsStockOutRepository.findByProductId(stkIdx);
	}
	
	public synchronized List<ZmsStockOut> findAllZmsStockOutGroupByProduct(int firmId,int statu){
		 List<ZmsStockOut> zmsStockOuts= zmsStockOutRepository.findAllZmsStockInGroupByProduct(firmId, statu);
		 zmsStockOuts.stream().forEach(zmsSO->{
			 ZmsProduct zmsProduct=zmsProductRepository.findOne(zmsSO.getProductId());
			 zmsSO.setProductUnit(zmsProduct.getProductUnit());
			 zmsSO.setProductName(zmsProduct.getProductName());
		 });
		 
		 return zmsStockOuts;
	}
	
	
	public List<ZmsStockOut> findZmsStockOutByUsernameAndSurname(int firmId,String userName,String userSurname){
		
		List<ZmsStockOut> stockOuts=zmsStockOutRepository.findZmsStockOutByUsernameAndSurname(firmId, userName, userSurname);
		stockOuts.forEach(sto->{
			User user=userService.findUserById(sto.getUserId());
			sto.setUser(user);
			ZmsProduct zmsProduct=zmsProductRepository.findOne(sto.getProductId());
			sto.setProductName(zmsProduct.getProductName());
			sto.setProductUnit(zmsProduct.getProductUnit());
			
		});
		
		return zmsStockOutRepository.findZmsStockOutByUsernameAndSurname(firmId, userName, userSurname);
	}
	
	public synchronized ZmsStockOut findZmsStockOutByProduct(int statu,long productId){
		 List<ZmsStockOut> zmsSO= zmsStockOutRepository.findZmsStockOutByProduct(statu,productId);
		 
		 ZmsStockOut zmsStockOut=new ZmsStockOut();
		 if(zmsSO.size()>0) {
			 ZmsProduct zmsProduct=new ZmsProduct();
			  zmsProduct=zmsProductRepository.findOne(zmsSO.get(0).getProductId());
			  zmsStockOut.setProductUnit(zmsProduct.getProductUnit());
			  zmsStockOut.setProductName(zmsProduct.getProductName());
			  zmsStockOut.setProductId(productId);
		}
		 
		zmsSO.stream().forEach(zo->{
			 zmsStockOut.setSellCount(zmsStockOut.getSellCount()+zo.getSellCount());
			 zmsStockOut.setSellPrice(zmsStockOut.getSellPrice()+zo.getSellPrice());
		});
		
		return zmsStockOut;
	}
	
	
	public synchronized HmiResultObj createStockOut(ZmsStockOut zmsStockOut){
		HmiResultObj hmiResultObj=new HmiResultObj();
		try {
			stockBusiness.addToOutStock(zmsStockOut.getFirmId(), zmsStockOut.getProductId(), zmsStockOut.getSellCount(),zmsStockOut.getSellStatu());
			zmsStockOut= zmsStockOutRepository.save(zmsStockOut);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			stockBusiness.removeToOutStock(zmsStockOut.getFirmId(), zmsStockOut.getProductId(), zmsStockOut.getSellCount());
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
				zmsStockOut.setStkIdx(zmsStockOutDetail.getStkIdx());
				zmsStockOutRepository.delete(zmsStockOut);
			}
			
		} catch (Exception e) {
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		return hmiResultObj;
	}
	
	
}
