package com.beinplanner.contollers.stripeService.dao;

public class CouponDao {

	private String duration; //"forever","once","repeating"
	private String id;
	private int percentageOf;
	private int durationInMonths;
	private int amountOff;
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPercentageOf() {
		return percentageOf;
	}
	public void setPercentageOf(int percentageOf) {
		this.percentageOf = percentageOf;
	}
	public int getDurationInMonths() {
		return durationInMonths;
	}
	public void setDurationInMonths(int durationInMonths) {
		this.durationInMonths = durationInMonths;
	}
	public int getAmountOff() {
		return amountOff;
	}
	public void setAmountOff(int amountOff) {
		this.amountOff = amountOff;
	}
	
	
}
