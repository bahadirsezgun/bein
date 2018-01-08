package com.beinplanner.test.user;

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

import tr.com.beinplanner.dashboard.businessEntity.ActiveMember;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.beinplanner","tr.com.beinplanner"})
@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@RunWith(SpringRunner.class)
@SpringBootTest 
public class UserTest {

	@Configuration
    static class ContextConfiguration {
      
		@Bean
        public UserService userService() {
			UserService userService = new UserService();
            // set properties, etc.
            return userService;
        }
		
   }
	
	
	@Autowired
	UserService userService;
	
	
	@Test
	public void findActiveMembers() {
		ActiveMember activeMember= userService.findActiveMemberCount(1);
	
		assertTrue(activeMember!=null);
	}
	
	@Test
	public void findByUsernameAndUsersurname() {
		List<User> users=userService.findByUsernameAndUsersurname("%", "di%", 1,1);
	
		users.forEach(u->System.out.println(u.getUserName()+" "+u.getUserSurname()));
		
		assertTrue(users!=null);
	}
}
