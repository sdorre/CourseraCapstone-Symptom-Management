package com.capstone.symptommanagement.client;

import java.util.Collection;

import com.capstone.symptommanagement.core.Medication;

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
public interface MedicationSvcApi {
	
	public static final String NAME_PARAMETER = "name";
	
	// The path where we expect the VideoSvc to live
	public static final String MEDICATION_SVC_PATH = "/medication";

	// The path to search videos by title
	public static final String MEDICATION_NAME_SEARCH_PATH = MEDICATION_SVC_PATH + "/search/findByName";
	
	@GET(MEDICATION_SVC_PATH)
	public Collection<Medication> getMedicationList();
	
	@POST(MEDICATION_SVC_PATH)
	public Void addMedication(@Body Medication med);
	
	@GET(MEDICATION_NAME_SEARCH_PATH)
	public Collection<Medication> findByName(@Query(NAME_PARAMETER) String name);	
}
