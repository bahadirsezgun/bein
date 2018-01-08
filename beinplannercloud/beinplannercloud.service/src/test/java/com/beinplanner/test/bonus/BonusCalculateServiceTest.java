package com.beinplanner.test.bonus;

import static org.junit.Assert.assertTrue;

import java.util.Date;

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

import tr.com.beinplanner.bonus.business.service.UserBonusCalculateClassService;
import tr.com.beinplanner.bonus.business.service.UserBonusCalculatePersonalService;
import tr.com.beinplanner.bonus.business.service.UserBonusCalculateService;
import tr.com.beinplanner.bonus.businessDao.UserBonusObj;
import tr.com.beinplanner.bonus.dao.UserBonusPaymentPersonal;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.OhbeUtil;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.beinplanner","tr.com.beinplanner"})
@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@RunWith(SpringRunner.class)
@SpringBootTest 
public class BonusCalculateServiceTest {

	
	@Configuration
    static class ContextConfiguration {
      
		
		
		
		@Bean
        public UserBonusCalculatePersonalService userBonusCalculatePersonalService() {
			UserBonusCalculatePersonalService userBonusCalculatePersonalService = new UserBonusCalculatePersonalService();
            // set properties, etc.
            return userBonusCalculatePersonalService;
        }
		/*
		@Bean
        public UserBonusCalculateClassService userBonusCalculateClassService() {
			UserBonusCalculateClassService userBonusCalculateClassService = new UserBonusCalculateClassService();
            // set properties, etc.
            return userBonusCalculateClassService;
        }*/
   }
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	SettingsService settingsService;
	
	
	@Before
	public void setLoginSession() {
		loginSession.setPtGlobal(settingsService.findPtGlobalByFirmId(1));
		loginSession.setPtRules(settingsService.findPtRulesByFirmId(1));
		loginSession.setUser(userService.findUserById(48));
	}
	
	@Autowired
	UserBonusCalculatePersonalService userBonusCalculatePersonalService;
	/*
	@Autowired
	UserBonusCalculateClassService userBonusCalculateClassService;
	*/
	@Test
	public void findStaffBonusObj() {
		UserBonusCalculateService userBonusCalculateService=userBonusCalculatePersonalService;
		
		Date startDate= OhbeUtil.getThatDateForNight("01/10/2017", "dd/MM/yyyy");
		Date endDate= OhbeUtil.getDateForNextMonth(OhbeUtil.getThatDateForNight("01/10/2017", "dd/MM/yyyy"),1);
		
		UserBonusObj userBonusObj= userBonusCalculateService.findStaffBonusObj(48, startDate, endDate, 1);
		
		assertTrue(userBonusObj!=null);
		
		userBonusObj.getUserBonusPaymentFactories().forEach(ppf->{
			   System.out.println("BON AMOUNT "+((UserBonusPaymentPersonal)ppf).getBonAmount() +"  BON AMOUNT "+((UserBonusPaymentPersonal)ppf).getUserId());
	    });
		
		userBonusObj.getUserBonusDetailObjs().forEach(UBDO->{
			System.out.println("PAYMENT AMOUNT :: "+UBDO.getStaffPaymentAmount()+" SCHTID "+UBDO.getSchtId());
		});
		
	}
}
