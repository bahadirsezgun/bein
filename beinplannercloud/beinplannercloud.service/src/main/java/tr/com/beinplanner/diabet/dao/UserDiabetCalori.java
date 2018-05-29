package tr.com.beinplanner.diabet.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_diabet_calori")
public class UserDiabetCalori {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UDC_ID")
	private long udcId;
	
	@Column(name="USER_ID")
	private long userId;
	
	@Column(name="CAL_AMOUNT")
	private double calAmount;
	
	@Column(name="SALE_ID")
	private long saleId;
	
	@Column(name="SALE_TYPE")
	private String saleType;

	public long getUdcId() {
		return udcId;
	}

	public void setUdcId(long udcId) {
		this.udcId = udcId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getCalAmount() {
		return calAmount;
	}

	public void setCalAmount(double calAmount) {
		this.calAmount = calAmount;
	}

	public long getSaleId() {
		return saleId;
	}

	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	
	
	
}
