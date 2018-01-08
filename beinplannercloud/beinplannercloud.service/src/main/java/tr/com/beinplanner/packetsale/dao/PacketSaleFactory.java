package tr.com.beinplanner.packetsale.dao;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="progType")
@JsonSubTypes({@JsonSubTypes.Type(value = PacketSalePersonal.class, name = "psp"),
			   @JsonSubTypes.Type(value = PacketSaleClass.class, name = "psc"),
			   @JsonSubTypes.Type(value = PacketSaleMembership.class, name = "psm")})
public abstract class PacketSaleFactory {

	private Date 	salesDate;

	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}
	
	
	
	
	
	
}
