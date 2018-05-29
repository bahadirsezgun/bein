package tr.com.beinplanner.diabet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tr.com.beinplanner.diabet.dao.UserDiabetCalori;

@Repository
public interface DiabetCaloriRepository extends CrudRepository<UserDiabetCalori, Long> {

	public UserDiabetCalori findByUserIdAndSaleIdAndSaleType(long userId,long saleId,String saleType);
	
}
