package tr.com.beinplanner.packetsale.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.repository.PacketSaleClassRepository;

@Component
@Qualifier("packetSaleClassBusiness")
public class PacketSaleClassBusiness implements IPacketSale {

	@Autowired
	PacketSaleClassRepository packetSaleClassRepository;
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	@Qualifier("packetSaleMembershipBusiness")
	IPacketSale iPacketSale;
	
	
	
	@Override
	public List<PacketSaleFactory> findAllSalesForUser(long userId) {
		
		List<PacketSaleFactory> psfs=new ArrayList<>();
		
		iPacketSale.findAllSalesForUser(userId).forEach(psp->{
			psfs.add((PacketSaleFactory)psp);
		});
		
		packetSaleClassRepository.findByUserId(userId).forEach(psp->{
			psp.setPacketPaymentFactory((PacketPaymentClass)packetPaymentService.findClassPacketPaymentBySaleId(psp.getSaleId()));
			psfs.add((PacketSaleFactory)psp);
		});
		return psfs;
   }

}
