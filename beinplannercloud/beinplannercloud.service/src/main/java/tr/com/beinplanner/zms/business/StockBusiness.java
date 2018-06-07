package tr.com.beinplanner.zms.business;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.util.ZmsStatuUtil;
import tr.com.beinplanner.zms.dao.ZmsStock;
import tr.com.beinplanner.zms.repository.ZmsStockRepository;

@Service
@Qualifier("stockBusiness")
public class StockBusiness {

	@Autowired
	ZmsStockRepository zmsStockRepository;
	
	@Transactional
	public void addToInStock(int firmId,long productId,int stockCount,int year) {
		ZmsStock zmsStock=zmsStockRepository.findByFirmIdAndProductIdAndStkYear(firmId, productId,year);
		if(zmsStock==null) {
			zmsStock=new ZmsStock();
			zmsStock.setFirmId(firmId);
			zmsStock.setProductId(productId);
			zmsStock.setStockInCount(stockCount);
		}else {
			zmsStock.setStockInCount(zmsStock.getStockInCount()+stockCount);
		}
		try {
			zmsStockRepository.save(zmsStock);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	@Transactional
	public void removeToInStock(int firmId,long productId,int stockCount,int year) {
		ZmsStock zmsStock=zmsStockRepository.findByFirmIdAndProductIdAndStkYear(firmId, productId,year);
		if(zmsStock!=null) {
			zmsStock.setStockInCount(zmsStock.getStockInCount()-stockCount);
			try {
				zmsStockRepository.save(zmsStock);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void addToOutStock(int firmId,long productId,int sellCount,int statu,int year) {
		if(statu==ZmsStatuUtil.ZMS_STATU_DONE) {
			ZmsStock zmsStock=zmsStockRepository.findByFirmIdAndProductIdAndStkYear(firmId, productId,year);
			if(zmsStock==null) {
				zmsStock=new ZmsStock();
				zmsStock.setFirmId(firmId);
				zmsStock.setProductId(productId);
				zmsStock.setStockOutCount(sellCount);
			}else {
				zmsStock.setStockOutCount(zmsStock.getStockOutCount()+sellCount);
			}
			zmsStockRepository.save(zmsStock);
		}
	}
	
	public void removeToOutStock(int firmId,long productId,int sellCount,int year) {
		    
		ZmsStock zmsStock=zmsStockRepository.findByFirmIdAndProductIdAndStkYear(firmId, productId,year);
			if(zmsStock!=null) {
				zmsStock.setStockOutCount(zmsStock.getStockInCount()-sellCount);
				zmsStockRepository.save(zmsStock);
			}
		
	}
	
	

	
}
