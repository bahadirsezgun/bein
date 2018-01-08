package tr.com.beinplanner.user.repository;

import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.user.dao.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	List<User> findAllByFirmId(int firmId);

	List<User> findAllByFirmIdAndUserType(int firmId,int userType);
	
	Optional<User> findByUserEmail(String userEmail);
	
	@Query(value="SELECT a.* " + 
			"				 FROM user a" + 
			"				 WHERE a.USER_ID IN (SELECT b.USER_ID FROM schedule_users_personal_plan b, schedule_time_plan c,user d " + 
			"				                      WHERE b.SCHT_ID=c.SCHT_ID " + 
			"				  					AND d.USER_ID=b.USER_ID " + 
			"										AND d.FIRM_ID=:firmId " + 
			"				                       AND c.PLAN_START_DATE>(SELECT CURDATE()) " + 
			"				                       GROUP BY b.USER_ID)",nativeQuery=true)
	List<User> findActiveMemberInPersonalPlanning(@Param("firmId") int firmId);
	
	@Query(value="SELECT a.* " + 
			"				 FROM user a" + 
			"				 WHERE a.USER_ID IN (SELECT b.USER_ID FROM schedule_users_class_plan b, schedule_time_plan c,user d " + 
			"				                      WHERE b.SCHT_ID=c.SCHT_ID " + 
			"				  					AND d.USER_ID=b.USER_ID " + 
			"										AND d.FIRM_ID=:firmId " + 
			"				                       AND c.PLAN_START_DATE>(SELECT CURDATE()) " + 
			"				                       GROUP BY b.USER_ID)",nativeQuery=true)
	List<User> findActiveMemberInClassPlanning(@Param("firmId") int firmId);
	
	
	
	
	@Query(value="SELECT a.*" + 
		"	 FROM user a" + 
		"	 WHERE a.USER_ID IN (SELECT d.USER_ID FROM schedule_membership_plan b,user d " + 
		"	                      WHERE d.USER_ID=b.USER_ID" + 
		"							AND d.FIRM_ID=:firmId " + 
		"	                       AND b.SMP_END_DATE>(SELECT CURDATE())" + 
		"	                       GROUP BY b.USER_ID)",nativeQuery=true)
	List<User> findActiveMemberInMembershipPlanning(@Param("firmId") int firmId);


	List<User> findByUserNameStartingWithAndUserSurnameStartingWithAndFirmIdAndUserType(String userName,String userSurname,int firmId,int userType);
	
	
}
