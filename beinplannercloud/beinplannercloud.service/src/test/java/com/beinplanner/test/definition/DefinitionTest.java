package com.beinplanner.test.definition;

import static org.junit.Assert.assertTrue;

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

import tr.com.beinplanner.bonus.business.service.UserBonusCalculatePersonalService;
import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.user.service.UserService;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.beinplanner","tr.com.beinplanner"})
@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@RunWith(SpringRunner.class)
@SpringBootTest 
public class DefinitionTest {
	
	@Configuration
    static class ContextConfiguration {
      
		@Bean
        public DefinitionService definitionService() {
			DefinitionService definitionService = new DefinitionService();
            // set properties, etc.
            return definitionService;
        }
		
   }
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	SettingsService settingsService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DefinitionService definitionService;

	@Before
	public void setLoginSession() {
		loginSession.setPtGlobal(settingsService.findPtGlobalByFirmId(1));
		loginSession.setPtRules(settingsService.findPtRulesByFirmId(1));
		loginSession.setUser(userService.findUserById(48));
	}
	
	
	@Test
	public void findFirm() {
		DefFirm defFirm=definitionService.findFirm(2);
		System.out.println("I AM HERE");
		assertTrue(defFirm!=null);
		
	}
	
	@Test
	public void findFirmByEmail() {
		DefFirm defFirm=definitionService.findFirmByEMail("bbcsezgun@gmail.com");
		System.out.println("I AM HERE "+defFirm.getFirmEmail());
		assertTrue(defFirm!=null);
		
	}
	
}
