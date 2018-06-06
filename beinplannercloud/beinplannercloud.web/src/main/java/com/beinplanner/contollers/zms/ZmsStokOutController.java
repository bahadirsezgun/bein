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
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.zms.dao.ZmsStockOut;
import tr.com.beinplanner.zms.service.ZmsProductService;
import tr.com.beinplanner.zms.service.ZmsStockOutService;

@RestController
@RequestMapping("/bein/zms/stockout")
public class ZmsStokOutController {

	@Autowired
	ZmsStockOutService zmsStockOutService;
	
	
	@Autowired
	ZmsProductService zmsProductService;
	
	@Autowired
	LoginSession loginSession;
	
	
	
	@PostMapping(value="/create") 
	public @ResponseBody HmiResultObj create(@RequestBody ZmsStockOut zmsStockOut ){
		zmsStockOut.setStaffId(loginSession.getUser().getUserId());
		zmsStockOut.setFirmId(loginSession.getUser().getFirmId());
		return zmsStockOutService.createStockOut(zmsStockOut);
	}
	
	@PostMapping(value="/delete") 
	public @ResponseBody HmiResultObj delete(@RequestBody  ZmsStockOut zmsStockOut ){
		zmsStockOut.setFirmId(loginSession.getUser().getFirmId());
		return zmsStockOutService.deleteStockOut(zmsStockOut);
	}
	
	
	@PostMapping(value="/findByName") 
	public @ResponseBody List<ZmsStockOut> findByName(@RequestBody User user ){
		return zmsStockOutService.findZmsStockOutByUsernameAndSurname(loginSession.getUser().getFirmId(), user.getUserName(), user.getUserSurname());
	}
	
	
}
