package tr.com.beinplanner.diabet.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="user_diabet")
public class UserDiabet {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="DIA_ID")
	private long diaId;
	
	@Column(name="UDC_ID")
	private long udcId;
	
	@Transient
	private double calAmount;
	
	@Transient
	private long saleId;
	
	@Transient
	private String saleType;
	
	@Transient
	private long userId;
	
	@Column(name="DIA_DATE")
	private Date diaDate;
	
	@Transient
	private String diaDateName;
	
	
	@Column(name="DIA_MORNING_CAL")
	private double diaMorningCal;
	
	@Column(name="DIA_MORNING_PROTEIN")
	private double diaMorningProtein;
	
	@Column(name="DIA_MORNING_COMMENT")
	private String diaMorningComment;
	
	
	@Column(name="DIA_MORNING_GAP_CAL")
	private double diaMorningGapCal;
	
	@Column(name="DIA_MORNING_GAP_PROTEIN")
	private double diaMorningGapProtein;
	
	@Column(name="DIA_MORNING_GAP_COMMENT")
	private String diaMorningGapComment;
	
	
	
	
	@Column(name="DIA_AFTERNOON_CAL")
	private double diaAfternoonCal;
	
	@Column(name="DIA_AFTERNOON_PROTEIN")
	private double diaAfternoonProtein;
	
	@Column(name="DIA_AFTERNOON_COMMENT")
	private String diaAfternoonComment;
	
	
	@Column(name="DIA_AFTERNOON_GAP_CAL")
	private double diaAfternoonGapCal;
	
	@Column(name="DIA_AFTERNOON_GAP_PROTEIN")
	private double diaAfternoonGapProtein;
	
	@Column(name="DIA_AFTERNOON_GAP_COMMENT")
	private String diaAfternoonGapComment;
	
	
	@Column(name="DIA_NIGHT_CAL")
	private double diaNightCal;
	
	@Column(name="DIA_NIGHT_PROTEIN")
	private double diaNightProtein;
	
	@Column(name="DIA_NIGHT_COMMENT")
	private String diaNightComment;
	
	
	@Column(name="DIA_NIGHT_GAP_CAL")
	private double diaNightGapCal;
	
	@Column(name="DIA_NIGHT_GAP_PROTEIN")
	private double diaNightGapProtein;
	
	@Column(name="DIA_NIGHT_GAP_COMMENT")
	private String diaNightGapComment;

	public long getDiaId() {
		return diaId;
	}

	public void setDiaId(long diaId) {
		this.diaId = diaId;
	}

	public long getUdcId() {
		return udcId;
	}

	public void setUdcId(long udcId) {
		this.udcId = udcId;
	}

	public Date getDiaDate() {
		return diaDate;
	}

	public void setDiaDate(Date diaDate) {
		this.diaDate = diaDate;
	}

	public double getDiaMorningCal() {
		return diaMorningCal;
	}

	public void setDiaMorningCal(double diaMorningCal) {
		this.diaMorningCal = diaMorningCal;
	}

	public double getDiaMorningProtein() {
		return diaMorningProtein;
	}

	public void setDiaMorningProtein(double diaMorningProtein) {
		this.diaMorningProtein = diaMorningProtein;
	}

	public String getDiaMorningComment() {
		return diaMorningComment;
	}

	public void setDiaMorningComment(String diaMorningComment) {
		this.diaMorningComment = diaMorningComment;
	}

	public double getDiaMorningGapCal() {
		return diaMorningGapCal;
	}

	public void setDiaMorningGapCal(double diaMorningGapCal) {
		this.diaMorningGapCal = diaMorningGapCal;
	}

	public double getDiaMorningGapProtein() {
		return diaMorningGapProtein;
	}

	public void setDiaMorningGapProtein(double diaMorningGapProtein) {
		this.diaMorningGapProtein = diaMorningGapProtein;
	}

	public String getDiaMorningGapComment() {
		return diaMorningGapComment;
	}

	public void setDiaMorningGapComment(String diaMorningGapComment) {
		this.diaMorningGapComment = diaMorningGapComment;
	}

	public double getDiaAfternoonCal() {
		return diaAfternoonCal;
	}

	public void setDiaAfternoonCal(double diaAfternoonCal) {
		this.diaAfternoonCal = diaAfternoonCal;
	}

	public double getDiaAfternoonProtein() {
		return diaAfternoonProtein;
	}

	public void setDiaAfternoonProtein(double diaAfternoonProtein) {
		this.diaAfternoonProtein = diaAfternoonProtein;
	}

	public String getDiaAfternoonComment() {
		return diaAfternoonComment;
	}

	public void setDiaAfternoonComment(String diaAfternoonComment) {
		this.diaAfternoonComment = diaAfternoonComment;
	}

	public double getDiaAfternoonGapCal() {
		return diaAfternoonGapCal;
	}

	public void setDiaAfternoonGapCal(double diaAfternoonGapCal) {
		this.diaAfternoonGapCal = diaAfternoonGapCal;
	}

	public double getDiaAfternoonGapProtein() {
		return diaAfternoonGapProtein;
	}

	public void setDiaAfternoonGapProtein(double diaAfternoonGapProtein) {
		this.diaAfternoonGapProtein = diaAfternoonGapProtein;
	}

	public String getDiaAfternoonGapComment() {
		return diaAfternoonGapComment;
	}

	public void setDiaAfternoonGapComment(String diaAfternoonGapComment) {
		this.diaAfternoonGapComment = diaAfternoonGapComment;
	}

	public double getDiaNightCal() {
		return diaNightCal;
	}

	public void setDiaNightCal(double diaNightCal) {
		this.diaNightCal = diaNightCal;
	}

	public double getDiaNightProtein() {
		return diaNightProtein;
	}

	public void setDiaNightProtein(double diaNightProtein) {
		this.diaNightProtein = diaNightProtein;
	}

	public String getDiaNightComment() {
		return diaNightComment;
	}

	public void setDiaNightComment(String diaNightComment) {
		this.diaNightComment = diaNightComment;
	}

	public double getDiaNightGapCal() {
		return diaNightGapCal;
	}

	public void setDiaNightGapCal(double diaNightGapCal) {
		this.diaNightGapCal = diaNightGapCal;
	}

	public double getDiaNightGapProtein() {
		return diaNightGapProtein;
	}

	public void setDiaNightGapProtein(double diaNightGapProtein) {
		this.diaNightGapProtein = diaNightGapProtein;
	}

	public String getDiaNightGapComment() {
		return diaNightGapComment;
	}

	public void setDiaNightGapComment(String diaNightGapComment) {
		this.diaNightGapComment = diaNightGapComment;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getDiaDateName() {
		return diaDateName;
	}

	public void setDiaDateName(String diaDateName) {
		this.diaDateName = diaDateName;
	}
	
	
	
	
	
}
