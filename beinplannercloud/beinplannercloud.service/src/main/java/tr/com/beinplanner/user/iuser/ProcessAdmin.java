package tr.com.beinplanner.user.iuser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tr.com.beinplanner.menu.dao.MenuSubTbl;
import tr.com.beinplanner.menu.dao.MenuTbl;
import tr.com.beinplanner.menu.service.MenuService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.UserTypes;

@Component(value="processAdmin")
@Scope("prototype")
public class ProcessAdmin implements ProcessInterface {

	@Autowired
	MenuService menuService;

	@Autowired
	UserService userService;

	
	@Override
	public int getUserCompletePercent(User user) {
		return 0;// processUserDao.getUserCompletePercentToAdmin(user);
	}

	@Override
	public List<User> findAll(int firmId) {
		return userService.findAllByFirmIdAndUserType(firmId, UserTypes.USER_TYPE_ADMIN_INT);
	}
	
	@Override
	public List<User> findAllInChain(int firmId) {
		return null;
	}
	
	@Override
	public List<User> findByUserNameAndSurnameInChain(String userName, String userSurname, int firmId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public HmiResultObj deleteUser(User user) {
		return null;
	}
	
	
	@Override
	public List<MenuTbl> getMenuSide(int firmId) {
		List<MenuTbl> menuTbls=menuService.findSideUpperMenuByUserType(UserTypes.USER_TYPE_ADMIN_INT,firmId);
		
		for (MenuTbl menuTbl : menuTbls) {
			List<MenuSubTbl> menuSubTbls=menuService.findSideSubMenuByUserType(UserTypes.USER_TYPE_ADMIN_INT, menuTbl.getMenuId(),firmId);
			menuTbl.setMenuSubTbls(menuSubTbls);
		}
		
		
		return menuTbls;
		
	}

	@Override
	public List<MenuTbl> getMenuTop(int firmId) {
		List<MenuTbl> menuTopTbls=menuService.findTopMenuByUserType(UserTypes.USER_TYPE_ADMIN_INT,firmId);
		return menuTopTbls;
	}

	@Override
	public List<User> findByNameAndSurname(String userName, String userSurname,int firmId) {
		// TODO Auto-generated method stub
		return null;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	

	@Override
	public HmiResultObj createUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findByNameAndSaleProgramWithNoPlan(String userName, String userSurname, long progId, int type,int firmId,long schId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<User> findByNameAndSaleProgramWithPlan(String userName, String userSurname, long progId, int type,int firmId,long schId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuTbl getMenuDashBoard(int firmId) {
		return menuService.findMenuDashboardByUserTypeAndFirmId(UserTypes.USER_TYPE_ADMIN_INT,firmId);
	}

	
	

	

	
}
