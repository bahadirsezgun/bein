package com.beinplanner.contollers.zms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.util.ZmsStatuUtil;
import tr.com.beinplanner.zms.dao.ZmsProduct;
import tr.com.beinplanner.zms.dao.ZmsStock;
import tr.com.beinplanner.zms.dao.ZmsStockIn;
import tr.com.beinplanner.zms.dao.ZmsStockOut;
import tr.com.beinplanner.zms.service.ZmsProductService;
import tr.com.beinplanner.zms.service.ZmsStockService;

@RestController
@RequestMapping("/bein/zms/stock")
public class ZmsStockController {

	
	@Autowired
	ZmsProductService zmsProductService;
	
	@Autowired
	ZmsStockService zmsStockService;
	
	
	@Autowired
	LoginSession loginSession;
	
	
	@PostMapping(value="/findAllZmsStock") 
	public @ResponseBody List<ZmsStock> findAllZmsStock( ){
		
		List<ZmsStock> zmsStocks= zmsStockService.findAllZmsStock(loginSession.getUser().getFirmId());
		
		zmsStocks.forEach(zmsSI->{
			ZmsProduct zmsProduct=zmsProductService.findById(zmsSI.getProductId());
			zmsSI.setZmsProduct(zmsProduct);
			
		});
		
		return zmsStocks;
	}
	
}
