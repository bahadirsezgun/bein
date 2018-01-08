package com.beinplanner.test.payment;

import static org.junit.Assert.assertTrue;

import java.util.List;

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

import tr.com.beinplanner.dashboard.businessEntity.LeftPaymentInfo;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonal;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.service.PacketSaleService;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.beinplanner","tr.com.beinplanner"})
@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@RunWith(SpringRunner.class)
@SpringBootTest 
public class PacketPaymentTest {

	@Configuration
    static class ContextConfiguration {
      
		@Bean
        public PacketPaymentService packetPaymentService() {
			PacketPaymentService packetPaymentService = new PacketPaymentService();
            // set properties, etc.
            return packetPaymentService;
        }
		
        @Bean
        public PacketSaleService packetSaleService() {
        	PacketSaleService packetSaleService = new PacketSaleService();
            // set properties, etc.
            return packetSaleService;
        }
   }
	
	
	@Autowired
	PacketSaleService packetSaleService;
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Test
	public void findLeftPacketPayments() {
		LeftPaymentInfo leftPaymentInfo= packetPaymentService.findLeftPacketPayments(1);
		leftPaymentInfo.toString();
		assertTrue(leftPaymentInfo!=null);
	}
	
	@Test
	public void findPacketPaymentPersonalById() {
		PacketPaymentPersonal packetPaymentPersonal=(PacketPaymentPersonal) packetPaymentService.findPersonalPacketPaymentById(22L);
		System.out.println("PAY AMOUNT "+ packetPaymentPersonal.getPayAmount());
		assertTrue(packetPaymentPersonal!=null);
	}
	@Test
	public void findPacketPaymentClassById() {
		PacketPaymentClass packetPaymentClass=(PacketPaymentClass) packetPaymentService.findClassPacketPaymentById(2);
		System.out.println("PAY AMOUNT "+ packetPaymentClass.getPayAmount());
		assertTrue(packetPaymentClass!=null);
	}
	
	@Test
	public void findLast5packetPayments() {
		List<PacketPaymentFactory> packetPaymentFactory=packetPaymentService.findLast5packetPayments(1);
		assertTrue(packetPaymentFactory!=null);
	}
	
}
