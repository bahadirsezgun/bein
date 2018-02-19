package tr.com.beinplanner.settings.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.settings.dao.PtGlobal;
import tr.com.beinplanner.settings.dao.PtLock;
import tr.com.beinplanner.settings.dao.PtRules;
import tr.com.beinplanner.settings.repository.PtGlobalRepository;
import tr.com.beinplanner.settings.repository.PtLockRepository;
import tr.com.beinplanner.settings.repository.PtRulesRepository;

@Service
@Qualifier(value="settingsService")
public class SettingsService {

	@Autowired
	PtGlobalRepository ptGlobalRepository;
	
	@Autowired
	PtRulesRepository ptRulesRepository;
	
	
	@Autowired
	PtLockRepository ptLockRepository;
	
	public PtRules findByRuleIdAndFirmId(int ruleId,int firmId) {
		return ptRulesRepository.findByRuleIdAndFirmId(ruleId, firmId);
	}
	
	
	public List<PtRules> findPtRulesByFirmId(int firmId){
		return ptRulesRepository.findByFirmId(firmId); 
	}
	
	public PtRules createPtRules(PtRules ptRules) {
		return ptRulesRepository.save(ptRules);
	}
	
	public PtGlobal createPtGlobal(PtGlobal ptGlobal) {
		return ptGlobalRepository.save(ptGlobal);
	}
		
	
	
	
	public PtGlobal findPtGlobalByFirmId(int firmId) {
		
		PtGlobal ptGlobal=ptGlobalRepository.findByFirmId(firmId);
		
		if(ptGlobal.getPtDateFormat().equals("%d/%m/%y")){
			ptGlobal.setPtDbDateFormat("dd/MM/yy");
			ptGlobal.setPtScrDateFormat("dd/MM/yy");
		}else if(ptGlobal.getPtDateFormat().equals("%d.%m.%y")){
			ptGlobal.setPtDbDateFormat("MM.dd.yy");
			ptGlobal.setPtScrDateFormat("MM.dd.yy");
		}else if(ptGlobal.getPtDateFormat().equals("%m/%d/%y")){
			ptGlobal.setPtDbDateFormat("MM/dd/yy");
			ptGlobal.setPtScrDateFormat("MM/dd/yy");
		}else if(ptGlobal.getPtDateFormat().equals("%m.%d.%y")){
			ptGlobal.setPtDbDateFormat("MM.dd.yy");
			ptGlobal.setPtScrDateFormat("MM.dd.yy");
		}else if(ptGlobal.getPtDateFormat().equals("%d/%m/%Y")){
			ptGlobal.setPtDbDateFormat("dd/MM/yyyy");
			ptGlobal.setPtScrDateFormat("dd/MM/yyyy");
		}else if(ptGlobal.getPtDateFormat().equals("%d.%m.%Y")){
			ptGlobal.setPtDbDateFormat("dd.MM.yyyy");
			ptGlobal.setPtScrDateFormat("dd.MM.yyyy");
		}else if(ptGlobal.getPtDateFormat().equals("%m/%d/%Y")){
			ptGlobal.setPtDbDateFormat("MM/dd/yyyy");
			ptGlobal.setPtScrDateFormat("MM/dd/yyyy");
		}else if(ptGlobal.getPtDateFormat().equals("%m.%d.%Y")){
			ptGlobal.setPtDbDateFormat("MM.dd.yyyy");
			ptGlobal.setPtScrDateFormat("MM.dd.yyyy");
		}
		
		return ptGlobal;
	}
	
	
	public PtLock createPtLock(PtLock ptLock) {
		return ptLockRepository.save(ptLock);
	}
	
	public PtLock findPtLock(int firmId) {
		return ptLockRepository.findOne(firmId);
	}
}
