package tr.com.beinplanner.packetsale.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembership;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonal;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleComparator;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.repository.PacketSaleClassRepository;
import tr.com.beinplanner.packetsale.repository.PacketSaleMembershipRepository;
import tr.com.beinplanner.packetsale.repository.PacketSalePersonalRepository;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.RestrictionUtil;

@Service
@Qualifier("packetSaleService")
public class PacketSaleService {

	@Autowired
	LoginSession loginSession;
	
	@Autowired
	PacketSaleClassRepository packetSaleClassRepository;

	@Autowired
	PacketSalePersonalRepository packetSalePersonalRepository;
	
	@Autowired
	PacketSaleMembershipRepository packetSaleMembershipRepository;
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	
	public List<PacketSaleFactory> findUserBoughtPackets(long userId){
			List<PacketSaleFactory> packetSaleFactories=new ArrayList<>();
		
			if(loginSession.getPacketRestriction().getPersonalRestriction()==RestrictionUtil.RESTIRICTION_FLAG_YES) {
				List<PacketSalePersonal> packetSalePersonals=packetSalePersonalRepository.findByUserId(userId);
				
				packetSalePersonals.forEach(ps->{
					ps.setPacketPaymentFactory((PacketPaymentPersonal)packetPaymentService.findPersonalPacketPaymentBySaleId(ps.getSaleId()));
				});
				
				packetSalePersonals.forEach(ps->packetSaleFactories.add(ps));
			}
			
			if(loginSession.getPacketRestriction().getGroupRestriction()==RestrictionUtil.RESTIRICTION_FLAG_YES) {
				List<PacketSaleClass> packetSaleClasses=packetSaleClassRepository.findByUserId(userId);
				packetSaleClasses.forEach(ps->{
					ps.setPacketPaymentFactory((PacketPaymentClass)packetPaymentService.findClassPacketPaymentBySaleId(ps.getSaleId()));
				});
				packetSaleClasses.forEach(ps->packetSaleFactories.add(ps));
			}
		
			if(loginSession.getPacketRestriction().getMembershipRestriction()==RestrictionUtil.RESTIRICTION_FLAG_YES) {
				List<PacketSaleMembership> packetSaleMemberships=packetSaleMembershipRepository.findByUserId(userId);
				packetSaleMemberships.forEach(ps->{
					ps.setPacketPaymentFactory((PacketPaymentMembership)packetPaymentService.findMembershipPacketPaymentBySaleId(ps.getSaleId()));
				});
				
				packetSaleMemberships.forEach(ps->packetSaleFactories.add(ps));
			}
			
			Collections.sort(packetSaleFactories, new PacketSaleComparator());
		
		return packetSaleFactories;
	}
	
	
	
	public PacketSaleClass findPacketSaleClassById(long saleId) {
		PacketSaleClass packetSaleClass=packetSaleClassRepository.findOne(saleId);
		packetSaleClass.getUser().setPassword(null);
		return packetSaleClass;
	}
	
	public PacketSalePersonal findPacketSalePersonalById(long saleId) {
		PacketSalePersonal packetSalePersonal=packetSalePersonalRepository.findOne(saleId);
		packetSalePersonal.getUser().setPassword(null);
		return packetSalePersonal;
	}
	
	public PacketSaleMembership findPacketSaleMembershipById(long saleId) {
		PacketSaleMembership packetSaleMembership=packetSaleMembershipRepository.findOne(saleId);
		packetSaleMembership.getUser().setPassword(null);
		return packetSaleMembership;
	}
	
	public List<PacketSaleClass> findPacketSaleClassWithNoPayment(int firmId){
		List<PacketSaleClass> packetSaleClasses=packetSaleClassRepository.findPacketSaleClassWithNoPayment(firmId);
		packetSaleClasses.forEach(psf->{psf.getUser().setPassword(null);});
		
		return packetSaleClasses;
	}
	
	public List<PacketSaleMembership> findPacketSaleMembershipWithNoPayment(int firmId){
		List<PacketSaleMembership> packetSaleMemberships=packetSaleMembershipRepository.findPacketSaleMembershipWithNoPayment(firmId);
		packetSaleMemberships.forEach(psf->{psf.getUser().setPassword(null);});
		
		return packetSaleMemberships;
	}
	
	public List<PacketSalePersonal> findPacketSalePersonalWithNoPayment(int firmId){
		List<PacketSalePersonal> packetSalePersonals=packetSalePersonalRepository.findPacketSalePersonalWithNoPayment(firmId);
		packetSalePersonals.forEach(psf->{psf.getUser().setPassword(null);});
		return packetSalePersonals;
	}
	
	public List<PacketSaleFactory> findLast5PacketSales(int firmId){
		List<PacketSalePersonal> packetSalePersonals=packetSalePersonalRepository.findLast5PacketSales(firmId);
		List<PacketSaleMembership> packetSaleMemberships=packetSaleMembershipRepository.findLast5PacketSales(firmId);
		List<PacketSaleClass> packetSaleClasses=packetSaleClassRepository.findLast5PacketSales(firmId);
		
		List<PacketSaleFactory> packetSaleFactories=new ArrayList<PacketSaleFactory>();
		packetSaleFactories.addAll(packetSalePersonals);
		packetSaleFactories.addAll(packetSaleMemberships);
		packetSaleFactories.addAll(packetSaleClasses);
		
		
		
		
		
		return packetSaleFactories;
	}
	
	
}
