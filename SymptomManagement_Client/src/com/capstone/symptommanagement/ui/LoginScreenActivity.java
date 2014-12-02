package com.capstone.symptommanagement.ui;

import java.util.Collection;
import java.util.concurrent.Callable;

import com.capstone.symptommanagement.CallableTask;
import com.capstone.symptommanagement.R;
import com.capstone.symptommanagement.TaskCallback;
import com.capstone.symptommanagement.client.UserRightsSvc;
import com.capstone.symptommanagement.client.UserRightsSvcApi;
import com.capstone.symptommanagement.core.UserRights;
import com.capstone.symptommanagement.core.UserRights.Rights;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreenActivity extends Activity {

	private static final String TAG = "SymptomMgmt";
	
	private String server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);	
		
		final TextView tvUsername = (TextView)findViewById(R.id.login_username);
		final TextView tvPassword = (TextView)findViewById(R.id.login_password);

		final Button connect = (Button)findViewById(R.id.login_button);
		
		connect.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				final String username = tvUsername.getText().toString();
				final String password = tvPassword.getText().toString();
				
				final UserRightsSvcApi svc = UserRightsSvc.init(server, username, password);
			
				CallableTask.invoke(new Callable<Collection<UserRights>>() {

					@Override
					public Collection<UserRights> call() throws Exception {
						
						return svc.findByLogin(username);
					}
				}, new TaskCallback<Collection<UserRights>>() {

					@Override
					public void success(Collection<UserRights> result) {
						// OAuth 2.0 grant was successful and we
						// can talk to the server, open up the video listing
						Intent intent = null;
						
						if(result.size()==1){
							Rights right = result.iterator().next().getRight();
							switch(right){
								case ADMIN:
									Log.i(TAG, "start Admin configuration");
									break;
								case DOCTOR:
									Log.i(TAG, "start Doctor Home Page");
									intent = new Intent(
											LoginScreenActivity.this,
											DoctorHomeActivity.class);
									break;
								case PATIENT:
									Log.i(TAG, "start Patient home page");
									intent = new Intent(
											LoginScreenActivity.this,
											PatientHomeActivity.class);
									break;
								default:
									Log.i(TAG, "unknown right for this user !");
									break;
							}
							
							//add user information to the next activity and start this activity.
							intent.putExtra("username", username);
							intent.putExtra("password", password);
							//intent.putExtra("authToken",null);
							startActivity(intent);
						}else{
							Log.i(TAG, "There are more than one Right for this User");
						}
					}

					@Override
					public void error(Exception e) {
						Log.e(LoginScreenActivity.class.getName(), "Error logging in via OAuth.", e);
						
						Toast.makeText(
								LoginScreenActivity.this,
								"Login failed, check your Internet connection and credentials.",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
		String hostname = sharedPrefs.getString("preference_server_hostname", "NULL");
		String port  = sharedPrefs.getString("preference_server_port", "NULL");
		server = hostname+":"+port;
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case(R.id.settings):
			return startSettings(); 
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
	public boolean startSettings(){
		Intent intent = new Intent(LoginScreenActivity.this, SettingsActivity.class);
		startActivity(intent);
		return true;
		
	}
}