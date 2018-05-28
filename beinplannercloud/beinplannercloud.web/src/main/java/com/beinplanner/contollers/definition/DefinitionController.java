package com.beinplanner.contollers.definition;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.definition.dao.DefCalendarTimes;
import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.definition.dao.DefSporProgram;
import tr.com.beinplanner.definition.dao.DefSporProgramDevice;
import tr.com.beinplanner.definition.dao.DefTest;
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
	
	@PostMapping(value="/firm/find")
	public  @ResponseBody DefFirm findDefFirm() {
		return definitionService.findFirm(loginSession.getUser().getFirmId());
	}
	
	@PostMapping(value="/firm/create")
	public  @ResponseBody HmiResultObj createDefFirm(@RequestBody DefFirm defFirm) {
		
		DefFirm defF=definitionService.findFirm(loginSession.getUser().getFirmId());
		
		defFirm.setFirmId(loginSession.getUser().getFirmId());
		defFirm.setCreateTime(new Date());
		defFirm.setFirmRestriction(defF.getFirmRestriction());
		defFirm.setFirmGroupId(defF.getFirmGroupId());
		defFirm.setStripeCustId(defF.getStripeCustId());
		defFirm.setStripePlanId(defF.getStripePlanId());
		defFirm.setFirmApproved(defF.getFirmApproved());
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		try {
			definitionService.createFirm(defFirm);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			
		} catch (Exception e) {
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			
		}
		
		
		return hmiResultObj;
	}
	
	@PostMapping(value="/defTest/findAll")
	public  @ResponseBody List<DefTest> findByFirmId() {
		return definitionService.findAllDefTestByFirmId(loginSession.getUser().getFirmId());
	}
	
	@PostMapping(value="/defTest/findById/{testId}")
	public  @ResponseBody DefTest findByTestId(@PathVariable(value="testId") long testId ) {
		return definitionService.findDefTestById(testId);
	}
	
	@PostMapping(value="/defTest/create")
	public  @ResponseBody HmiResultObj createDefTest(@RequestBody DefTest defTest) {
		defTest.setFirmId(loginSession.getUser().getFirmId());
		HmiResultObj hmiResultObj=new HmiResultObj();
		defTest= definitionService.createDefTest(defTest);
		hmiResultObj.setResultObj(defTest);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
	}
	
	@PostMapping(value="/defTest/delete")
	public  @ResponseBody HmiResultObj deleteDefTest(@RequestBody DefTest defTest) {
		defTest.setFirmId(loginSession.getUser().getFirmId());
		return definitionService.deleteDefTest(defTest);
	}
	
	
	
	
	@PostMapping(value="/defSport/findAll")
	public  @ResponseBody List<DefSporProgram> findSporProgramByFirmId() {
		return definitionService.findAllDefSporProgramByFirmId(loginSession.getUser().getFirmId());
	}
	
	@PostMapping(value="/defSport/findById/{spId}")
	public  @ResponseBody DefSporProgram findBySpId(@PathVariable(value="spId") long testId ) {
		return definitionService.findDefSportById(testId);
	}
	
	@PostMapping(value="/defSport/create")
	public  @ResponseBody HmiResultObj createDefSportProgram(@RequestBody DefSporProgram defSporProgram) {
		defSporProgram.setFirmId(loginSession.getUser().getFirmId());
		HmiResultObj hmiResultObj=new HmiResultObj();
		defSporProgram= definitionService.createDefSporProgram(defSporProgram);
		hmiResultObj.setResultObj(defSporProgram);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
	}
	
	@PostMapping(value="/defSport/delete")
	public  @ResponseBody HmiResultObj deleteDefSporProgram(@RequestBody DefSporProgram defSporProgram) {
		defSporProgram.setFirmId(loginSession.getUser().getFirmId());
		return definitionService.deleteDefSporProgram(defSporProgram);
	}
	
	
	
	
	@PostMapping(value="/defSportDevice/findBySp/{spId}")
	public  @ResponseBody List<DefSporProgramDevice> findSporProgramDeviceBySpId(@PathVariable(value="spId") long spId) {
		return definitionService.findAllDefSporProgramDeviceBySpId(spId);
	}
	
	@PostMapping(value="/defSportDevice/findById/{spdId}")
	public  @ResponseBody DefSporProgramDevice findBySpdId(@PathVariable(value="spdId") long spdId ) {
		return definitionService.findDefSportDeviceById(spdId);
	}
	
	@PostMapping(value="/defSportDevice/create")
	public  @ResponseBody HmiResultObj createDefSportProgramDevice(@RequestBody DefSporProgramDevice defSporProgramDevice) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		defSporProgramDevice= definitionService.createDefSporProgramDevice(defSporProgramDevice);
		hmiResultObj.setResultObj(defSporProgramDevice);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
	}
	
	@PostMapping(value="/defSportDevice/delete")
	public  @ResponseBody HmiResultObj deleteDefSporProgramDevice(@RequestBody DefSporProgramDevice defSporProgramDevice) {
		return definitionService.deleteDefSporProgramDevice(defSporProgramDevice);
	}
	
	
}
