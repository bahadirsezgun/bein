package com.beinplanner.contollers.mobile.entity;

public class PsfMobile {

	private long 	saleId;
	private String 	salesDateStr;
	private String  salesDay;
	private String  salesDayName;
	private String  progName;
	private String  progType;
	
	private double  dept;
	private String  startDate;
	private String  endDate;
	private int 	leftClassCount;
	private int 	leftDayCount;
	private String  ptCurrency;
	private String  dateFormat;
	private int 	active;
	
	public long getSaleId() {
		return saleId;
	}
	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}
	public String getSalesDateStr() {
		return salesDateStr;
	}
	public void setSalesDateStr(String salesDateStr) {
		this.salesDateStr = salesDateStr;
	}
	public String getSalesDay() {
		return salesDay;
	}
	public void setSalesDay(String salesDay) {
		this.salesDay = salesDay;
	}
	public String getSalesDayName() {
		return salesDayName;
	}
	public void setSalesDayName(String salesDayName) {
		this.salesDayName = salesDayName;
	}
	public String getProgName() {
		return progName;
	}
	public void setProgName(String progName) {
		this.progName = progName;
	}
	public String getProgType() {
		return progType;
	}
	public void setProgType(String progType) {
		this.progType = progType;
	}
	public double getDept() {
		return dept;
	}
	public void setDept(double dept) {
		this.dept = dept;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getLeftClassCount() {
		return leftClassCount;
	}
	public void setLeftClassCount(int leftClassCount) {
		this.leftClassCount = leftClassCount;
	}
	public int getLeftDayCount() {
		return leftDayCount;
	}
	public void setLeftDayCount(int leftDayCount) {
		this.leftDayCount = leftDayCount;
	}
	public String getPtCurrency() {
		return ptCurrency;
	}
	public void setPtCurrency(String ptCurrency) {
		this.ptCurrency = ptCurrency;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	
	
	
	
}
