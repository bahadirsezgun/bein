package tr.com.beinplanner.schedule.businessEntity;

import java.util.Date;
import java.util.List;

import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;

public class ScheduleWeekTimeObj {

	private String 	time;
	private Date 	calendarDate;
	private String 	calendarDateName;
	
	
	private List<ScheduleTimeObj> scheduleTimeObjs;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Date getCalendarDate() {
		return calendarDate;
	}

	public void setCalendarDate(Date calendarDate) {
		this.calendarDate = calendarDate;
	}

	public String getCalendarDateName() {
		return calendarDateName;
	}

	public void setCalendarDateName(String calendarDateName) {
		this.calendarDateName = calendarDateName;
	}

	

	public List<ScheduleTimeObj> getScheduleTimeObjs() {
		return scheduleTimeObjs;
	}

	public void setScheduleTimeObjs(List<ScheduleTimeObj> scheduleTimeObjs) {
		this.scheduleTimeObjs = scheduleTimeObjs;
	}

	

	
	
	
}
