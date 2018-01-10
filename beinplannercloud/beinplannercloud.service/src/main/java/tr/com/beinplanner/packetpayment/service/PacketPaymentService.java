package tr.com.beinplanner.packetpayment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.LeftPaymentInfo;
import tr.com.beinplanner.packetpayment.business.IPacketPayment;
import tr.com.beinplanner.packetpayment.business.PacketPaymentClassBusiness;
import tr.com.beinplanner.packetpayment.business.PacketPaymentMembershipBusiness;
import tr.com.beinplanner.packetpayment.business.PacketPaymentPersonalBusiness;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembership;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonal;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.service.PacketSaleService;

@Service
@Qualifier("packetPaymentService")
public class PacketPaymentService {

	@Autowired
	PacketPaymentClassBusiness packetPaymentClassBusiness;
	
	@Autowired
	PacketPaymentPersonalBusiness packetPaymentPersonalBusiness;
	
	@Autowired
	PacketPaymentMembershipBusiness packetPaymentMembershipBusiness;
	
	@Autowired
	PacketSaleService packetSaleService;
	
	IPacketPayment iPacketPayment;
	
	
	
	
	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId,IPacketPayment iPacketPayment) {
		return iPacketPayment.findPacketPaymentBySaleId(saleId);
	}
	
	public PacketPaymentFactory findPacketPaymentById(long Id,IPacketPayment iPacketPayment) {
		return iPacketPayment.findPacketPaymentById(Id);
	}
	
	
	public double findTotalIncomePaymentInDate(Date startDate,Date endDate,int firmId) {
		return iPacketPayment.findTotalIncomePaymentInDate(startDate, endDate, firmId);
	}
	
	public List<PacketPaymentFactory> findLast5packetPayments(int firmId){
		iPacketPayment=packetPaymentPersonalBusiness;
		return iPacketPayment.findLast5packetPaymentsInChain(firmId);
	}
	
	public LeftPaymentInfo findLeftPacketPayments(int firmId){
		iPacketPayment=packetPaymentPersonalBusiness;
		return iPacketPayment.findLeftPacketPaymentsInChain(firmId);
	}
	
}
