package org.symptom.management.client;

import java.util.Collection;

import org.symptom.management.repository.Doctor;

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
public interface DoctorSvcApi {
	
	public static final String NAME_PARAMETER = "name";

	public static final String LOGIN_PARAMETER = "login";
	
	// The path where we expect the VideoSvc to live
	public static final String DOCTOR_SVC_PATH = "/doctor";

	public static final String TOKEN_PATH = "/oauth/token";

	// The path to search videos by title
	public static final String DOCTOR_NAME_SEARCH_PATH = DOCTOR_SVC_PATH + "/search/findByName";

	// The path to search videos by title
	public static final String DOCTOR_LOGIN_SEARCH_PATH = DOCTOR_SVC_PATH + "/search/findByLogin";

	@GET(DOCTOR_SVC_PATH)
	public Collection<Doctor> getDoctorList();
	
	@POST(DOCTOR_SVC_PATH)
	public boolean addDoctor(@Body Doctor p);
	
	@GET(DOCTOR_NAME_SEARCH_PATH)
	public Collection<Doctor> findByName(@Query(NAME_PARAMETER) String name);

	@GET(DOCTOR_LOGIN_SEARCH_PATH)
	public Collection<Doctor> findByLogin(@Query(LOGIN_PARAMETER) String login);	
}
