package com.beinplanner.contollers.measurement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.diabet.dao.UserDiabet;
import tr.com.beinplanner.measurement.dao.UserMeasurement;
import tr.com.beinplanner.measurement.service.MeasurementService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;

@RestController
@RequestMapping("/bein/measurement")
public class MeasurementController {

	
	
	@Autowired
	MeasurementService measurementService;
	
	
	@PostMapping(value="/find/{userId}")
	public  @ResponseBody List<UserMeasurement> findByUserId(@PathVariable long userId ) {
		return measurementService.findByUserId(userId);
	}
	
	@PostMapping(value="/create")
	public  @ResponseBody HmiResultObj create(@RequestBody UserMeasurement userMeasurement ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		try {
			measurementService.createUserMeasurement(userMeasurement);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			
		} catch (Exception e) {
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			
		}
	  return hmiResultObj;
	}
	
	@PostMapping(value="/delete")
	public  @ResponseBody HmiResultObj delete(@RequestBody UserMeasurement userMeasurement ) {
		return measurementService.deleteUserDiabet(userMeasurement);
	}
}
