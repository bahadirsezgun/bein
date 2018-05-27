package tr.com.beinplanner.definition.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.definition.dao.DefTest;

@Repository
public interface DefTestRepository  extends CrudRepository<DefTest, Long> {
	
	
	public List<DefTest> findByFirmId(int firmId);
	
}
