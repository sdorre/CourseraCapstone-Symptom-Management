package com.capstone.symptommanagement.client;
/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */

import com.capstone.symptommanagement.EasyHttpClient;
import com.capstone.symptommanagement.SecuredRestBuilder;
import com.capstone.symptommanagement.ui.LoginScreenActivity;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;

public class UserRightsSvc {

	public static final String CLIENT_ID = "mobile";

	private static UserRightsSvcApi doctorSvc_;

	public static synchronized UserRightsSvcApi getOrShowLogin(Context ctx) {
		if (doctorSvc_ != null) {
			return doctorSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized UserRightsSvcApi init(String server, String user,
			String pass) {

		doctorSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(server + UserRightsSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(UserRightsSvcApi.class);

		return doctorSvc_;
	}
}
