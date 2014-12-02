package org.symptom.management.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.symptom.management.client.DoctorSvcApi;

/**
 * An interface for a repository that can store Doctor
 * objects and allow them to be searched by name or login.
 * 
 * @author stephane
 *
 */

@RepositoryRestResource(path = DoctorSvcApi.DOCTOR_SVC_PATH)
public interface DoctorRepository extends MongoRepository<Doctor, Long>{

	// Find all doctors with a matching name
	public Collection<Doctor> findByName(
			@Param(DoctorSvcApi.NAME_PARAMETER) String name);

	// Find all doctors with a given username. 
	// We do it in a way that there is only one doctor for each username
	public Collection<Doctor> findByLogin(
			@Param(DoctorSvcApi.LOGIN_PARAMETER) String login);
}
