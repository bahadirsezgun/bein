package tr.com.beinplanner.bonus.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.bonus.dao.UserBonusPaymentClass;
import tr.com.beinplanner.bonus.dao.UserBonusPaymentPersonal;
import tr.com.beinplanner.bonus.repository.UserBonusPaymentClassRepository;
import tr.com.beinplanner.bonus.repository.UserBonusPaymentPersonalRepository;

@Service
@Qualifier("userBonusPaymentService")
public class UserBonusPaymentService {

	@Autowired
	UserBonusPaymentClassRepository userBonusPaymentClassRepository;
	
	@Autowired
	UserBonusPaymentPersonalRepository userBonusPaymentPersonalRepository;
	
	
  public List<UserBonusPaymentClass>	findUserBonusPaymentClassByDate(long schStaffId, Date startDate, Date endDate){
	  return userBonusPaymentClassRepository.findUserBonusPaymentClassByDate(schStaffId, startDate, endDate);
  }

  public List<UserBonusPaymentPersonal>	findUserBonusPaymentPersonalByDate(long schStaffId, Date startDate, Date endDate){
	  return userBonusPaymentPersonalRepository.findUserBonusPaymentPersonalByDate(schStaffId, startDate, endDate);
  }
  
  public double findTotalOfMonthBonusPaymentPersonal(int firmId, int month ,int year) {
	  List<UserBonusPaymentPersonal> userBonusPaymentPersonals=userBonusPaymentPersonalRepository.findByFirmIdAndBonMonthAndBonYear(firmId, month, year);
	  double totalPayment=userBonusPaymentPersonals.stream().mapToDouble(ubpp->ubpp.getBonAmount()).sum();
	  return totalPayment;
  }
  
  public double findTotalOfMonthBonusPaymentClass(int firmId, int month ,int year) {
	  List<UserBonusPaymentClass> userBonusPaymentClasses=userBonusPaymentClassRepository.findByFirmIdAndBonMonthAndBonYear(firmId, month, year);
	  double totalPayment=userBonusPaymentClasses.stream().mapToDouble(ubpc->ubpc.getBonAmount()).sum();
	  return totalPayment;
  }
  
  public double findTotalOfDateBonusPaymentPersonal(int firmId, Date startDate ,Date endDate) {
	  List<UserBonusPaymentPersonal> userBonusPaymentPersonals=userBonusPaymentPersonalRepository.findTotalOfDateBonusPaymentPersonal(firmId, startDate, endDate);
	  double totalPayment=userBonusPaymentPersonals.stream().mapToDouble(ubpp->ubpp.getBonAmount()).sum();
	  return totalPayment;
  }
  
  public double findTotalOfDateBonusPaymentClass(int firmId, Date startDate ,Date endDate) {
	  List<UserBonusPaymentClass> userBonusPaymentClasses=userBonusPaymentClassRepository.findTotalOfDateBonusPaymentClass(firmId, startDate, endDate);
	  double totalPayment=userBonusPaymentClasses.stream().mapToDouble(ubpc->ubpc.getBonAmount()).sum();
	  return totalPayment;
  }
  
}
