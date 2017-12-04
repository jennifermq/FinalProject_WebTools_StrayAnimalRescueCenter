package com.qing.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.qing.dao.AdoptionRecordDAO;
import com.qing.dao.AnimalDAO;
import com.qing.dao.RescueRecordDAO;
import com.qing.dao.TreatmentRecordDAO;
import com.qing.dao.UserDAO;
import com.qing.pojo.Adopter;
import com.qing.pojo.AdopterUserAccount;
import com.qing.pojo.AdoptionRecord;
import com.qing.pojo.Employee;
import com.qing.pojo.EmployeeUserAccount;
import com.qing.pojo.RescueRecord;

@Controller
//@RequestMapping("/user/*")
public class UserController  {

	@Autowired
	@Qualifier("userDAO")
	UserDAO userDAO;
	
	@Autowired
	@Qualifier("animalDAO")
	AnimalDAO animalDAO;
	
	@Autowired
	@Qualifier("rescueRecordDAO")
	RescueRecordDAO rescueRecordDAO;
	
	@Autowired
	@Qualifier("treatmentRecordDAO")
	TreatmentRecordDAO treatmentRecordDAO;
	
	@Autowired
	@Qualifier("adoptionRecordDAO")
	AdoptionRecordDAO adoptionRecordDAO;
	
	@RequestMapping(value="/user/logout", method=RequestMethod.GET)
	public ModelAndView getLogout( HttpServletRequest request ) throws Exception{
		//ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		session.invalidate();
		return new ModelAndView("user_loginAsAdopter");
	}
	
	@RequestMapping(value="/user/signup",method = RequestMethod.GET)
	public ModelAndView getSignUp() throws Exception{
		//ModelAndView mv = new ModelAndView();
		return new ModelAndView("user_signup");
	}
	
	@RequestMapping(value="/user/signup",method = RequestMethod.POST)
	public ModelAndView postSignUp( HttpServletRequest request,
									@RequestParam("email") String email, 
									@RequestParam("password") String password,
									@RequestParam("password2") String password2,
									@RequestParam("lastname") String lastname,
									@RequestParam("firstname") String firstname,
									@RequestParam("gender") String gender,
									@RequestParam("address") String address,
									@RequestParam("phone") String phone)
											throws Exception{
		//System.out.println("Inside post method");;
		//ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		
		try{
			if(userDAO.isAdopterEmailValid(email)) {
				if(!password.equals(password2)) {
					return new ModelAndView("user_passwordDifferent");
				}
				else {
					Adopter adopter = userDAO.addAdopter(email, password, lastname, firstname, gender, address, phone);
					session.setAttribute("user", adopter);
					session.setAttribute("userType", "adopter");
					return new ModelAndView("adopter_dashboard");
				}
			}
			else {
				
				//return "user_emailNotValid";
				return new ModelAndView("user_emailInvalid");
			}
		}
		catch (Exception e)	{
			System.out.println("UserController_postSignUp: " + e);
		}
		return new ModelAndView("error");
	}
	
	@RequestMapping(value="/user/loginAsAdopter",method = RequestMethod.GET)
	public ModelAndView getLoginAsAdopter(){
		ModelAndView mv = new ModelAndView();
		return new ModelAndView("user_loginAsAdopter");
	}
	
	@RequestMapping(value="/user/loginAsEmployee", method = RequestMethod.GET)
	public ModelAndView getLoginAsEmployee(){
		ModelAndView mv = new ModelAndView();
		return new ModelAndView("user_loginAsEmployee");
	}
	
