package org.symptom.management.client;

import java.util.Collection;

import org.symptom.management.repository.Medication;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This interface defines an API for a MedicationSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * 
 * 
 * @author stephane
 *
 */
public interface MedicationSvcApi {
	
	public static final String NAME_PARAMETER = "name";
	
	// The path to the medication
	public static final String MEDICATION_SVC_PATH = "/medication";

	// The path to search medication by name
	public static final String MEDICATION_NAME_SEARCH_PATH = MEDICATION_SVC_PATH + "/search/findByName";
	
	@GET(MEDICATION_SVC_PATH)
	public Collection<Medication> getMedicationList();
	
	@POST(MEDICATION_SVC_PATH)
	public Void addMedication(@Body Medication med);
	
	@GET(MEDICATION_NAME_SEARCH_PATH)
	public Collection<Medication> findByName(@Query(NAME_PARAMETER) String name);	
}
