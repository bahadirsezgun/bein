package tr.com.beinplanner.program.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.program.dao.ProgramPersonal;

@Repository
public interface ProgramPersonalRepository extends CrudRepository<ProgramPersonal, Long>{

	List<ProgramPersonal> findByFirmId(int firmId);
	
}
