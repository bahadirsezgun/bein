package tr.com.beinplanner.packetsale.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import tr.com.beinplanner.packetpayment.business.PacketPaymentPersonalBusiness;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonal;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.comparator.PacketSaleComparator;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.repository.PacketSalePersonalRepository;

@Component
@Qualifier("packetSalePersonalBusiness")
public class PacketSalePersonalBusiness implements IPacketSale {

	@Autowired
	PacketSalePersonalRepository packetSalePersonalRepository;
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	PacketPaymentPersonalBusiness packetPaymentPersonalBusiness;
	
	@Autowired
	@Qualifier("packetSaleClassBusiness")
	IPacketSale iPacketSale;
	
	
	
	@Override
	public List<PacketSaleFactory> findPacketSaleWithNoPayment(int firmId) {
		List<PacketSaleFactory> packetSaleFactories=new ArrayList<PacketSaleFactory>();
		List<PacketSalePersonal> packetSalePersonals=packetSalePersonalRepository.findPacketSalePersonalWithNoPayment(firmId);
		packetSaleFactories.addAll(packetSalePersonals);
		
		Collections.sort(packetSaleFactories,new PacketSaleComparator());
		
		return packetSaleFactories;
	}



	@Override
	public PacketSaleFactory findPacketSaleById(long saleId) {
		return packetSalePersonalRepository.findOne(saleId);
	}



	@Override
	public List<PacketSaleFactory> findLast5PacketSalesInChain(int firmId) {
		
		List<PacketSaleFactory> packetSaleFactories=iPacketSale.findLast5PacketSalesInChain(firmId);
		List<PacketSalePersonal> packetSalePersonals=packetSalePersonalRepository.findLast5PacketSales(firmId);
		packetSaleFactories.addAll(packetSalePersonals);
		Collections.sort(packetSaleFactories,new PacketSaleComparator());
		return packetSaleFactories;
	}



	@Override
	public List<PacketSaleFactory> findAllSalesForUserInChain(long userId) {
		
		List<PacketSaleFactory> psfs=new ArrayList<>();
		
		iPacketSale.findAllSalesForUserInChain(userId).forEach(psp->{
			psfs.add((PacketSaleFactory)psp);
		});
		
		packetSalePersonalRepository.findByUserId(userId).forEach(psp->{
			psp.setPacketPaymentFactory((PacketPaymentPersonal)packetPaymentService.findPacketPaymentBySaleId(psp.getSaleId(),packetPaymentPersonalBusiness));
			psfs.add((PacketSaleFactory)psp);
		});
		
		
		Collections.sort(psfs, new PacketSaleComparator());
		
		return psfs;
   }

}
