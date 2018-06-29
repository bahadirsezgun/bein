package com.beinplanner.contollers.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.beinplanner.contollers.mobile.entity.PsfMobile;
import com.beinplanner.contollers.mobile.service.UserLoginControlService;

import tr.com.beinplanner.diabet.dao.UserDiabet;
import tr.com.beinplanner.diabet.dao.UserDiabetCalori;
import tr.com.beinplanner.diabet.service.DiabetService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.measurement.dao.UserMeasurement;
import tr.com.beinplanner.measurement.service.MeasurementService;
import tr.com.beinplanner.packetsale.business.IPacketSale;
import tr.com.beinplanner.packetsale.business.PacketSaleClassBusiness;
import tr.com.beinplanner.packetsale.business.PacketSaleMembershipBusiness;
import tr.com.beinplanner.packetsale.business.PacketSalePersonalBusiness;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.sport.dao.UserSportProgram;
import tr.com.beinplanner.sport.service.SportProgramService;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.ProgramTypes;
import tr.com.beinplanner.util.SaleTypeUtil;

@RestController
@RequestMapping("/mobile/member")
public class MobileMemberController {

	@Autowired
	UserLoginControlService userLoginControlService;
	
	@Autowired
	PacketSaleService packetSaleService;
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	MeasurementService measurementService;
	
	
	@Autowired
	DiabetService diabetService;
	
	@Autowired
	SportProgramService sportProgramService;
	
	
	@Autowired
	PacketSaleClassBusiness packetSaleClassBusiness;

	@Autowired
	PacketSalePersonalBusiness packetSalePersonalBusiness;
	
	@Autowired
	PacketSaleMembershipBusiness packetSaleMembershipBusiness;
	
	
	
