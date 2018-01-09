package tr.com.beinplanner.packetsale.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
@Service
@Qualifier("packetSaleBusinessImpl")
public class PacketSaleBusinessImpl implements IPacketSale {

	@Autowired
	@Qualifier("packetSalePersonalBusiness")
	IPacketSale iPacketSale;
	
	
	@Override
	public List<PacketSaleFactory> findAllSalesForUser(long userId) {
		return iPacketSale.findAllSalesForUser(userId);
	}

	
}
