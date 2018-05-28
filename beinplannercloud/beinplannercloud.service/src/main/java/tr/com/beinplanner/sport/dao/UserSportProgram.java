package tr.com.beinplanner.sport.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="user_sport_program")
public class UserSportProgram {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="USP_ID")
	private long uspId;
	
	@Column(name="USER_ID")
	private long userId;
	
	@Column(name="SP_ID")
	private long spId;
	@Transient
	private String spName;
	
	
	
	@Column(name="SPD_ID")
	private long spdId;
	@Transient
	private String spdName;
	
	@Column(name="APPLY_DATE")
	private int applyDate;
	
	@Transient
	private String applyDateName;
	
	@Column(name="USP_COMMENT")
	private String uspComment;
	
	@Column(name="SALE_ID")
	private long saleId;
	
	@Column(name="SALE_TYPE")
	private String saleType;

	
	
	public long getUspId() {
		return uspId;
	}

	public void setUspId(long uspId) {
		this.uspId = uspId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getSpId() {
		return spId;
	}

	public void setSpId(long spId) {
		this.spId = spId;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public long getSpdId() {
		return spdId;
	}

	public void setSpdId(long spdId) {
		this.spdId = spdId;
	}

	public String getSpdName() {
		return spdName;
	}

	public void setSpdName(String spdName) {
		this.spdName = spdName;
	}

	public int getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(int applyDate) {
		this.applyDate = applyDate;
	}

	public String getApplyDateName() {
		return applyDateName;
	}

	public void setApplyDateName(String applyDateName) {
		this.applyDateName = applyDateName;
	}

	public String getUspComment() {
		return uspComment;
	}

	public void setUspComment(String uspComment) {
		this.uspComment = uspComment;
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
