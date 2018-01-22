package tr.com.beinplanner.definition.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.definition.dao.DefBonus;
import tr.com.beinplanner.definition.repository.DefBonusRepository;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("definitionService")
public class DefinitionService {

	@Autowired
	DefBonusRepository defBonusRepository;
	
	public List<DefBonus> findByUserIdAndBonusTypeAndBonusIsType(long userId,int bonusType,int bonusIsType){
		return defBonusRepository.findByUserIdAndBonusTypeAndBonusIsType(userId, bonusType, bonusIsType);
	}
	
	public HmiResultObj create(DefBonus defBonus){
		defBonus=defBonusRepository.save(defBonus);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultObj(defBonus);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		return hmiResultObj;
	}
	
	public HmiResultObj delete(DefBonus defBonus){
		defBonusRepository.delete(defBonus);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
	}
	
}
