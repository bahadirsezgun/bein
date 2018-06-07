package tr.com.beinplanner.zms.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="zms_stock")
public class ZmsStock {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="STK_IDX")
	private long stkIdx;
	
	@JsonIgnore
	@Column(name="FIRM_ID")
	private int firmId;
	
	@Column(name="STK_YEAR")
	private int stkYear;
	
	
	@Column(name="PRODUCT_ID")
	private long productId;
	
	@Column(name="STOCK_IN_COUNT")
	private int stockInCount;
	
	@Column(name="STOCK_OUT_COUNT")
	private int stockOutCount;

	@Transient
	private ZmsProduct zmsProduct;
	
	public long getStkIdx() {
		return stkIdx;
	}

	public void setStkIdx(long stkIdx) {
		this.stkIdx = stkIdx;
	}

	

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getStockInCount() {
		return stockInCount;
	}

	public void setStockInCount(int stockInCount) {
		this.stockInCount = stockInCount;
	}

	public int getStockOutCount() {
		return stockOutCount;
	}

	public void setStockOutCount(int stockOutCount) {
		this.stockOutCount = stockOutCount;
	}

	public ZmsProduct getZmsProduct() {
		return zmsProduct;
	}

	public void setZmsProduct(ZmsProduct zmsProduct) {
		this.zmsProduct = zmsProduct;
	}

	public int getFirmId() {
		return firmId;
	}

	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}

	public int getStkYear() {
		return stkYear;
	}

	public void setStkYear(int stkYear) {
		this.stkYear = stkYear;
	}
	
	
	
}
