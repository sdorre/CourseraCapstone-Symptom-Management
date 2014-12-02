package com.capstone.symptommanagement.client;

import java.util.Collection;

import com.capstone.symptommanagement.core.UserRights;

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
public interface UserRightsSvcApi {
	
	public static final String LOGIN_PARAMETER = "login";

	// The path where we expect the VideoSvc to live
	public static final String USER_SVC_PATH = "/userRights";

	public static final String TOKEN_PATH = "/oauth/token";

	// The path to search videos by title
	public static final String USER_RIGHTS_LOGIN_SEARCH_PATH = USER_SVC_PATH + "/search/findByLogin";

	@GET(USER_SVC_PATH)
	public Collection<UserRights> getUserRightsList();
	
	@POST(USER_SVC_PATH)
	public Void addUserRights(@Body UserRights u);
	
	@GET(USER_RIGHTS_LOGIN_SEARCH_PATH)
	public Collection<UserRights> findByLogin(@Query(LOGIN_PARAMETER) String login);
	
}
