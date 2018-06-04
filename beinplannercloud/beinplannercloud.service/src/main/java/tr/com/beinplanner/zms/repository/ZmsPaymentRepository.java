package tr.com.beinplanner.zms.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.zms.dao.ZmsPayment;

@Repository
public interface ZmsPaymentRepository  extends CrudRepository<ZmsPayment, Long>{

	public ZmsPayment findByStkIdx(long stkIdx);
}
