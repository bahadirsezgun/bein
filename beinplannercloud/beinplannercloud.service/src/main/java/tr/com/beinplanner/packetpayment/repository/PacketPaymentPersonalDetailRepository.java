package tr.com.beinplanner.packetpayment.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonalDetail;
@Repository
public interface PacketPaymentPersonalDetailRepository  extends CrudRepository<PacketPaymentPersonalDetail, Long> {

	public List<PacketPaymentPersonalDetail> findByPayId(long payId);
	
}
