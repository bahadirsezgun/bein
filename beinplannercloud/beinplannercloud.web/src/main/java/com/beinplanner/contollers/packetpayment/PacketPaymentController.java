package com.beinplanner.contollers.packetpayment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.packetpayment.business.IPacketPayment;
import tr.com.beinplanner.packetpayment.business.PacketPaymentClassBusiness;
import tr.com.beinplanner.packetpayment.business.PacketPaymentMembershipBusiness;
import tr.com.beinplanner.packetpayment.business.PacketPaymentPersonalBusiness;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClassDetail;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentDetailFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembership;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembershipDetail;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonal;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonalDetail;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.result.HmiResultObj;
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
	
	
	@RequestMapping(value="/save", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj save(@RequestBody PacketPaymentFactory packetPaymentFactory){
		if(packetPaymentFactory instanceof PacketPaymentPersonal)
			iPacketPayment=packetPaymentPersonalBusiness;
		else if(packetPaymentFactory instanceof PacketPaymentClass)
			iPacketPayment=packetPaymentClassBusiness;
		else if(packetPaymentFactory instanceof PacketPaymentMembership)
			iPacketPayment=packetPaymentMembershipBusiness;
		
		return packetPaymentService.saveIt(packetPaymentFactory,iPacketPayment);
	}
	
	@RequestMapping(value="/deleteDetail", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteDetail(@RequestBody PacketPaymentDetailFactory packetPaymentDetailFactory){
		if(packetPaymentDetailFactory instanceof PacketPaymentPersonalDetail)
			iPacketPayment=packetPaymentPersonalBusiness;
		else if(packetPaymentDetailFactory instanceof PacketPaymentClassDetail)
			iPacketPayment=packetPaymentClassBusiness;
		else if(packetPaymentDetailFactory instanceof PacketPaymentMembershipDetail)
			iPacketPayment=packetPaymentMembershipBusiness;
		
		return packetPaymentService.deleteDetail(packetPaymentDetailFactory,iPacketPayment);
	}
	
	@RequestMapping(value="/deleteAll", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteDetail(@RequestBody PacketPaymentFactory packetPaymentFactory){
		if(packetPaymentFactory instanceof PacketPaymentPersonal)
			iPacketPayment=packetPaymentPersonalBusiness;
		else if(packetPaymentFactory instanceof PacketPaymentClass)
			iPacketPayment=packetPaymentClassBusiness;
		else if(packetPaymentFactory instanceof PacketPaymentMembership)
			iPacketPayment=packetPaymentMembershipBusiness;
		
		return packetPaymentService.deleteAll(packetPaymentFactory,iPacketPayment);
	}
	
	
}
