package tr.com.beinplanner.zms.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.zms.dao.ZmsProduct;

@Repository
public interface ZmsProductRepository  extends CrudRepository<ZmsProduct, Long> {

	public List<ZmsProduct> findByFirmId(int firmId);
	
}
