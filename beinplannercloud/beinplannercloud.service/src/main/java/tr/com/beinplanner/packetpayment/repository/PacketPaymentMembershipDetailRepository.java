package tr.com.beinplanner.packetpayment.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.packetpayment.dao.PacketPaymentClassDetail;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembershipDetail;
@Repository
public interface PacketPaymentMembershipDetailRepository  extends CrudRepository<PacketPaymentMembershipDetail, Long> {

	public List<PacketPaymentMembershipDetail> findByPayId(long payId);
	
}
