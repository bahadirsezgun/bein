package tr.com.beinplanner.packetsale.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import tr.com.beinplanner.packetpayment.business.PacketPaymentClassBusiness;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.comparator.PacketSaleComparator;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.repository.PacketSaleClassRepository;

@Component
@Qualifier("packetSaleClassBusiness")
public class PacketSaleClassBusiness implements IPacketSale {

	@Autowired
	PacketSaleClassRepository packetSaleClassRepository;
	
	@Autowired
	PacketPaymentClassBusiness packetPaymentClassBusiness;
	
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	@Qualifier("packetSaleMembershipBusiness")
	IPacketSale iPacketSale;
	
	
	
	@Override
	public PacketSaleFactory findPacketSaleById(long saleId) {
		return packetSaleClassRepository.findOne(saleId);
	}



	@Override
	public List<PacketSaleFactory> findPacketSaleWithNoPayment(int firmId) {
		List<PacketSaleFactory> packetSaleFactories=new ArrayList<PacketSaleFactory>();
		List<PacketSaleClass> packetSaleClasses=packetSaleClassRepository.findPacketSaleClassWithNoPayment(firmId);
		packetSaleFactories.addAll(packetSaleClasses);
		
		Collections.sort(packetSaleFactories,new PacketSaleComparator());
		
		return packetSaleFactories;
	}



	@Override
	public List<PacketSaleFactory> findLast5PacketSalesInChain(int firmId) {
		List<PacketSaleFactory> packetSaleFactories=iPacketSale.findLast5PacketSalesInChain(firmId);
		List<PacketSaleClass> packetSaleClasss=packetSaleClassRepository.findLast5PacketSales(firmId);
		packetSaleFactories.addAll(packetSaleClasss);
		return packetSaleFactories;
	}



	@Override
	public List<PacketSaleFactory> findAllSalesForUserInChain(long userId) {
		
		List<PacketSaleFactory> psfs=new ArrayList<>();
		
		iPacketSale.findAllSalesForUserInChain(userId).forEach(psp->{
			psfs.add((PacketSaleFactory)psp);
		});
		
		packetSaleClassRepository.findByUserId(userId).forEach(psp->{
			psp.setPacketPaymentFactory((PacketPaymentClass)packetPaymentService.findPacketPaymentBySaleId(psp.getSaleId(),packetPaymentClassBusiness));
			psfs.add((PacketSaleFactory)psp);
		});
		return psfs;
   }

}
