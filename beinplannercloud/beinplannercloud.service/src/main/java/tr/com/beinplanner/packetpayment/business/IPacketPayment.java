package tr.com.beinplanner.packetpayment.business;

import java.util.Date;
import java.util.List;

import tr.com.beinplanner.dashboard.businessEntity.LeftPaymentInfo;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;

public interface IPacketPayment {

	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId);
	
	public PacketPaymentFactory findPacketPaymentById(long Id);
	
	public LeftPaymentInfo findLeftPacketPaymentsInChain(int firmId);
	
	public List<PacketPaymentFactory> findLast5packetPaymentsInChain(int firmId);
	
	public double findTotalIncomePaymentInDate(Date startDate,Date endDate,int firmId);
}
