package com.capstone.symptommanagement.auth;

import com.capstone.symptommanagement.ui.LoginScreenActivity;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class SymptomMgmtAuthenticator extends AbstractAccountAuthenticator{

	private static String TAG = "SymptomMgmtAuthenticator";
	private final Context mContext;
	
	public SymptomMgmtAuthenticator(Context context) {
		super(context);
		this.mContext = context;
	}


	/**
	 * Used when the user want to log in and add a new account to the device.
	 * First Time using the app, or when password has changed.
	 */
	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
		Log.i(TAG, "adding new account");
		
		//create an Intent to launch the Login Activity
		final Intent intent = new Intent(mContext, LoginScreenActivity.class);
		//intent.putExtra(LoginScreenActivity.ARG_ACCOUNT_TYPE, accountType);
		//intent.putExtra(LoginScreenActivity.ARG_AUTH_TYPE, authTokenType);
		//intent.putExtra(LoginScreenActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		
		//give the intent to the asked bundle
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}


	/**
	 * Get the stored auth-token from a previous successful logi-in.
	 */
	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {

		//extract authentication information from the Account Manager
		final AccountManager mAcManager = AccountManager.get(mContext);
		
		String authToken = mAcManager.peekAuthToken(account, authTokenType);
		//try a new authentication for the user
		if(TextUtils.isEmpty(authToken)){
			String password = mAcManager.getPassword(account);
			if(password!=null){
				//send a request to authenticate on the server
				//authToken = ServerAuthenticate.getToken()
			}
		}
		
		//test the new authToken from teh server
		if(!TextUtils.isEmpty(authToken)){
			Bundle bundle = new Bundle();
			
			bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
			bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
			bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
			
			return bundle;
		}
		
		//Here, it means that we couldn't access the user password. We need to 
		//reprompt the user for it.
		final Intent intent = new Intent(mContext, LoginScreenActivity.class);
	    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
	    //intent.putExtra(LoginScreenActivity.ARG_ACCOUNT_TYPE, account.type);
	    //intent.putExtra(LoginScreenActivity.ARG_AUTH_TYPE, authTokenType);
	    final Bundle bundle = new Bundle();
	    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
	    return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		 return authTokenType;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		return null;
	}
	
	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		return null;
	}


	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		return null;
	}
	
	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		return null;
	}

}
