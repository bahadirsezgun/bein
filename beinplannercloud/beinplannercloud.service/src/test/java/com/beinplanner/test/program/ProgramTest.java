package com.beinplanner.test.program;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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

import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.program.dao.ProgramMembership;
import tr.com.beinplanner.program.dao.ProgramMembershipDetail;
import tr.com.beinplanner.program.dao.ProgramPersonal;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ProgDurationTypes;
import tr.com.beinplanner.util.RestrictionUtil;
import tr.com.beinplanner.util.StatuTypes;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.beinplanner","tr.com.beinplanner"})
@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@RunWith(SpringRunner.class)
@SpringBootTest 
public class ProgramTest {

	@Configuration
    static class ContextConfiguration {
      
		@Bean
        public ProgramService programService() {
			ProgramService programService = new ProgramService();
            // set properties, etc.
            return programService;
        }
   }
	
	@Autowired
	ProgramService programService;
	
	
	@Test
	public void findPersonalProgramById() {
		
		ProgramPersonal programPersonal=programService.findProgramPersonalById(14);
		assertTrue(programPersonal!=null);
		
		DefFirm defFirm=programPersonal.getDefFirm();
		System.out.println("FIRM ID IS  "+defFirm.getFirmId()+" AND NAME IS "+defFirm.getFirmName());
		assertTrue(defFirm!=null);
		
	}
	
	private ProgramMembership getProgramMembership() {
		ProgramMembership programMembership=new ProgramMembership();
		programMembership.setProgId(14);
		programMembership.setFirmId(1);
		programMembership.setFreezeDuration(1);
		programMembership.setFreezeDurationType(ProgDurationTypes.DURATION_TYPE_WEEKLY);
		programMembership.setMaxFreezeCount(3);
		programMembership.setProgComment("TEST 3 VERİSİ");
		programMembership.setProgDescription("TEST ICIN HAZIRLANMIŞ PROGRAM");
		programMembership.setProgDuration(1);
		programMembership.setProgDurationType(ProgDurationTypes.DURATION_TYPE_MONTHLY);
		programMembership.setProgIcon("-");
		programMembership.setProgName("1 YILLIK (TEST)"); 
		programMembership.setProgPrice(1000D);
		programMembership.setProgRestriction(RestrictionUtil.RESTIRICTION_FLAG_NO);
		programMembership.setProgStatus(StatuTypes.PASSIVE);
		programMembership.setProgUserId(0L);
		programMembership.setProgShortName("1AS-T");
		List<ProgramMembershipDetail> programMembershipDetails=new ArrayList<ProgramMembershipDetail>();
		
		ProgramMembershipDetail pmd1=new ProgramMembershipDetail();
		pmd1.setProgRestrictedDay(1);
		pmd1.setProgRestrictedTime(1);
		
		programMembershipDetails.add(pmd1);
		/*
		ProgramMembershipDetail pmd2=new ProgramMembershipDetail();
		pmd2.setProgRestrictedDay(2);
		pmd2.setProgRestrictedTime(2);
		
		programMembershipDetails.add(pmd2);
		*/
		programMembership.setProgramMembershipDetails(programMembershipDetails);
		
		return programMembership;
	}
	
	
	@Test
	public void findMembershipProgramById() {
		
		ProgramMembership programMembership=getProgramMembership();
		programService.createProgramMembership(programMembership);
		System.out.println("PROG ID IS "+programMembership.getProgId());
		ProgramMembership programMembershipInDb=programService.findProgramMembershipById(programMembership.getProgId());
		assertTrue(programMembershipInDb!=null);
		System.out.println("PROGRAM MEMBERSHIP SELECTED "+programMembershipInDb.getFirmId());
		DefFirm defFirm=programMembershipInDb.getDefFirm();
		System.out.println("FIRM ID IS  "+defFirm.getFirmId()+" AND NAME IS "+defFirm.getFirmName());
		assertTrue(defFirm!=null);
		
	}
	
	
	@Test
	public void createProgramMembership() {
		ProgramMembership programMembership=getProgramMembership();
		HmiResultObj hmiResultObj=programService.createProgramMembership(programMembership);
		ProgramMembership pm=(ProgramMembership)hmiResultObj.getResultObj();
		
		programMembership.getProgramMembershipDetails().forEach(pmd->{
			pmd.setProgId(pm.getProgId());
		});
		
		programService.createProgramMembershipDetails(programMembership.getProgramMembershipDetails(), pm.getProgId());
		
		programMembership.getProgramMembershipDetails().forEach(pmd->{
			System.out.println(pmd.getProgDetId());
		});
	}
	
	
}
