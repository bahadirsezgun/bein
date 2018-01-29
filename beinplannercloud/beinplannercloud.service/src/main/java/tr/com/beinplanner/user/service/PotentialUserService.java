package tr.com.beinplanner.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.user.dao.UserPotential;
import tr.com.beinplanner.user.repository.PotentialUserRepository;
import tr.com.beinplanner.user.repository.UserRepository;

@Service
@Qualifier("potentialUserService")
public class PotentialUserService {

	@Autowired
	PotentialUserRepository potentialUserRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public UserPotential createPotentialUser(UserPotential userPotential){
		return potentialUserRepository.save(userPotential);
	}
		
	
	public List<UserPotential> findByUsernameAndUsersurname(String userName,String userSurname,int firmId){
		
		List<UserPotential> potentials=potentialUserRepository.findByUserNameStartingWithAndUserSurnameStartingWithAndFirmId(userName, userSurname, firmId);
		potentials.forEach(pu->{
			if(pu.getStaffId()>0) {
				pu.setStaff(userRepository.findOne(pu.getStaffId()));
			}
		});
		return potentials;
	}
	
	public UserPotential findById(long id){
		
		UserPotential potential=potentialUserRepository.findOne(id);
		if(potential.getStaffId()>0) {
			potential.setStaff(userRepository.findOne(potential.getStaffId()));
		}
		return potential;
	}
}
