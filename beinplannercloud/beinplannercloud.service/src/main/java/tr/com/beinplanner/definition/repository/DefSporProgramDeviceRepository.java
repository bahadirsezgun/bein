package tr.com.beinplanner.definition.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.definition.dao.DefSporProgramDevice;
@Repository
public interface DefSporProgramDeviceRepository extends CrudRepository<DefSporProgramDevice, Long> {

	public List<DefSporProgramDevice> findBySpId(long spId);
}
