package tr.com.beinplanner.diabet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.diabet.dao.UserDiabet;
import tr.com.beinplanner.diabet.dao.UserDiabetCalori;
import tr.com.beinplanner.diabet.repository.DiabetCaloriRepository;
import tr.com.beinplanner.diabet.repository.DiabetRepository;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("diabetService")
public class DiabetService {

	@Autowired
	DiabetRepository diabetRepository;
	
	@Autowired
	DiabetCaloriRepository diabetCaloriRepository;
	
	
	
	public UserDiabetCalori findByUserIdAndSaleIdAndSaleType(long userId,long saleId,String saleType){
		return diabetCaloriRepository.findByUserIdAndSaleIdAndSaleType(userId, saleId, saleType);
	}
	
	public List<UserDiabet> findByUdcId(long udcId){
		
		List<UserDiabet> userDiabets=diabetRepository.findByUdcId(udcId);
		userDiabets.forEach(ud->{
			ud.setDiaDateName(DateTimeUtil.getDayNames(ud.getDiaDate()));
		});
		
		return userDiabets;
	}
	
	
	public UserDiabet createUserDiabet(UserDiabet userDiabet){
		return diabetRepository.save(userDiabet);
	}
	
	public HmiResultObj deleteUserDiabet(UserDiabet userDiabet){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			try {
				diabetRepository.delete(userDiabet);
			} catch (Exception e) {
				hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}
		return hmiResultObj;
	}
	
	public UserDiabetCalori createUserDiabetCalori(UserDiabetCalori userDiabetCalori){
		return diabetCaloriRepository.save(userDiabetCalori);
	}
	
	public HmiResultObj deleteUserDiabetCalori(UserDiabetCalori userDiabetCalori){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			try {
				diabetCaloriRepository.delete(userDiabetCalori);
			} catch (Exception e) {
				hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}
		return hmiResultObj;
	}
	
	
}
