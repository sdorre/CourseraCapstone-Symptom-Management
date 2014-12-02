package com.capstone.symptommanagement.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import com.capstone.symptommanagement.CallableTask;
import com.capstone.symptommanagement.R;
import com.capstone.symptommanagement.TaskCallback;
import com.capstone.symptommanagement.client.DoctorSvc;
import com.capstone.symptommanagement.client.DoctorSvcApi;
import com.capstone.symptommanagement.client.PatientSvc;
import com.capstone.symptommanagement.client.PatientSvcApi;
import com.capstone.symptommanagement.core.Doctor;
import com.capstone.symptommanagement.core.Patient;
import com.capstone.symptommanagement.core.UserRights;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorHomeActivity extends Activity{

	private String GCM_KEY;
	private static final String TAG = "SymptomMgmt.DoctorHome";
	private String server;
	
	private PatientArrayAdapter adapter;
	
	private Doctor doctor;
	private ArrayList<Patient> patients;
	private GoogleCloudMessaging gcm;
	
	private String username;
	private String password;
	
	private TextView tvDoctorInfo;
	private TextView tvPatientFilter;
	
	private ListView lvPatientList;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_doctor);	
		
		
		SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
		String hostname = sharedPrefs.getString("preference_server_hostname", "NULL");
		String port  = sharedPrefs.getString("preference_server_port", "NULL");
		server = hostname+":"+port;
		
		GCM_KEY = sharedPrefs.getString("preference_gcm_project_number", "278967913331");
		
		Bundle extras = getIntent().getExtras();
		username = extras.getString("username");
		password = extras.getString("password");
		
		tvDoctorInfo = (TextView)findViewById(R.id.doctor_home_information);
		tvPatientFilter = (TextView)findViewById(R.id.doctor_home_listview_patient_filter);
		
		lvPatientList = (ListView)findViewById(R.id.doctor_home_listview);

		lvPatientList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(
					AdapterView<?> parent, View view,
					int position, long id) {
				Patient patient = (Patient)parent.getItemAtPosition(position);
				
				Intent intent = new Intent(
						DoctorHomeActivity.this,
						DoctorCheckPatientActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("patientLogin", patient.getLogin());
				startActivity(intent);
				
			}
			
		});
		
		final DoctorSvcApi svc = DoctorSvc.init(server, username, password);
		final PatientSvcApi patientsvc = PatientSvc.init(server, username, password);
		

		
		// Get Doctor's information
		CallableTask.invoke(new Callable<Collection<Doctor>>() {

			@Override
			public Collection<Doctor> call() throws Exception {
				
				return svc.findByLogin(username);
			}
		}, new TaskCallback<Collection<Doctor>>() {

			@Override
			public void success(Collection<Doctor> result) {
		
				if(!result.isEmpty()){
					final Doctor me = result.iterator().next();
					tvDoctorInfo.setText("doc. "+ me.getName() +" "+ me.getLastname() +" \n ID :"+me.getId());
					
					//save Doctor entity in Activity
					setDoctor(me);
					
					//  Get All patients for the logged doctor and get a regId if this doctor hasn't yet.
					CallableTask.invoke(new Callable<Collection<Patient>>() {

						@Override
						public Collection<Patient> call() throws Exception {
							
							Log.i(TAG, "Doctor.getRedId() "+doctor.getRegid());
							
							if(doctor.getRegid()==null || doctor.getRegid().isEmpty()){
								
								try{
									// get a regIg with google
									gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
									Log.i(TAG, "GCM_KEY : "+GCM_KEY);
									String regId = gcm.register(GCM_KEY);
									if(!TextUtils.isEmpty(regId)){
										doctor.setRegid(regId);
										
										
										
										// save this regId on the server
										svc.addDoctor(doctor);
									}else{
										Log.i(TAG, "failed to obtains a regID");
										Toast.makeText(getApplicationContext(),
														"Failed to obtain a regId with Google. We'll try the next time.",
														Toast.LENGTH_SHORT).show();
									}
								}catch(Exception e){
									Log.e(TAG, "failed to obtains a regID, E="+e);
								}
							}
							
							return patientsvc.findByDoctor(me);
						}
					}, new TaskCallback<Collection<Patient>>() {

						@Override
						public void success(Collection<Patient> result) {
					
							ArrayList<Patient> pat = new ArrayList<Patient>(result);
							setPatients(pat);
							adapter = new PatientArrayAdapter(getBaseContext(), pat);
							lvPatientList.setAdapter(adapter);
							
							tvPatientFilter.addTextChangedListener(new TextWatcher(){

								@Override
								public void onTextChanged(CharSequence s, int start, int before,
										int count) {
									adapter.filter(tvPatientFilter.getText().toString());
								}

								@Override
								public void beforeTextChanged(CharSequence s, int start, int count,
										int after) {
								}

								@Override
								public void afterTextChanged(Editable s) {
								}
								
							});
						}
						
						@Override
						public void error(Exception e) {
							Log.e(TAG, "Error in communication with the Server", e);
							
							Toast.makeText(
									DoctorHomeActivity.this,
									"Communication with the Server failed.",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
			
			@Override
			public void error(Exception e) {
				Log.e(TAG, "Error in communication with the Server", e);
				
				Toast.makeText(
						DoctorHomeActivity.this,
						"Communication with the Server failed.",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	protected void onResume(){
		super.onResume();
		tvPatientFilter.setText("");
		
	}
	
	public void setDoctor(Doctor doc){
		this.doctor = doc;
	}
	
	public void setPatients(ArrayList<Patient> patients){
		this.patients = patients;
	}
}
