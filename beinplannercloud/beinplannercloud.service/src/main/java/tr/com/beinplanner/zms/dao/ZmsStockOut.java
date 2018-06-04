package tr.com.beinplanner.zms.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="zms_stock_out")
public class ZmsStockOut {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SKT_IDX")
	private long sktIdx;
	
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

	public long getSktIdx() {
		return sktIdx;
	}

	public void setSktIdx(long sktIdx) {
		this.sktIdx = sktIdx;
	}

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
	
	
}
