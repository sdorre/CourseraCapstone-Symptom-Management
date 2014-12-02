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

public class DoctorSvc {

	public static final String CLIENT_ID = "mobile";

	private static DoctorSvcApi doctorSvc_;

	public static synchronized DoctorSvcApi getOrShowLogin(Context ctx) {
		if (doctorSvc_ != null) {
			return doctorSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized DoctorSvcApi init(String server, String user,
			String pass) {

		doctorSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(server + DoctorSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(DoctorSvcApi.class);

		return doctorSvc_;
	}
}
