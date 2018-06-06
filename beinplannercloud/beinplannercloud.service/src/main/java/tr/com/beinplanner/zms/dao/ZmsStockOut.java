package tr.com.beinplanner.zms.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tr.com.beinplanner.user.dao.User;

@Entity
@Table(name="zms_stock_out")
public class ZmsStockOut {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="STK_IDX")
	private long stkIdx;
	
	@Column(name="USER_ID")
	private long userId;
	
	
	@Column(name="PRODUCT_ID")
	private long productId;
	
	@Column(name="SELL_PRICE")
	private double sellPrice;
	
	@Column(name="SELL_COUNT")
	private int sellCount;
	
	@Column(name="SELL_OUT_DATE")
	private Date sellOutDate;
	
	
	@Column(name="SELL_COMMENT")
	private String sellComment;
	
	@Column(name="SELL_STATU")
	private int sellStatu;
	
	@Column(name="STAFF_ID")
	private long staffId;

	@Transient
	private String productUnit;

	@Transient
	private String productName;

	@JsonIgnore
	@Transient
	private int firmId;

	@Transient
	private User user;

	
	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public int getSellCount() {
		return sellCount;
	}

	public void setSellCount(int sellCount) {
		this.sellCount = sellCount;
	}

	public Date getSellOutDate() {
		return sellOutDate;
	}

	public void setSellOutDate(Date sellOutDate) {
		this.sellOutDate = sellOutDate;
	}

	public String getSellComment() {
		return sellComment;
	}

	public void setSellComment(String sellComment) {
		this.sellComment = sellComment;
	}

	public int getSellStatu() {
		return sellStatu;
	}

	public void setSellStatu(int sellStatu) {
		this.sellStatu = sellStatu;
	}

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getFirmId() {
		return firmId;
	}

	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getStkIdx() {
		return stkIdx;
	}

	public void setStkIdx(long stkIdx) {
		this.stkIdx = stkIdx;
	}
	
	
}
