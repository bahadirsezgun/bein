package tr.com.beinplanner.packetsale.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.repository.PacketSalePersonalRepository;

@Component
@Qualifier("packetSalePersonalBusiness")
public class PacketSalePersonalBusiness implements IPacketSale {

	@Autowired
	PacketSalePersonalRepository packetSalePersonalRepository;
	
	@Autowired
	@Qualifier("packetSaleClassBusiness")
	IPacketSale iPacketSale;
	
	
	
	@Override
	public List<PacketSaleFactory> findAllSalesForUser(long userId) {
		
		List<PacketSaleFactory> psfs=new ArrayList<>();
		
		iPacketSale.findAllSalesForUser(userId).forEach(psp->{
			psfs.add((PacketSaleFactory)psp);
		});
		
		
		packetSalePersonalRepository.findByUserId(userId).forEach(psp->{
			psfs.add((PacketSaleFactory)psp);
		});
		return psfs;
   }

}
