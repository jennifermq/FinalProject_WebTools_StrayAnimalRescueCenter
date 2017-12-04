package com.qing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.qing.dao.AnimalDAO;

@Controller



public class AdminController {
	
	@Autowired
	@Qualifier("animalDAO")
	AnimalDAO animalDAO;
	
	@RequestMapping(value="/admin/report",method = RequestMethod.GET)
	public ModelAndView getadminReport( ) throws Exception{
		ModelAndView mv = new ModelAndView();
	
		try {
			mv.addObject("total", animalDAO.getTotalCount());
			mv.addObject("dogPercentage", animalDAO.getDogPercentage());
			mv.addObject("catPercentage", animalDAO.getCatPercentage());
			mv.addObject("otherPercentage", animalDAO.getOtherPercentage());
			mv.setViewName("adopter_dashboard");
		}
		catch(Exception e) {
			System.out.println(e);
			mv.setViewName("error");
		}
		
		return mv;
	}
}
