package tr.com.beinplanner.zms.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.zms.dao.ZmsStockOutDetail;

@Repository
public interface ZmsStockOutDetailRepository  extends CrudRepository<ZmsStockOutDetail, Long> {

	public List<ZmsStockOutDetail> findByStkIdx(long stkIdx);
}
