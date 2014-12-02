package org.symptom.management.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.symptom.management.client.DoctorSvcApi;
import org.symptom.management.repository.Doctor;
import org.symptom.management.repository.DoctorRepository;

import com.google.common.collect.Lists;

/**
 * 
 * @author stephane
 *
 */

// Tell Spring that this class is a Controller that should 
// handle certain HTTP requests for the DispatcherServlet
@Controller
public class DoctorSvc implements DoctorSvcApi {
	
	@Autowired
	private DoctorRepository doctors;


	@RequestMapping(value=DoctorSvcApi.DOCTOR_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Doctor> getDoctorList() {
		return Lists.newArrayList(doctors.findAll());
	}

	@RequestMapping(value=DoctorSvcApi.DOCTOR_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean addDoctor(@RequestBody Doctor d) {
		doctors.save(d);
		return true;
	}

	@RequestMapping(value=DoctorSvcApi.DOCTOR_NAME_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Doctor> findByName(
			@RequestParam(NAME_PARAMETER) String name) 
	{
		return doctors.findByName(name);
	}

	@RequestMapping(value=DoctorSvcApi.DOCTOR_LOGIN_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Doctor> findByLogin(
			@RequestParam(LOGIN_PARAMETER) String login) 
	{
		return doctors.findByLogin(login);
	}
}
