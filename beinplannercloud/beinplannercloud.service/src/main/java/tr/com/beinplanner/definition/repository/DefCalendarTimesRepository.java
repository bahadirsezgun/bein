package tr.com.beinplanner.definition.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.definition.dao.DefCalendarTimes;

@Repository
public interface DefCalendarTimesRepository extends CrudRepository<DefCalendarTimes, Long> {

}
