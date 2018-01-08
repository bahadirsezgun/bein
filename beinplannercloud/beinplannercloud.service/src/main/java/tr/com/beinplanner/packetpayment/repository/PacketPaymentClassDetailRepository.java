package tr.com.beinplanner.packetpayment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.packetpayment.dao.PacketPaymentClassDetail;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentDetailFactory;

@Repository
public interface PacketPaymentClassDetailRepository extends CrudRepository<PacketPaymentClassDetail, Long>{

		

}
