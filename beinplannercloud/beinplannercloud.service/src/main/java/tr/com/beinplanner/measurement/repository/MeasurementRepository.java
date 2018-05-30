package tr.com.beinplanner.measurement.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.measurement.dao.UserMeasurement;

@Repository
public interface MeasurementRepository  extends CrudRepository<UserMeasurement, Long>{

	public List<UserMeasurement> findByUserId(long userId);
}
