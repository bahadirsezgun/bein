package tr.com.beinplanner.packetpayment.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.packetpayment.dao.PacketPaymentClassDetail;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentDetailFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonalDetail;

@Repository
public interface PacketPaymentClassDetailRepository extends CrudRepository<PacketPaymentClassDetail, Long>{

		
	public List<PacketPaymentClassDetail> findByPayId(long payId);
	
}
