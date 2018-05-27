package tr.com.beinplanner.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.UserTests;
import tr.com.beinplanner.user.repository.UserTestsRepository;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("userTestService")
public class UserTestService {

	
	@Autowired
	UserTestsRepository userTestsRepository;
	

	@Autowired
	DefinitionService definitionService;
	
	
	@Autowired
	LoginSession loginSession;
	
	public synchronized HmiResultObj create(UserTests userTests){
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		userTests= userTestsRepository.save(userTests);
		
		hmiResultObj.setResultObj(userTests);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		
		return hmiResultObj;
		
	}
	
	public synchronized HmiResultObj delete(UserTests userTests){
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		userTestsRepository.delete(userTests);
		
		hmiResultObj.setResultObj(userTests);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		
		return hmiResultObj;
		
	}
	
   public synchronized HmiResultObj findUserTests(long userId){
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		List<UserTests> userTests= userTestsRepository.findByUserId(userId);
		
		userTests.stream().forEach(ut->{
			ut.setTestName(definitionService.findDefTestById(ut.getTestId()).getTestName());
		});
		
		
		hmiResultObj.setResultObj(userTests);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		
		return hmiResultObj;
		
	}
	
	
}