	@RequestMapping(value="/user/loginAsEmployee", method = RequestMethod.POST)
	public ModelAndView postLoginEmployee( HttpServletRequest request,
									@RequestParam("employeeid") long id, 
									@RequestParam("password") String password )	
											throws Exception{
		//System.out.println("Inside controller method");
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
				
		try{
			EmployeeUserAccount eua = userDAO.getEmployeeUserAccount(id, password);
			Employee e = userDAO.getEmployee(eua);
			System.out.println(e.getName());
			if(e != null) {
				session.setAttribute("user", e);
				if(e.getRole().equals("registrator")) {
					session.setAttribute("userType", "registrator");
					List<RescueRecord> recordList = rescueRecordDAO.getRescueRecordList();
					for(RescueRecord rr: recordList) {
						rr.setAnimal(animalDAO.getAnimalById(rr.getAnimalid()));
						rr.setRegistrator(userDAO.getEmployeeByID(rr.getRegistratorid()));
					}
					session.setAttribute("rescueRecordList", recordList);
					mv.setViewName("registrator_workarea");
					mv.addObject("registrator", e);
					boolean isEmpty = false;
					if( recordList.size() == 0 ) {
						isEmpty = true;
					}
					mv.addObject("isEmpty", isEmpty);
					mv.addObject("message","No content.");
					return mv;
				}
				else if(e.getRole().equals("veterinarian")) {
					session.setAttribute("userType", "veterinarian");
					List<RescueRecord> recordList = rescueRecordDAO.getRescueRecordList();
					for(RescueRecord rr: recordList) {
						rr.setAnimal(animalDAO.getAnimalById(rr.getAnimalid()));
						rr.setRegistrator(userDAO.getEmployeeByID(rr.getRegistratorid()));
					}
					session.setAttribute("rescueRecordList", recordList);
					mv.setViewName("veterinarian_workarea");
					boolean isEmpty = false;
					if( recordList.size() == 0 ) {
						isEmpty = true;
					}
					mv.addObject("isEmpty", isEmpty);
					mv.addObject("message","No content.");
					return mv;
				}
				else if(e.getRole().equals("adoption")) {
					session.setAttribute("userType", "adoption");
					mv.setViewName("adoption_workarea");
					//mv.addObject("adoption", e);
					//mv.addObject("adoptionRecordList", adoptionRecordDAO.getAdoptionRecordList());
					
					List<AdoptionRecord> recordList = adoptionRecordDAO.getAdoptionRecordListPending();
					for(AdoptionRecord r: recordList) {
						r.setAnimal(animalDAO.getAnimalById(r.getAnimalId()));
						r.setAdopter(userDAO.getAdopterByEmail(r.getAdopterEmail()));
					}
					session.setAttribute("records", recordList);
					boolean isEmpty = false;
					if( recordList.size() == 0 ) {
						isEmpty = true;
					}
					mv.addObject("isEmpty", isEmpty);
					mv.addObject("message","No content.");
					return mv;
				}
				else if(e.getRole().equals("admin")) {
					session.setAttribute("userType", "admin");
					mv.setViewName("admin_report");
					mv.addObject("total", animalDAO.getTotalCount());
					mv.addObject("dogPercentage", animalDAO.getDogPercentage());
					mv.addObject("catPercentage", animalDAO.getCatPercentage());
					mv.addObject("otherPercentage", animalDAO.getOtherPercentage());
					mv.addObject("malePercentage", animalDAO.getMalePercentage());
					mv.addObject("femalePercentage", animalDAO.getFemalePercentage());
					return mv;
				}
				else {
					System.out.println("Role exception.");
					return new ModelAndView("user_userNotFound");
				}
			}
		}
		catch (Exception e)	{
			System.out.println(e);
			mv.setViewName("user_userNotFound");
		}
		return mv;
		
	}
	
	@RequestMapping(value="/user/loginAsAdopter", method=RequestMethod.POST)
	public ModelAndView postLoginAdopter( HttpServletRequest request,
									@RequestParam("email") String email, 
									@RequestParam("password") String password )	
											throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
				
		try{	
			AdopterUserAccount aua = userDAO.getAdopterUserAccount(email, password);
			Adopter a = userDAO.getAdopter(aua);
			if(a != null) {
				System.out.println("a is not null.");
				mv.setViewName("adopter_dashboard");
				session.setAttribute("user", a);
				session.setAttribute("userType", "adopter");
				mv.addObject("adopter", a);
			}
			else {
				//user not found
				System.out.println("User not found.");
				return new ModelAndView("user_userNotFound");
			}
		}
		catch (Exception e)	{
			System.out.println(e);
			mv.setViewName("user_userNotFound");
		}
		return mv;
	}
	
	@RequestMapping(value="/",method = RequestMethod.GET)
	public String home(Locale locale, Model model){
		return "login";
	}
}