package org.symptom.management.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.symptom.management.client.CheckInSvcApi;

/**
 * An interface for a repository that can store CheckIn
 * objects and allow them to be searched by date and by Patient.
 * 
 * @author stephane
 *
 */

@RepositoryRestResource(path = CheckInSvcApi.CHECK_IN_SVC_PATH)
public interface CheckInRepository extends MongoRepository<CheckIn, Long>{

	// Find all checkIn with a specified date
	public Collection<CheckIn> findByDate(
			@Param(CheckInSvcApi.DATE_PARAMETER) String date);
	
	// Find all CheckIn that belongs to a Patient
	public Collection<CheckIn> findByPatient(
			@Param(CheckInSvcApi.PATIENT_PARAMETER) Patient p);
}
