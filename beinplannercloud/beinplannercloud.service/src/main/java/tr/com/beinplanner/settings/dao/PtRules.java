package tr.com.beinplanner.settings.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="pt_rules")
public class PtRules {

	@Id
	@Column(name="PTR_ID")
	private int ptrId;
	
	public int getPtrId() {
		return ptrId;
	}
	public void setPtrId(int ptrId) {
		this.ptrId = ptrId;
	}
	@Column(name="RULE_ID")
	private int ruleId;
	
	@Column(name="FIRM_ID")
	private int firmId;
	
	
	@Column(name="RULE_NAME")
	private String ruleName;
	
	
	@Column(name="RULE_VALUE")
	private int ruleValue;
	
	
	public int getRuleId() {
		return ruleId;
	}
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public int getRuleValue() {
		return ruleValue;
	}
	public void setRuleValue(int ruleValue) {
		this.ruleValue = ruleValue;
	}
	public int getFirmId() {
		return firmId;
	}
	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}
	
	
}
