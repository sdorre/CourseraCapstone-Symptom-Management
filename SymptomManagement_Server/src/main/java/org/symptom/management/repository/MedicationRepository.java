package org.symptom.management.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.symptom.management.client.MedicationSvcApi;

/**
 * An interface for a repository that can store medication
 * objects and allow them to be searched by title.
 * 
 * @author stephane
 *
 */

@RepositoryRestResource(path = MedicationSvcApi.MEDICATION_SVC_PATH)
public interface MedicationRepository extends MongoRepository<Medication, Long>{

	// Find all medication with a matching name
	public Collection<Medication> findByName(
			@Param(MedicationSvcApi.NAME_PARAMETER) String name);
}
