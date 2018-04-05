package tr.com.beinplanner.schedule.comparator;

import java.util.Comparator;

import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;

public class ScheduleTimePlanComparator implements Comparator<ScheduleTimePlan> {

	@Override
    public int compare(ScheduleTimePlan ps1, ScheduleTimePlan ps2) {
        return ps1.getPlanStartDate().compareTo(ps2.getPlanStartDate());
    }


}
