package com.beinplanner.contollers.definition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.definition.dao.DefCalendarTimes;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;

@RestController
@RequestMapping("/bein/definition")
public class DefinitionController {

	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	LoginSession loginSession;
	
	@PostMapping(value="/defCalendarTimes/create")
	public  @ResponseBody HmiResultObj createDefCalendarTimes(@RequestBody DefCalendarTimes defCalendarTimes) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		defCalendarTimes.setFirmId(loginSession.getUser().getFirmId());
		defCalendarTimes=definitionService.createDefCalendarTimes(defCalendarTimes);
		return hmiResultObj;
	}
	
	@PostMapping(value="/defCalendarTimes/find")
	public  @ResponseBody DefCalendarTimes findDefCalendarTimes() {
		return definitionService.findCalendarTimes(loginSession.getUser().getFirmId());
	}
	
	
}
