package tr.com.beinplanner.definition.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="def_spor_program_device")
public class DefSporProgramDevice {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SPD_ID")
	private long spdId;
	
	@Column(name="SP_ID")
	private long spId;
	
	@Column(name="SPD_NAME")
	private String spdName;
	
	@Transient
	private String spName;

	public long getSpdId() {
		return spdId;
	}

	public void setSpdId(long spdId) {
		this.spdId = spdId;
	}

	public long getSpId() {
		return spId;
	}

	public void setSpId(long spId) {
		this.spId = spId;
	}

	public String getSpdName() {
		return spdName;
	}

	public void setSpdName(String spdName) {
		this.spdName = spdName;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}
	
	
	
	
}
