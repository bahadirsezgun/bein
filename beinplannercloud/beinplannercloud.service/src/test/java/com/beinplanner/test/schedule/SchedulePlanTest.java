package com.beinplanner.test.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import tr.com.beinplanner.definition.dao.DefCalendarTimes;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.program.dao.ProgramClass;
import tr.com.beinplanner.program.dao.ProgramFactory;
import tr.com.beinplanner.program.dao.ProgramPersonal;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.businessEntity.PeriodicTimePlan;
import tr.com.beinplanner.schedule.businessEntity.ScheduleCalendarObj;
import tr.com.beinplanner.schedule.businessEntity.ScheduleTimeObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;
import tr.com.beinplanner.schedule.repository.ScheduleUsersPersonalPlanRepository;
import tr.com.beinplanner.schedule.service.IScheduleService;
import tr.com.beinplanner.schedule.service.ScheduleClassService;
import tr.com.beinplanner.schedule.service.SchedulePersonalService;
import tr.com.beinplanner.schedule.service.ScheduleService;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.OhbeUtil;
import tr.com.beinplanner.util.ProgramTypes;
import tr.com.beinplanner.util.TimeTypes;

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
	
	@Autowired
	ScheduleClassService scheduleClassService;
	
	@Autowired
	SchedulePersonalService schedulePersonalService;
	

	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	SettingsService settingsService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	ProgramService programService;
	
	@Test
    public void findLastClasses() throws Exception {
        //LastClasses lastClasses = scheduleService.findLastOfClasses(1);
        // Assert against a `.json` file in the same package as the test
       //assertNotNull(lastClasses);
    } 
	
	@Before
	public void setLoginSession() {
		loginSession.setPtGlobal(settingsService.findPtGlobalByFirmId(1));
		loginSession.setPtRules(settingsService.findPtRulesByFirmId(1));
		loginSession.setUser(userService.findUserById(1));
	}
	
	
	private ScheduleTimePlan generateScheduleTimePlan() {
		ScheduleTimePlan scheduleTimePlan=new ScheduleTimePlan();
		
		scheduleTimePlan.setSchtStaffId(48);
		
		scheduleTimePlan.setTpComment("TEST DATA");
		List<ScheduleFactory> scheduleFactories=new ArrayList<>();
			ScheduleUsersPersonalPlan supp1=new ScheduleUsersPersonalPlan();
			User u1=userService.findUserById(9);
			supp1.setUser(u1);
			supp1.setUserId(9);
		
			
			scheduleFactories.add(supp1);
		
		scheduleTimePlan.setScheduleFactories(scheduleFactories);
			
			
		ProgramPersonal pf=programService.findProgramPersonalById(14);
		scheduleTimePlan.setProgId(pf.getProgId());
		scheduleTimePlan.setProgramFactory(pf);
		
		
		scheduleTimePlan.setPeriod(1);
		scheduleTimePlan.setPeriodCount(4);
		
		
		List<PeriodicTimePlan> periodicTimePlans=new ArrayList<>();
		
		PeriodicTimePlan p1=new PeriodicTimePlan();
		p1.setProgDay(3);
		p1.setProgStartTime("10:00");
		
		PeriodicTimePlan p2=new PeriodicTimePlan();
		p2.setProgDay(5);
		p2.setProgStartTime("09:00");
		
		
		PeriodicTimePlan p3=new PeriodicTimePlan();
		p3.setProgDay(7);
		p3.setProgStartTime("12:00");
		
		
		periodicTimePlans.add(p1);
		periodicTimePlans.add(p2);
		periodicTimePlans.add(p3);
		
		scheduleTimePlan.setPeriodicTimePlans(periodicTimePlans);
		
		scheduleTimePlan.setPlanStartDate(DateTimeUtil.getThatDayFormatNotNull("28.02.2018 11:00", "dd.MM.yyyy HH:mm"));
		
		
		
		return scheduleTimePlan;
	}
	
	
	@Test
	public void createScheduleTimePlan() {
		
		
		ScheduleTimePlan scheduleTimePlan=generateScheduleTimePlan();
		
		
		
		
		IScheduleService iScheduleService=null;
		
		List<HmiResultObj> hmiResultObjs=new ArrayList<>();
		
		if(scheduleTimePlan.getProgramFactory() instanceof ProgramClass)
	    	iScheduleService=scheduleClassService;
	    else if (scheduleTimePlan.getProgramFactory() instanceof ProgramPersonal)
	    	iScheduleService=schedulePersonalService;
		
		DefCalendarTimes defCalendarTimes= definitionService.findCalendarTimes(loginSession.getUser().getFirmId());
		scheduleTimePlan.setPlanEndDate(OhbeUtil.getDateForNextMinute(((Date)scheduleTimePlan.getPlanStartDate().clone()),defCalendarTimes.getDuration()));
		
		SchedulePlan schedulePlan=null;
		if(scheduleTimePlan.getSchId()==0) {
			schedulePlan=new SchedulePlan();
			schedulePlan.setProgId(((ProgramPersonal)scheduleTimePlan.getProgramFactory()).getProgId());
			schedulePlan.setProgType(ProgramTypes.PROGRAM_PERSONAL);
			schedulePlan.setSchCount(((ProgramPersonal)scheduleTimePlan.getProgramFactory()).getProgCount());
			schedulePlan.setSchStaffId(scheduleTimePlan.getSchtStaffId());
			schedulePlan.setFirmId(loginSession.getUser().getFirmId());
			schedulePlan=scheduleService.createSchedulePlan(schedulePlan);
		}else {
			schedulePlan=scheduleService.findSchedulePlanById(scheduleTimePlan.getSchId());
		}
		
		HmiResultObj hmiResultObj=iScheduleService.createPlan(scheduleTimePlan,schedulePlan);
		hmiResultObjs.add(hmiResultObj);
		
		ScheduleTimePlan returnedTimePlan=(ScheduleTimePlan)hmiResultObj.getResultObj();
		
		
		System.out.println("PERIOD  :: "+scheduleTimePlan.getPeriod());
		
		if(scheduleTimePlan.getPeriod()==1) {
			List<ScheduleTimePlan> scheduleTimePlans=generateTimePlans(returnedTimePlan, schedulePlan, iScheduleService);
			System.out.println("SCH ID :: "+schedulePlan.getSchId());
			
			for (ScheduleTimePlan stp : scheduleTimePlans) {
				System.out.println("PLAN START DATE :: "+stp.getPlanStartDate());
				
				hmiResultObjs.add(iScheduleService.createPlan(stp,schedulePlan));
			}
		}
		
		
	}
	
	
	
	private List<ScheduleTimePlan> generateTimePlans(ScheduleTimePlan scheduleTimePlan,SchedulePlan schedulePlan,IScheduleService iScheduleService){
		List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanBySchId(schedulePlan.getSchId());
		int progCount=schedulePlan.getSchCount();
		int leftProgCount=progCount-scheduleTimePlans.size();
		int periodCount=scheduleTimePlan.getPeriodCount();
		
		List<ScheduleTimePlan> generatedSTPs=new ArrayList<>();
		
		DefCalendarTimes defCalendarTimes= definitionService.findCalendarTimes(loginSession.getUser().getFirmId());
		int progDuration=defCalendarTimes.getDuration();
		
			
			if(leftProgCount<periodCount)
				periodCount=leftProgCount;
			
			Date startDate=scheduleTimePlan.getPlanStartDate();
			List<Date> dates=getStudioPlanDayArrays(startDate, scheduleTimePlan.getPeriodicTimePlans(), periodCount);
			for (Date date : dates) {
				ScheduleTimePlan schTP=(ScheduleTimePlan)scheduleTimePlan.clone();
				
				schTP.setPlanStartDate(date);
				Date endDate=(Date)date.clone();
				endDate=OhbeUtil.getDateForNextMinute(endDate, progDuration);
				schTP.setPlanEndDate(endDate);
				generatedSTPs.add(schTP);
			}
			
			
			return generatedSTPs;
	}
	
	
	
	
	private List<Date> getStudioPlanDayArrays(Date startDate,List<PeriodicTimePlan> periodicTimePlans,int dpAdet){
		Calendar startPoint=Calendar.getInstance();
		startPoint.setTime((Date)startDate.clone());
		
		//String[] gunArr=gunler.split("#");
		
		boolean monday=false;
		boolean tuesday=false;
		boolean wednesday=false;
		boolean thursday=false;
		boolean friday=false;
		boolean saturday=false;
		boolean sunday=false;
		
		String mondayTime="";
		String tuesdayTime="";
		String wednesdayTime="";
		String thursdayTime="";
		String fridayTime="";
		String saturdayTime="";
		String sundayTime="";
	
		
		for (PeriodicTimePlan scheduleTimeObj : periodicTimePlans) {
	        String gun=""+scheduleTimeObj.getProgDay();
	        String times=scheduleTimeObj.getProgStartTime();
	        if(gun.equals("1")){
	        	monday=true;
	        	mondayTime=times;
	        }else if(gun.equals("2")){
	        	tuesday=true;
	        	tuesdayTime=times;
	        }else if(gun.equals("3")){
	        	wednesday=true;
	        	wednesdayTime=times;
	        }else if(gun.equals("4")){
	        	thursday=true;
	        	thursdayTime=times;
	        }else if(gun.equals("5")){
	        	friday=true;
	        	fridayTime=times;
	        }else if(gun.equals("6")){
	        	saturday=true;
	        	saturdayTime=times;
	        }else if(gun.equals("7")){
	        	sunday=true;
	        	sundayTime=times;
	        }
	        
	        
	        
        }
		
		
		List<Date> gunlerList=new ArrayList<Date>();
		int dersSayisi=dpAdet;
		int dateCounter=0;
		for (int i = 0; i < dpAdet; i++) {
	        
			   while(true){
				   if(dateCounter>0)
					   startPoint.add(Calendar.DATE, 1);
				   
				   String day=TimeTypes.getDateStrByFormatEEEByTurkish(new Date(startPoint.getTimeInMillis()));
				   if(day.equals(TimeTypes.TIME_Pzt) && monday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
					   if(!mondayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate, mondayTime);
					   }
             		   gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Sal) && tuesday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!tuesdayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,tuesdayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Car) && wednesday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!wednesdayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,wednesdayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Per) && thursday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!thursdayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,thursdayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Cum) && friday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!fridayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,fridayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Cmt) && saturday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		   
             		  if(!saturdayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,saturdayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Paz) && sunday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!sundayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,sundayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }
				   
				   dateCounter=dateCounter+1;
				   
			   }
			   dersSayisi--;
      		   if(dersSayisi==0)
      			  break;
		}
		return gunlerList;
	}
	
	
	/**
	 * 
	 * @param scheduleCalendarObj
	 * @comment this private method generate calendar dates. It can be daily, weekly and monthly. ScheduleCalendarObj abject attribute of duration is 1 for daily, 7 for weekly and 30 for monthly.
	 *          In shortly, duration attributes sets day count. 
	 * @return List<ScheduleTimeObj>
	 */
	private List<ScheduleTimeObj> generateScheduleTimeObj(ScheduleCalendarObj scheduleCalendarObj){
		int duration=scheduleCalendarObj.getDayDuration();
		List<ScheduleTimeObj> scheduleTimeObjs=new ArrayList<ScheduleTimeObj>();
		for (int i = 0; i < duration; i++) {
			Date startDate=(Date)OhbeUtil.getDateForNextDate(scheduleCalendarObj.getCalendarDate(), i).clone();
			ScheduleTimeObj scheduleTimeObj=new ScheduleTimeObj();
			scheduleTimeObj.setCalendarDate(startDate);
			scheduleTimeObj.setCalendarDateName(DateTimeUtil.getDayNames(startDate));
			scheduleTimeObjs.add(scheduleTimeObj);
		}
		return scheduleTimeObjs;
	}
	
}
