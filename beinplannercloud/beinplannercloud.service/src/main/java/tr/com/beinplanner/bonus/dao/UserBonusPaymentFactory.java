package tr.com.beinplanner.bonus.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import tr.com.beinplanner.user.dao.User;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="bonType")
@JsonSubTypes({@JsonSubTypes.Type(value = UserBonusPaymentClass.class, name = "bpc"),
			   @JsonSubTypes.Type(value = UserBonusPaymentPersonal.class, name = "bpp")})
public abstract class UserBonusPaymentFactory {
	
	private Date bonPaymentDate;

	public Date getBonPaymentDate() {
		return bonPaymentDate;
	}

	public void setBonPaymentDate(Date bonPaymentDate) {
		this.bonPaymentDate = bonPaymentDate;
	}
	
}
