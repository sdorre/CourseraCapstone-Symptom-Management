package org.symptom.management.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.symptom.management.Application;
import org.symptom.management.client.CheckInSvcApi;
import org.symptom.management.gcm.Content;
import org.symptom.management.gcm.GcmCommunication;
import org.symptom.management.repository.CheckIn;
import org.symptom.management.repository.CheckInRepository;
import org.symptom.management.repository.Doctor;
import org.symptom.management.repository.Patient;
import org.symptom.management.repository.CheckIn.Eating;
import org.symptom.management.repository.CheckIn.PainState;

import com.google.common.collect.Lists;

/**
 * 
 * @author stephane
 *
 */

@Controller
public class CheckInSvc implements CheckInSvcApi {
	
	@Autowired
	private CheckInRepository checkIns;


	@RequestMapping(value=CheckInSvcApi.CHECK_IN_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<CheckIn> getCheckInList() {
		return Lists.newArrayList(checkIns.findAll());
	}

	@RequestMapping(value=CheckInSvcApi.CHECK_IN_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean addCheckIn(@RequestBody CheckIn check) {
		
		Date now = new Date();
		long severeDuration=0;
		long moderateDuration=0;
		long noEatDuration=0;
		
		Collection<CheckIn> checks = checkIns.findByPatient(check.getPatient());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		
		// check all previous recorded check-in to see since when the patient feels really bad.
		for(CheckIn item : checks){
			
			Patient patient = item.getPatient();
			Doctor doctor = patient.getDoctor();
			
			try {
				// get the recorded date for this check-in
				Date date = df.parse(item.getDate());
				
				// compare to today
				long difference = now.getTime()-date.getTime();
				
				// get the maximum time since the patient feels moderate pain.
				if(item.getPainstate()==PainState.MODERATE){
					if(difference > moderateDuration){
						moderateDuration = difference;						
					}
					
				// get the maximum time since the patient feels severe pain.
				}else if(item.getPainstate()==PainState.SEVERE){
					if(difference > moderateDuration){
						moderateDuration = difference;
					}else if(difference > severeDuration){
						severeDuration = difference;
					}
					
				}else{	 // patient is going well ... 
					// in the last past hours we need to reset the time the patient felt bad.
					if( difference < moderateDuration){
						moderateDuration = 0;
					}else if( difference < severeDuration){
						severeDuration = 0;
					}
				}
				
				// get the maximum time since the patient can't eat.
				if(item.getEating()==Eating.CANT_EAT){
					if(difference > noEatDuration){
						noEatDuration = difference;
					}
				}else{// the patient can eat => reset time when patient can't eat
					if(difference< noEatDuration){
						noEatDuration = 0;
					}
				}
				
				// now, if one of the three maximum reaches the limit defined bellow, we need to alert the Doctor.
				if( ((severeDuration/3600000)>12) 
						|| ((moderateDuration/3600000)>16) 
						|| ((noEatDuration/3600000)>12)){
					
					// ALERT !! Patient feels realy bad !
					System.out.println("ALERT ! Dr. "+doctor.getName()+" "+doctor.getLastname()
							+", your patient :"+patient.getName()+" "+patient.getLastname()+" is really bad.");
					
					Content content = new Content();
					content.addRegId(doctor.getRegid());
					content.createData("ALERT Symptom Management",
										"Patient"+patient.getLastname()+" "+patient.getName()+" feels really bad.");
					GcmCommunication.post(Application.API_KEY, content);
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		// save this new checkIn
		checkIns.save(check);
		return true;
	}

	@RequestMapping(value=CheckInSvcApi.CHECK_IN_DATE_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<CheckIn> findByDate(
			@RequestParam(DATE_PARAMETER) String date) 
	{
		return checkIns.findByDate(date);
	}

	@RequestMapping(value=CheckInSvcApi.CHECK_IN_PATIENT_SEARCH_PATH, method=RequestMethod.POST)
	public @ResponseBody Collection<CheckIn> findByPatient(
			@RequestBody Patient p) 
	{
		return checkIns.findByPatient(p);
	}

}
