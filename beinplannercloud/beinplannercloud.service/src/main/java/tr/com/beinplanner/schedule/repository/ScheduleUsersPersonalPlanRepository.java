package tr.com.beinplanner.schedule.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;

@Repository
public interface ScheduleUsersPersonalPlanRepository   extends CrudRepository<ScheduleUsersPersonalPlan, Long>{

	public List<ScheduleUsersPersonalPlan> findBySchtId(long schtId);
	
	public List<ScheduleUsersPersonalPlan> findBySaleId(long saleId);
	
}
