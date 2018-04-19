package tr.com.beinplanner.bonus.businessDao;

import java.util.List;

public class UserBonusReportObj {

	private List<UserBonusObj> userPBonusObj;
	private List<UserBonusObj> userCBonusObj;
	
	public List<UserBonusObj> getUserPBonusObj() {
		return userPBonusObj;
	}
	public void setUserPBonusObj(List<UserBonusObj> userPBonusObj) {
		this.userPBonusObj = userPBonusObj;
	}
	public List<UserBonusObj> getUserCBonusObj() {
		return userCBonusObj;
	}
	public void setUserCBonusObj(List<UserBonusObj> userCBonusObj) {
		this.userCBonusObj = userCBonusObj;
	}
	
	
	
}
