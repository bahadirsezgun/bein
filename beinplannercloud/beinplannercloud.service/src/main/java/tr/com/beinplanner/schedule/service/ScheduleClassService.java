package tr.com.beinplanner.schedule.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetsale.business.PacketSaleClassBusiness;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.program.dao.ProgramClass;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.comparator.ScheduleTimePlanComparator;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersClassPlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;
import tr.com.beinplanner.schedule.facade.SchedulePersonalClassFacadeService;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersClassPlanRepository;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.BonusPayedUtil;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.util.SaleStatus;
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
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	PacketSaleService packetSaleService;
	

	@Autowired
	PacketSaleClassBusiness packetSaleClassBusiness;
	
	@Autowired
	UserService userService;
	
	
	
	
	
	
	@Override
	public synchronized HmiResultObj createPlan(ScheduleTimePlan scheduleTimePlan,SchedulePlan schedulePlan) {
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleTimePlanCreateInChain(scheduleTimePlan,schedulePlan);
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			
			scheduleTimePlan.setSchId(schedulePlan.getSchId());
			scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_NORMAL);
			
			
			
			scheduleTimePlan=scheduleTimePlanRepository.save(scheduleTimePlan);
			
			hmiResultObj.setResultObj(scheduleTimePlan);
			
			List<ScheduleUsersClassPlan> scheduleFactories=new ArrayList<>();
			
			scheduleTimePlan.getScheduleFactories().forEach(scf->{
				scheduleFactories.add((ScheduleUsersClassPlan)scf);
			});
			
			for (ScheduleUsersClassPlan scf : scheduleFactories) {
				scf.setSchtId(scheduleTimePlan.getSchtId());
				if(scf.getSaleId()==0) {
				
					PacketSaleClass psf=(PacketSaleClass)packetSaleClassBusiness.findPacketSaleBySchIdAndUserId(schedulePlan.getSchId(), scf.getUserId());
					if(psf==null) {
					    psf=new PacketSaleClass();
						psf.setUserId(scf.getUserId());
						psf.setProgId(((ProgramClass)scheduleTimePlan.getProgramFactory()).getProgId());
						psf.setSalesComment("automaticSale");
						psf.setPacketPrice(((ProgramClass)scheduleTimePlan.getProgramFactory()).getProgPrice()*((ProgramClass)scheduleTimePlan.getProgramFactory()).getProgCount());
						psf.setSalesDate(new Date());
						psf.setChangeDate(new Date());
						psf.setStaffId(loginSession.getUser().getUserId());
						psf.setProgCount(((ProgramClass)scheduleTimePlan.getProgramFactory()).getProgCount());
						psf.setBonusPayedFlag(BonusPayedUtil.BONUS_PAYED_NO);
						psf.setSaleStatu(SaleStatus.SALE_CONTINUE_PLANNED);
						
						User user=userService.findUserById(scf.getUserId());
						psf.setUser(user);
						
						psf.setProgramFactory(programService.findProgramClassById(psf.getProgId()));
						
						
						psf =(PacketSaleClass)packetSaleService.sale(psf).getResultObj();
						scf.setSaleId(psf.getSaleId());
				 	}else {
				 		scf.setSaleId(psf.getSaleId());
				 	}
				}
				scheduleUsersClassPlanRepository.save(scf);
			}
		}
		return hmiResultObj;
	}

	
	@Override
	public synchronized HmiResultObj addUserInScheduleTimePlan(ScheduleFactory scheduleFactory) {
		ScheduleUsersClassPlan supp=(ScheduleUsersClassPlan)scheduleFactory;
		
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleChange(supp.getSchtId());
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
		if(supp.getSaleId()==0) {
			
			ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findOne(supp.getSchtId());
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(scheduleTimePlan.getSchId());
			ProgramClass programClass=programService.findProgramClassById(schedulePlan.getProgId());
			
			
			PacketSaleClass psf=new PacketSaleClass();
			psf.setUserId(supp.getUserId());
			psf.setProgId(programClass.getProgId());
			psf.setSalesComment("automaticSale");
			psf.setPacketPrice(programClass.getProgPrice());
			psf.setSalesDate(new Date());
			psf.setChangeDate(new Date());
			psf.setStaffId(loginSession.getUser().getUserId());
			psf.setProgCount(programClass.getProgCount());
			psf.setBonusPayedFlag(BonusPayedUtil.BONUS_PAYED_NO);
			psf.setSaleStatu(SaleStatus.SALE_CONTINUE_PLANNED);
			
			User user=userService.findUserById(psf.getUserId());
			psf.setUser(user);
			
			psf.setProgramFactory(programService.findProgramClassById(psf.getProgId()));
			
			
			psf =(PacketSaleClass)packetSaleService.sale(psf).getResultObj();
			supp.setSaleId(psf.getSaleId());
		}
		
		
		 supp= scheduleUsersClassPlanRepository.save(supp);
		
		 hmiResultObj.setResultObj(supp);
		 
		}
		return hmiResultObj;
	}
	
	

	@Override
	public synchronized HmiResultObj updateScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleTimePlanUpdateInChain(scheduleTimePlan);
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			hmiResultObj=schedulePersonalClassFacadeService.canScheduleChange(scheduleTimePlan.getSchtId());
			if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
				scheduleTimePlanRepository.save(scheduleTimePlan);
			}
		}
		return hmiResultObj;
	}






	@Override
	public synchronized HmiResultObj cancelScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
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
	public synchronized HmiResultObj postponeScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
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
	public synchronized HmiResultObj deleteScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
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


    public synchronized HmiResultObj deleteScheduleUsersClassTimePlan(ScheduleUsersClassPlan scheduleUsersClassPlan,ScheduleTimePlan scheduleTimePlan) {
		
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleTimePlanDelete(scheduleTimePlan);
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			scheduleUsersClassPlanRepository.delete(scheduleUsersClassPlan);
		}
		return hmiResultObj;
	}



	@Override
	public synchronized ScheduleTimePlan findScheduleTimePlanBySaleId(PacketSaleFactory psf) {
		ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findScheduleTimePlanClassBySaleId(((PacketSaleClass)psf).getSaleId());
		
		if(scheduleTimePlan!=null) {
			
			List<ScheduleUsersClassPlan> supp=scheduleUsersClassPlanRepository.findBySchtId(scheduleTimePlan.getSchtId());
			supp.forEach(scf->{
				
				User user=userService.findUserById(scf.getUserId());
				scf.setUser(user);
				scf.getUser().setSucpId(scf.getSucpId());
				
				scf.getUser().setSaleId(scf.getSaleId());
				
			});
			
			scheduleTimePlan.setSchtId(0);
			scheduleTimePlan.setScheduleFactories(new ArrayList<>());
			scheduleTimePlan.getScheduleFactories().addAll(supp);
			
		}else {
			scheduleTimePlan=new ScheduleTimePlan();
			scheduleTimePlan.setScheduleFactories(new ArrayList<>());
			ScheduleUsersClassPlan scf=new ScheduleUsersClassPlan();
			scf.setSaleCount(((PacketSaleClass)psf).getProgCount());
			scf.setSaleId(((PacketSaleClass)psf).getSaleId());
			scf.setUser(((PacketSaleClass)psf).getUser());
			scf.getUser().setSaleId(scf.getSaleId());
			scheduleTimePlan.getScheduleFactories().add(scf);
			
		}
		
		return scheduleTimePlan;
	}
	
	public synchronized ScheduleTimePlan findScheduleTimePlanPlanByDateTimeForStaff(long schStaffId, Date startDate){
		ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findScheduleTimePlanClassPlanByDateTimeForStaff(schStaffId, startDate);
		if(scheduleTimePlan!=null) {
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(scheduleTimePlan.getSchId());
			List<ScheduleTimePlan> scheduleTimePlans=scheduleTimePlanRepository.findBySchId(schedulePlan.getSchId());
			Collections.sort(scheduleTimePlans,new ScheduleTimePlanComparator());
			int i=1;
			for (ScheduleTimePlan stp : scheduleTimePlans) {
				if(stp.getSchtId()==scheduleTimePlan.getSchtId()) {
					stp.setPlanCount(i);
					break;
				}
				i++;
			}
			
			
			scheduleTimePlan.setProgramFactory( programService.findProgramClassById(schedulePlan.getProgId()));
			scheduleTimePlan.setSchCount(schedulePlan.getSchCount());
			
			
		}
		
		return scheduleTimePlan;
	}
	
	
	public synchronized List<ScheduleTimePlan> findScheduleTimePlansPlanByDatesForStaff(long schStaffId, Date startDate, Date endDate,int firmId){
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleTimePlanRepository.findScheduleTimePlansClassPlanByDatesForStaff(schStaffId, startDate, endDate);
		scheduleTimePlans.forEach(sctp->{
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(sctp.getSchId());
			sctp.setProgramFactory( programService.findProgramClassById(schedulePlan.getProgId()));
		});
		return scheduleTimePlans;
	}
	
	
	public synchronized List<ScheduleFactory> findScheduleUsersPlanBySchtId(long schtId){
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		
		scheduleFactories.addAll(scheduleUsersClassPlanRepository.findBySchtId(schtId));
		
		scheduleFactories.forEach(sf->{
			User user=userService.findUserById(((ScheduleUsersClassPlan)sf).getUserId());
			sf.setUser(user);
			sf.getUser().setSucpId(((ScheduleUsersClassPlan)sf).getSucpId());
		});
		
		
		return scheduleFactories;
	}
	
	
	
	
	@Override
	public synchronized List<ScheduleFactory> findScheduleUsersPlanBySchId(long schId) {
		 List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		 if(schId>0) {
		  
			List<ScheduleUsersClassPlan> scheduleUsersClassPlans=  scheduleUsersClassPlanRepository.findScheduleUsersPlanBySchId(schId);
					scheduleUsersClassPlans.forEach(sucp->{
						User user=userService.findUserById(sucp.getUserId());
						user.setSucpId(sucp.getSucpId());
						sucp.setUser(user);
			});
			 
			 scheduleFactories.addAll(scheduleUsersClassPlans);
		   
		 }
		 return scheduleFactories;
	}


	public synchronized List<ScheduleFactory> findScheduleUsersPlanBySaleId(long saleId){
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		
		scheduleFactories.addAll(scheduleUsersClassPlanRepository.findBySaleId(saleId));
		scheduleFactories.forEach(sf->{
			
			User user=userService.findUserById(((ScheduleUsersClassPlan)sf).getUserId());
			user.setSucpId(((ScheduleUsersClassPlan)sf).getSucpId());
			sf.setUser(user);
			
			ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findOne(((ScheduleUsersClassPlan)sf).getSchtId());
			sf.setPlanStartDate(scheduleTimePlan.getPlanStartDate());
			sf.setPlanEndDate(scheduleTimePlan.getPlanEndDate());
			sf.setStatuTp(scheduleTimePlan.getStatuTp());				
		});
		return scheduleFactories;
	}
	
	
	
	
	
}
