package tr.com.beinplanner.schedule.dao;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.user.dao.User;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({@JsonSubTypes.Type(value = ScheduleUsersClassPlan.class, name = "sucp"),
			   @JsonSubTypes.Type(value = ScheduleMembershipPlan.class, name = "smp"),
			   @JsonSubTypes.Type(value = ScheduleUsersPersonalPlan.class, name = "supp")})
public abstract class ScheduleFactory {

	

	
	
	
	
	
}
