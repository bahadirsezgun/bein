package com.beinplanner.contollers.diabet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.diabet.dao.UserDiabet;
import tr.com.beinplanner.diabet.dao.UserDiabetCalori;
import tr.com.beinplanner.diabet.service.DiabetService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;

@RestController
@RequestMapping("/bein/diabet")
public class DiabetController {

	@Autowired
	DiabetService diabetService;
	
	
	@PostMapping(value="/calori/find/{userId}/{saleId}/{saleType}")
	public  @ResponseBody UserDiabetCalori findByUserIdAndSaleIdAndSaleType(@PathVariable long userId,@PathVariable long saleId,@PathVariable String saleType ) {
		return diabetService.findByUserIdAndSaleIdAndSaleType(userId, saleId, saleType);
	}
	
	@PostMapping(value="/find/{udcId}")
	public  @ResponseBody List<UserDiabet> findByUdcId(@PathVariable long udcId) {
		return diabetService.findByUdcId(udcId);
	}
	
	
	@PostMapping(value="/create")
	public  @ResponseBody HmiResultObj createUserDiabet(@RequestBody UserDiabet userDiabet) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		try {
			
			if(userDiabet.getUdcId()==0) {
				UserDiabetCalori userDiabetCalori=new UserDiabetCalori();
				userDiabetCalori.setCalAmount(userDiabet.getCalAmount());
				userDiabetCalori.setSaleId(userDiabet.getSaleId());
				userDiabetCalori.setSaleType(userDiabet.getSaleType());
				userDiabetCalori.setUserId(userDiabet.getUserId());
				
				userDiabetCalori=diabetService.createUserDiabetCalori(userDiabetCalori);
				
				userDiabet.setUdcId(userDiabetCalori.getUdcId());
				
			}
			
			
			userDiabet=diabetService.createUserDiabet(userDiabet);
			hmiResultObj.setResultObj(userDiabet);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			
		} catch (Exception e) {
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			
		}
	  return hmiResultObj;
	}
	
	
	@PostMapping(value="/delete")
	public  @ResponseBody HmiResultObj deleteUserDiabet(@RequestBody UserDiabet userDiabet) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		try {
			diabetService.deleteUserDiabet(userDiabet);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			
		} catch (Exception e) {
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			
		}
	  return hmiResultObj;
	}
	
	@PostMapping(value="/calori/create")
	public  @ResponseBody HmiResultObj createUserDiabetCalori(@RequestBody UserDiabetCalori userDiabetCalori) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		try {
			userDiabetCalori=diabetService.createUserDiabetCalori(userDiabetCalori);
			hmiResultObj.setResultObj(userDiabetCalori);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			
		} catch (Exception e) {
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			
		}
	  return hmiResultObj;
	}
	
	@PostMapping(value="/calori/delete")
	public  @ResponseBody HmiResultObj deleteUserDiabetCalori(@RequestBody UserDiabetCalori userDiabetCalori) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		try {
			diabetService.deleteUserDiabetCalori(userDiabetCalori);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			
		} catch (Exception e) {
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			
		}
	  return hmiResultObj;
	}
	
}
