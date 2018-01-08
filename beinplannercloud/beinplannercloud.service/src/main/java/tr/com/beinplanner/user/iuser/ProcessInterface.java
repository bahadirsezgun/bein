package tr.com.beinplanner.user.iuser;

import java.util.List;

import tr.com.beinplanner.menu.dao.MenuTbl;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.User;

public interface ProcessInterface {

	
	public int getUserCompletePercent(User user);
	
	public List<User> findAll(int firmId);
	
	public List<User> findAllInChain(int firmId);
	
	public List<User> findByUserNameAndSurnameInChain(String userName,String userSurname,int firmId);
	
	public List<User> findByNameAndSurname(String userName,String userSurname,int firmId);
	
	public List<User> findByNameAndSaleProgramWithNoPlan(String userName,String userSurname,long progId,int type,int firmId,long schId);
	
	public List<User> findByNameAndSaleProgramWithPlan(String userName,String userSurname,long progId,int type,int firmId,long schId);
	
	public MenuTbl getMenuDashBoard(int firmId);
		
	public List<MenuTbl> getMenuSide(int firmId);
	
	public List<MenuTbl> getMenuTop(int firmId);
	
	public HmiResultObj deleteUser(User user);

	public HmiResultObj createUser(User user);

	
	
}
