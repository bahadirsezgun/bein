package tr.com.beinplanner.measurement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.diabet.dao.UserDiabet;
import tr.com.beinplanner.measurement.dao.UserMeasurement;
import tr.com.beinplanner.measurement.repository.MeasurementRepository;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("measurementService")
public class MeasurementService {

	
	@Autowired
	MeasurementRepository measurementRepository;
	
	
	
	public List<UserMeasurement> findByUserId(long userId){
		return measurementRepository.findByUserId(userId);
	}
	
	
	public UserMeasurement createUserMeasurement(UserMeasurement userMeasurement){
		return measurementRepository.save(userMeasurement);
	}
	
	public HmiResultObj deleteUserDiabet(UserMeasurement userMeasurement){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			try {
				measurementRepository.delete(userMeasurement);
			} catch (Exception e) {
				hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}
		return hmiResultObj;
	}
	
	
}
