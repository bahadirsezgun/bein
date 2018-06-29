package tr.com.beinplanner.measurement.dao;

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
@Table(name="user_measurement")
public class UserMeasurement {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FIT_SEQ")
	private long fitSeq;
	
	@Column(name="USER_ID")
	private long userId;
	
	
	@Column(name="BOY")
	private int boy;
	
	@Column(name="KILO")
	private double kilo;
	
	@Column(name="YAG")
	private double yag;
	
	@Column(name="MEAS_DATE")
	private Date measDate;
	
	@Transient
	private String measDateStr;
	
	
	@Column(name="FIT_ACIKLAMA")
	private String fitAciklama;
	
	@Column(name="KARIN")
	private double karin;
	
	@Column(name="KAS")
	private double kas;
	@Column(name="BASEN")
	private double basen;
	@Column(name="BICEPS")
	private double biceps;
	@Column(name="TRICEPS")
	private double triceps;
	@Column(name="GOGUS")
	private double gogus;
	@Column(name="BACAK")
	private double bacak;
	@Column(name="BILEK")
	private double bilek;
	@Column(name="BMI")
	private double bmi;
	@Column(name="BMR")
	private double bmr;
	
	@Transient
	private User user;

	public long getFitSeq() {
		return fitSeq;
	}

	public void setFitSeq(long fitSeq) {
		this.fitSeq = fitSeq;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	

	public int getBoy() {
		return boy;
	}

	public void setBoy(int boy) {
		this.boy = boy;
	}

	public double getKilo() {
		return kilo;
	}

	public void setKilo(double kilo) {
		this.kilo = kilo;
	}

	public double getYag() {
		return yag;
	}

	public void setYag(double yag) {
		this.yag = yag;
	}

	public Date getMeasDate() {
		return measDate;
	}

	public void setMeasDate(Date measDate) {
		this.measDate = measDate;
	}

	public String getFitAciklama() {
		return fitAciklama;
	}

	public void setFitAciklama(String fitAciklama) {
		this.fitAciklama = fitAciklama;
	}

	public double getKarin() {
		return karin;
	}

	public void setKarin(double karin) {
		this.karin = karin;
	}

	public double getKas() {
		return kas;
	}

	public void setKas(double kas) {
		this.kas = kas;
	}

	public double getBasen() {
		return basen;
	}

	public void setBasen(double basen) {
		this.basen = basen;
	}

	public double getBiceps() {
		return biceps;
	}

	public void setBiceps(double biceps) {
		this.biceps = biceps;
	}

	public double getTriceps() {
		return triceps;
	}

	public void setTriceps(double triceps) {
		this.triceps = triceps;
	}

	public double getGogus() {
		return gogus;
	}

	public void setGogus(double gogus) {
		this.gogus = gogus;
	}

	public double getBacak() {
		return bacak;
	}

	public void setBacak(double bacak) {
		this.bacak = bacak;
	}

	public double getBilek() {
		return bilek;
	}

	public void setBilek(double bilek) {
		this.bilek = bilek;
	}

	public double getBmi() {
		return bmi;
	}

	public void setBmi(double bmi) {
		this.bmi = bmi;
	}

	public double getBmr() {
		return bmr;
	}

	public void setBmr(double bmr) {
		this.bmr = bmr;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMeasDateStr() {
		return measDateStr;
	}

	public void setMeasDateStr(String measDateStr) {
		this.measDateStr = measDateStr;
	}
	
	
	
	
	
	
}
