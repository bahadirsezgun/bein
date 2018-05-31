package com.beinplanner.contollers.packetsale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.diabet.dao.UserDiabet;
import tr.com.beinplanner.diabet.dao.UserDiabetCalori;
import tr.com.beinplanner.diabet.service.DiabetService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.sport.dao.UserSportProgram;
import tr.com.beinplanner.sport.service.SportProgramService;

@RestController
@RequestMapping("/bein/packetsale")
public class PacketSaleController {

	
	@Autowired
	PacketSaleService packetSaleService;
	
	@Autowired
	DiabetService diabetService;
	
	@Autowired
	SportProgramService sportProgramService;
	
	
	@Autowired
	LoginSession loginSession;
	
	@RequestMapping(value="/leftPayments", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory>	leftPayments(){
		return packetSaleService.findLeftPaymentsInChain(loginSession.getUser().getFirmId());
	}
	
	@RequestMapping(value="/findUserBoughtPackets/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> findUserBoughtPackets(@PathVariable("userId") long userId ){
		return packetSaleService.findUserBoughtPackets(userId);
	}
	
	
	

	@RequestMapping(value="/findUserBoughtPacketsForReport/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> findUserBoughtPacketsForReport(@PathVariable("userId") long userId ){
		
		List<PacketSaleFactory> packetSaleFactories=packetSaleService.findUserBoughtPackets(userId);
		
		packetSaleFactories.forEach(psf->{
			if(psf instanceof PacketSaleMembership) {
				UserDiabetCalori udc= diabetService.findByUserIdAndSaleIdAndSaleType(((PacketSaleMembership) psf).getUserId(), ((PacketSaleMembership) psf).getSaleId(), ((PacketSaleMembership) psf).getProgType());
				if(udc!=null) {
					psf.setUserDiabetCalori(udc);
					List<UserDiabet> userDiabets=diabetService.findByUdcId(udc.getUdcId());
					  psf.setUserDiabets(userDiabets);
				 }
				
				List<UserSportProgram> usp= sportProgramService.findBySaleIdAndSaleTypeAndUserId(((PacketSaleMembership) psf).getSaleId(), ((PacketSaleMembership) psf).getProgType(), ((PacketSaleMembership) psf).getUserId());
				psf.setUserSportPrograms(usp);
				
				
			}else if(psf instanceof PacketSalePersonal) {
				UserDiabetCalori udc= diabetService.findByUserIdAndSaleIdAndSaleType(((PacketSalePersonal) psf).getUserId(), ((PacketSalePersonal) psf).getSaleId(), ((PacketSalePersonal) psf).getProgType());
				if(udc!=null) {
					psf.setUserDiabetCalori(udc);
					List<UserDiabet> userDiabets=diabetService.findByUdcId(udc.getUdcId());
					  psf.setUserDiabets(userDiabets);
					}
				
				List<UserSportProgram> usp= sportProgramService.findBySaleIdAndSaleTypeAndUserId(((PacketSalePersonal) psf).getSaleId(), ((PacketSalePersonal) psf).getProgType(), ((PacketSalePersonal) psf).getUserId());
				psf.setUserSportPrograms(usp);
				
			}else if(psf instanceof PacketSaleClass) {
				UserDiabetCalori udc= diabetService.findByUserIdAndSaleIdAndSaleType(((PacketSaleClass) psf).getUserId(), ((PacketSaleClass) psf).getSaleId(), ((PacketSaleClass) psf).getProgType());
				if(udc!=null) {
					psf.setUserDiabetCalori(udc);
					List<UserDiabet> userDiabets=diabetService.findByUdcId(udc.getUdcId());
					  psf.setUserDiabets(userDiabets);
					}
				List<UserSportProgram> usp= sportProgramService.findBySaleIdAndSaleTypeAndUserId(((PacketSaleClass) psf).getSaleId(), ((PacketSaleClass) psf).getProgType(), ((PacketSaleClass) psf).getUserId());
				psf.setUserSportPrograms(usp);
			}
			
			
		});
		
		return packetSaleFactories;
	}
	
	
	@RequestMapping(value="/findUserBoughtPacketsForCalendar/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> findUserBoughtPacketsForCalendar(@PathVariable("userId") long userId ){
		return packetSaleService.findUserBoughtPacketsForCalendar(userId);
	}
	
	@RequestMapping(value="/sale", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj sale(@RequestBody PacketSaleFactory packetSaleFactory ){
		return packetSaleService.sale(packetSaleFactory);
	}
	
	@RequestMapping(value="/sale/changePrice", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj changePrice(@RequestBody PacketSaleFactory packetSaleFactory ){
		return packetSaleService.changePrice(packetSaleFactory);
	}
	
	
	@RequestMapping(value="/deletePacketSale", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deletePacketSale(@RequestBody PacketSaleFactory packetSaleFactory ){
		return packetSaleService.deletePacketSale(packetSaleFactory);
	}
}
