package com.beinplanner.contollers.booking.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.definition.dao.DefCalendarTimes;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.packetsale.dao.PacketSaleMembership;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.program.dao.ProgramClass;
import tr.com.beinplanner.program.dao.ProgramPersonal;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.businessEntity.ScheduleCalendarObj;
import tr.com.beinplanner.schedule.businessEntity.ScheduleTimeObj;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.service.IScheduleService;
import tr.com.beinplanner.schedule.service.ScheduleClassService;
import tr.com.beinplanner.schedule.service.SchedulePersonalService;
import tr.com.beinplanner.schedule.service.ScheduleService;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.OhbeUtil;
import tr.com.beinplanner.util.ProgramTypes;
import tr.com.beinplanner.util.UserTypes;

@RestController
@RequestMapping("/bein/private/booking")
public class PrivateBookingController {


	@Autowired
	LoginSession loginSession;
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	ScheduleClassService scheduleClassService;
	
	@Autowired
	SchedulePersonalService schedulePersonalService;
	
	IScheduleService iScheduleService;
	
	
	
	@Autowired
	UserService userService;
	
	@Autowired
	DefinitionService definitionService;
	
	
	@RequestMapping(value="/generateScheduleTPBySaledPacket", method = RequestMethod.POST) 
	public ScheduleTimePlan	findSaledPacket(@RequestBody PacketSaleFactory psf){
		
		if(psf instanceof PacketSalePersonal) {
			iScheduleService=schedulePersonalService;
		}else if (psf instanceof PacketSaleClass) {
			iScheduleService=scheduleClassService;
		}
		
		return iScheduleService.findScheduleTimePlanBySaleId(psf);
	
	}
	
	/**
	 *
	 * @param scheduleTimePlan
	 * @comment Creating of Schedule time plan by the calendar page. This method (createScheduleTimePlan) will control this creation is suiatbale or not.
	 *          If it is suitable than schedule time plan is going to be created by. </br>
	 *          This process control,</br>
	 *          1-Trainer has a class at the same time </br>
	 *          2-Schedule time plan is binded to Schedule plan than </br>
	 *          	2.1 Schedule Plan is able to create new plan or the plan is finished or not.
	 *              2.2 Member has saled packet by its own sales count. Control the member that she gets more time from her token</br>
	 *          3-The member has saled packet, if is not then , program automaticly sale related packet to him.</br>
	 *          Note: Plan end time set by this method. Page will send only start time of the booking.
	 * @return HmiResultObj </br>
	 *         <table >
	 *         <tr>
	 *         <td style='border:1px solid black'>ResultStatu</td>
	 *         <td style='border:1px solid black'>ResultMessage</td>
	 *         <td style='border:1px solid black'>ResultObj</td>
	 *         </tr>
	 *         <tr>
	 *         <td style='border:1px solid black'>Success/Fail</td>
	 *         <td style='border:1px solid black'>Success/Fail</td>
	 *         <td style='border:1px solid black'>ScheduleTimePlanObj</td>
	 *         </tr>
	 *         </table>
	 */
	@PostMapping(value="/createScheduleTimePlan")
	public HmiResultObj createScheduleTimePlan(@RequestBody ScheduleTimePlan scheduleTimePlan) {
		if(scheduleTimePlan.getProgramFactory() instanceof ProgramClass)
	    	iScheduleService=scheduleClassService;
	    else if (scheduleTimePlan.getProgramFactory() instanceof ProgramPersonal)
	    	iScheduleService=schedulePersonalService;
		
		DefCalendarTimes defCalendarTimes= definitionService.findCalendarTimes(loginSession.getUser().getFirmId());
		scheduleTimePlan.setPlanEndDate(OhbeUtil.getDateForNextMinute(((Date)scheduleTimePlan.getPlanStartDate().clone()),defCalendarTimes.getDuration()));
		
		return iScheduleService.createPlan(scheduleTimePlan);
	}
	
