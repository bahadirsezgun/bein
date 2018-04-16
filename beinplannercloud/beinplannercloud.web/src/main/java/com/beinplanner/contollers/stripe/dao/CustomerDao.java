package com.beinplanner.contollers.stripe.dao;

import java.util.List;

public class CustomerDao {
	private String id;
	private long accountBalance;
	private String currency;
	private String email;
	
	private List<SubscriptionDao> subscriptionDaos;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(long accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<SubscriptionDao> getSubscriptionDaos() {
		return subscriptionDaos;
	}

	public void setSubscriptionDaos(List<SubscriptionDao> subscriptionDaos) {
		this.subscriptionDaos = subscriptionDaos;
	}
	
	
}
