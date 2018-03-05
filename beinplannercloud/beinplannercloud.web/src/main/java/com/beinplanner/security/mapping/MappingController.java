package com.beinplanner.security.mapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MappingController {

	
	@RequestMapping(value="/login")
	public  ModelAndView login()  {
		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		return model;
	} 
	
	@RequestMapping(value="/")
	public ModelAndView loginRoot()  {
		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		return model;
	}
	
	@RequestMapping(value="/firmRegister")
	public  ModelAndView register()  {
		ModelAndView model = new ModelAndView();
		model.setViewName("register");
		return model;
	} 
	/*
	@RequestMapping(value="/error")
	public  ModelAndView error()  {
		ModelAndView model = new ModelAndView();
		model.setViewName("error");
		return model;
	} 
	*/
	@RequestMapping(value={ "/beincloud"}, method = RequestMethod.GET)
	public  ModelAndView beincloud(){
		ModelAndView model = new ModelAndView();
		model.setViewName("/bein/index");
		return model;
	}
	/*
	@RequestMapping(value={ "/error"}, method = RequestMethod.GET)
	public @ResponseBody String error(){
		return "/lock";
	}
	*/
	@RequestMapping(value= "/logout")
	public ModelAndView logout(){
		ModelAndView model = new ModelAndView();
		model.setViewName("/lock");
		return model;
	}
	
	@RequestMapping(value= "/lock")
	public ModelAndView lock(){
		ModelAndView model = new ModelAndView();
		model.setViewName("/error");
		return model;
	}
}
