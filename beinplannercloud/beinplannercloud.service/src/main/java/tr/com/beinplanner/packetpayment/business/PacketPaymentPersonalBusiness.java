package tr.com.beinplanner.packetpayment.business;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.LeftPaymentInfo;
import tr.com.beinplanner.packetpayment.comparator.PacketPaymentComparator;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonal;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentPersonalRepository;
import tr.com.beinplanner.packetsale.comparator.PacketSaleComparator;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.repository.PacketSalePersonalRepository;

@Service
@Qualifier("packetPaymentPersonalBusiness")
public class PacketPaymentPersonalBusiness implements IPacketPayment {

	
	@Autowired
	PacketPaymentPersonalRepository packetPaymentPersonalRepository;
	
	@Autowired
	PacketSalePersonalRepository packetSalePersonalRepository;
	
	@Autowired
	@Qualifier("packetPaymentClassBusiness")
	IPacketPayment iPacketPayment;
	
	
	
	
	@Override
	public PacketPaymentFactory findPacketPaymentById(long id) {
		return packetPaymentPersonalRepository.findOne(id);
	}




	@Override
	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId) {
		return packetPaymentPersonalRepository.findBySaleId(saleId);
	}




	@Override
	public double findTotalIncomePaymentInDate(Date startDate, Date endDate, int firmId) {
		 double totalPayment=iPacketPayment.findTotalIncomePaymentInDate(startDate, endDate, firmId);
		 List<PacketPaymentPersonal> packetPaymentPersonals=packetPaymentPersonalRepository.findPacketPaymentPersonalForDate(startDate,endDate,firmId);
		 double totalPaymentForPersonal=packetPaymentPersonals.stream().mapToDouble(ppp->ppp.getPayAmount()).sum();
		 return totalPayment+totalPaymentForPersonal;
	}




	@Override
	public List<PacketPaymentFactory> findLast5packetPaymentsInChain(int firmId) {
		List<PacketPaymentFactory> packetPaymentFactories=iPacketPayment.findLast5packetPaymentsInChain(firmId);
		packetPaymentFactories.addAll(packetPaymentPersonalRepository.findLast5packetPayments(firmId));
		Collections.sort(packetPaymentFactories, new PacketPaymentComparator());
		return packetPaymentFactories;
	}




	@Override
	public LeftPaymentInfo findLeftPacketPaymentsInChain(int firmId) {
		
		LeftPaymentInfo leftPaymentInfo=iPacketPayment.findLeftPacketPaymentsInChain(firmId);
		
		List<PacketPaymentPersonal> packetPaymentPersonals= packetPaymentPersonalRepository.findLeftPacketPaymentPersonal(firmId);
		double leftPP=packetPaymentPersonals
				.stream()
				.mapToDouble(ppp->{
					             	PacketSalePersonal packetSalePersonal=packetSalePersonalRepository.findOne(ppp.getSaleId());              
					             	double payment= packetSalePersonal.getPacketPrice()-ppp.getPayAmount();
					             	return payment;
							    }
				).sum();
		
		leftPaymentInfo.setLeftPayment(leftPaymentInfo.getLeftPayment()+leftPP);
		leftPaymentInfo.setLeftPaymentCount(packetPaymentPersonals.size()
											+leftPaymentInfo.getLeftPaymentCount());

		List<PacketSalePersonal> packetSalePersonals=packetSalePersonalRepository.findPacketSalePersonalWithNoPayment(firmId);
		
		double leftPPN=packetSalePersonals
				.stream()
				.mapToDouble(psp->psp.getPacketPrice()
				).sum();
		
		
		leftPaymentInfo.setNoPayment(leftPPN+leftPaymentInfo.getNoPayment());
		leftPaymentInfo.setNoPaymentCount(leftPaymentInfo.getNoPaymentCount()
				+packetSalePersonals.size());
		
		return leftPaymentInfo;
	}


}
