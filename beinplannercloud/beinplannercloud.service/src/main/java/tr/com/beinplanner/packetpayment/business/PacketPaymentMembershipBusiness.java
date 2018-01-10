package tr.com.beinplanner.packetpayment.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.LeftPaymentInfo;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembership;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentMembershipRepository;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.repository.PacketSaleMembershipRepository;

@Service
@Qualifier("packetPaymentMembershipBusiness")
public class PacketPaymentMembershipBusiness implements IPacketPayment {

	@Autowired
	PacketPaymentMembershipRepository packetPaymentMembershipRepository;
	
	@Autowired
	PacketSaleMembershipRepository packetSaleMembershipRepository;
	
	
	
	@Override
	public PacketPaymentFactory findPacketPaymentById(long id) {
		return packetPaymentMembershipRepository.findOne(id);
	}


	@Override
	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId) {
		return packetPaymentMembershipRepository.findBySaleId(saleId);
	}


	@Override
	public double findTotalIncomePaymentInDate(Date startDate, Date endDate, int firmId) {
		  List<PacketPaymentMembership> packetPaymentMemberships=packetPaymentMembershipRepository.findPacketPaymentMembershipForDate(startDate,endDate,firmId);
		  double totalPaymentForMembership=packetPaymentMemberships.stream().mapToDouble(ppp->ppp.getPayAmount()).sum();
		  return totalPaymentForMembership;
	}


	@Override
	public List<PacketPaymentFactory> findLast5packetPaymentsInChain(int firmId) {
		List<PacketPaymentFactory> packetPaymentFactories=new ArrayList<PacketPaymentFactory>();
		packetPaymentFactories.addAll(packetPaymentMembershipRepository.findLast5packetPayments(firmId));
		return packetPaymentFactories;
	}


	@Override
	public LeftPaymentInfo findLeftPacketPaymentsInChain(int firmId) {
		LeftPaymentInfo leftPaymentInfo=new LeftPaymentInfo();
		
		List<PacketPaymentMembership> packetPaymentMemberships= packetPaymentMembershipRepository.findLeftPacketPaymentMembership(firmId);
		double leftPP=packetPaymentMemberships
				.stream()
				.mapToDouble(ppp->{
					             	PacketSaleMembership packetSaleMembership=packetSaleMembershipRepository.findOne(ppp.getSaleId());              
					             	double payment= packetSaleMembership.getPacketPrice()-ppp.getPayAmount();
					             	return payment;
							    }
				).sum();
		
		leftPaymentInfo.setLeftPayment(leftPP);
		leftPaymentInfo.setLeftPaymentCount(leftPaymentInfo.getLeftPaymentCount());

		List<PacketSaleMembership> packetSaleMemberships=packetSaleMembershipRepository.findPacketSaleMembershipWithNoPayment(firmId);
		
		double leftPPN=packetSaleMemberships
				.stream()
				.mapToDouble(psp->psp.getPacketPrice()
				).sum();
		
		
		leftPaymentInfo.setNoPayment(leftPPN);
		leftPaymentInfo.setNoPaymentCount(packetSaleMemberships.size());
		
		return leftPaymentInfo;
	}

}
