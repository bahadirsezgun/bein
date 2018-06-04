package tr.com.beinplanner.zms.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="zms_payment")
public class ZmsPayment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PAY_IDX")
	private long payIdx;
	
	@Column(name="STK_IDX")
	private long stkIdx;
	
	@Column(name="PAY_AMOUNT")
	private double payAmount;

	@Column(name="PAY_DATE")
	private Date payDate;
	
	@Column(name="PAY_CONFIRM")
	private int payConfirm;
	
	@Column(name="STAFF_ID")
	private long staffId;
	
	@Column(name="PAY_TYPE")
	private int payType;

	public long getPayIdx() {
		return payIdx;
	}

	public void setPayIdx(long payIdx) {
		this.payIdx = payIdx;
	}

	public double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public int getPayConfirm() {
		return payConfirm;
	}

	public void setPayConfirm(int payConfirm) {
		this.payConfirm = payConfirm;
	}

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public long getStkIdx() {
		return stkIdx;
	}

	public void setStkIdx(long stkIdx) {
		this.stkIdx = stkIdx;
	}


	
	
	
	
	
}
