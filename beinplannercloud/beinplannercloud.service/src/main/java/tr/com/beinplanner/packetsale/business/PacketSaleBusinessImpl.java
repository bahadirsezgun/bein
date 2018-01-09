package tr.com.beinplanner.packetsale.business;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.packetsale.dao.PacketSaleComparator;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
@Service
@Qualifier("packetSaleBusinessImpl")
public class PacketSaleBusinessImpl implements IPacketSale {

	
	IPacketSale iPacketSale;
	
	@Autowired
	PacketSalePersonalBusiness packetSalePersonalBusiness;
	
	
	@Override
	public List<PacketSaleFactory> findAllSalesForUser(long userId) {
		iPacketSale=packetSalePersonalBusiness;
		
		List<PacketSaleFactory> packetSaleFactories=iPacketSale.findAllSalesForUser(userId);
		Collections.sort(packetSaleFactories, new PacketSaleComparator());
		return packetSaleFactories;
	}

	
}
