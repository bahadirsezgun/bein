package tr.com.beinplanner.schedule.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetsale.business.PacketSalePersonalBusiness;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.program.dao.ProgramPersonal;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.business.ISchedulePersonalClass;
import tr.com.beinplanner.schedule.comparator.ScheduleTimePlanComparator;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersClassPlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;
import tr.com.beinplanner.schedule.facade.SchedulePersonalClassFacadeService;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipTimePlanRepository;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersPersonalPlanRepository;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.BonusPayedUtil;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.util.SaleStatus;
import tr.com.beinplanner.util.StatuTypes;

@Service
@Qualifier("schedulePersonalService")
public class SchedulePersonalService implements IScheduleService {

	@Autowired
	SchedulePlanRepository schedulePlanRepository;
	
	@Autowired
	ScheduleTimePlanRepository scheduleTimePlanRepository;
	
	@Autowired
	ScheduleMembershipTimePlanRepository scheduleMembershipTimePlanRepository;
	
	@Autowired
	ScheduleUsersPersonalPlanRepository scheduleUsersPersonalPlanRepository;
	
	@Autowired
	@Qualifier("schedulePersonal")
	ISchedulePersonalClass iSchedulePersonalClass;
	
	@Autowired
	@Qualifier("schedulePersonalFacade")
	SchedulePersonalClassFacadeService schedulePersonalClassFacadeService;
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PacketSaleService packetSaleService;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	PacketSalePersonalBusiness packetSalePersonalBusiness;
	
	@Override
	public synchronized  HmiResultObj createPlan(ScheduleTimePlan scheduleTimePlan,SchedulePlan schedulePlan) {
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleTimePlanCreateInChain(scheduleTimePlan,schedulePlan);
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			
			scheduleTimePlan.setSchId(schedulePlan.getSchId());
			scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_NORMAL);
			scheduleTimePlan=scheduleTimePlanRepository.save(scheduleTimePlan);
			hmiResultObj.setResultObj(scheduleTimePlan);
			
			List<ScheduleUsersPersonalPlan> scheduleFactories=new ArrayList<>();

			scheduleTimePlan.getScheduleFactories().forEach(scf->{
				scheduleFactories.add((ScheduleUsersPersonalPlan)scf);
			});
			
