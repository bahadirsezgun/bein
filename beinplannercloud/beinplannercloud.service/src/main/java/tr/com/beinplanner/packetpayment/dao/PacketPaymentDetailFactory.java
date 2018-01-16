package tr.com.beinplanner.packetpayment.dao;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({@JsonSubTypes.Type(value = PacketPaymentPersonalDetail.class, name = "pppd"),
			   @JsonSubTypes.Type(value = PacketPaymentClassDetail.class, name = "ppcd"),
			   @JsonSubTypes.Type(value = PacketPaymentMembershipDetail.class, name = "ppmd")})
public abstract class PacketPaymentDetailFactory {
	
}
