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
import tr.com.beinplanner.packetsale.business.IPacketSale;
import tr.com.beinplanner.packetsale.business.PacketSaleClassBusiness;
import tr.com.beinplanner.packetsale.business.PacketSaleMembershipBusiness;
import tr.com.beinplanner.packetsale.business.PacketSalePersonalBusiness;
import tr.com.beinplanner.packetsale.comparator.PacketSaleComparator;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.util.RestrictionUtil;

@Service
@Qualifier("packetSaleService")
public class PacketSaleService {

	@Autowired
	LoginSession loginSession;
	
	@Autowired
	PacketSaleClassBusiness packetSaleClassBusiness;

	@Autowired
	PacketSalePersonalBusiness packetSalePersonalBusiness;
	
	@Autowired
	PacketSaleMembershipBusiness packetSaleMembershipBusiness;
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	IPacketSale iPacketSale;
	
	
	public List<PacketSaleFactory> findUserBoughtPackets(long userId){
		iPacketSale=packetSalePersonalBusiness;
		return iPacketSale.findAllSalesForUserInChain(userId);
	}
	
	
	
	public PacketSaleFactory findPacketSaleById(long saleId,IPacketSale iPacketSale) {
		return iPacketSale.findPacketSaleById(saleId);
	}
	
	
	
	public List<PacketSaleFactory> findPacketSaleWithNoPayment(int firmId,IPacketSale iPacketSale){
		return iPacketSale.findPacketSaleWithNoPayment(firmId);
	}
	
	
	
	public List<PacketSaleFactory> findLast5PacketSales(int firmId){
		iPacketSale=packetSalePersonalBusiness;
		return iPacketSale.findLast5PacketSalesInChain(firmId);
	}
	
	
}
