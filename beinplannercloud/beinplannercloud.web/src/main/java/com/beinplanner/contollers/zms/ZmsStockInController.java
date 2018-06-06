package com.beinplanner.contollers.zms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ZmsStatuUtil;
import tr.com.beinplanner.zms.dao.ZmsStockIn;
import tr.com.beinplanner.zms.dao.ZmsStockOut;
import tr.com.beinplanner.zms.service.ZmsProductService;
import tr.com.beinplanner.zms.service.ZmsStockInService;
import tr.com.beinplanner.zms.service.ZmsStockOutService;

@RestController
@RequestMapping("/bein/zms/stockin")
public class ZmsStockInController {

	@Autowired
	ZmsStockInService zmsStockInService;
	
	
	@Autowired
	ZmsProductService zmsProductService;
	
	@Autowired
	LoginSession loginSession;
	
	
	
	@PostMapping(value="/create") 
	public @ResponseBody HmiResultObj create(@RequestBody ZmsStockIn zmsStockIn ){
		zmsStockIn.setStaffId(loginSession.getUser().getUserId());
		zmsStockIn.setFirmId(loginSession.getUser().getFirmId());
		return zmsStockInService.createStockIn(zmsStockIn);
	}
	
	@PostMapping(value="/delete") 
	public @ResponseBody HmiResultObj delete(@RequestBody ZmsStockIn zmsStockIn ){
		zmsStockIn.setFirmId(loginSession.getUser().getFirmId());
		return zmsStockInService.deleteStockIn(zmsStockIn);
	}
	
	
	
	
	
	
}
