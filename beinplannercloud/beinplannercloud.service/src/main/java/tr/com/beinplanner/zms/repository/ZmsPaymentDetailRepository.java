package tr.com.beinplanner.zms.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.zms.dao.ZmsPaymentDetail;

@Repository
public interface ZmsPaymentDetailRepository  extends CrudRepository<ZmsPaymentDetail, Long> {

	public List<ZmsPaymentDetail> findByPayIdx(long payIdx);
	
}
