package tr.com.beinplanner.packetpayment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembershipDetail;
@Repository
public interface PacketPaymentMembershipDetailRepository  extends CrudRepository<PacketPaymentMembershipDetail, Long> {

}
