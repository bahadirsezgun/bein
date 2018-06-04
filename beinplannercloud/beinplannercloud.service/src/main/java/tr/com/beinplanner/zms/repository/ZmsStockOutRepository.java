package tr.com.beinplanner.zms.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.zms.dao.ZmsStockOut;

@Repository
public interface ZmsStockOutRepository  extends CrudRepository<ZmsStockOut, Long>{

	public List<ZmsStockOut> findByProductId(long productId);
}
