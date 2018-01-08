package tr.com.beinplanner.income.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.income.dao.PtExpenses;
import tr.com.beinplanner.income.repository.PtExpensesRepository;
import tr.com.beinplanner.util.OhbeUtil;

@Service
@Qualifier(value="ptExpensesService")
public class PtExpensesService {

	@Autowired
	PtExpensesRepository ptExpensesRepository;
	
	public List<PtExpenses> findPtExpensesForMonth(int year, int month, int firmId ){
		String monthStr=""+month;
		if(month<10)
			monthStr="0"+month;
		
		String startDateStr="01/"+monthStr+"/"+year;
		Date startDate=OhbeUtil.getThatDateForNight(startDateStr,"dd/MM/yyyy"); 
		Date endDate=OhbeUtil.getDateForNextMonth(startDate, 1);
		
		return ptExpensesRepository.findPtExpensesForDate(startDate, endDate, firmId);
	}
	
	public List<PtExpenses> findPtExpensesForDate(Date startDate, Date endDate, int firmId ){
		
		return ptExpensesRepository.findPtExpensesForDate(startDate, endDate, firmId);
	}
	
}
