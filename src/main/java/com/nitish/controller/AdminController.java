package com.nitish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nitish.dao.ChildDAO;

@Controller



public class AdminController {
	
	@Autowired
	@Qualifier("childDAO")
	ChildDAO childDAO;
	
	@RequestMapping(value="/admin/report",method = RequestMethod.GET)
	public ModelAndView getadminReport( ) throws Exception{
		ModelAndView mv = new ModelAndView();
	
		try {
			mv.addObject("total", childDAO.getTotalCount());
			mv.addObject("age1Percentage", childDAO.getAge1Percentage());
			mv.addObject("age2Percentage", childDAO.getAge2Percentage());
			mv.addObject("otherPercentage", childDAO.getOtherPercentage());
			mv.setViewName("adopter_dashboard");
		}
		catch(Exception e) {
			System.out.println(e);
			mv.setViewName("error");
		}
		
		return mv;
	}
}
