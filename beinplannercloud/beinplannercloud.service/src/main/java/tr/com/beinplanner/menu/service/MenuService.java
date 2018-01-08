package tr.com.beinplanner.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.menu.dao.MenuSubTbl;
import tr.com.beinplanner.menu.dao.MenuTbl;
import tr.com.beinplanner.menu.repository.MenuRepository;
import tr.com.beinplanner.menu.repository.MenuSubRepository;


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
	
}
