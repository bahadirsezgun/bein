package tr.com.beinplanner.zms.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.zms.dao.ZmsStockIn;

@Repository
public interface ZmsStockInRepository  extends CrudRepository<ZmsStockIn, Long>{

	public List<ZmsStockIn> findByProductId(long productId);
	
}
