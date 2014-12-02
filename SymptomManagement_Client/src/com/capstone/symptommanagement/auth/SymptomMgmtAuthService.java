package com.capstone.symptommanagement.auth;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service to handle the authentication for the SymptomManagementApp
 * redirect to our custom class SymptomMgmtAuthenticator
 * @author stephane
 *
 */
public class SymptomMgmtAuthService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		SymptomMgmtAuthenticator authenticator = new SymptomMgmtAuthenticator(this);
		return authenticator.getIBinder();
	}
}
