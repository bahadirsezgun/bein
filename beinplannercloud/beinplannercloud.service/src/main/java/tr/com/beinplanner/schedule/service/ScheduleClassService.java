package tr.com.beinplanner.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersClassPlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersClassPlanRepository;

@Service
@Qualifier("scheduleClassService")
public class ScheduleClassService implements IScheduleService {

	
	@Autowired
	SchedulePlanRepository schedulePlanRepository;
	
	@Autowired
	ScheduleTimePlanRepository scheduleTimePlanRepository;
	
	@Autowired
	ScheduleUsersClassPlanRepository scheduleUsersClassPlanRepository;
	
	
	
	
	
	@Override
	public synchronized HmiResultObj createPlan(ScheduleTimePlan scheduleTimePlan) {
		// TODO Auto-generated method stub
		return null;
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
		return scheduleTimePlanRepository.findScheduleTimePlanClassPlanByDateTimeForStaff(schStaffId, startDate);
	}
	
	
	public List<ScheduleTimePlan> findScheduleTimePlansPlanByDatesForStaff(long schStaffId, Date startDate, Date endDate,int firmId){
		return scheduleTimePlanRepository.findScheduleTimePlansClassPlanByDatesForStaff(schStaffId, startDate, endDate);
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
