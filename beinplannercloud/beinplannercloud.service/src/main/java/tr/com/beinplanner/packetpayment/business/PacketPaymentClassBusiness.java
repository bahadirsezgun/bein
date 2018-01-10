package tr.com.beinplanner.packetpayment.business;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.LeftPaymentInfo;
import tr.com.beinplanner.packetpayment.comparator.PacketPaymentComparator;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonal;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentClassRepository;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.repository.PacketSaleClassRepository;

@Service
@Qualifier("packetPaymentClassBusiness")
public class PacketPaymentClassBusiness implements IPacketPayment {


	@Autowired
	PacketPaymentClassRepository packetPaymentClassRepository;
	
	@Autowired
	PacketSaleClassRepository packetSaleClassRepository;
	
	@Autowired
	@Qualifier("packetPaymentMembershipBusiness")
	IPacketPayment iPacketPayment;
	
	
	
	@Override
	public PacketPaymentFactory findPacketPaymentById(long id) {
		return packetPaymentClassRepository.findOne(id);
	}


	@Override
	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId) {
		return packetPaymentClassRepository.findBySaleId(saleId);
	}


	@Override
	public double findTotalIncomePaymentInDate(Date startDate, Date endDate, int firmId) {
		 double totalPayment=iPacketPayment.findTotalIncomePaymentInDate(startDate, endDate, firmId);
		 List<PacketPaymentClass> packetPaymentClasses=packetPaymentClassRepository.findPacketPaymentClassForDate(startDate,endDate,firmId);
		 double totalPaymentForClass=packetPaymentClasses.stream().mapToDouble(ppp->ppp.getPayAmount()).sum();
		 return totalPayment+totalPaymentForClass;
	}


	@Override
	public List<PacketPaymentFactory> findLast5packetPaymentsInChain(int firmId) {
		List<PacketPaymentFactory> packetPaymentFactories=iPacketPayment.findLast5packetPaymentsInChain(firmId);
		packetPaymentFactories.addAll(packetPaymentClassRepository.findLast5packetPayments(firmId));
		return packetPaymentFactories;
	}


	@Override
	public LeftPaymentInfo findLeftPacketPaymentsInChain(int firmId) {
		LeftPaymentInfo leftPaymentInfo=iPacketPayment.findLeftPacketPaymentsInChain(firmId);
		
		List<PacketPaymentClass> packetPaymentClasss= packetPaymentClassRepository.findLeftPacketPaymentClass(firmId);
		double leftPP=packetPaymentClasss
				.stream()
				.mapToDouble(ppp->{
					             	PacketSaleClass packetSaleClass=packetSaleClassRepository.findOne(ppp.getSaleId());              
					             	double payment= packetSaleClass.getPacketPrice()-ppp.getPayAmount();
					             	return payment;
							    }
				).sum();
		
		leftPaymentInfo.setLeftPayment(leftPaymentInfo.getLeftPayment()+leftPP);
		leftPaymentInfo.setLeftPaymentCount(packetPaymentClasss.size()
											+leftPaymentInfo.getLeftPaymentCount());

		List<PacketSaleClass> packetSaleClasss=packetSaleClassRepository.findPacketSaleClassWithNoPayment(firmId);
		
		double leftPPN=packetSaleClasss
				.stream()
				.mapToDouble(psp->psp.getPacketPrice()
				).sum();
		
		
		leftPaymentInfo.setNoPayment(leftPPN+leftPaymentInfo.getNoPayment());
		leftPaymentInfo.setNoPaymentCount(leftPaymentInfo.getNoPaymentCount()
				+packetSaleClasss.size());
		
		return leftPaymentInfo;
	}

}
