package tr.com.beinplanner.definition.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.definition.dao.DefSporProgram;
@Repository
public interface DefSporProgramRepository extends CrudRepository<DefSporProgram, Long>{

	public List<DefSporProgram> findByFirmId(int firmId);
}
