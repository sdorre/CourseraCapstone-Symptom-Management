package org.symptom.management.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.symptom.management.client.UserRightsSvcApi;

/**
 * An interface for a repository that can store User Rights
 * objects and allow them to be searched by username (login used in APK)
 * 
 * @author stephane
 *
 */

@RepositoryRestResource(path = UserRightsSvcApi.USER_SVC_PATH)
public interface UserRightsRepository extends MongoRepository<UserRights, Long>{

	// Find the right with a matching user login
	public Collection<UserRights> findByLogin(
			@Param(UserRightsSvcApi.LOGIN_PARAMETER) String login);	
}