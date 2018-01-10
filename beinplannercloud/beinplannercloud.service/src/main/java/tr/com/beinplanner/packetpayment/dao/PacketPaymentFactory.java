package tr.com.beinplanner.packetpayment.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.user.dao.User;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({@JsonSubTypes.Type(value = PacketPaymentPersonal.class, name = "ppp"),
			   @JsonSubTypes.Type(value = PacketPaymentClass.class, name = "ppc"),
			   @JsonSubTypes.Type(value = PacketPaymentMembership.class, name = "ppm")})
public abstract class PacketPaymentFactory {

	
	private Date payDate;

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	
	
	
	

	
	
	
		
	
	
}
