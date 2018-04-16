package com.beinplanner.contollers.stripe.dao;

import java.util.Date;
import java.util.List;

public class SubscriptionDao {

	private String id;
	private String billing;
	private long billing_cycle_anchor;
	private boolean cancel_at_period_end;
	private String canceled_at;
	private long created;
	private Date createdDate;
	private long currentPeriodEnd;
	private Date currentPeriodEndDate;
	private long currentPeriodStart;
	private Date currentPeriodStartDate;
	private String customer;
	private List<PlanDao> planDaos;
	private PlanDao planDao;
	private int quantity;
	private long start;
	private long trialEnd;
	private long trialStart;
	
	private Date startDate;
	private Date trialEndDate;
	private Date trialStartDate;
	private String status;
	
	private CouponDao couponDao;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public long getBilling_cycle_anchor() {
		return billing_cycle_anchor;
	}

	public void setBilling_cycle_anchor(long billing_cycle_anchor) {
		this.billing_cycle_anchor = billing_cycle_anchor;
	}

	public boolean isCancel_at_period_end() {
		return cancel_at_period_end;
	}

	public void setCancel_at_period_end(boolean cancel_at_period_end) {
		this.cancel_at_period_end = cancel_at_period_end;
	}

	public String getCanceled_at() {
		return canceled_at;
	}

	public void setCanceled_at(String canceled_at) {
		this.canceled_at = canceled_at;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public long getCurrentPeriodEnd() {
		return currentPeriodEnd;
	}

	public void setCurrentPeriodEnd(long currentPeriodEnd) {
		this.currentPeriodEnd = currentPeriodEnd;
	}

	public Date getCurrentPeriodEndDate() {
		return currentPeriodEndDate;
	}

	public void setCurrentPeriodEndDate(Date currentPeriodEndDate) {
		this.currentPeriodEndDate = currentPeriodEndDate;
	}

	public long getCurrentPeriodStart() {
		return currentPeriodStart;
	}

	public void setCurrentPeriodStart(long currentPeriodStart) {
		this.currentPeriodStart = currentPeriodStart;
	}

	public Date getCurrentPeriodStartDate() {
		return currentPeriodStartDate;
	}

	public void setCurrentPeriodStartDate(Date currentPeriodStartDate) {
		this.currentPeriodStartDate = currentPeriodStartDate;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public List<PlanDao> getPlanDaos() {
		return planDaos;
	}

	public void setPlanDaos(List<PlanDao> planDaos) {
		this.planDaos = planDaos;
	}

	public PlanDao getPlanDao() {
		return planDao;
	}

	public void setPlanDao(PlanDao planDao) {
		this.planDao = planDao;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getTrialEnd() {
		return trialEnd;
	}

	public void setTrialEnd(long trialEnd) {
		this.trialEnd = trialEnd;
	}

	public long getTrialStart() {
		return trialStart;
	}

	public void setTrialStart(long trialStart) {
		this.trialStart = trialStart;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getTrialEndDate() {
		return trialEndDate;
	}

	public void setTrialEndDate(Date trialEndDate) {
		this.trialEndDate = trialEndDate;
	}

	public Date getTrialStartDate() {
		return trialStartDate;
	}

	public void setTrialStartDate(Date trialStartDate) {
		this.trialStartDate = trialStartDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CouponDao getCouponDao() {
		return couponDao;
	}

	public void setCouponDao(CouponDao couponDao) {
		this.couponDao = couponDao;
	}
	
	
}
