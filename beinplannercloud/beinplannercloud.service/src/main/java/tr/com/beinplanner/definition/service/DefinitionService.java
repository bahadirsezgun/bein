package tr.com.beinplanner.definition.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.definition.dao.DefBonus;
import tr.com.beinplanner.definition.dao.DefCalendarTimes;
import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.definition.dao.DefSporProgram;
import tr.com.beinplanner.definition.dao.DefSporProgramDevice;
import tr.com.beinplanner.definition.dao.DefTest;
import tr.com.beinplanner.definition.repository.DefBonusRepository;
import tr.com.beinplanner.definition.repository.DefCalendarTimesRepository;
import tr.com.beinplanner.definition.repository.DefFirmRepository;
import tr.com.beinplanner.definition.repository.DefSporProgramDeviceRepository;
import tr.com.beinplanner.definition.repository.DefSporProgramRepository;
import tr.com.beinplanner.definition.repository.DefTestRepository;
import tr.com.beinplanner.program.dao.ProgramFactory;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.repository.UserTestsRepository;
import tr.com.beinplanner.util.BonusTypes;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("definitionService")
public class DefinitionService {

	@Autowired
	DefBonusRepository defBonusRepository;
	
	@Autowired
	DefCalendarTimesRepository defCalendarTimesRepository;
	
	@Autowired
	DefFirmRepository defFirmRepository;
	
	@Autowired
	DefTestRepository defTestRepository;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	UserTestsRepository userTestsRepository;
	
	
	@Autowired
	DefSporProgramRepository defSporProgramRepository;
	
	@Autowired
	DefSporProgramDeviceRepository defSporProgramDeviceRepository;
	
	
	public DefFirm findFirm(int firmId){
		return defFirmRepository.findOne(firmId);
	}
	
	public DefFirm findFirmByEMail(String email){
		return defFirmRepository.findByFirmEmail(email);
	}
		
	public DefFirm createFirm(DefFirm defFirm){
		return defFirmRepository.save(defFirm);
	}
	
	
	public DefTest findDefTest(long testId){
		return defTestRepository.findOne(testId);
	}
	
	public List<DefTest> findAllDefTestByFirmId(int firmId){
		return defTestRepository.findByFirmId(firmId);
	}
	
	
	
	public DefTest findDefTestById(long testId){
		return defTestRepository.findOne(testId);
	}
	
	public DefTest createDefTest(DefTest defTest){
		return defTestRepository.save(defTest);
	}
	
	public HmiResultObj deleteDefTest(DefTest defTest){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			try {
				defTestRepository.delete(defTest);
			} catch (Exception e) {
				hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}
		return hmiResultObj;
	}
	
	
	public List<DefBonus> findByUserIdAndBonusTypeAndBonusIsType(long userId,int bonusType,int bonusIsType){
		List<DefBonus> defBonuses=defBonusRepository.findByUserIdAndBonusTypeAndBonusIsType(userId, bonusType, bonusIsType);
		defBonuses.forEach(defb->{
			ProgramFactory programFactory= null;
			if(defb.getBonusType()==BonusTypes.BONUS_TYPE_PERSONAL) {
			   programFactory= programService.findProgramPersonalById(defb.getBonusProgId());
			}else {
				programFactory= programService.findProgramClassById(defb.getBonusProgId());
			}
			defb.setProgramFactory(programFactory);
		});
		return defBonuses;
	}
	
	public HmiResultObj createDefBonus(DefBonus defBonus){
		
		List<DefBonus> defBonuses=findByUserIdAndBonusTypeAndBonusIsType(defBonus.getUserId(), defBonus.getBonusType(), defBonus.getBonusIsType());
		
		boolean sameDefinitionOfBonus=false;
		
		
		for (DefBonus defb : defBonuses) {
			if(defb.getBonusProgId()==defBonus.getBonusProgId() && defb.getBonusCount()==defBonus.getBonusCount()) {
				sameDefinitionOfBonus=true;
				break;
			}
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		if(sameDefinitionOfBonus) {
			hmiResultObj.setResultMessage("sameDefinitionOfBonus");
			hmiResultObj.setResultObj(defBonus);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}else {
			defBonus=defBonusRepository.save(defBonus);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultObj(defBonus);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		}
		
		
		
		
		return hmiResultObj;
	}
	
	public HmiResultObj deleteDefBonus(DefBonus defBonus){
		defBonusRepository.delete(defBonus);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
	}
	
	
	
	public DefCalendarTimes findCalendarTimes(int firmId) {
		
		DefCalendarTimes defCalendarTimes=null;
		try {
			defCalendarTimes = defCalendarTimesRepository.findDCTByFirmId(firmId);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return defCalendarTimes;
	}

	public DefCalendarTimes createDefCalendarTimes(DefCalendarTimes defCalendarTimes) {
		DefCalendarTimes defct=defCalendarTimesRepository.findDCTByFirmId(defCalendarTimes.getFirmId());
		if(defct!=null)
		  defCalendarTimesRepository.delete(defct);		
		
		return defCalendarTimesRepository.save(defCalendarTimes);
	}
	
	
	
	
	
	
	public List<DefSporProgram> findAllDefSporProgramByFirmId(int firmId){
		return defSporProgramRepository.findByFirmId(firmId);
	}
	
	public DefSporProgram findDefSportById(long spId){
		return defSporProgramRepository.findOne(spId);
	}
	
	public DefSporProgram createDefSporProgram(DefSporProgram defSporProgram){
		return defSporProgramRepository.save(defSporProgram);
	}
	
	public HmiResultObj deleteDefSporProgram(DefSporProgram defSporProgram){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			try {
				defSporProgramRepository.delete(defSporProgram);
			} catch (Exception e) {
				hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}
		return hmiResultObj;
	}
	
	
	
	public List<DefSporProgramDevice> findAllDefSporProgramDeviceBySpId(long spId){
		return defSporProgramDeviceRepository.findBySpId(spId);
	}
	
	public DefSporProgramDevice findDefSportDeviceById(long spdId){
		return defSporProgramDeviceRepository.findOne(spdId);
	}
	
	public DefSporProgramDevice createDefSporProgramDevice(DefSporProgramDevice defSporProgramDevice){
		return defSporProgramDeviceRepository.save(defSporProgramDevice);
	}
	
	public HmiResultObj deleteDefSporProgramDevice(DefSporProgramDevice defSporProgramDevice){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			try {
				defSporProgramDeviceRepository.delete(defSporProgramDevice);
			} catch (Exception e) {
				hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}
		return hmiResultObj;
	}
	
	
	
	
}
