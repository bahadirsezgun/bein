package tr.com.beinplanner.packetsale.business;

import java.util.List;

import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;

public interface IPacketSale {

	public List<PacketSaleFactory> findAllSalesForUser(long userId);
	
}
