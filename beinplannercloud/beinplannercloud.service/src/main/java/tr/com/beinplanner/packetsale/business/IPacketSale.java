package tr.com.beinplanner.packetsale.business;

import java.util.List;

import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;

public interface IPacketSale {


	
	public PacketSaleFactory findPacketSaleById(long saleId);
	
	public List<PacketSaleFactory> findPacketSaleWithNoPayment(int firmId);
	
	
	public List<PacketSaleFactory> findAllSalesForUserInChain(long userId);
	
	public List<PacketSaleFactory> findLast5PacketSalesInChain(int firmId);
	
}
