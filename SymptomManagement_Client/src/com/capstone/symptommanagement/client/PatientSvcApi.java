package com.capstone.symptommanagement.client;

import java.util.Collection;

import com.capstone.symptommanagement.core.Doctor;
import com.capstone.symptommanagement.core.Patient;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This interface defines an API for a VideoSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * 
 * 
 * @author jules
 *
 */
public interface PatientSvcApi {
	
	public static final String NAME_PARAMETER = "name";
	
	public static final String DOCTOR_PARAMETER = "doctor";
	
	public static final String LOGIN_PARAMETER = "login";

	// The path where we expect the VideoSvc to live
	public static final String PATIENT_SVC_PATH = "/patient";

	public static final String TOKEN_PATH = "/oauth/token";

	// The path to search videos by title
	public static final String PATIENT_NAME_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByName";
	
	// The path to search videos by title
	public static final String PATIENT_DOCTOR_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByDoctor";

	// The path to search videos by title
	public static final String PATIENT_LOGIN_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByLogin";

	@GET(PATIENT_SVC_PATH)
	public Collection<Patient> getPatientList();
	
	@POST(PATIENT_SVC_PATH)
	public boolean addPatient(@Body Patient p);
	
	@GET(PATIENT_NAME_SEARCH_PATH)
	public Collection<Patient> findByName(@Query(NAME_PARAMETER) String name);

	@GET(PATIENT_LOGIN_SEARCH_PATH)
	public Collection<Patient> findByLogin(@Query(LOGIN_PARAMETER) String login);
	
	@POST(PATIENT_DOCTOR_SEARCH_PATH)
	public Collection<Patient> findByDoctor(@Body Doctor doctor);
	
}
