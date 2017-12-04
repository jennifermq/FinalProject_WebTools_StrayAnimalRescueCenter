package com.qing.controller;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.qing.dao.AnimalDAO;
import com.qing.dao.RescueRecordDAO;
import com.qing.dao.UserDAO;
import com.qing.exception.RescueRecordException;
import com.qing.pojo.Animal;
import com.qing.pojo.Employee;
import com.qing.pojo.RescueRecord;

@Controller
//@RequestMapping("/registration/*")
public class RegisterController {
	
	@Autowired
	@Qualifier("animalDAO")
	AnimalDAO animalDAO;

	@Autowired
	@Qualifier("userDAO")
	UserDAO userDAO;
	
	@Autowired
	@Qualifier("rescueRecordDAO")
	RescueRecordDAO rescueRecordDAO;
	
	@Autowired
	ServletContext servletContext;
	
	@RequestMapping(value="/registration/registrator_workarea",method = RequestMethod.GET)
	public ModelAndView getRegisterWorkarea() throws RescueRecordException{
		ModelAndView mv = new ModelAndView();
		try {
			System.out.println("Inside getRegisterWorkarea()");
			mv.setViewName("registrator_workarea");
			mv.addObject("recordList", rescueRecordDAO.getRescueRecordList());
		}
		catch(Exception e) {
			System.out.println("getRegisterWorkarea() : " + e);
		}
		return mv;
	}
	
	@RequestMapping(value="/registration/newAnimal",method = RequestMethod.GET)
	public ModelAndView getRegisterNew() throws RescueRecordException{
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("registrator_addAnimal");
			mv.addObject("recordList", rescueRecordDAO.getRescueRecordList());
		}
		catch(Exception e) {
			System.out.println("getRegisterNew() : " + e);
		}
		return mv;
	}
	
	@RequestMapping(value="/registration/newAnimal", method=RequestMethod.POST)
	public ModelAndView postRegisterNew( HttpServletRequest request,
											@RequestParam("type") String type, 
											@RequestParam("gender") String gender,
											@RequestParam("color") String color,
											@ModelAttribute("model") Animal model)
						throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		//System.out.println("1 - " + filePath);
		try{
			//Animal a = new Animal();
			//a.setType(type);
			//a.setGender(gender);
			//a.setColor(color);
			//a.setStatus("untreated");
			if (model.getFile() != "" || model.getFile() != null) {
				File directory;
				String check = File.separator; // Checking if system is linux
												// based or windows based by
												// checking seprator used.
				String path = null;
				if (check.equalsIgnoreCase("\\")) {
					path = servletContext.getRealPath("").replace("build\\", ""); // gives real path as Lab9/build/web/
					System.out.println("2 - " + path);							// so we need to replace build in the path
				}
				
				System.out.println("3 - " + path);
				
//				if (check.equalsIgnoreCase("/")) {
//					System.out.println("4 - " + path);
//					path = servletContext.getRealPath("").replace("build/", "");
//					path += "/"; // Adding trailing slash for Mac systems.
//					System.out.println("5 - " + path);	
//				}
				directory = new File(path + "\\" + model.getPhoto());
				System.out.println("4 - " + directory);
				boolean temp = directory.exists();
				if (!temp) {
					temp = directory.mkdir();
				}
				if (temp) {
					// We need to transfer to a file
					CommonsMultipartFile photoInMemory = model.getPhoto();

					String fileName = photoInMemory.getOriginalFilename();
					// could generate file names as well

					File localFile = new File(directory.getPath(), fileName);

					// move the file from memory to the file

					photoInMemory.transferTo(localFile);
					model.setFile(localFile.getPath());
					System.out.println("File is stored at " + localFile.getPath());
				}
			}
			model.setType(type);
			model.setGender(gender);
			model.setColor(color);
			model.setStatus("untreated");
			animalDAO.addAnimal(model);
			
			RescueRecord rr = new RescueRecord();
			Employee e = (Employee) session.getAttribute("user");
			//System.out.println(e.getEmployeeId());
			rr.setAnimal(model);
			rr.setAnimalid(model.getAnimalId());
			rr.setRegistrator(e);
			rr.setRegistratorid(e.getEmployeeId());
			Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR); 
	        int month = c.get(Calendar.MONTH); 
	        int date = c.get(Calendar.DATE);
			rr.setDate(month+1, date, year);
			rescueRecordDAO.addRescueRecord(rr);
			
			List<RescueRecord> recordList = rescueRecordDAO.getRescueRecordList();
			for(RescueRecord r: recordList) {
				r.setAnimal(animalDAO.getAnimalById(r.getAnimalid()));
				r.setRegistrator(userDAO.getEmployeeByID(r.getRegistratorid()));
			}
			session.setAttribute("rescueRecordList", recordList);
			mv.setViewName("registrator_workarea");
			//mv.addObject("rescueRecordList", rescueRecordDAO.getRescueRecordList());
			return mv;
		}
		catch (Exception e)	{
			System.out.println(e + ": " + e.getMessage() + ": " + e.getCause());
		}
		return mv;
		
	}
	
	
}
