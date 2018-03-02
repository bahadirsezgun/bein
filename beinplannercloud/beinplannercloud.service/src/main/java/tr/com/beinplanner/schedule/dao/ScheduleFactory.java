package tr.com.beinplanner.schedule.dao;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import tr.com.beinplanner.user.dao.User;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({@JsonSubTypes.Type(value = ScheduleUsersClassPlan.class, name = "sucp"),
			   @JsonSubTypes.Type(value = ScheduleMembershipPlan.class, name = "smp"),
			   @JsonSubTypes.Type(value = ScheduleUsersPersonalPlan.class, name = "supp")})
public abstract class ScheduleFactory implements Cloneable{

	
	@Override
	public Object clone()  {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private Date planStartDate;
	private Date planEndDate;

	public Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}
	
	
	
	
	
}
