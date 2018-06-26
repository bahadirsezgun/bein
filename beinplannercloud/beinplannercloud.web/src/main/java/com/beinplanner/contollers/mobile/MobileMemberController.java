package com.beinplanner.contollers.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.beinplanner.contollers.mobile.entity.PsfMobile;
import com.beinplanner.contollers.mobile.service.UserLoginControlService;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.ProgramTypes;

@RestController
@RequestMapping("/mobile/member")
public class MobileMemberController {

	@Autowired
	UserLoginControlService userLoginControlService;
	
	@Autowired
	PacketSaleService packetSaleService;
	
	@Autowired
	LoginSession loginSession;
	
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
}
