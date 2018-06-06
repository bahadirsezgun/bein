package tr.com.beinplanner.zms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.zms.dao.ZmsStock;
import tr.com.beinplanner.zms.repository.ZmsStockRepository;

@Service
@Qualifier("zmsStockService")
public class ZmsStockService {

	
	@Autowired
	ZmsStockRepository zmsStockRepository;
	
	public synchronized List<ZmsStock> findAllZmsStock(int firmId){
		return zmsStockRepository.findByFirmId(firmId);
	}
	
	public synchronized ZmsStock findZmsStockByProductId(int firmId,long productId){
		return zmsStockRepository.findByFirmIdAndProductId(firmId, productId);
	}
	
}
