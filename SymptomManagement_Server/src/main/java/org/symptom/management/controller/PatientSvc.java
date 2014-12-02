package org.symptom.management.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.symptom.management.client.PatientSvcApi;
import org.symptom.management.repository.Doctor;
import org.symptom.management.repository.Patient;
import org.symptom.management.repository.PatientRepository;

import com.google.common.collect.Lists;

/**
 * 
 * @author stephane
 *
 */

// Tell Spring that this class is a Controller that should 
// handle certain HTTP requests for the DispatcherServlet
@Controller
public class PatientSvc implements PatientSvcApi {
	
	@Autowired
	private PatientRepository patients;


	@RequestMapping(value=PatientSvcApi.PATIENT_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Patient> getPatientList() {
		return Lists.newArrayList(patients.findAll());
	}

	@RequestMapping(value=PatientSvcApi.PATIENT_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean addPatient(@RequestBody Patient p) {
		patients.save(p);
		return true;
	}

	@RequestMapping(value=PatientSvcApi.PATIENT_NAME_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Patient> findByName(
			@RequestParam(NAME_PARAMETER) String name) 
	{
		return patients.findByName(name);
	}

	@RequestMapping(value=PatientSvcApi.PATIENT_LOGIN_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Patient> findByLogin(
			@RequestParam(LOGIN_PARAMETER) String login) 
	{
		return patients.findByLogin(login);
	}
	
	@RequestMapping(value=PatientSvcApi.PATIENT_DOCTOR_SEARCH_PATH, method=RequestMethod.POST)
	public @ResponseBody Collection<Patient> findByDoctor(
			@RequestBody Doctor doctor) 
	{
		return patients.findByDoctor(doctor);
	}
}
