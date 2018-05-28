package com.beinplanner.contollers.sports;

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
import tr.com.beinplanner.sport.dao.UserSportProgram;
import tr.com.beinplanner.sport.service.SportProgramService;

@RestController
@RequestMapping("/bein/sport")
public class SportProgramController {

	
	@Autowired
	LoginSession loginSession;
	
	
	@Autowired
	SportProgramService sportProgramService;
	
	
	
	
	@PostMapping(value="/findBySaleIdAndSaleTypeAndUserId/{saleId}/{saleType}/{userId}") 
	public @ResponseBody List<UserSportProgram>	findBySaleIdAndSaleTypeAndUserId(@PathVariable(value="saleId") long saleId,@PathVariable(value="saleType") String saleType,@PathVariable(value="userId") long userId ){
		return sportProgramService.findBySaleIdAndSaleTypeAndUserId(saleId, saleType, userId);
	}
	
	@PostMapping(value="/create") 
	public @ResponseBody HmiResultObj	create(@RequestBody UserSportProgram userSportProgram ){
		return sportProgramService.create(userSportProgram);
	}
	
	@PostMapping(value="/delete") 
	public @ResponseBody HmiResultObj	delete(@RequestBody UserSportProgram userSportProgram ){
		return sportProgramService.delete(userSportProgram);
	}
}
