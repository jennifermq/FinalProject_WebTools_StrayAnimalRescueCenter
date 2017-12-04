package com.qing.controller;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.qing.dao.AdoptionRecordDAO;
import com.qing.dao.AnimalDAO;
import com.qing.dao.UserDAO;
import com.qing.pojo.Adopter;
import com.qing.pojo.AdoptionRecord;
import com.qing.pojo.Animal;

@Controller
//@RequestMapping("/adoption/*")

public class AdopterController {
	
	@Autowired
	@Qualifier("animalDAO")
	AnimalDAO animalDAO;
	
	@Autowired
	@Qualifier("adoptionRecordDAO")
	AdoptionRecordDAO adoptionDAO;
	
	@Autowired
	@Qualifier("userDAO")
	UserDAO userDAO;
	
	@RequestMapping(value="/adopter/dashboard",method = RequestMethod.GET)
	public ModelAndView getAdoptionDashboard( ) throws Exception{
		return new ModelAndView("adopter_dashboard");
	}
	
	@RequestMapping(value="/adopter/adoptionList",method = RequestMethod.GET)
	public ModelAndView getAdoptionList( HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		try{
			mv.setViewName("adopter_adoptionlist");
			mv.addObject("adoptionList", animalDAO.getAdopterAnimalAdoptionList());
			session.setAttribute("adoptionList", animalDAO.getAdopterAnimalAdoptionList());
			
		}
		catch(Exception e){
			System.out.println("AdopterController - getAdoptionList");
		}
		return mv;
	}
	
	@RequestMapping(value="/adopter/adoptionList", method=RequestMethod.POST)
	public ModelAndView postAdoptionList( HttpServletRequest request,
											@RequestParam ("animalSelected") String animalIndex)
						throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		try{
			
			List<Animal> animalList = animalDAO.getAdopterAnimalAdoptionList();
			Animal animal = animalList.get(Integer.parseInt(animalIndex));
			session.setAttribute("animalSelected", animal);
			mv.addObject("animalSelected", animal);
			mv.setViewName("adopter_animalDetail");
		}
		catch (Exception e)	{
			System.out.println("AdopterController - postAdoptionList");
		}
		return mv;
		
	}
	
	@RequestMapping(value="/adopter/animalDetail", method = RequestMethod.GET)
	public ModelAndView getAnimalDetail( HttpServletRequest request, 
									@RequestParam ("animalViewed") long animalid) 
						throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		try{
			mv.setViewName("adopter_animalDetail");
			//List<Animal> animalList = animalDAO.getAnimalList();
			Animal animal = animalDAO.getAnimalById(animalid);
			System.out.println("getAnimalDetail: " + animal.getAnimalId());
			session.setAttribute("animalViewed", animal);
			mv.addObject("animalViewed", animal);
		}
		catch(Exception e) {
			System.out.println(e);
			mv.setViewName("error");
		}
		return mv;
		
	}
	
	@RequestMapping(value="/adopter/animalDetail", method = RequestMethod.POST)
	public ModelAndView postAnimalDetail( HttpServletRequest request ) throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		//System.out.println("postAnimalDetail_1");
		
		try{
			Adopter adopter = (Adopter) session.getAttribute("user");
			Animal animal = (Animal) session.getAttribute("animalViewed");
			AdoptionRecord record = new AdoptionRecord();
			System.out.println(adopter.getEmail());
			
			record.setAdopter(adopter);
			record.setAdopterEmail(adopter.getEmail());
			record.setAnimal(animal);
			record.setAnimalId(animal.getAnimalId());
			System.out.println(record.getAnimalId());
			
			record.setStatus("pending");
			record.getAnimal().setStatus("pending");
			
			Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR); 
	        int month = c.get(Calendar.MONTH); 
	        int date = c.get(Calendar.DATE);
			record.setDate(month+1, date, year);
			
			adoptionDAO.addAdoptionRecord(record);
			
			record.getAnimal().setStatus("treated");
			animalDAO.updateAnimal(record.getAnimal().getAnimalId(), "pending");
			
			mv.setViewName("adopter_myAdoptionHistory");
			List<AdoptionRecord> myAdoptions = adoptionDAO.getAdoptionRecordListByAdopter(adopter);
			
			for(AdoptionRecord ar: myAdoptions) {
				ar.setAdopter(adopter);
				ar.setAnimal(animalDAO.getAnimalById(ar.getAnimalId()));
			}
			session.setAttribute("myAdoptions", myAdoptions);
			session.setAttribute("adoptionList", animalDAO.getAdopterAnimalAdoptionList());
		}
		catch(Exception e){
			System.out.println("AdopterController - getAdoptionList");
			mv.setViewName("error");
		}
		return mv;
	}
	
	@RequestMapping(value="/adopter/myInfo",method = RequestMethod.GET)
	public ModelAndView getMyInfo(HttpServletRequest request) 
						throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		
		try{
			mv.setViewName("adopter_myInfo");
			Adopter adopter = (Adopter) session.getAttribute("user");
			mv.addObject("adopter", adopter);
		}
		catch(Exception e){
			System.out.println("AdopterController - getMyInfo");
		}
		return mv;
	}
	
	@RequestMapping(value="/adopter/myInfo", method = RequestMethod.POST)
	public ModelAndView postMyInfo( HttpServletRequest request,
									@RequestParam ("phone") String phone,
									@RequestParam ("address") String address)
						throws Exception{
		System.out.println("postMyInfo1");
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		
		try{
			List<Animal> animalList = animalDAO.getAdopterAnimalAdoptionList();
			Adopter adopter = (Adopter) session.getAttribute("user");
			adopter.setPhone(phone);
			adopter.setAddress(address);
			String email = adopter.getEmail();
			
			
			System.out.println("postMyInfo2");
			userDAO.updateAdopter(email, phone, address);
			
			mv.setViewName("adopter_dashboard");
		}
		catch (Exception e)	{
			System.out.println("AdopterController - postMyInfo");
			mv.setViewName("error");
		}
		return mv;
		
	}
	
	@RequestMapping(value="/adopter/adoptionHistory",method = RequestMethod.GET)
	public ModelAndView getMyHistory(HttpServletRequest request) 
						throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		
		try{
			mv.setViewName("adopter_myAdoptionHistory");
			Adopter adopter = (Adopter) session.getAttribute("user");
			List<AdoptionRecord> myAdoptions = adoptionDAO.getAdoptionRecordListByAdopter(adopter);
			
			for(AdoptionRecord ar: myAdoptions) {
				ar.setAdopter(adopter);
				ar.setAnimal(animalDAO.getAnimalById(ar.getAnimalId()));
			}
			session.setAttribute("myAdoptions", myAdoptions);
		}
		catch(Exception e){
			System.out.println("AdopterController - getMyInfo");
			mv.setViewName("error");
		}
		return mv;
	}
	
}
