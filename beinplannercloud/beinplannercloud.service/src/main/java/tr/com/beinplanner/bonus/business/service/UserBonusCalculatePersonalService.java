package tr.com.beinplanner.bonus.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.bonus.business.calculator.CalculateService;
import tr.com.beinplanner.bonus.business.calculator.personal.CalculatePersonalBonusToRate;
import tr.com.beinplanner.bonus.business.calculator.personal.CalculatePersonalBonusToStatic;
import tr.com.beinplanner.bonus.business.calculator.personal.CalculatePersonalBonusToStaticRate;
import tr.com.beinplanner.bonus.businessDao.UserBonusObj;
import tr.com.beinplanner.bonus.dao.UserBonusPaymentFactory;
import tr.com.beinplanner.bonus.dao.UserBonusPaymentPersonal;
import tr.com.beinplanner.bonus.service.UserBonusPaymentService;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.service.ScheduleService;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.BonusTypes;

@Service
@Scope("prototype")
public class UserBonusCalculatePersonalService implements  UserBonusCalculateService{

	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	CalculatePersonalBonusToRate calculatePersonalBonusToRate;
	
	@Autowired
	CalculatePersonalBonusToStatic calculatePersonalBonusToStatic;
	
	@Autowired
	CalculatePersonalBonusToStaticRate calculatePersonalBonusToStaticRate;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserBonusPaymentService userBonusPaymentService;
	
	@SuppressWarnings("unchecked")
	@Override
	public UserBonusObj findStaffBonusObj(long schStaffId, Date startDate, Date endDate,int firmId) {
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlansPersonalPlanByDatesForStaff(schStaffId, startDate, endDate,firmId);
		
		CalculateService calculateService=null;
		User  staff=userService.findUserById(schStaffId);
	
		if(staff.getBonusTypeP()==BonusTypes.BONUS_IS_TYPE_RATE){
			calculateService=calculatePersonalBonusToRate;
		}else if(staff.getBonusTypeP()==BonusTypes.BONUS_IS_TYPE_STATIC){
			calculateService=calculatePersonalBonusToStatic;
		}else if(staff.getBonusTypeP()==BonusTypes.BONUS_IS_TYPE_STATIC_RATE){
			calculateService=calculatePersonalBonusToStaticRate;
		}
		
		
		
		
		List<? extends UserBonusPaymentFactory> bonusPaymentPersonals=userBonusPaymentService.findUserBonusPaymentPersonalByDate(schStaffId, startDate, endDate);
		
		
		double payedAmount=0;
		if(bonusPaymentPersonals!=null){
			for (UserBonusPaymentFactory userBonusPaymentFactory : bonusPaymentPersonals) {
				payedAmount+=((UserBonusPaymentPersonal)userBonusPaymentFactory).getBonAmount();
			}
		}
		UserBonusObj userBonusObj=calculateService.calculateIt(scheduleTimePlans,schStaffId,staff.getFirmId());
		userBonusObj.setUserBonusPaymentFactories((List<UserBonusPaymentFactory>)bonusPaymentPersonals);
		userBonusObj.setPayedAmount(payedAmount);
		userBonusObj.setBonusType(staff.getBonusTypeP());
		
		
		return userBonusObj;
	}

	
	
	
}
