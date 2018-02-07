package tr.com.beinplanner.packetsale.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import tr.com.beinplanner.packetpayment.business.PacketPaymentMembershipBusiness;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembership;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.comparator.PacketSaleComparator;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.facade.IPacketSaleFacade;
import tr.com.beinplanner.packetsale.repository.PacketSaleMembershipRepository;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.schedule.service.ScheduleMembershipService;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.util.StatuTypes;

@Component
@Qualifier("packetSaleMembershipBusiness")
public class PacketSaleMembershipBusiness implements IPacketSale {

	@Autowired
	PacketSaleMembershipRepository packetSaleMembershipRepository;
		
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	PacketPaymentMembershipBusiness packetPaymentMembershipBusiness;
	
	
	@Autowired
	@Qualifier("packetSaleMembershipFacade")
	IPacketSaleFacade iPacketSaleFacade;
	
	
	@Autowired
	ScheduleMembershipService scheduleMembershipService;
	
	
	@Override
	public HmiResultObj saleIt(PacketSaleFactory packetSaleFactory) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		PacketSaleMembership psm=(PacketSaleMembership)packetSaleFactory;
		
			Date smpStartDate=(Date)psm.getSmpStartDate().clone();
			hmiResultObj= iPacketSaleFacade.canSale(psm.getUserId(), smpStartDate);
		
			if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS_STR){
				psm.setChangeDate(new Date());
				try {
					psm=packetSaleMembershipRepository.save(psm);
					
					hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
					hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
					hmiResultObj.setResultObj(psm);

					
					
					ScheduleMembershipPlan scheduleFactory=(ScheduleMembershipPlan)scheduleMembershipService.findScheduleFactoryPlanBySaleId(psm.getSaleId());
					if(scheduleFactory!=null)
						scheduleFactory=new ScheduleMembershipPlan();
					
					scheduleFactory.setSmpComment(psm.getSalesComment());
					scheduleFactory.setProgId(psm.getProgId());
					//scheduleFactory.setSaleId(psm.getSaleId());
					//scheduleFactory.setUserId(psm.getUserId());
					scheduleFactory.setSmpStartDate(smpStartDate);
					scheduleFactory.setSmpFreezeCount(0);
					scheduleFactory.setSmpPrice(psm.getPacketPrice());
					scheduleFactory.setSmpStatus(StatuTypes.ACTIVE);
					scheduleFactory.setSmpStartDate(psm.getSmpStartDate());
					
					
					scheduleMembershipService.createPlan(scheduleFactory);
				
					
				} catch (Exception e) {
					hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
					hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
				}
		    }
			
			return hmiResultObj;
	}
	
	@Override
	public HmiResultObj deleteIt(PacketSaleFactory packetSaleFactory) {
		HmiResultObj hmiResultObj= iPacketSaleFacade.canSaleDelete(packetSaleFactory);
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			packetSaleMembershipRepository.delete((PacketSaleMembership)packetSaleFactory);
		}
		return hmiResultObj;
	}
	
	@Override
	public List<PacketSaleFactory> findPacketSaleWithNoPayment(int firmId) {
		List<PacketSaleFactory> packetSaleFactories=new ArrayList<PacketSaleFactory>();
		List<PacketSaleMembership> packetSaleMemberships=packetSaleMembershipRepository.findPacketSaleMembershipWithNoPayment(firmId);
		packetSaleFactories.addAll(packetSaleMemberships);
		
		Collections.sort(packetSaleFactories,new PacketSaleComparator());
		
		return packetSaleFactories;
	}



	@Override
	public List<PacketSaleFactory> findLeftPaymentsInChain(int firmId) {
		List<PacketSaleFactory> packetSaleFactories=new ArrayList<>();
		
		
		List<PacketSaleMembership> packetSaleMembershipsNoPayment=packetSaleMembershipRepository.findPacketSaleMembershipWithNoPayment(firmId);
		
		List<PacketSaleMembership> packetSaleMembershipsLeftPayment=packetSaleMembershipRepository.findPacketSaleMembershipWithLeftPayment(firmId);
		packetSaleMembershipsLeftPayment.forEach(pslp->{
			pslp.setPacketPaymentFactory((PacketPaymentMembership)packetPaymentService.findPacketPaymentBySaleId(pslp.getSaleId(), packetPaymentMembershipBusiness));
		});
		
		
		packetSaleFactories.addAll(packetSaleMembershipsLeftPayment);
		packetSaleFactories.addAll(packetSaleMembershipsNoPayment);
		return packetSaleFactories;
	}

	@Override
	public PacketSaleFactory findPacketSaleById(long saleId) {
		return packetSaleMembershipRepository.findOne(saleId);
	}



	@Override
	public List<PacketSaleFactory> findLast5PacketSalesInChain(int firmId) {
		List<PacketSaleFactory> packetSaleFactories=new ArrayList<PacketSaleFactory>();
		List<PacketSaleMembership> packetSaleMemberships=packetSaleMembershipRepository.findLast5PacketSales(firmId);
		packetSaleFactories.addAll(packetSaleMemberships);
		return packetSaleFactories;
	}



	@Override
	public List<PacketSaleFactory> findAllSalesForUserInChain(long userId) {
		
		List<PacketSaleFactory> psfs=new ArrayList<>();
		
		packetSaleMembershipRepository.findByUserId(userId).forEach(psp->{
			psp.setPacketPaymentFactory((PacketPaymentMembership)packetPaymentService.findPacketPaymentBySaleId(psp.getSaleId(),packetPaymentMembershipBusiness));
			
			psfs.add((PacketSaleFactory)psp);
			
			ScheduleMembershipPlan scheduleFactory= (ScheduleMembershipPlan)scheduleMembershipService.findScheduleFactoryPlanBySaleId(psp.getSaleId());
			
			psp.setSmpStartDate((Date)scheduleFactory.getSmpStartDate().clone());
			
		});
		return psfs;
   }


}
