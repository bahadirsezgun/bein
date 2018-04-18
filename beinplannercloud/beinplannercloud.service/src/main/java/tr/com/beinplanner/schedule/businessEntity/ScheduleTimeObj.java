package tr.com.beinplanner.schedule.businessEntity;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.user.dao.User;

public class ScheduleTimeObj {

	
	public User staff;
	
	public List<User> getStaffs() {
		return staffs;
	}

	public void setStaffs(List<User> staffs) {
		this.staffs = staffs;
	}

	private Date calendarDate;
	private String calendarDateName;
	
	
	private ScheduleTimePlan scheduleTimePlan;
	
	
	

	/**
	 * @comment This attribute is for calendar times of scheduler staff.
	 */
	@Transient
	private List<User> staffs;
	


	
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

	public User getStaff() {
		return staff;
	}

	public void setStaff(User staff) {
		this.staff = staff;
	}

	public ScheduleTimePlan getScheduleTimePlan() {
		return scheduleTimePlan;
	}

	public void setScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		this.scheduleTimePlan = scheduleTimePlan;
	}

	
	
	
	
}
