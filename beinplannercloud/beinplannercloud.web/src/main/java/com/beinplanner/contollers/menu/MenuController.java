package com.beinplanner.contollers.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.menu.dao.MenuTbl;
import tr.com.beinplanner.menu.service.MenuService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.UserTypes;

@RestController
@RequestMapping("/bein/menu")
public class MenuController {

	@Autowired
	MenuService menuService;
	
	
	
	@Autowired
	LoginSession loginSession;
	
	@PostMapping(value="/getMenuLeft")
	public  @ResponseBody HmiResultObj getMenuLeft() {
		List<MenuTbl> menuTbl= menuService.findSideUpperMenuByUserType(loginSession.getUser().getUserType(), loginSession.getUser().getFirmId());
		menuTbl.forEach(m->{
			 m.setMenuSubTbls(menuService.findSideSubMenuByUserType(loginSession.getUser().getUserType(), m.getMenuId() , loginSession.getUser().getFirmId()));
		   });
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(menuTbl);
		return hmiResultObj;
	}
	
	@PostMapping(value="/getTopMenu")
	public  @ResponseBody HmiResultObj getTopMenu() {
		List<MenuTbl> menuTbl= menuService.findTopMenuByUserType(loginSession.getUser().getUserType(), loginSession.getUser().getFirmId());
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(menuTbl);
		return hmiResultObj;
	}
	
	@PostMapping(value="/getDashboardMenu")
	public  @ResponseBody HmiResultObj findDashboardMenu(HttpServletRequest request ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		
		MenuTbl menuTbl=menuService.findMenuDashboardByUserTypeAndFirmId(loginSession.getUser().getUserType(),loginSession.getUser().getFirmId());
		hmiResultObj.setResultObj(menuTbl);
		return hmiResultObj;
	}
	
}