	@CrossOrigin(origins = "*")
	@PostMapping(value="/findUserBoughtPackets") 
	public @ResponseBody List<PsfMobile> findUserBoughtPackets(@RequestBody User user){
		User u=null;
		try {
			u = userLoginControlService.getUser(user);
		} catch (Exception e) {
			return null;
		}
		
		List<PacketSaleFactory> packetSaleFactories=packetSaleService.findUserBoughtPackets(u.getUserId());
		List<PsfMobile> psfMobiles=new ArrayList<>();
		
		
		packetSaleFactories.forEach(psf->{
			PsfMobile psfMobile=new PsfMobile();
			psfMobile.setDateFormat(loginSession.getPtGlobal().getPtScrDateFormat());
			psfMobile.setPtCurrency(loginSession.getPtGlobal().getPtCurrency());
			
			if(psf instanceof PacketSalePersonal) {
				
				psfMobile.setSaleId(((PacketSalePersonal)psf).getSaleId());
				psfMobile.setProgName(((PacketSalePersonal)psf).getProgramFactory().getProgName());
				
				Date startDate=new Date();
				Date endDate=new Date();
				
				if(psf.getScheduleFactory()!=null || psf.getScheduleFactory().size()>0 ) {
					startDate=psf.getScheduleFactory().get(0).getPlanStartDate();
					endDate=psf.getScheduleFactory().get(psf.getScheduleFactory().size()-1).getPlanStartDate();
				}
				psfMobile.setStartDate(DateTimeUtil.getDateStrByFormat(startDate, psfMobile.getDateFormat()));
				psfMobile.setEndDate(DateTimeUtil.getDateStrByFormat(endDate, psfMobile.getDateFormat()));
				psfMobile.setProgType(ProgramTypes.PACKET_SALE_PERSONAL);
				double payment=0;
				if(((PacketSalePersonal) psf).getPacketPaymentFactory()!=null)
				 payment=((PacketSalePersonal) psf).getPacketPaymentFactory().getPayAmount();
				
				double packetPrice=((PacketSalePersonal) psf).getPacketPrice();
				psfMobile.setDept(packetPrice-payment);
				
				int progCount=((PacketSalePersonal) psf).getProgCount();
				int doneClassCount=((PacketSalePersonal) psf).getDoneClassCount();
				int leftClassCount=progCount-doneClassCount;
				
				psfMobile.setLeftClassCount(leftClassCount);
				psfMobile.setSalesDateStr(DateTimeUtil.getDateStrByFormat(psf.getSalesDate(), psfMobile.getDateFormat()));
				psfMobile.setSalesDayName(DateTimeUtil.getDayNames(psf.getSalesDate()));
				psfMobile.setSalesDay(""+DateTimeUtil.getDayOfDate(psf.getSalesDate()));
				
			}else if(psf instanceof PacketSaleClass) {
				psfMobile.setSaleId(((PacketSaleClass)psf).getSaleId());
				psfMobile.setProgName(((PacketSaleClass)psf).getProgramFactory().getProgName());
				
				Date startDate=new Date();
				Date endDate=new Date();
				
				if(psf.getScheduleFactory()!=null || psf.getScheduleFactory().size()>0 ) {
					startDate=psf.getScheduleFactory().get(0).getPlanStartDate();
					endDate=psf.getScheduleFactory().get(psf.getScheduleFactory().size()-1).getPlanStartDate();
				}
				psfMobile.setStartDate(DateTimeUtil.getDateStrByFormat(startDate, psfMobile.getDateFormat()));
				psfMobile.setEndDate(DateTimeUtil.getDateStrByFormat(endDate, psfMobile.getDateFormat()));
				psfMobile.setProgType(ProgramTypes.PACKET_SALE_CLASS);
				double payment=0;
				if(((PacketSaleClass) psf).getPacketPaymentFactory()!=null)
				 payment=((PacketSaleClass) psf).getPacketPaymentFactory().getPayAmount();
				
				double packetPrice=((PacketSaleClass) psf).getPacketPrice();
				psfMobile.setDept(packetPrice-payment);
				
				int progCount=((PacketSaleClass) psf).getProgCount();
				int doneClassCount=((PacketSaleClass) psf).getDoneClassCount();
				int leftClassCount=progCount-doneClassCount;
				
				psfMobile.setLeftClassCount(leftClassCount);
				psfMobile.setSalesDateStr(DateTimeUtil.getDateStrByFormat(psf.getSalesDate(), psfMobile.getDateFormat()));
				psfMobile.setSalesDayName(DateTimeUtil.getDayNames(psf.getSalesDate()));
				psfMobile.setSalesDay(""+DateTimeUtil.getDayOfDate(psf.getSalesDate()));
			}else if(psf instanceof PacketSaleMembership) {
				psfMobile.setSaleId(((PacketSaleMembership)psf).getSaleId());
				psfMobile.setProgName(((PacketSaleMembership)psf).getProgramFactory().getProgName());
				
				Date startDate=new Date();
				Date endDate=new Date();
				
				if(psf.getScheduleFactory()!=null || psf.getScheduleFactory().size()>0 ) {
					startDate=((ScheduleMembershipPlan)(psf.getScheduleFactory().get(0))).getSmpStartDate();
					endDate=((ScheduleMembershipPlan)(psf.getScheduleFactory().get(0))).getSmpEndDate();
				}
				psfMobile.setStartDate(DateTimeUtil.getDateStrByFormat(startDate, psfMobile.getDateFormat()));
				psfMobile.setEndDate(DateTimeUtil.getDateStrByFormat(endDate, psfMobile.getDateFormat()));
				psfMobile.setProgType(ProgramTypes.PACKET_SALE_MEMBERSHIP);
				double payment=0;
				if(((PacketSaleMembership) psf).getPacketPaymentFactory()!=null)
				 payment=((PacketSaleMembership) psf).getPacketPaymentFactory().getPayAmount();
				
				double packetPrice=((PacketSaleMembership) psf).getPacketPrice();
				psfMobile.setDept(packetPrice-payment);
				
				psfMobile.setLeftDayCount(DateTimeUtil.getDifferenceBettwenTwoTimes(new Date(), endDate));
				psfMobile.setSalesDateStr(DateTimeUtil.getDateStrByFormat(psf.getSalesDate(), psfMobile.getDateFormat()));
				psfMobile.setSalesDayName(DateTimeUtil.getDayNames(psf.getSalesDate()));
				psfMobile.setSalesDay(""+DateTimeUtil.getDayOfDate(psf.getSalesDate()));
			}
			psfMobiles.add(psfMobile);
		});
		return psfMobiles;
	}
	
	
	@CrossOrigin(origins = "*")
	@PostMapping(value="/findUserMeasurements") 
	public @ResponseBody List<UserMeasurement> findUserMeasurements(@RequestBody User user){
		User u=null;
		try {
			u = userLoginControlService.getUser(user);
		} catch (Exception e) {
			return null;
		}
		
		List<UserMeasurement> userMeasurements=measurementService.findByUserId(u.getUserId());
		userMeasurements.forEach(um->{
			um.setMeasDateStr(DateTimeUtil.getDateStrByFormat(um.getMeasDate(), loginSession.getPtGlobal().getPtScrDateFormat()));
		});
		
		return userMeasurements;
		
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping(value="/findPacketDetails/{saleId}/{saleType}") 
	public @ResponseBody PacketSaleFactory findPacketDetails(@RequestBody User user,@PathVariable long saleId,@PathVariable String saleType){
		User u=null;
		try {
			u = userLoginControlService.getUser(user);
		} catch (Exception e) {
			return null;
		}
		
		IPacketSale iPacketSale=null;
		if(saleType.equals(SaleTypeUtil.SALE_TYPE_CLASS)) {
			iPacketSale=packetSaleClassBusiness;
		}else if(saleType.equals(SaleTypeUtil.SALE_TYPE_PERSONAL)) {
			iPacketSale=packetSalePersonalBusiness;
		}else if(saleType.equals(SaleTypeUtil.SALE_TYPE_MEMBERSHIP)) {
			iPacketSale=packetSaleMembershipBusiness;
		}
		
		
		PacketSaleFactory psf=packetSaleService.findUserBoughtPacketsBySaleId(saleId, iPacketSale);
		
		
			if(psf instanceof PacketSaleMembership) {
				UserDiabetCalori udc= diabetService.findByUserIdAndSaleIdAndSaleType(((PacketSaleMembership) psf).getUserId(), ((PacketSaleMembership) psf).getSaleId(), ((PacketSaleMembership) psf).getProgType());
				if(udc!=null) {
					psf.setUserDiabetCalori(udc);
					List<UserDiabet> userDiabets=diabetService.findByUdcId(udc.getUdcId());
					  psf.setUserDiabets(userDiabets);
				 }
				
				List<UserSportProgram> usp= sportProgramService.findBySaleIdAndSaleTypeAndUserId(((PacketSaleMembership) psf).getSaleId(), ((PacketSaleMembership) psf).getProgType(), ((PacketSaleMembership) psf).getUserId());
				psf.setUserSportPrograms(usp);
				
				
			}else if(psf instanceof PacketSalePersonal) {
				UserDiabetCalori udc= diabetService.findByUserIdAndSaleIdAndSaleType(((PacketSalePersonal) psf).getUserId(), ((PacketSalePersonal) psf).getSaleId(), ((PacketSalePersonal) psf).getProgType());
				if(udc!=null) {
					psf.setUserDiabetCalori(udc);
					List<UserDiabet> userDiabets=diabetService.findByUdcId(udc.getUdcId());
					  psf.setUserDiabets(userDiabets);
					}
				
				List<UserSportProgram> usp= sportProgramService.findBySaleIdAndSaleTypeAndUserId(((PacketSalePersonal) psf).getSaleId(), ((PacketSalePersonal) psf).getProgType(), ((PacketSalePersonal) psf).getUserId());
				psf.setUserSportPrograms(usp);
				
			}else if(psf instanceof PacketSaleClass) {
				UserDiabetCalori udc= diabetService.findByUserIdAndSaleIdAndSaleType(((PacketSaleClass) psf).getUserId(), ((PacketSaleClass) psf).getSaleId(), ((PacketSaleClass) psf).getProgType());
				if(udc!=null) {
					psf.setUserDiabetCalori(udc);
					List<UserDiabet> userDiabets=diabetService.findByUdcId(udc.getUdcId());
					  psf.setUserDiabets(userDiabets);
					}
				List<UserSportProgram> usp= sportProgramService.findBySaleIdAndSaleTypeAndUserId(((PacketSaleClass) psf).getSaleId(), ((PacketSaleClass) psf).getProgType(), ((PacketSaleClass) psf).getUserId());
				psf.setUserSportPrograms(usp);
			}
		
			return psf;
	}
	
	
}
