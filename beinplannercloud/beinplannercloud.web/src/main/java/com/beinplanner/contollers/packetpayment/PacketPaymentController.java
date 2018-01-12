package com.beinplanner.contollers.packetpayment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.packetpayment.business.IPacketPayment;
import tr.com.beinplanner.packetpayment.business.PacketPaymentClassBusiness;
import tr.com.beinplanner.packetpayment.business.PacketPaymentMembershipBusiness;
import tr.com.beinplanner.packetpayment.business.PacketPaymentPersonalBusiness;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.util.ProgramTypes;

@RestController
@RequestMapping("/bein/packetpayment")
public class PacketPaymentController {

	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	IPacketPayment iPacketPayment;
	
	@Autowired
	PacketPaymentPersonalBusiness packetPaymentPersonalBusiness;
	
	@Autowired
	PacketPaymentClassBusiness packetPaymentClassBusiness;
	
	@Autowired
	PacketPaymentMembershipBusiness packetPaymentMembershipBusiness;
	
	
	
	
	@RequestMapping(value="/findPacketPaymentBySaleId/{saleId}/{type}", method = RequestMethod.POST) 
	public @ResponseBody PacketPaymentFactory findPacketPaymentBySaleId(@PathVariable("saleId") long saleId,@PathVariable("type") String type ){
		if(type.equals(ProgramTypes.PACKET_PAYMENT_PERSONAL))
			iPacketPayment=packetPaymentPersonalBusiness;
		else if(type.equals(ProgramTypes.PACKET_PAYMENT_CLASS))
			iPacketPayment=packetPaymentClassBusiness;
		else if(type.equals(ProgramTypes.PACKET_PAYMENT_MEMBERSHIP))
			iPacketPayment=packetPaymentMembershipBusiness;
		
		return packetPaymentService.findPacketPaymentBySaleId(saleId, iPacketPayment);
	}
}
