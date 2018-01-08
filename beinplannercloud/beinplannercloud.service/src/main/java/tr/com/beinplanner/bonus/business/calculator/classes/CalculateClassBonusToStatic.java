package tr.com.beinplanner.bonus.business.calculator.classes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.bonus.business.calculator.CalculateService;
import tr.com.beinplanner.bonus.businessDao.UserBonusDetailObj;
import tr.com.beinplanner.bonus.businessDao.UserBonusObj;
import tr.com.beinplanner.definition.dao.DefBonus;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersClassPlan;
import tr.com.beinplanner.schedule.service.ScheduleFactoryService;
import tr.com.beinplanner.settings.dao.PtRules;
import tr.com.beinplanner.util.BonusTypes;
import tr.com.beinplanner.util.PaymentConfirmUtil;
import tr.com.beinplanner.util.RuleUtil;
import tr.com.beinplanner.util.StatuTypes;
@Service
@Scope("prototype")
public class CalculateClassBonusToStatic implements CalculateService {

	@Autowired
	ScheduleFactoryService scheduleFactoryService;
	
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	PacketSaleService packetSaleService;
	
	@Override
	public UserBonusObj calculateIt(List<ScheduleTimePlan> scheduleTimePlans,long staffId,int firmId) {
		UserBonusObj userBonusObj=new UserBonusObj();
		
		PtRules ptRulesBonusForConfirmedPayment=loginSession.getPtRules()
					.stream()
					.filter(ptr->ptr.getRuleId()==RuleUtil.rulePayBonusForConfirmedPayment)
					.findFirst().get();
		PtRules ptRulesCreditCardCommissionRate=loginSession.getPtRules()
		.stream()
		.filter(ptr->ptr.getRuleId()==RuleUtil.ruleCreditCardCommissionRate)
		.findFirst().get();
		
		PtRules ptRulesCreditCardCommission=loginSession.getPtRules()
		.stream()
		.filter(ptr->ptr.getRuleId()==RuleUtil.ruleCreditCardCommission)
		.findFirst().get();
		
		
		
		
		int bonusPaymentRule=ptRulesBonusForConfirmedPayment.getRuleValue();
		int creditCardCommissionRate=ptRulesCreditCardCommissionRate.getRuleValue();
		int creditCardCommission=ptRulesCreditCardCommission.getRuleValue();
		
		
		userBonusObj.setBonusPaymentRule(bonusPaymentRule);
		userBonusObj.setCreditCardCommissionRate(creditCardCommissionRate);
		userBonusObj.setCreditCardCommissionRule(creditCardCommission);


		
		List<DefBonus> defBonuses= definitionService.findByUserIdAndBonusTypeAndBonusIsType(staffId, BonusTypes.BONUS_TYPE_CLASS, BonusTypes.BONUS_IS_TYPE_STATIC);
		
		int i=1;
		
		double willPayAmount=0;
		
		List<UserBonusDetailObj> userBonusDetailObjs=new ArrayList<UserBonusDetailObj>();
		
		for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
			if(scheduleTimePlan.getStatuTp()!=StatuTypes.TIMEPLAN_POSTPONE){
			UserBonusDetailObj userBonusDetailObj=new UserBonusDetailObj();
			
			double bonusPrice=0;
			for (DefBonus defBonus : defBonuses) {
				if(defBonus.getBonusProgId()==scheduleTimePlan.getSchedulePlan().getProgId()){
					bonusPrice=defBonus.getBonusValue();
					break;
				}
			}
			
			
			List<ScheduleFactory> usersInTimePlan=scheduleFactoryService.findScheduleUsersClassPlanBySchtId(scheduleTimePlan.getSchtId());
			userBonusDetailObj.setScheduleFactories(usersInTimePlan);
			double totalTimePlanPayment=0;
			
			for (ScheduleFactory scheduleFactory : usersInTimePlan) {
				PacketSaleClass packetSaleClass=packetSaleService.findPacketSaleClassById(((ScheduleUsersClassPlan)scheduleFactory).getSaleId());
				PacketPaymentClass packetPaymentClass=packetSaleClass.getPacketPaymentClass();
				
				double unitPrice=0;
				int saleCount=0;
				
				if(packetPaymentClass!=null){
					if(bonusPaymentRule==RuleUtil.RULE_OK){
						if(packetPaymentClass.getPayConfirm()==PaymentConfirmUtil.PAYMENT_CONFIRM){
							unitPrice=bonusPrice/usersInTimePlan.size();
							totalTimePlanPayment+=unitPrice;
						}
					}else{
						unitPrice=bonusPrice/usersInTimePlan.size();
						totalTimePlanPayment+=unitPrice;
						saleCount=packetSaleClass.getProgCount();
					}
					
				}
				((ScheduleUsersClassPlan)scheduleFactory).setUnitPrice(unitPrice);
				((ScheduleUsersClassPlan)scheduleFactory).setSaleCount(saleCount);
			}
			
			
			
			
			userBonusDetailObj.setSchCount(scheduleTimePlan.getSchCount());
			userBonusDetailObj.setPlanStartDateStr(scheduleTimePlan.getPlanStartDateStr());
			userBonusDetailObj.setClassCount(i);
			userBonusDetailObj.setProgName(scheduleTimePlan.getProgName());
			userBonusDetailObj.setBonusValue(bonusPrice);
			userBonusDetailObj.setPacketUnitPrice(totalTimePlanPayment);
			userBonusDetailObj.setStaffPaymentAmount(totalTimePlanPayment);
			userBonusDetailObj.setSchtId(scheduleTimePlan.getSchtId());
			
			willPayAmount+=userBonusDetailObj.getStaffPaymentAmount();
			
			userBonusDetailObjs.add(userBonusDetailObj);
		   i++;	
		}
		}
		
		userBonusObj.setWillPayAmount(willPayAmount);
		userBonusObj.setSchStaffId(staffId);
		userBonusObj.setUserBonusDetailObjs(userBonusDetailObjs);
		return userBonusObj;
	}
}
