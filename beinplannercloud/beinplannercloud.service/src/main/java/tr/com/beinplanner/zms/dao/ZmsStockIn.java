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

@Entity
@Table(name="zms_stock_in")
public class ZmsStockIn {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="STK_IDX")
	private long stkIdx;
	
	
	@Column(name="PRODUCT_ID")
	private long productId;
	
	@Column(name="STOCK_PRICE")
	private double stockPrice;
	
	@Column(name="STOCK_COUNT")
	private int stockCount;
	
	@Column(name="STOCK_IN_DATE",nullable=true)
	private Date stockInDate;
	
	
	@Column(name="STOCK_COMMENT")
	private String stockComment;
	
	@Column(name="STAFF_ID")
	private long staffId;

	
	
	
	@Transient
	private String productUnit;

	@Transient
	private String productName;

	@JsonIgnore
	@Transient
	private int firmId;

	
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}

	public int getStockCount() {
		return stockCount;
	}

	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

	public Date getStockInDate() {
		return stockInDate;
	}

	public void setStockInDate(Date stockInDate) {
		this.stockInDate = stockInDate;
	}

	public String getStockComment() {
		return stockComment;
	}

	public void setStockComment(String stockComment) {
		this.stockComment = stockComment;
	}

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public long getStkIdx() {
		return stkIdx;
	}

	public void setStkIdx(long stkIdx) {
		this.stkIdx = stkIdx;
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
	
	
	
}
