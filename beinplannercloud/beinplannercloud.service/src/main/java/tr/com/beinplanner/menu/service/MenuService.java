package tr.com.beinplanner.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.menu.dao.MenuSubTbl;
import tr.com.beinplanner.menu.dao.MenuTbl;
import tr.com.beinplanner.menu.repository.MenuRepository;
import tr.com.beinplanner.menu.repository.MenuSubRepository;
import tr.com.beinplanner.util.UserTypes;


@Service
@Qualifier("menuService")
public class MenuService {

	@Autowired
	MenuRepository menuRepository;
	
	@Autowired
	MenuSubRepository menuSubRepository;
	
	public List<MenuTbl> findTopMenuByUserType(int userType,long firmId){
		return menuRepository.findTopMenuByUserType(userType, firmId);
	}

	public List<MenuTbl> findSideUpperMenuByUserType(int userType,long firmId){
		return menuRepository.findSideUpperMenuByUserType(userType, firmId);
	}
	
	public List<MenuSubTbl> findSideSubMenuByUserType(int userType, long upperMenu,long firmId){
		return menuSubRepository.findSideSubMenuByUserType(userType, upperMenu, firmId);
	}
	
	
	public MenuTbl findMenuDashboardByUserTypeAndFirmId(int userType,int firmId){
		return menuRepository.findMenuDashboardByUserTypeAndFirmId(userType, firmId);
	}
	
	
	public List<MenuTbl> findUserAuthorizedMenus(int authUserType,long firmId){
		
		
		
		List<MenuTbl> menuAllTbls=menuRepository.findSideUpperMenuByUserType(UserTypes.getUserTypeInt(UserTypes.USER_TYPE_ADMIN), firmId);
		for (MenuTbl menuAllTbl : menuAllTbls) {
			List<MenuSubTbl> menuLevel2s=menuSubRepository.findSideSubMenuByUserType(UserTypes.getUserTypeInt(UserTypes.USER_TYPE_ADMIN), menuAllTbl.getMenuId(),firmId);
			menuAllTbl.setMenuSubTbls(menuLevel2s);
		}
		
		List<MenuTbl> menuAuthTbls=menuRepository.findSideUpperMenuByUserType(authUserType,firmId);
		
		
		for (MenuTbl menuTbl : menuAllTbls) {
			menuTbl.setAuthority(0);
			for (MenuTbl menuAuthTbl : menuAuthTbls) {
				if(menuAuthTbl.getMenuId()==menuTbl.getMenuId()){
					menuTbl.setAuthority(1);
					break;
				}
			}
			if(menuTbl.getAuthority()==1){
				List<MenuSubTbl> menuAuthLevelTbls=menuSubRepository.findSideSubMenuByUserType(authUserType, menuTbl.getMenuId(),firmId);
				List<MenuSubTbl> menuAllLevel2s=menuTbl.getMenuSubTbls();
				for (MenuSubTbl menuAllLevel2 : menuAllLevel2s) {
					menuAllLevel2.setAuthority(0);
					for (MenuSubTbl menuAuthLevel2 : menuAuthLevelTbls) {
						if(menuAllLevel2.getMenuId()==menuAuthLevel2.getMenuId()){
							menuAllLevel2.setAuthority(1);
							break;
						}
					}
				}
			}
			
			
		}
		return menuAllTbls;
	}
	
	public List<MenuTbl> findAllTopMenu(int userType,int firmId) {
		List<MenuTbl> menuAllTbls=menuRepository.findTopMenuByUserType(UserTypes.getUserTypeInt(UserTypes.USER_TYPE_ADMIN), firmId);
		List<MenuTbl> menuAuthTbls=menuRepository.findTopMenuByUserType(UserTypes.getUserTypeInt(UserTypes.USER_TYPE_ADMIN), firmId);
		
		for (MenuTbl menuTbl : menuAllTbls) {
			menuTbl.setAuthority(0);
			for (MenuTbl menuAuthTbl : menuAuthTbls) {
				if(menuAuthTbl.getMenuId()==menuTbl.getMenuId()){
					menuTbl.setAuthority(1);
					break;
				}
			}
			
		}
		return menuAllTbls;
	}
	
}
