package tr.com.beinplanner.definition.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="def_spor_program")
public class DefSporProgram {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SP_ID")
	private long spId;
	
	@Column(name="SP_NAME")
	private String spName;
	
	@Column(name="FIRM_ID")
	private int firmId;

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

	public int getFirmId() {
		return firmId;
	}

	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}
	
	
	
	
}
