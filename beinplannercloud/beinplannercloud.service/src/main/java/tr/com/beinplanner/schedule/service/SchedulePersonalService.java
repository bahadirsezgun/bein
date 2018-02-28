package tr.com.beinplanner.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.status.StatusUtil;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.program.dao.ProgramPersonal;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.business.ISchedulePersonalClass;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;
import tr.com.beinplanner.schedule.facade.SchedulePersonalClassFacadeService;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipTimePlanRepository;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersPersonalPlanRepository;
import tr.com.beinplanner.util.BonusPayedUtil;
import tr.com.beinplanner.util.ProgramTypes;
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
	PacketSaleService packetSaleService;
	
	@Autowired
	ProgramService programService;
	
	
	
	@Override
	public synchronized  HmiResultObj createPlan(ScheduleTimePlan scheduleTimePlan,SchedulePlan schedulePlan) {
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleTimePlanCreateInChain(scheduleTimePlan);
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			
			scheduleTimePlan.setSchId(schedulePlan.getSchId());
			scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_NORMAL);
			
			
			
			scheduleTimePlan=scheduleTimePlanRepository.save(scheduleTimePlan);
			
			List<ScheduleUsersPersonalPlan> scheduleFactories=new ArrayList<>();
			
			scheduleTimePlan.getScheduleFactories().forEach(scf->{
				scheduleFactories.add((ScheduleUsersPersonalPlan)scf);
			});
			
			for (ScheduleUsersPersonalPlan scf : scheduleFactories) {
				scf.setSchtId(scheduleTimePlan.getSchtId());
				if(scf.getSaleId()==0) {
				
					PacketSalePersonal psf=new PacketSalePersonal();
					psf.setUserId(scf.getUserId());
					psf.setProgId(((ProgramPersonal)scheduleTimePlan.getProgramFactory()).getProgId());
					psf.setSalesComment("automaticSale");
					psf.setPacketPrice(((ProgramPersonal)scheduleTimePlan.getProgramFactory()).getProgPrice());
					psf.setSalesDate(new Date());
					psf.setChangeDate(new Date());
					psf.setStaffId(loginSession.getUser().getUserId());
					psf.setProgCount(((ProgramPersonal)scheduleTimePlan.getProgramFactory()).getProgCount());
					psf.setBonusPayedFlag(BonusPayedUtil.BONUS_PAYED_NO);
					psf.setSaleStatu(SaleStatus.SALE_CONTINUE_PLANNED);
					
					psf =(PacketSalePersonal)packetSaleService.sale(psf).getResultObj();
					scf.setSaleId(psf.getSaleId());
				}
				scheduleUsersPersonalPlanRepository.save(scf);
			}
		}
		return hmiResultObj;
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







	public ScheduleTimePlan findScheduleTimePlanPlanByDateTimeForStaff(long schStaffId, Date startDate){
		ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findScheduleTimePlanPersonalPlanByDateTimeForStaff(schStaffId, startDate);
		if(scheduleTimePlan!=null) {
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(scheduleTimePlan.getSchId());
			scheduleTimePlan.setProgramFactory( programService.findProgramPersonalById(schedulePlan.getProgId()));
		}
		
		return scheduleTimePlan;
	}
	
	
	
	public List<ScheduleTimePlan> findScheduleTimePlansPlanByDatesForStaff(long schStaffId, Date startDate, Date endDate,int firmId){
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleTimePlanRepository.findScheduleTimePlansPersonalPlanByDatesForStaff(schStaffId, startDate, endDate);
		scheduleTimePlans.forEach(sctp->{
			SchedulePlan schedulePlan=schedulePlanRepository.findOne(sctp.getSchId());
			sctp.setProgramFactory( programService.findProgramPersonalById(schedulePlan.getProgId()));
		});
		return scheduleTimePlans;
	}
	
	public List<ScheduleFactory> findScheduleUsersPlanBySchtId(long schtId){
		
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		scheduleFactories.addAll(scheduleUsersPersonalPlanRepository.findBySchtId(schtId));
		return scheduleFactories;
	}
	
	
	
		
	
	
	@Override
	public ScheduleTimePlan findScheduleTimePlanBySaleId(PacketSaleFactory psf) {
		
		ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findScheduleTimePlanPersonalBySaleId(((PacketSalePersonal)psf).getSaleId());
		
		if(scheduleTimePlan!=null) {
			
			List<ScheduleUsersPersonalPlan> supp=scheduleUsersPersonalPlanRepository.findBySchtId(scheduleTimePlan.getSchtId());
			supp.forEach(scf->{
				scf.getUser().setSaleId(scf.getSaleId());
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
			scf.setUser(((PacketSalePersonal)psf).getUser());
			scf.getUser().setSaleId(scf.getSaleId());
			
			
			scheduleTimePlan.getScheduleFactories().add(scf);
		}
		
		return scheduleTimePlan;
	}



	public List<ScheduleFactory> findScheduleUsersPlanBySaleId(long saleId){
		
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		
		scheduleFactories.addAll(scheduleUsersPersonalPlanRepository.findBySaleId(saleId));
		
		return scheduleFactories;
	}
}
