package tr.com.beinplanner.definition.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="def_test")
public class DefTest {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TEST_ID")
	private long testId;
	
	@JsonIgnore
	@Column(name="FIRM_ID")
	private int firmId;
	
	@Column(name="TEST_NAME")
	private String testName;

	public long getTestId() {
		return testId;
	}

	public void setTestId(long testId) {
		this.testId = testId;
	}

	public int getFirmId() {
		return firmId;
	}

	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	
	
	
	
}
