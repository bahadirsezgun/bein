package com.beinplanner.test.sale;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetsale.business.PacketSaleClassBusiness;
import tr.com.beinplanner.packetsale.business.PacketSaleMembershipBusiness;
import tr.com.beinplanner.packetsale.business.PacketSalePersonalBusiness;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.user.service.UserService;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.beinplanner","tr.com.beinplanner"})
@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@RunWith(SpringRunner.class)
@SpringBootTest 
public class PacketSaleTest {

	@Autowired
	UserService userService;
	
	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	SettingsService settingsService;
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	PacketSaleClassBusiness packetSaleClassBusiness;
	
	@Autowired
	PacketSalePersonalBusiness packetSalePersonalBusiness;
	
	@Autowired
	PacketSaleMembershipBusiness packetSaleMembershipBusiness;
	
	
	@Before
	public void setLoginSession() {
		loginSession.setPtGlobal(settingsService.findPtGlobalByFirmId(1));
		loginSession.setPtRules(settingsService.findPtRulesByFirmId(1));
		loginSession.setUser(userService.findUserById(48));
	}
	
	@Configuration
    static class ContextConfiguration {
       
		 @Bean
        public PacketSaleService packetSaleService() {
        	PacketSaleService packetSaleService = new PacketSaleService();
            // set properties, etc.
            return packetSaleService;
        }
   }
	
	
	@Autowired
	PacketSaleService packetSaleService;
	
	
	
	@Test
	public void findPacketSalePersonal() {
		PacketSaleFactory packetSalePersonal= packetSaleService.findPacketSaleById(40L,packetSalePersonalBusiness);
		
		
		assertTrue(packetSalePersonal!=null);
	}
	
	
	
	@Test
	public void findLastPacketSalePersonal() {
		
		List<PacketSaleFactory> packetSaleFactories= packetSaleService.findLast5PacketSales(1);
		
		packetSaleFactories.forEach(psf->{
			if(psf instanceof PacketSalePersonal)
				System.out.println("PACKET SALE PERSONAL : "+((PacketSalePersonal)psf).getUser().getUserName());
		});
	}
	
	@Test
	public void findUserBoughtPackets() {
		
		List<PacketSaleFactory> packetSaleFactories= packetSaleService.findUserBoughtPackets(49L);
		
		packetSaleFactories.forEach(psf->{
			if(psf instanceof PacketSalePersonal)
				System.out.println("PACKET SALE PERSONAL : "+((PacketSalePersonal)psf).getSalesDate());
			else if(psf instanceof PacketSaleClass)
				System.out.println("PACKET SALE CLASS : "+((PacketSaleClass)psf).getSalesDate());
			
			
		});
	}
	
}
