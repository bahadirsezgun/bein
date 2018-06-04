package tr.com.beinplanner.zms.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="zms_stock_out_detail")
public class ZmsStockOutDetail {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="STKD_IDX")
	private long stkdIdx;
	
	@Column(name="STK_IDX")
	private long stkIdx;
	
	@Column(name="SELL_STATU")
	private int sellStatu;
	
	@Column(name="SELL_DATE")
	private Date sellDate;
	
	@Column(name="SELL_COUNT")
	private int sellCount;
	
	@Column(name="SELL_COMMENT")
	private String sellComment;
	
	
	@Column(name="STAFF_ID")
	private long staffId;

	

	public int getSellStatu() {
		return sellStatu;
	}

	public void setSellStatu(int sellStatu) {
		this.sellStatu = sellStatu;
	}

	public Date getSellDate() {
		return sellDate;
	}

	public void setSellDate(Date sellDate) {
		this.sellDate = sellDate;
	}

	public int getSellCount() {
		return sellCount;
	}

	public void setSellCount(int sellCount) {
		this.sellCount = sellCount;
	}

	public String getSellComment() {
		return sellComment;
	}

	public void setSellComment(String sellComment) {
		this.sellComment = sellComment;
	}

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public long getStkdIdx() {
		return stkdIdx;
	}

	public void setStkdIdx(long stkdIdx) {
		this.stkdIdx = stkdIdx;
	}

	public long getStkIdx() {
		return stkIdx;
	}

	public void setStkIdx(long stkIdx) {
		this.stkIdx = stkIdx;
	}
	
	

}
