package tr.com.beinplanner.sport.comparator;

import java.util.Comparator;

import tr.com.beinplanner.sport.dao.UserSportProgram;

public class SportComparator implements Comparator<UserSportProgram> {

	@Override
    public int compare(UserSportProgram ps1, UserSportProgram ps2) {
        return ps2.getApplyDate()>ps1.getApplyDate()?-1:ps2.getApplyDate()<ps1.getApplyDate()?1:0;
    }
}


