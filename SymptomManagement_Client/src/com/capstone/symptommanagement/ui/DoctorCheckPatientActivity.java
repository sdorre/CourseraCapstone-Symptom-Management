package com.capstone.symptommanagement.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Callable;

import com.capstone.symptommanagement.CallableTask;
import com.capstone.symptommanagement.R;
import com.capstone.symptommanagement.TaskCallback;
import com.capstone.symptommanagement.client.CheckInSvc;
import com.capstone.symptommanagement.client.CheckInSvcApi;
import com.capstone.symptommanagement.client.PatientSvc;
import com.capstone.symptommanagement.client.PatientSvcApi;
import com.capstone.symptommanagement.core.CheckIn;
import com.capstone.symptommanagement.core.CheckIn.Eating;
import com.capstone.symptommanagement.core.CheckIn.PainState;
import com.capstone.symptommanagement.core.Medication;
import com.capstone.symptommanagement.core.Patient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorCheckPatientActivity extends Activity{

	private static final String TAG = "SymptomMgmt.DoctorCheckPatient";
	
	private String server;
	private Patient patient = null;
	private Collection<CheckIn> checkIns = null;
	private PatientSvcApi patientsvc = null;
	private CheckInSvcApi checkInScv = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_patient_doctor);
		
		SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
		String hostname = sharedPrefs.getString("preference_server_hostname", "NULL");
		String port  = sharedPrefs.getString("preference_server_port", "NULL");
		server = hostname+":"+port;
		
		final TextView tvPatientId = (TextView)findViewById(R.id.doctor_patient_information);
		final EditText etPatientMedication = (EditText)findViewById(R.id.doctor_patient_medication);
		Button bCheckMedication = (Button)findViewById(R.id.doctor_patient_updateMedicationButton);
		
		final TextView tvPatientData = (TextView)findViewById(R.id.doctor_patient_checkin_data);
		
		bCheckMedication.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String med = etPatientMedication.getText().toString();
				String[] medication = med.split(",");
				
				patient.setMedicationlist(new ArrayList<String>(Arrays.asList(medication)));
				
				Log.i(TAG, "new medication for patient "+patient.getLastname()+" : ("+medication.length+") "+medication.toString());
				
				CallableTask.invoke(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						
						return patientsvc.addPatient(patient);
					}
				}, new TaskCallback<Boolean>() {

					@Override
					public void success(Boolean result) {
						
						if(result){
							Log.i(TAG, "medication correctly updated !");
							
							Toast.makeText(getApplicationContext(), "New medication correctly updated.",
											Toast.LENGTH_SHORT).show();
						}
					}
						
					@Override
					public void error(Exception e) {
						Log.e(TAG, "Error in communication with the Server", e);
						
						Toast.makeText(
								DoctorCheckPatientActivity.this,
								"Communication with the Server failed.",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			
		});
		
		Bundle extras = getIntent().getExtras();
		final String patientLogin = extras.getString("patientLogin");
		final String username = extras.getString("username");
		final String password = extras.getString("password");
		
		patientsvc = PatientSvc.init(server, username, password);
		checkInScv = CheckInSvc.init(server, username, password);
		
		CallableTask.invoke(new Callable<Collection<Patient>>() {

			@Override
			public Collection<Patient> call() throws Exception {
				
				return patientsvc.findByLogin(patientLogin);
			}
		}, new TaskCallback<Collection<Patient>>() {

			@Override
			public void success(Collection<Patient> result) {
				
				if(result!=null){
					
					Patient pat = result.iterator().next();
					setPatient(pat);
					
					tvPatientId.setText("Patient : " +patient.getLastname()+ " " +patient.getName()+"\n"
								+"birth:"+patient.getBirthdate()+"\n"
								+"number:"+ patient.getMedicalnumber());
					if(patient.getMedicationlist()!=null){
						Iterator<String> it = patient.getMedicationlist().iterator();
						String medication = "";
						while(it.hasNext()){
							medication = medication + it.next() + ",";
						}
						etPatientMedication.setText(medication);
						Log.i(TAG, patient.getId()+","+patient.getName()+","+patient.getLastname()+","+patient.getBirthdate()+","
								+patient.getMedicalnumber()+","+patient.getLogin()+","+patient.getDoctor());
					}
					
					CallableTask.invoke(new Callable<Collection<CheckIn>>() {

						@Override
						public Collection<CheckIn> call() throws Exception {
							
							return checkInScv.findByPatient(patient);
						}
					}, new TaskCallback<Collection<CheckIn>>() {

						@Override
						public void success(Collection<CheckIn> result) {
							
							if(result!=null){
								setCheckInList(result);
								Log.i(TAG, "get all check-In for this patient");
								int number = result.size();
								int painStateBAD = 0;
								int painStateWELL = 0;
								int painStateMODERATE = 0;
								
								int eatingNO = 0;
								int eatingYES = 0;
								int eatingSOME = 0;

								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
								
								ArrayList<Medication> medication = null;
								int medicationNotTaken = 0;
								
								for(CheckIn item : result){
									if(item.getPainstate()==PainState.SEVERE)
										painStateBAD++;
									else if(item.getPainstate()==PainState.MODERATE)
										painStateMODERATE++;
									else if(item.getPainstate()==PainState.WELL_CONTROLLED)
										painStateWELL++;
									
									
									if(item.getEating()==Eating.CANT_EAT)
										eatingYES++;
									else if(item.getEating()==Eating.NO)
										eatingNO++;
									else if(item.getEating()==Eating.SOME)
										eatingSOME++;
									
									medication = new ArrayList<Medication>(item.getMedicationlist());
									for (Medication med : medication){
										if(!med.getTaken()){
											medicationNotTaken++;
										}
									}
									
									try {
										Date dateCheck = df.parse(item.getDate());
										Log.i(TAG, "CheckIn date recreated : "+dateCheck);
										Date now = new Date();
										Log.i(TAG, "difference between today and this CheckIn is (in hours):"
												+( ( now.getTime()-dateCheck.getTime() ) /3600000) );
									} catch (ParseException e) {
										e.printStackTrace();
									}
									
									
								}
								Log.i(TAG, "RESULT : on "+number+" checkIn : can eat="+eatingNO+" some ="+eatingSOME+" stop eat="+eatingYES);
								Log.i(TAG, "RESULT : on "+number+" checkIn : BAD="+painStateBAD+" WELL="+painStateWELL+" MODERATE="+painStateMODERATE);
								tvPatientData.setText("on "+number+" checkIn :\n"+
													"can eat="+eatingNO+"\n" +
													"some ="+eatingSOME+"\n" +
													"can't eat="+eatingYES+"\n"+
													"feels bad="+painStateBAD+"\n" +
													"feels well="+painStateWELL+"\n" +
													"feels moderate="+painStateMODERATE);
								
							}
						}
							
						@Override
						public void error(Exception e) {
							Log.e(TAG, "Error in communication with the Server", e);
							
							Toast.makeText(
									DoctorCheckPatientActivity.this,
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
						DoctorCheckPatientActivity.this,
						"Communication with the Server failed.",
						Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	private void setPatient(Patient pat){
		this.patient = pat;
	}
	
	private void setCheckInList(Collection<CheckIn> checkIns){
		this.checkIns = checkIns;
	}
}
