package tr.com.beinplanner.packetsale.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.status.StatusUtil;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.business.IPacketSale;
import tr.com.beinplanner.packetsale.business.PacketSaleClassBusiness;
import tr.com.beinplanner.packetsale.business.PacketSaleMembershipBusiness;
import tr.com.beinplanner.packetsale.business.PacketSalePersonalBusiness;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleUsersClassPlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.OhbeUtil;
import tr.com.beinplanner.util.StatuTypes;

@Service
@Qualifier("packetSaleService")
public class PacketSaleService {

	@Autowired
	LoginSession loginSession;
	
	@Autowired
	PacketSaleClassBusiness packetSaleClassBusiness;

	@Autowired
	PacketSalePersonalBusiness packetSalePersonalBusiness;
	
	@Autowired
	PacketSaleMembershipBusiness packetSaleMembershipBusiness;
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	
	
	
	public synchronized List<PacketSaleFactory> findUserBoughtPackets(long userId){
		IPacketSale iPacketSale=packetSalePersonalBusiness;
		
		List<PacketSaleFactory> psfs=iPacketSale.findAllSalesForUserInChain(userId);
		
		psfs.forEach(psf->{
			if(psf instanceof PacketSalePersonal) {
				if(psf.getScheduleFactory()!=null) {
					
					psf.getScheduleFactory().stream().forEach(psfsf->{
						if(((ScheduleUsersPersonalPlan)psfsf).getPlanStartDate().before(DateTimeUtil.getZonedTodayDate(loginSession.getPtGlobal().getPtTz()))) {
							if(((ScheduleUsersPersonalPlan)psfsf).getStatuTp()==StatuTypes.TIMEPLAN_NORMAL) {
								((PacketSalePersonal)psf).setDoneClassCount(((PacketSalePersonal)psf).getDoneClassCount()+1);
							}
						}
						
						
					});
				}
			}else if(psf instanceof PacketSaleClass) {
				if(psf.getScheduleFactory()!=null) {
					
					psf.getScheduleFactory().stream().forEach(psfsf->{
						if(((ScheduleUsersClassPlan)psfsf).getPlanStartDate().before(DateTimeUtil.getZonedTodayDate(loginSession.getPtGlobal().getPtTz()))) {
							if(((ScheduleUsersClassPlan)psfsf).getStatuTp()==StatuTypes.TIMEPLAN_NORMAL) {
								((PacketSaleClass)psf).setDoneClassCount(((PacketSaleClass)psf).getDoneClassCount()+1);
							}
						}
						
						
					});
				}
			}
		});
		
		
		return psfs;
	}
	
	
	
	public synchronized PacketSaleFactory findUserBoughtPacketsBySaleId(long saleId,IPacketSale iPacketSale){
		
		PacketSaleFactory psf=iPacketSale.findAllSalesForUserInSaleId(saleId);
		
		
			if(psf instanceof PacketSalePersonal) {
				if(psf.getScheduleFactory()!=null) {
					
					psf.getScheduleFactory().stream().forEach(psfsf->{
						if(((ScheduleUsersPersonalPlan)psfsf).getPlanStartDate().before(DateTimeUtil.getZonedTodayDate(loginSession.getPtGlobal().getPtTz()))) {
							if(((ScheduleUsersPersonalPlan)psfsf).getStatuTp()==StatuTypes.TIMEPLAN_NORMAL) {
								((PacketSalePersonal)psf).setDoneClassCount(((PacketSalePersonal)psf).getDoneClassCount()+1);
							}
						}
						
						
					});
				}
			}else if(psf instanceof PacketSaleClass) {
				if(psf.getScheduleFactory()!=null) {
					
					psf.getScheduleFactory().stream().forEach(psfsf->{
						if(((ScheduleUsersClassPlan)psfsf).getPlanStartDate().before(DateTimeUtil.getZonedTodayDate(loginSession.getPtGlobal().getPtTz()))) {
							if(((ScheduleUsersClassPlan)psfsf).getStatuTp()==StatuTypes.TIMEPLAN_NORMAL) {
								((PacketSaleClass)psf).setDoneClassCount(((PacketSaleClass)psf).getDoneClassCount()+1);
							}
						}
						
						
					});
				}
			}
		
		
		
		return psf;
	}
	
	
	
	public synchronized List<PacketSaleFactory> findUserBoughtPacketsForCalendar(long userId){
		IPacketSale iPacketSale=packetSalePersonalBusiness;
		return iPacketSale.findAllSalesForCalendarUserInChain(userId);
	}
	
	
	public synchronized PacketSaleFactory findPacketSaleById(long saleId,IPacketSale iPacketSale) {
		return iPacketSale.findPacketSaleById(saleId);
	}
	
	public synchronized PacketSaleFactory findPacketSaleBySchIdAndUserId(long schId,long userId,IPacketSale iPacketSale) {
		return iPacketSale.findPacketSaleBySchIdAndUserId(schId,userId);
	}
	
	
	
	public synchronized List<PacketSaleFactory> findPacketSaleWithNoPayment(int firmId,IPacketSale iPacketSale){
		return iPacketSale.findPacketSaleWithNoPayment(firmId);
	}
	
	public synchronized List<PacketSaleFactory> findLeftPaymentsInChain(int firmId){
		IPacketSale iPacketSale=packetSalePersonalBusiness;
		return iPacketSale.findLeftPaymentsInChain(firmId);
	}
	
	public List<PacketSaleFactory> findLast5PacketSales(int firmId){
		IPacketSale iPacketSale=packetSalePersonalBusiness;
		return iPacketSale.findLast5PacketSalesInChain(firmId);
	}
	
	public synchronized HmiResultObj changePrice(PacketSaleFactory psf){
		IPacketSale iPacketSale=null;
		if(psf instanceof PacketSalePersonal)
			iPacketSale=packetSalePersonalBusiness;
		else if (psf instanceof PacketSaleClass)
			iPacketSale=packetSaleClassBusiness;
		else if (psf instanceof PacketSaleMembership)
			iPacketSale=packetSaleMembershipBusiness;
		
		return iPacketSale.saleIt(psf);
	}
	
	public synchronized HmiResultObj sale(PacketSaleFactory psf){
		IPacketSale iPacketSale=null;
		if(psf instanceof PacketSalePersonal)
			iPacketSale=packetSalePersonalBusiness;
		else if (psf instanceof PacketSaleClass)
			iPacketSale=packetSaleClassBusiness;
		else if (psf instanceof PacketSaleMembership)
			iPacketSale=packetSaleMembershipBusiness;
		
		return iPacketSale.saleIt(psf);
	}
	
	public synchronized HmiResultObj deletePacketSale(PacketSaleFactory psf){
		IPacketSale iPacketSale=null;
		if(psf instanceof PacketSalePersonal)
			iPacketSale=packetSalePersonalBusiness;
		else if (psf instanceof PacketSaleClass)
			iPacketSale=packetSaleClassBusiness;
		else if (psf instanceof PacketSaleMembership)
			iPacketSale=packetSaleMembershipBusiness;
		
		return iPacketSale.deleteIt(psf);
	}
	
}
