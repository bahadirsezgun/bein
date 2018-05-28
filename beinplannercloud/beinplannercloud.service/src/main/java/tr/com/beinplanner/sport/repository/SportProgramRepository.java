package tr.com.beinplanner.sport.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.sport.dao.UserSportProgram;


@Repository
public interface SportProgramRepository extends CrudRepository<UserSportProgram, Long> {

	public List<UserSportProgram> findBySaleIdAndSaleTypeAndUserId(long saleId,String saleType,long userId);
	
}
