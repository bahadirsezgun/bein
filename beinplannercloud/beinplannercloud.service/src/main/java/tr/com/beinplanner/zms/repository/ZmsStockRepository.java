package tr.com.beinplanner.zms.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.zms.dao.ZmsStock;

@Repository
public interface ZmsStockRepository  extends CrudRepository<ZmsStock, Long> {

	public List<ZmsStock> findByFirmId(int firmId);
	
	public ZmsStock findByFirmIdAndProductId(int firmId,long productId);
}
