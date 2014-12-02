package org.symptom.management.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.symptom.management.client.PatientSvcApi;

/**
 * An interface for a repository that can store Patient
 * objects and allow them to be searched by name, login or Doctor.
 * 
 * @author stephane
 *
 */

@RepositoryRestResource(path = PatientSvcApi.PATIENT_SVC_PATH)
public interface PatientRepository extends MongoRepository<Patient, Long>{

	// Find all Patient with a matching name (e.g., Video.name)
	public Collection<Patient> findByName(
			@Param(PatientSvcApi.NAME_PARAMETER) String name);
	
	// Find all patient that are followed by a specified doctor
	public Collection<Patient> findByDoctor(
			@Param(PatientSvcApi.DOCTOR_PARAMETER) Doctor doctor);

	// Find the patient with a given login
	public Collection<Patient> findByLogin(
			@Param(PatientSvcApi.LOGIN_PARAMETER) String login);
}
