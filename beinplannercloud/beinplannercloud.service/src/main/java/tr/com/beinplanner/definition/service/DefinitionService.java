package tr.com.beinplanner.definition.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.definition.dao.DefBonus;
import tr.com.beinplanner.definition.repository.DefBonusRepository;

@Service
@Qualifier("definitionService")
public class DefinitionService {

	@Autowired
	DefBonusRepository defBonusRepository;
	
	public List<DefBonus> findByUserIdAndBonusTypeAndBonusIsType(long userId,int bonusType,int bonusIsType){
		return defBonusRepository.findByUserIdAndBonusTypeAndBonusIsType(userId, bonusType, bonusIsType);
	}
}
