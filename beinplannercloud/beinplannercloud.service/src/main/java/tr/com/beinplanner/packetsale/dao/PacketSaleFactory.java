package tr.com.beinplanner.packetsale.dao;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import tr.com.beinplanner.diabet.dao.UserDiabet;
import tr.com.beinplanner.diabet.dao.UserDiabetCalori;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.sport.dao.UserSportProgram;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="progType")
@JsonSubTypes({@JsonSubTypes.Type(value = PacketSalePersonal.class, name = "psp"),
			   @JsonSubTypes.Type(value = PacketSaleClass.class, name = "psc"),
			   @JsonSubTypes.Type(value = PacketSaleMembership.class, name = "psm")})
public abstract class PacketSaleFactory {

	private Date 	salesDate;

	private List<ScheduleFactory> scheduleFactory;

	private UserDiabetCalori userDiabetCalori;
	
	private List<UserDiabet> userDiabets;
	
	private List<UserSportProgram> userSportPrograms;
	
	
	
	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public List<ScheduleFactory> getScheduleFactory() {
		return scheduleFactory;
	}

	public void setScheduleFactory(List<ScheduleFactory> scheduleFactory) {
		this.scheduleFactory = scheduleFactory;
	}

	public List<UserDiabet> getUserDiabets() {
		return userDiabets;
	}

	public void setUserDiabets(List<UserDiabet> userDiabets) {
		this.userDiabets = userDiabets;
	}

	public List<UserSportProgram> getUserSportPrograms() {
		return userSportPrograms;
	}

	public void setUserSportPrograms(List<UserSportProgram> userSportPrograms) {
		this.userSportPrograms = userSportPrograms;
	}

	public UserDiabetCalori getUserDiabetCalori() {
		return userDiabetCalori;
	}

	public void setUserDiabetCalori(UserDiabetCalori userDiabetCalori) {
		this.userDiabetCalori = userDiabetCalori;
	}
	
	
	
	
	
	
	
}