	/**
	 *
	 * @param scheduleTimePlan
	 * @comment Schedule time plan try to change on the calendar page than updateScheduleDate method will control this change is suiatbale or not.
	 *          If it is suitable than schedule time plan is going to be change by. This method only changes time and trainer nothing else.
	 * @return
	 */
	@PostMapping(value="/updateScheduleDate")
	public HmiResultObj updateScheduleTimePlan(@RequestBody ScheduleTimePlan scheduleTimePlan) {
		// TODO Schedule Time plan will be updated after drag and drop on calendar page. Implementation did not started yet.
		HmiResultObj hmiResultObj=new HmiResultObj();
	
		return hmiResultObj;
		
	}
	
	
	@PostMapping(value="/findTimes")
	public List<String> findTimes() {
		
		List<String> allDayTimes=new ArrayList<String>();
		
		DefCalendarTimes defCalendarTimes= definitionService.findCalendarTimes(loginSession.getUser().getFirmId());
		
		
		int startTimeCal=Integer.parseInt(defCalendarTimes.getStartTime().replace(":", ""));
		int endTimeCal=Integer.parseInt(defCalendarTimes.getEndTime().replace(":", ""));
		if(endTimeCal==0){
			endTimeCal=2400;
		}
		int duration=defCalendarTimes.getCalPeriod();		
		int classDuration=defCalendarTimes.getDuration();		
		int sequence=classDuration/duration;
		int loopCount=24*60/duration;
		
		Calendar startTimeCalendar=Calendar.getInstance();
		startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[0]));
		startTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[1]));
		startTimeCalendar.set(Calendar.SECOND, 0);
		startTimeCalendar.set(Calendar.MILLISECOND, 0);
		
		
		for (int i = 0; i < loopCount; i++) {
			int startTime=startTimeCal+i*duration;
			int timeMinute=startTime-startTimeCal;
			
			Calendar sequenceTimeCalendar=Calendar.getInstance();
			sequenceTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
			sequenceTimeCalendar.set(Calendar.MINUTE, (startTimeCalendar.get(Calendar.MINUTE)+timeMinute));
			sequenceTimeCalendar.set(Calendar.SECOND, 0);
			sequenceTimeCalendar.set(Calendar.MILLISECOND, 0);
			
			
			String generatedMinute=sequenceTimeCalendar.get(Calendar.MINUTE)<10?"0"+sequenceTimeCalendar.get(Calendar.MINUTE):""+sequenceTimeCalendar.get(Calendar.MINUTE);
			
			String generatedHour=sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY)==0?"24":""+sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY);
			int generatedStartTime=Integer.parseInt(generatedHour+""+generatedMinute);
			
			String generatedTime="";
			String saatG="";
			String dakikaG="";
			
			if(generatedStartTime<1000){
				saatG="0"+(""+generatedStartTime).substring(0,1);
				dakikaG=(""+generatedStartTime).substring(1,3);
			}else{
				saatG=(""+generatedStartTime).substring(0,2);
				dakikaG=(""+generatedStartTime).substring(2,4);
			}
			generatedTime=saatG+":"+dakikaG;
			if(generatedStartTime>=startTimeCal && generatedStartTime<=endTimeCal) 
				allDayTimes.add(generatedTime);
			
		}
		
		return allDayTimes;
	}
	
	
	
	@PostMapping(value="/findAllPlanByDate")
	public List<ScheduleTimeObj> findAllPlanByDate(@RequestBody ScheduleCalendarObj scheduleCalendarObj) {
	    
		
		List<ScheduleTimeObj> scheduleTimeObjs=generateScheduleTimeObj(scheduleCalendarObj);
		List<User> users= userService.findAllByFirmIdAndUserType(loginSession.getUser().getFirmId(), UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT);
		List<String> times=findTimes();
		
		scheduleTimeObjs.forEach(sto->{
			if(sto.getStaffs()==null) {
				sto.setStaffs(new ArrayList<>());
			}
		  for (User user : users) { 
			  User userInTimePlan=null;
			    try {
				   userInTimePlan=(User)user.clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			  
			   if(userInTimePlan.getScheduleTimePlans()==null)
				   userInTimePlan.setScheduleTimePlans(new ArrayList<>());
			      
				   
				for(String t : times)  { 
					Date controlDate=DateTimeUtil.setHourMinute((Date)sto.getCalendarDate().clone(),t);
					ScheduleTimePlan stpfc=scheduleClassService.findScheduleTimePlanPlanByDateTimeForStaff(userInTimePlan.getUserId(), controlDate);
				    if(stpfc!=null) {
				    	stpfc.setScheduleFactories(scheduleClassService.findScheduleUsersPlanBySchtId(stpfc.getSchtId()));
				    	userInTimePlan.getScheduleTimePlans().add(stpfc);
				    }else {
				    	 ScheduleTimePlan stpfp=schedulePersonalService.findScheduleTimePlanPlanByDateTimeForStaff(userInTimePlan.getUserId(), controlDate);
						 if(stpfp!=null) {
							   stpfp.setScheduleFactories(schedulePersonalService.findScheduleUsersPlanBySchtId(stpfp.getSchtId()));
							   userInTimePlan.getScheduleTimePlans().add(stpfp);
						 }else {
							 ScheduleTimePlan scheduleTimePlan=new ScheduleTimePlan();
							 scheduleTimePlan.setPlanStartDate(controlDate);
							 scheduleTimePlan.setStaff(user);
							 userInTimePlan.getScheduleTimePlans().add(scheduleTimePlan);
							 
						 }
				    }
				}
			   
			   sto.getStaffs().add(userInTimePlan);
			}   
		});		
			
			
		return scheduleTimeObjs;
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
