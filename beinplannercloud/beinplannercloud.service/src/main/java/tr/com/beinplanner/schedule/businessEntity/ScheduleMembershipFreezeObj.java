package tr.com.beinplanner.schedule.businessEntity;

import java.util.Date;

public class ScheduleMembershipFreezeObj {

	private Date smpStartDate;
	private long smpId;
	private String smpComment;
	private int freezeDuration;
	
	public Date getSmpStartDate() {
		return smpStartDate;
	}
	public void setSmpStartDate(Date smpStartDate) {
		this.smpStartDate = smpStartDate;
	}
	public long getSmpId() {
		return smpId;
	}
	public void setSmpId(long smpId) {
		this.smpId = smpId;
	}
	public String getSmpComment() {
		return smpComment;
	}
	public void setSmpComment(String smpComment) {
		this.smpComment = smpComment;
	}
	public int getFreezeDuration() {
		return freezeDuration;
	}
	public void setFreezeDuration(int freezeDuration) {
		this.freezeDuration = freezeDuration;
	}
	
	
	
}
