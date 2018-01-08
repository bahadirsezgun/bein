package com.beinplanner.test.schedule;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import tr.com.beinplanner.dashboard.businessEntity.LastClasses;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.service.ScheduleService;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.beinplanner","tr.com.beinplanner"})
@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@RunWith(SpringRunner.class)
@SpringBootTest 
public class SchedulePlanTest {

	
	@Configuration
    static class ContextConfiguration {
       
		 @Bean
        public ScheduleService scheduleService() {
			 ScheduleService scheduleService = new ScheduleService();
            // set properties, etc.
            return scheduleService;
        }
   }
	
	
	@Autowired
	ScheduleService scheduleService;
	
	@Test
    public void findLastClasses() throws Exception {
        //LastClasses lastClasses = scheduleService.findLastOfClasses(1);
        // Assert against a `.json` file in the same package as the test
       //assertNotNull(lastClasses);
    } 
	
	
}
