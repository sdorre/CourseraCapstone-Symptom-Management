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

public class CheckInSvc {

	public static final String CLIENT_ID = "mobile";

	private static CheckInSvcApi checkInSvc_;

	public static synchronized CheckInSvcApi getOrShowLogin(Context ctx) {
		if (checkInSvc_ != null) {
			return checkInSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized CheckInSvcApi init(String server, String user,
			String pass) {

		checkInSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(server + CheckInSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(CheckInSvcApi.class);

		return checkInSvc_;
	}
}
