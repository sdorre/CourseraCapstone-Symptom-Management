package org.symptom.management.client;

import java.util.Collection;

import org.symptom.management.repository.CheckIn;
import org.symptom.management.repository.Patient;

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
 * @author stephane
 *
 */
public interface CheckInSvcApi {
	
	public static final String DATE_PARAMETER = "date";
	
	public static final String PATIENT_PARAMETER = "patient";

	// The path where we expect the VideoSvc to live
	public static final String CHECK_IN_SVC_PATH = "/checkIn";
	
	public static final String TOKEN_PATH = "/oauth/token";

	// The path to search videos by title
	public static final String CHECK_IN_DATE_SEARCH_PATH = CHECK_IN_SVC_PATH + "/search/findByDate";
	
	// The path to search videos by title
	public static final String CHECK_IN_PATIENT_SEARCH_PATH = CHECK_IN_SVC_PATH + "/search/findByPatient";

	@GET(CHECK_IN_SVC_PATH)
	public Collection<CheckIn> getCheckInList();
	
	@POST(CHECK_IN_SVC_PATH)
	public boolean addCheckIn(@Body CheckIn ch);
	
	@GET(CHECK_IN_DATE_SEARCH_PATH)
	public Collection<CheckIn> findByDate(@Query(DATE_PARAMETER) String date);
	
	@POST(CHECK_IN_PATIENT_SEARCH_PATH)
	public Collection<CheckIn> findByPatient(@Body Patient p);
	
}
