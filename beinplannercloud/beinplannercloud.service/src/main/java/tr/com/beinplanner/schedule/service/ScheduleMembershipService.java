package tr.com.beinplanner.schedule.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.beinplanner.program.dao.ProgramMembership;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.business.IScheduleMembership;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipTimePlan;
import tr.com.beinplanner.schedule.facade.ScheduleMembershipFacade;
import tr.com.beinplanner.util.OhbeUtil;
import tr.com.beinplanner.util.ProgDurationTypes;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("scheduleMembershipService")
public class ScheduleMembershipService {

	@Autowired
	@Qualifier("scheduleMembership")
	IScheduleMembership iScheduleMembership;
	
	@Autowired
	ProgramService programService;
	
	
	@Autowired
	@Qualifier("scheduleMembershipFacade")
	ScheduleMembershipFacade scheduleMembershipFacade;
	
	
	public ScheduleFactory findScheduleFactoryPlanBySaleId(long saleId) {
		return iScheduleMembership.findSchedulePlanBySaleId(saleId);
	}
	
	public ScheduleFactory findPlanById(long smpId) {
		return iScheduleMembership.findSchedulePlanById(smpId);
	}
	
	
	public ScheduleMembershipTimePlan findTimePlanById(long smtpId) {
		return iScheduleMembership.findScheduleTimePlanById(smtpId);
	}
	
	
	
	public HmiResultObj createPlan(ScheduleMembershipPlan scheduleMembershipPlan) {
		
		
		
		ProgramMembership programMembership=programService.findProgramMembershipById(scheduleMembershipPlan.getProgId());
		
		Date smpEndDate=(Date)scheduleMembershipPlan.getSmpStartDate().clone();
		
		if(programMembership.getProgDurationType()==ProgDurationTypes.DURATION_TYPE_DAILY){
			smpEndDate=ProgDurationTypes.getDateForNextDate(smpEndDate, programMembership.getProgDuration());
		}else if(programMembership.getProgDurationType()==ProgDurationTypes.DURATION_TYPE_WEEKLY){
			smpEndDate=ProgDurationTypes.getDateForNextDate(smpEndDate, programMembership.getProgDuration()*7);
		}else if(programMembership.getProgDurationType()==ProgDurationTypes.DURATION_TYPE_MONTHLY){
			smpEndDate=ProgDurationTypes.getDateForNextMonth(smpEndDate, programMembership.getProgDuration());
		}
		scheduleMembershipPlan.setSmpEndDate(smpEndDate);
		
		HmiResultObj hmiResultObj= scheduleMembershipFacade.canScheduleCreate(scheduleMembershipPlan);
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL_STR){
			return hmiResultObj;
		}
		
		hmiResultObj=iScheduleMembership.createPlan(scheduleMembershipPlan);
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS_STR){
			long smpId=((ScheduleMembershipPlan)hmiResultObj.getResultObj()).getSmpId();
			ScheduleMembershipTimePlan scheduleMembershipTimePlan=new ScheduleMembershipTimePlan();
			scheduleMembershipTimePlan.setSmpId(smpId);
			scheduleMembershipTimePlan.setSmpStartDate(scheduleMembershipPlan.getSmpStartDate());
			scheduleMembershipTimePlan.setSmpEndDate(scheduleMembershipPlan.getSmpEndDate());
			scheduleMembershipTimePlan.setSmpComment(scheduleMembershipPlan.getSmpComment());
			iScheduleMembership.createTimePlan(scheduleMembershipTimePlan);
		}
		
		return hmiResultObj;
		
		
		
	}
	
	public HmiResultObj createTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan) {
		return iScheduleMembership.createTimePlan(scheduleMembershipTimePlan);
	}
	
	public HmiResultObj deleteTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan) {
		return iScheduleMembership.deleteTimePlan(scheduleMembershipTimePlan);
	}
	
	
	public HmiResultObj freezeSchedule(@RequestBody ScheduleMembershipPlan smp){
		HmiResultObj hmiResultObj=new HmiResultObj();
		ScheduleMembershipPlan smpInDb=(ScheduleMembershipPlan)findPlanById(smp.getSmpId());
		ProgramMembership pmf=programService.findProgramMembershipById(smpInDb.getProgId());
		
		if(smpInDb.getSmpFreezeCount()>=pmf.getMaxFreezeCount()){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultMessage("maxFreeezeCountExceeded");
			return hmiResultObj;
		}
		
		hmiResultObj=scheduleMembershipFacade.canScheduleFreeze(smpInDb, smpInDb, pmf);
		
		
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_FAIL_STR)){
			return hmiResultObj;
		}else{
		
			int freezeDuration=pmf.getFreezeDuration();
			Date freezeEndDate=new Date();
			Date freezeStartDate=(Date)smp.getSmpStartDate().clone();
			if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_MONTHLY){
				freezeEndDate=OhbeUtil.getDateForNextMonth(smpInDb.getSmpEndDate(), freezeDuration);
				
			}else{
				if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_WEEKLY){
					freezeDuration=freezeDuration*7;
				}
				freezeEndDate=OhbeUtil.getDateForNextDate(smpInDb.getSmpEndDate(), freezeDuration);
			}
			
			smpInDb.setSmpEndDate(freezeEndDate);
			smpInDb.setSmpFreezeCount(smpInDb.getSmpFreezeCount()+1);
			createPlan(smpInDb);
			
			ScheduleMembershipTimePlan scheduleMembershipTimePlan=new ScheduleMembershipTimePlan();
			scheduleMembershipTimePlan.setSmpEndDate(freezeEndDate);
			scheduleMembershipTimePlan.setSmpStartDate(freezeStartDate);
			scheduleMembershipTimePlan.setSmpId(smp.getSmpId());
			scheduleMembershipTimePlan.setSmpComment(smp.getSmpComment());
			
			hmiResultObj=createTimePlan(scheduleMembershipTimePlan);
		}
		return hmiResultObj;
	}
	
	public HmiResultObj unFreezeSchedule(@PathVariable("smtpId") long smtpId,@PathVariable("smpId") long smpId) {
		
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		
		ScheduleMembershipPlan smp=(ScheduleMembershipPlan)findPlanById(smpId);
		ScheduleMembershipTimePlan scheduleMembershipTimePlan=findTimePlanById(smtpId);
		ProgramMembership pmf=programService.findProgramMembershipById(smp.getProgId());
		
		
		hmiResultObj=scheduleMembershipFacade.canScheduleUnFreeze(smp, scheduleMembershipTimePlan, pmf);
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL_STR){
			return hmiResultObj;
		}else{
		
				int freezeDuration=pmf.getFreezeDuration();
				
				if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_MONTHLY){
					smp.setSmpEndDate(OhbeUtil.getDateForNextMonth(smp.getSmpEndDate(), freezeDuration*-1));
				}else{
					if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_WEEKLY){
						freezeDuration=freezeDuration*7;
					}
					smp.setSmpEndDate(OhbeUtil.getDateForNextDate(smp.getSmpEndDate(), freezeDuration*-1));
				}
				
				smp.setSmpFreezeCount(smp.getSmpFreezeCount()-1);
				createPlan(smp);
				
				hmiResultObj=deleteTimePlan(scheduleMembershipTimePlan);
		}
		return hmiResultObj;
	}
	
	
}