			for (ScheduleUsersPersonalPlan scf : scheduleFactories) {
				scf.setSchtId(scheduleTimePlan.getSchtId());
				
				
				if(scf.getSaleId()==0) {
				
					PacketSalePersonal psf=(PacketSalePersonal)packetSalePersonalBusiness.findPacketSaleBySchIdAndUserId(schedulePlan.getSchId(), scf.getUserId());
					if(psf==null) {
					 psf=new PacketSalePersonal();
						psf.setUserId(scf.getUserId());
						psf.setProgId(((ProgramPersonal)scheduleTimePlan.getProgramFactory()).getProgId());
						psf.setSalesComment("automaticSale");
						psf.setPacketPrice(((ProgramPersonal)scheduleTimePlan.getProgramFactory()).getProgPrice()*((ProgramPersonal)scheduleTimePlan.getProgramFactory()).getProgCount());
						psf.setSalesDate(new Date());
						psf.setChangeDate(new Date());
						psf.setStaffId(loginSession.getUser().getUserId());
						psf.setProgCount(((ProgramPersonal)scheduleTimePlan.getProgramFactory()).getProgCount());
						psf.setBonusPayedFlag(BonusPayedUtil.BONUS_PAYED_NO);
						psf.setSaleStatu(SaleStatus.SALE_CONTINUE_PLANNED);
						
						psf =(PacketSalePersonal)packetSaleService.sale(psf).getResultObj();
						scf.setSaleId(psf.getSaleId());
					}else {
						scf.setSaleId(psf.getSaleId());
					}
				}
				scheduleUsersPersonalPlanRepository.save(scf);
			}
		}
		return hmiResultObj;
	}

	@Override
	public synchronized HmiResultObj addUserInScheduleTimePlan(ScheduleFactory scheduleFactory) {
		ScheduleUsersPersonalPlan supp=(ScheduleUsersPersonalPlan)scheduleFactory;
		
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleChange(supp.getSchtId());
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
		if(supp.getSaleId()==0) {
			
			ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findOne(supp.getSchtId());
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(scheduleTimePlan.getSchId());
			ProgramPersonal programPersonal=programService.findProgramPersonalById(schedulePlan.getProgId());
			
			
			PacketSalePersonal psf=new PacketSalePersonal();
			psf.setUserId(supp.getUserId());
			psf.setProgId(programPersonal.getProgId());
			psf.setSalesComment("automaticSale");
			psf.setPacketPrice(programPersonal.getProgPrice()*programPersonal.getProgCount());
			psf.setSalesDate(new Date());
			psf.setChangeDate(new Date());
			psf.setStaffId(loginSession.getUser().getUserId());
			psf.setProgCount(programPersonal.getProgCount());
			psf.setBonusPayedFlag(BonusPayedUtil.BONUS_PAYED_NO);
			psf.setSaleStatu(SaleStatus.SALE_CONTINUE_PLANNED);
			
			psf =(PacketSalePersonal)packetSaleService.sale(psf).getResultObj();
			supp.setSaleId(psf.getSaleId());
		}
		
		
		 supp= scheduleUsersPersonalPlanRepository.save(supp);
		
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


	public synchronized HmiResultObj deleteScheduleUsersPersonalTimePlan(ScheduleUsersPersonalPlan scheduleUsersPersonalPlan,ScheduleTimePlan scheduleTimePlan) {
		
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleTimePlanDelete(scheduleTimePlan);
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			scheduleUsersPersonalPlanRepository.delete(scheduleUsersPersonalPlan);
		}
		return hmiResultObj;
	}
	





	public synchronized ScheduleTimePlan findScheduleTimePlanPlanByDateTimeForStaff(long schStaffId, Date startDate){
		ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findScheduleTimePlanPersonalPlanByDateTimeForStaff(schStaffId, startDate);
		if(scheduleTimePlan!=null) {
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(scheduleTimePlan.getSchId());
			scheduleTimePlan.setProgramFactory( programService.findProgramPersonalById(schedulePlan.getProgId()));
			
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
			scheduleTimePlan.setSchCount(schedulePlan.getSchCount());
			
		}
		
		return scheduleTimePlan;
	}
	
	
	
	public synchronized List<ScheduleTimePlan> findScheduleTimePlansPlanByDatesForStaff(long schStaffId, Date startDate, Date endDate,int firmId){
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleTimePlanRepository.findScheduleTimePlansPersonalPlanByDatesForStaff(schStaffId, startDate, endDate);
		scheduleTimePlans.forEach(sctp->{
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(sctp.getSchId());
			sctp.setSchCount(schedulePlan.getSchCount());
			sctp.setProgramFactory( programService.findProgramPersonalById(schedulePlan.getProgId()));
		});
		return scheduleTimePlans;
	}
	
	public synchronized List<ScheduleFactory> findScheduleUsersPlanBySchtId(long schtId){
		
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		scheduleFactories.addAll(scheduleUsersPersonalPlanRepository.findBySchtId(schtId));
		scheduleFactories.forEach(sf->{
			
			User user=userService.findUserById(((ScheduleUsersPersonalPlan)sf).getUserId());
			sf.setUser(user);
			sf.getUser().setSuppId(((ScheduleUsersPersonalPlan)sf).getSuppId());
		});
		return scheduleFactories;
	}
	
	
	@Override
	public synchronized List<ScheduleFactory> findScheduleUsersPlanBySchId(long schId) {
		 List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		if(schId>0) {
		 
			
			List<ScheduleUsersPersonalPlan> scheduleUsersPersonalPlans=  scheduleUsersPersonalPlanRepository.findScheduleUsersPlanBySchId(schId);
			scheduleUsersPersonalPlans.forEach(supp->{
				User user=userService.findUserById(supp.getUserId());
				user.setSuppId(supp.getSuppId());
				supp.setUser(user);
		    });
			
			scheduleFactories.addAll(scheduleUsersPersonalPlanRepository.findScheduleUsersPlanBySchId(schId));
		
		 
		}
		 return scheduleFactories;
	}
		
	
	
	@Override
	public synchronized ScheduleTimePlan findScheduleTimePlanBySaleId(PacketSaleFactory psf) {
		
		ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findScheduleTimePlanPersonalBySaleId(((PacketSalePersonal)psf).getSaleId());
		
		if(scheduleTimePlan!=null) {
			
			List<ScheduleUsersPersonalPlan> supp=scheduleUsersPersonalPlanRepository.findBySchtId(scheduleTimePlan.getSchtId());
			supp.forEach(scf->{
				User user=userService.findUserById(((ScheduleUsersPersonalPlan)scf).getUserId());
				scf.setUser(user);
				scf.getUser().setSaleId(scf.getSaleId());
				scf.getUser().setSuppId(scf.getSuppId());
			});
			
			
			scheduleTimePlan.setSchtId(0);
			scheduleTimePlan.setScheduleFactories(new ArrayList<>());
			scheduleTimePlan.getScheduleFactories().addAll(supp);
			
		}else {
			scheduleTimePlan=new ScheduleTimePlan();
			
			scheduleTimePlan.setScheduleFactories(new ArrayList<>());
			ScheduleUsersPersonalPlan scf=new ScheduleUsersPersonalPlan();
			scf.setSaleCount(((PacketSalePersonal)psf).getProgCount());
			scf.setSaleId(((PacketSalePersonal)psf).getSaleId());
			
			User user=userService.findUserById(((PacketSalePersonal)psf).getUserId());
			scf.setUser(user);
			scf.getUser().setSaleId(scf.getSaleId());
			
			
			scheduleTimePlan.getScheduleFactories().add(scf);
		}
		
		return scheduleTimePlan;
	}



	public synchronized List<ScheduleFactory> findScheduleUsersPlanBySaleId(long saleId){
		
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		
		scheduleFactories.addAll(scheduleUsersPersonalPlanRepository.findBySaleId(saleId));
		
		scheduleFactories.forEach(sf->{
			ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findOne(((ScheduleUsersPersonalPlan)sf).getSchtId());
			sf.setPlanStartDate(scheduleTimePlan.getPlanStartDate());
			sf.setPlanEndDate(scheduleTimePlan.getPlanEndDate());
						
		});
		
		
		
		return scheduleFactories;
	}
}
