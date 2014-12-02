package org.symptom.management.client;

import java.util.Collection;

import org.symptom.management.repository.Doctor;
import org.symptom.management.repository.Patient;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This interface defines an API for a PatientSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * 
 * 
 * @author stephane
 *
 */
public interface PatientSvcApi {
	
	public static final String NAME_PARAMETER = "name";
	
	public static final String DOCTOR_PARAMETER = "doctor";
	
	public static final String LOGIN_PARAMETER = "login";
	
	public static final String REGID_PARAMETER = "regid";

	// The path to the patient repository
	public static final String PATIENT_SVC_PATH = "/patient";

	public static final String TOKEN_PATH = "/oauth/token";

	// The path to search patient by name
	public static final String PATIENT_NAME_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByName";
	
	// The path to search patient by doctor
	public static final String PATIENT_DOCTOR_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByDoctor";

	// The path to search patient by user login
	public static final String PATIENT_LOGIN_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByLogin";
	
	public static final String PATIENT_REGID_SAVE_PATH = PATIENT_SVC_PATH + "/saveRegId";

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
