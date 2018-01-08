package tr.com.beinplanner.packetpayment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.LeftPaymentInfo;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembership;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonal;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentClassRepository;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentMembershipRepository;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentPersonalRepository;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.service.PacketSaleService;

@Service
@Qualifier("packetPaymentService")
public class PacketPaymentService {

	@Autowired
	PacketPaymentClassRepository packetPaymentClassRepository;
	
	@Autowired
	PacketPaymentPersonalRepository packetPaymentPersonalRepository;
	
	@Autowired
	PacketPaymentMembershipRepository packetPaymentMembershipRepository;
	
	@Autowired
	PacketSaleService packetSaleService;
	
	
	public PacketPaymentFactory findClassPacketPaymentBySaleId(long saleId) {
		return packetPaymentClassRepository.findBySaleId(saleId);
	}
	
	public PacketPaymentFactory findPersonalPacketPaymentBySaleId(long saleId) {
		return packetPaymentPersonalRepository.findBySaleId(saleId);
	}
	
	public PacketPaymentFactory findMembershipPacketPaymentBySaleId(long saleId) {
		return packetPaymentMembershipRepository.findBySaleId(saleId);
	}
	
	
	public PacketPaymentFactory findPersonalPacketPaymentById(long payId) {
		return packetPaymentPersonalRepository.findOne(payId);
	}
	
	public PacketPaymentFactory findClassPacketPaymentById(long payId) {
		return packetPaymentClassRepository.findOne(payId);
	}
	
	
	public double findTotalIncomePaymentInDate(Date startDate,Date endDate,int firmId) {
		
		  List<PacketPaymentPersonal> packetPaymentPersonals=packetPaymentPersonalRepository.findPacketPaymentPersonalForDate(startDate,endDate,firmId);
		  double totalPaymentForPersonal=packetPaymentPersonals.stream().mapToDouble(ppp->ppp.getPayAmount()).sum();
		  
		  List<PacketPaymentClass> packetPaymentClasses=packetPaymentClassRepository.findPacketPaymentClassForDate(startDate,endDate,firmId);
		  double totalPaymentForClass=packetPaymentClasses.stream().mapToDouble(ppp->ppp.getPayAmount()).sum();
		  
		  List<PacketPaymentMembership> packetPaymentMemberships=packetPaymentMembershipRepository.findPacketPaymentMembershipForDate(startDate,endDate,firmId);
		  double totalPaymentForMembership=packetPaymentMemberships.stream().mapToDouble(ppp->ppp.getPayAmount()).sum();
		return totalPaymentForPersonal+totalPaymentForClass+totalPaymentForMembership;
	}
	
	public List<PacketPaymentFactory> findLast5packetPayments(int firmId){
		List<PacketPaymentPersonal> packetPaymentPersonals=packetPaymentPersonalRepository.findLast5packetPayments(firmId);
		List<PacketPaymentMembership> packetPaymentMemberships=packetPaymentMembershipRepository.findLast5packetPayments(firmId);
		List<PacketPaymentClass> packetPaymentClasses=packetPaymentClassRepository.findLast5packetPayments(firmId);
		
		
		List<PacketPaymentFactory> packetPaymentFactories=new ArrayList<PacketPaymentFactory>();
		packetPaymentFactories.addAll(packetPaymentClasses);
		packetPaymentFactories.addAll(packetPaymentMemberships);
		packetPaymentFactories.addAll(packetPaymentPersonals);
		return packetPaymentFactories;
	}
	
	
	public LeftPaymentInfo findLeftPacketPayments(int firmId){
		LeftPaymentInfo leftPaymentInfo=new LeftPaymentInfo();
		
		List<PacketPaymentPersonal> packetPaymentPersonals= packetPaymentPersonalRepository.findLeftPacketPaymentPersonal(firmId);
		
		List<PacketPaymentClass> packetPaymentClasses= packetPaymentClassRepository.findLeftPacketPaymentClass(firmId);
		List<PacketPaymentMembership> packetPaymentMemberships= packetPaymentMembershipRepository.findLeftPacketPaymentMembership(firmId);
		
		double leftPP=packetPaymentPersonals
				.stream()
				.mapToDouble(ppp->{
								  double payment= ppp.getPacketSaleFactory().getPacketPrice()-ppp.getPayAmount();
								   return payment;
							    }
				).sum();
		double leftPM=packetPaymentMemberships
				.stream()
				.mapToDouble(ppm->{
								  double payment= ppm.getPacketSaleFactory().getPacketPrice()-ppm.getPayAmount();
								   return payment;
							    }
				).sum();
		double leftPC=packetPaymentClasses
				.stream()
				.mapToDouble(ppc->{
								  double payment= ppc.getPacketSaleFactory().getPacketPrice()-ppc.getPayAmount();
								   return payment;
							    }
				).sum();
		
		
		leftPaymentInfo.setLeftPayment(leftPP+leftPM+leftPC);
		leftPaymentInfo.setLeftPaymentCount(packetPaymentPersonals.size()
											+packetPaymentMemberships.size()
											+packetPaymentClasses.size());
		
		
		List<PacketSaleClass> packetSaleClasses=packetSaleService.findPacketSaleClassWithNoPayment(firmId);
		List<PacketSaleMembership> packetSaleMemberships=packetSaleService.findPacketSaleMembershipWithNoPayment(firmId);
		List<PacketSalePersonal> packetSalePersonals=packetSaleService.findPacketSalePersonalWithNoPayment(firmId);
		
		double leftPCN=packetSaleClasses
				.stream()
				.mapToDouble(psc->psc.getPacketPrice()
				).sum();
		
		double leftPMN=packetSaleMemberships
				.stream()
				.mapToDouble(psm->psm.getPacketPrice()
				).sum();
		double leftPPN=packetSalePersonals
				.stream()
				.mapToDouble(psp->psp.getPacketPrice()
				).sum();
		
		
		leftPaymentInfo.setNoPayment(leftPPN+leftPMN+leftPCN);
		leftPaymentInfo.setNoPaymentCount(packetSaleClasses.size()
				+packetSaleMemberships.size()
				+packetSalePersonals.size());


		
		
		return leftPaymentInfo;
	}
	
	
	
}
