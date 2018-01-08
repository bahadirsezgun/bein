package tr.com.beinplanner.user.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.ActiveMember;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.repository.UserRepository;
import tr.com.beinplanner.util.OhbeUtil;

@Service
@Qualifier("userService")
public class UserService  {

	
	@Autowired
	UserRepository userRepository;
	
	
	
	
	public User findUserById(long userId){
		return userRepository.findOne(userId);
	}
	
	
	public List<User> findAllByFirmId(int firmId){
		return userRepository.findAllByFirmId(firmId);
	}
	public List<User> findAllByFirmIdAndUserType(int firmId,int userType){
		return userRepository.findAllByFirmIdAndUserType(firmId, userType);
	}
	
	public List<User> findByUsernameAndUsersurname(String userName,String userSurname,int firmId,int userType){
		return userRepository.findByUserNameStartingWithAndUserSurnameStartingWithAndFirmIdAndUserType(userName, userSurname, firmId,userType);
	}
	
	
	
	public List<User> finsSpecialDateOfUsers(int firmId){
		Date startDate=OhbeUtil.getDateForNextDate(new Date(), -1*5);
		Date endDate=OhbeUtil.getDateForNextDate(new Date(), 2);
		List<User>	userTblsHappy=new ArrayList<User>();
		try {
			List<User>	userTbls=userRepository.findAllByFirmId(firmId);
			userTbls.forEach(users->{
				Date birthDate=users.getUserBirthday();
	            if(birthDate!=null){
	            	Calendar calToday=Calendar.getInstance();
	            	Calendar cal=Calendar.getInstance();
	            	cal.setTimeInMillis(birthDate.getTime());
	            	
	            	cal.set(Calendar.YEAR, calToday.get(Calendar.YEAR));
	            	
	            	Date newBirth=new Date(cal.getTimeInMillis());
	            	if(newBirth.after(startDate) && newBirth.before(endDate)){
	            		userTblsHappy.add(users);
	            	}
	            }
			});
			return userTblsHappy;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public ActiveMember findActiveMemberCount(int firmId) {
		List<User> userInMemberships=userRepository.findActiveMemberInMembershipPlanning(firmId);
		List<User> userInClasss=userRepository.findActiveMemberInClassPlanning(firmId);
		List<User> userInPersonals=userRepository.findActiveMemberInPersonalPlanning(firmId);
		
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCCount(userInClasss.size());
		activeMember.setActiveMemberPCount(userInPersonals.size());
		activeMember.setActiveMemberMCount(userInMemberships.size());
		
		activeMember.setActiveMemberCount(activeMember.getActiveMemberCCount()+activeMember.getActiveMemberMCount()+activeMember.getActiveMemberPCount());;
		return activeMember;
	}
	
}
