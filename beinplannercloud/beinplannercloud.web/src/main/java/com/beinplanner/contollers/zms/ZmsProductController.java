package com.beinplanner.contollers.zms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.zms.dao.ZmsProduct;
import tr.com.beinplanner.zms.service.ZmsProductService;

@RestController
@RequestMapping("/bein/zms/product")
public class ZmsProductController {

	@Autowired
	ZmsProductService zmsProductService;
	
	@Autowired
	LoginSession loginSession;
	
	@PostMapping(value="/create") 
	public @ResponseBody HmiResultObj create(@RequestBody ZmsProduct zmsProduct ){
		zmsProduct.setFirmId(loginSession.getUser().getFirmId());
		zmsProduct.setStaffId(loginSession.getUser().getUserId());
		return zmsProductService.create(zmsProduct);
	}
	
	@PostMapping(value="/delete") 
	public @ResponseBody HmiResultObj delete(@RequestBody ZmsProduct zmsProduct ){
		return zmsProductService.delete(zmsProduct);
	}
	
	@PostMapping(value="/findAll") 
	public @ResponseBody List<ZmsProduct> findAll( ){
		return zmsProductService.findAllZmsProducts(loginSession.getUser().getFirmId());
	}
	
	
	@PostMapping(value="/findById/{productId}") 
	public @ResponseBody ZmsProduct findById(@PathVariable long productId){
		return zmsProductService.findById(productId);
	}
	
}
