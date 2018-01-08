package tr.com.beinplanner.program.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.program.dao.ProgramClass;
import tr.com.beinplanner.program.dao.ProgramMembership;

@Repository
public interface ProgramClassRepository  extends CrudRepository<ProgramClass, Long>{

	List<ProgramClass> findByFirmId(int firmId);
	
}
