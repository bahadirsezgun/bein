package tr.com.beinplanner.diabet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.diabet.dao.UserDiabet;

@Repository
public interface DiabetRepository extends CrudRepository<UserDiabet, Long> {

	public List<UserDiabet> findByUdcId(long udcId);
}
