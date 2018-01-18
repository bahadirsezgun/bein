package com.beinplanner.contollers.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.UserTypes;

@RestController
@RequestMapping("/bein/member")
public class MemberController {

	@Autowired
	UserService userService;
	
	@Autowired
	LoginSession loginSession;
	
	@PostMapping(value="/create")
	public  @ResponseBody HmiResultObj create(@RequestBody User user ) {
		user.setUserType(UserTypes.USER_TYPE_MEMBER_INT);
		user.setFirmId(loginSession.getUser().getFirmId());
		return userService.create(user);
	}
	
	@PostMapping(value="/delete/{userId}")
	public  @ResponseBody HmiResultObj delete(@PathVariable long userId ) {
		return userService.delete(userId);
	}
	
	@PostMapping(value="/findById/{userId}")
	public  @ResponseBody HmiResultObj doLogin(@PathVariable long userId,HttpServletRequest request ) {
		User user=userService.findUserById(userId);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(user);
		return hmiResultObj;
	}
	
	@PostMapping(value="/findAll")
	public  @ResponseBody HmiResultObj findAll(HttpServletRequest request ) {
		List<User> user=userService.findAllByFirmId(loginSession.getUser().getFirmId());
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(user);
		return hmiResultObj;
	}
	
	
	
	@PostMapping(value="/findByUsernameAndUsersurname")
	public  @ResponseBody HmiResultObj findByUsernameAndUsersurname(@RequestBody User user ) {
		List<User> users=userService.findByUsernameAndUsersurname(user.getUserName(), user.getUserSurname(), loginSession.getUser().getFirmId(),UserTypes.USER_TYPE_MEMBER_INT);
		
		users.forEach(u->{
			if(u.getProfileUrl()==null) {
				if(u.getUserGender()==UserTypes.GENDER_MALE) {
					u.setProfileUrl("profilem.png");
				}else {
					u.setProfileUrl("profile.png");
				}
			}
		});
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(users);
		return hmiResultObj;
	}
	
	
}
