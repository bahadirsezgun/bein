package tr.com.beinplanner.user.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_tests") 
public class UserTests {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TEST_SEQ")
	private long testSeq;
	
	@Column(name="USER_ID")
	private long userId;
	
	
	@Column(name="TEST_ID")
	private long testId;

	@Column(name="TEST_COMMENT")
	private String testComment;
	
	@Column(name="TEST_DATE")
	private Date testDate;

	
	@Transient
	private String testName;
	
	
	public long getTestSeq() {
		return testSeq;
	}

	public void setTestSeq(long testSeq) {
		this.testSeq = testSeq;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getTestId() {
		return testId;
	}

	public void setTestId(long testId) {
		this.testId = testId;
	}

	public String getTestComment() {
		return testComment;
	}

	public void setTestComment(String testComment) {
		this.testComment = testComment;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	
	
}
