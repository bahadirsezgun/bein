package tr.com.beinplanner.packetsale.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.packetpayment.business.IPacketPayment;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembership;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.business.IScheduleMembership;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.util.ResultStatuObj;
@Service
@Qualifier("packetSaleMembershipFacade")
public class PacketSaleMembershipFacade implements IPacketSaleFacade {

	@Autowired
	@Qualifier("packetPaymentMembershipBusiness")
	IPacketPayment iPacketPayment;
	
	
	@Autowired
	@Qualifier("scheduleMembership")
	IScheduleMembership iScheduleMembership;
	
	
	
	
	@Override
	public HmiResultObj canSaleDelete(PacketSaleFactory packetSaleFactory) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		PacketSaleMembership psc=(PacketSaleMembership)packetSaleFactory;
		PacketPaymentMembership packetPaymentMembership=(PacketPaymentMembership)iPacketPayment.findPacketPaymentBySaleId(psc.getSaleId());
		if(packetPaymentMembership==null) {
			
			ScheduleMembershipPlan scheduleMembershipPlan= iScheduleMembership.findSchedulePlanBySaleId(psc.getSaleId());
			if(scheduleMembershipPlan!=null) {
				hmiResultObj.setResultMessage("saledPacketHaveBooking");
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}
		}else {
			hmiResultObj.setResultMessage("saledPacketHavePayment");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		return hmiResultObj;
	}

}