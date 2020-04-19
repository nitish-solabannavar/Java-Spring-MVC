package com.nitish.controller;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nitish.dao.ChildDAO;
import com.nitish.dao.RescueRecordDAO;
import com.nitish.dao.TreatmentRecordDAO;
import com.nitish.dao.UserDAO;
import com.nitish.pojo.Child;
import com.nitish.pojo.Employee;
import com.nitish.pojo.RescueRecord;
import com.nitish.pojo.TreatmentRecord;

@Controller
//@RequestMapping("/doctor/*")
public class TreatmentController {
	@Autowired
	@Qualifier("childDAO")
	ChildDAO childDAO;
	
	@Autowired
	@Qualifier("userDAO")
	UserDAO userDAO;
	
	@Autowired
	@Qualifier("rescueRecordDAO")
	RescueRecordDAO rescueRecordDAO;
	
	@Autowired
	@Qualifier("treatmentRecordDAO")
	TreatmentRecordDAO treatmentRecordDAO;
	
	@RequestMapping(value="/doctor/newTreat",method = RequestMethod.GET)
	public ModelAndView getTreat( HttpServletRequest request, @RequestParam ("childSelected") long childid) throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		try{
			mv.setViewName("doctor_treat");
			//List<Child> childList = childDAO.getChildList();
			Child child = childDAO.getChildById(childid);
			System.out.println("getTreat: " + child.getChildId());
			session.setAttribute("childTreated", child);
			mv.addObject("childTreated", child);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return mv;
	}
	
	@RequestMapping(value="/doctor/newTreat", method=RequestMethod.POST)
	public ModelAndView postTreat( HttpServletRequest request ) throws Exception{
		ModelAndView mv = new ModelAndView();
		HttpSession session = (HttpSession) request.getSession();
		try{
			TreatmentRecord tr = new TreatmentRecord();
			Employee e = (Employee) session.getAttribute("user");
			Child a = (Child) session.getAttribute("childTreated");
			tr.setDoctor(e);
			tr.setChild(a);
			
			Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR); 
	        int month = c.get(Calendar.MONTH); 
	        int date = c.get(Calendar.DATE);
			tr.setDate(month+1, date, year);
			
			tr.setChildid(a.getChildId());
			tr.setDoctorid(e.getEmployeeId());
			childDAO.updateChild(a.getChildId(), "treated");
			treatmentRecordDAO.addTreatmentRecord(tr);
			List<RescueRecord> recordList = rescueRecordDAO.getRescueRecordList();
			for(RescueRecord rr: recordList) {
				rr.setChild(childDAO.getChildById(rr.getChildid()));
				rr.setRegistrator(userDAO.getEmployeeByID(rr.getRegistratorid()));
			}
			session.setAttribute("rescueRecordList", recordList);
			mv.setViewName("doctor_workarea");
			
			return mv;
		}
		catch (Exception e)	{
			System.out.println(e);
		}
		return mv;
		
	}
}
;