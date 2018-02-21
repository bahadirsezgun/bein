package tr.com.beinplanner.schedule.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.schedule.dao.ScheduleUsersClassPlan;

@Repository
public interface ScheduleUsersClassPlanRepository  extends CrudRepository<ScheduleUsersClassPlan, Long> {

	public List<ScheduleUsersClassPlan> findBySchtId(long schtId);
	
	public List<ScheduleUsersClassPlan> findBySaleId(long saleId);
	
	
}
