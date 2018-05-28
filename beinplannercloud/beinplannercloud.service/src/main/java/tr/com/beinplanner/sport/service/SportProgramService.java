package tr.com.beinplanner.sport.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.definition.dao.DefSporProgramDevice;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.packetsale.comparator.PacketSaleComparator;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.sport.comparator.SportComparator;
import tr.com.beinplanner.sport.dao.UserSportProgram;
import tr.com.beinplanner.sport.repository.SportProgramRepository;
import tr.com.beinplanner.user.dao.UserTests;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("sportProgramService")
public class SportProgramService {

	
	@Autowired
	SportProgramRepository sportProgramRepository;
	
	@Autowired
	DefinitionService definitionService;
	
	
	
	
	public synchronized List<UserSportProgram> findBySaleIdAndSaleTypeAndUserId(long saleId,String saleType,long userId) {
		
		List<UserSportProgram> userSportPrograms=sportProgramRepository.findBySaleIdAndSaleTypeAndUserId(saleId, saleType, userId);
		
		userSportPrograms.stream().forEach(usp->{
			usp.setSpName(definitionService.findDefSportById(usp.getSpId()).getSpName());
			usp.setSpdName(definitionService.findDefSportDeviceById(usp.getSpdId()).getSpdName());
			usp.setApplyDateName(DateTimeUtil.getDayNamesForScreen(usp.getApplyDate()));
		});
		
		Collections.sort(userSportPrograms,new SportComparator());
		
		return userSportPrograms;
	}
	
	
	public synchronized HmiResultObj create(UserSportProgram userSportProgram){
		HmiResultObj hmiResultObj=new HmiResultObj();
		userSportProgram= sportProgramRepository.save(userSportProgram);
		hmiResultObj.setResultObj(userSportProgram);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
		
	}
	
	public synchronized HmiResultObj delete(UserSportProgram userSportProgram){
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		sportProgramRepository.delete(userSportProgram);
		
		
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		
		return hmiResultObj;
		
	}
}
