package tr.com.beinplanner.zms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.OhbeUtil;
import tr.com.beinplanner.util.ZmsStatuUtil;
import tr.com.beinplanner.zms.dao.ZmsStock;
import tr.com.beinplanner.zms.dao.ZmsStockIn;
import tr.com.beinplanner.zms.dao.Custom_ZmsStockMonth;
import tr.com.beinplanner.zms.dao.Custom_ZmsStockOutOrder;
import tr.com.beinplanner.zms.dao.ZmsProduct;
import tr.com.beinplanner.zms.dao.ZmsStockOut;
import tr.com.beinplanner.zms.repository.ZmsProductRepository;
import tr.com.beinplanner.zms.repository.ZmsStockInRepository;
import tr.com.beinplanner.zms.repository.ZmsStockOutRepository;
import tr.com.beinplanner.zms.repository.ZmsStockRepository;

@Service
@Qualifier("zmsStockService")
public class ZmsStockService {

	
	@Autowired
	ZmsStockRepository zmsStockRepository;
	
	@Autowired
	ZmsStockInRepository zmsStockInRepository;
	
	@Autowired
	ZmsStockOutRepository zmsStockOutRepository;
	
	@Autowired
	ZmsProductRepository zmsProductRepository;
	
	@Autowired
	UserService userService;
	
	public synchronized List<ZmsStock> findAllZmsStock(int firmId){
		return zmsStockRepository.findByFirmId(firmId);
	}
	
	
	
	public synchronized ZmsStock findZmsStockByProductId(int firmId,long productId){
		return zmsStockRepository.findByFirmIdAndProductId(firmId, productId);
	}
	
	
	public List<ZmsStockOut> findZmsStockOutByStatusAndDate(int firmId,int year,int month,int statu){
		
		Date startDate=new Date();
		Date endDate=new Date();
		
		
		
		if(month!=0) {
			String monthStr=""+month;
			if(month<10)
				monthStr="0"+month;
			
			String startDateStr="01/"+monthStr+"/"+year;
			 startDate=OhbeUtil.getThatDateForNight(startDateStr,"dd/MM/yyyy"); 
			 endDate=OhbeUtil.getDateForNextMonth(startDate, 1);
		}else {
			String startDateStr="01/01/"+year;
			 startDate=OhbeUtil.getThatDateForNight(startDateStr,"dd/MM/yyyy"); 
			
			String endDateStr="01/01/"+(year+1);
			 endDate=OhbeUtil.getThatDateForNight(endDateStr,"dd/MM/yyyy"); 
			
		}
		
		
		
		List<ZmsStockOut> stockOuts=zmsStockOutRepository.findOrdersByStatusForDate(firmId, startDate, endDate, statu);
		stockOuts.forEach(sto->{
			User user=userService.findUserById(sto.getUserId());
			sto.setUser(user);
			ZmsProduct zmsProduct=zmsProductRepository.findOne(sto.getProductId());
			sto.setProductName(zmsProduct.getProductName());
			sto.setProductUnit(zmsProduct.getProductUnit());
			
		});
		
		return stockOuts;
	}
	
	
	public Custom_ZmsStockMonth findStocksAndOrdersForMonth(int firmId,int year,int month){
		Custom_ZmsStockMonth zmsStockMonth=new Custom_ZmsStockMonth();
		
		String monthStr=""+month;
		if(month<10)
			monthStr="0"+month;
		
		String startDateStr="01/"+monthStr+"/"+year;
		Date startDate=OhbeUtil.getThatDateForNight(startDateStr,"dd/MM/yyyy"); 
		Date endDate=OhbeUtil.getDateForNextMonth(startDate, 1);
		
		zmsStockMonth.setMonth(month);
		zmsStockMonth.setYear(year);
		
		List<ZmsStockIn> zmsStockIns=zmsStockInRepository.findStockInMonth(firmId, startDate, endDate);
		
		zmsStockIns.stream().forEach(zti->{
			zmsStockMonth.setStockInCount(zmsStockMonth.getStockInCount()+zti.getStockCount());
			zmsStockMonth.setStockInPrice(zmsStockMonth.getStockInPrice()+zti.getStockPrice());
		});
		
		List<ZmsStockOut> zmsStockOuts=zmsStockOutRepository.findStockOutByDate(firmId, startDate, endDate);
		zmsStockOuts.stream().forEach(zto->{
			zmsStockMonth.setStockOutCount(zmsStockMonth.getStockOutCount()+zto.getSellCount());
			zmsStockMonth.setStockOutPrice(zmsStockMonth.getStockOutPrice()+zto.getSellPrice());
		});
		
		return zmsStockMonth;
	}
	
	
	
	public Custom_ZmsStockOutOrder findOrdersByStatusForYear(int firmId,int year){
	
		
		String startDateStr="01/01/"+year;
		Date startDate=OhbeUtil.getThatDateForNight(startDateStr,"dd/MM/yyyy"); 
		
		String endDateStr="01/01/"+(year+1);
		Date endDate=OhbeUtil.getThatDateForNight(endDateStr,"dd/MM/yyyy"); 
		
		
		List<ZmsStockOut> zmsStockOuts=zmsStockOutRepository.findStockOutByDate(firmId, startDate, endDate);
		
		Custom_ZmsStockOutOrder custom_ZmsStockOutOrder=new Custom_ZmsStockOutOrder();
		custom_ZmsStockOutOrder.setYear(year);
		
		 Map<Integer,List<ZmsStockOut>> zmsStockOutssGroup=zmsStockOuts.stream().collect(Collectors.groupingBy(ZmsStockOut::getSellStatu));
		
		 zmsStockOutssGroup.forEach((k,zls)->{
			 int stockCount=zls.stream().mapToInt(z->z.getSellCount()).sum() ;
			 double stockPrice=zls.stream().mapToDouble(z->z.getSellPrice()).sum() ;
			
			 if(zls.get(0).getSellStatu()==ZmsStatuUtil.ZMS_STATU_PRE_ORDERED) {
				 custom_ZmsStockOutOrder.setPreOrderedCount(stockCount);
				 custom_ZmsStockOutOrder.setPreOrderedPrice(stockPrice); 
			 }else if(zls.get(0).getSellStatu()==ZmsStatuUtil.ZMS_STATU_ORDERED) {
				 custom_ZmsStockOutOrder.setOrderedCount(stockCount);
				 custom_ZmsStockOutOrder.setOrderedPrice(stockPrice); 
			 }else if(zls.get(0).getSellStatu()==ZmsStatuUtil.ZMS_STATU_SENDED) {
				 custom_ZmsStockOutOrder.setSendedCount(stockCount);
				 custom_ZmsStockOutOrder.setSendedPrice(stockPrice); 
			 }else if(zls.get(0).getSellStatu()==ZmsStatuUtil.ZMS_STATU_DONE) {
				 custom_ZmsStockOutOrder.setDoneCount(stockCount);
				 custom_ZmsStockOutOrder.setDonePrice(stockPrice); 
			 }
			 
		});
		 
		return custom_ZmsStockOutOrder;
	}
	
	
	
}
