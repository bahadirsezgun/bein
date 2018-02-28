package tr.com.beinplanner.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.status.StatusUtil;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersClassPlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;
import tr.com.beinplanner.schedule.facade.SchedulePersonalClassFacadeService;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersClassPlanRepository;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.util.StatuTypes;

@Service
@Qualifier("scheduleClassService")
public class ScheduleClassService implements IScheduleService {

	
	@Autowired
	SchedulePlanRepository schedulePlanRepository;
	
	@Autowired
	ScheduleTimePlanRepository scheduleTimePlanRepository;
	
	@Autowired
	ScheduleUsersClassPlanRepository scheduleUsersClassPlanRepository;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	@Qualifier("scheduleClassFacade")
	SchedulePersonalClassFacadeService schedulePersonalClassFacadeService;
	
	
	@Override
	public synchronized HmiResultObj createPlan(ScheduleTimePlan scheduleTimePlan,SchedulePlan schedulePlan) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	

	@Override
	public HmiResultObj updateScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleChange(scheduleTimePlan.getSchtId());
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			scheduleTimePlanRepository.save(scheduleTimePlan);
		}
		return hmiResultObj;
	}






	@Override
	public HmiResultObj cancelScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleChange(scheduleTimePlan.getSchtId());
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			if(scheduleTimePlan.getStatuTp()==StatuTypes.TIMEPLAN_CANCEL) {
				scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_NORMAL);
			}else {
				scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_CANCEL);
			}
			scheduleTimePlanRepository.save(scheduleTimePlan);
		}
		return hmiResultObj;
	}






	@Override
	public HmiResultObj postponeScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleChange(scheduleTimePlan.getSchtId());
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			
			if(scheduleTimePlan.getStatuTp()==StatuTypes.TIMEPLAN_POSTPONE) {
				scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_NORMAL);
			}else {
				scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_POSTPONE);
			}
			
			scheduleTimePlanRepository.save(scheduleTimePlan);
		}
		return hmiResultObj;
	}






	@Override
	public HmiResultObj deleteScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleTimePlanDelete(scheduleTimePlan);
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(scheduleTimePlan.getSchId());
			List<ScheduleTimePlan> scheduleTimePlans=scheduleTimePlanRepository.findBySchId(schedulePlan.getSchId());
			if(scheduleTimePlans.size()==1) {
				schedulePlanRepository.delete(schedulePlan.getSchId());
			}else {
				scheduleTimePlanRepository.delete(scheduleTimePlan);
			}
		}
		return hmiResultObj;
	}






	@Override
	public ScheduleTimePlan findScheduleTimePlanBySaleId(PacketSaleFactory psf) {
		ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findScheduleTimePlanPersonalBySaleId(((PacketSaleClass)psf).getSaleId());
		
		if(scheduleTimePlan!=null) {
			
			List<ScheduleUsersClassPlan> supp=scheduleUsersClassPlanRepository.findBySchtId(scheduleTimePlan.getSchtId());
			scheduleTimePlan.setScheduleFactories(new ArrayList<>());
			scheduleTimePlan.getScheduleFactories().addAll(supp);
			
		}else {
			scheduleTimePlan=new ScheduleTimePlan();
			scheduleTimePlan.setScheduleFactories(new ArrayList<>());
			ScheduleUsersClassPlan scf=new ScheduleUsersClassPlan();
			scf.setSaleCount(((PacketSaleClass)psf).getProgCount());
			scf.setSaleId(((PacketSaleClass)psf).getSaleId());
			scf.setUser(((PacketSaleClass)psf).getUser());
			
			scheduleTimePlan.getScheduleFactories().add(scf);
			
		}
		
		return scheduleTimePlan;
	}
	
	public ScheduleTimePlan findScheduleTimePlanPlanByDateTimeForStaff(long schStaffId, Date startDate){
		ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findScheduleTimePlanClassPlanByDateTimeForStaff(schStaffId, startDate);
		if(scheduleTimePlan!=null) {
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(scheduleTimePlan.getSchId());
			scheduleTimePlan.setProgramFactory( programService.findProgramClassById(schedulePlan.getProgId()));
		}
		
		return scheduleTimePlan;
	}
	
	
	public List<ScheduleTimePlan> findScheduleTimePlansPlanByDatesForStaff(long schStaffId, Date startDate, Date endDate,int firmId){
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleTimePlanRepository.findScheduleTimePlansClassPlanByDatesForStaff(schStaffId, startDate, endDate);
		scheduleTimePlans.forEach(sctp->{
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(sctp.getSchId());
			sctp.setProgramFactory( programService.findProgramClassById(schedulePlan.getProgId()));
		});
		return scheduleTimePlans;
	}
	
	
	public List<ScheduleFactory> findScheduleUsersPlanBySchtId(long schtId){
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		
		scheduleFactories.addAll(scheduleUsersClassPlanRepository.findBySchtId(schtId));
		
		return scheduleFactories;
	}
	
	public List<ScheduleFactory> findScheduleUsersPlanBySaleId(long saleId){
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		
		scheduleFactories.addAll(scheduleUsersClassPlanRepository.findBySaleId(saleId));
		
		return scheduleFactories;
	}
}
