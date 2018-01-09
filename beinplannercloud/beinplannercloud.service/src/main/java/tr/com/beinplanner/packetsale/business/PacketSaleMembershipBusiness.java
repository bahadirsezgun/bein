package tr.com.beinplanner.packetsale.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembership;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.repository.PacketSaleMembershipRepository;

@Component
@Qualifier("packetSaleMembershipBusiness")
public class PacketSaleMembershipBusiness implements IPacketSale {

	@Autowired
	PacketSaleMembershipRepository packetSaleMembershipRepository;
		
	@Autowired
	PacketPaymentService packetPaymentService;
	
	
	
	@Override
	public List<PacketSaleFactory> findAllSalesForUser(long userId) {
		
		List<PacketSaleFactory> psfs=new ArrayList<>();
		
		packetSaleMembershipRepository.findByUserId(userId).forEach(psp->{
			psp.setPacketPaymentFactory((PacketPaymentMembership)packetPaymentService.findMembershipPacketPaymentBySaleId(psp.getSaleId()));
			
			psfs.add((PacketSaleFactory)psp);
		});
		return psfs;
   }


}
