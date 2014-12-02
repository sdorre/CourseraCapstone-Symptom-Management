package com.capstone.symptommanagement.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class CheckInActivity extends FragmentActivity 
							implements CheckInQuestionFragment.OnAnswerSelectedListener,
										CheckInMedicationFragment.OnMedicationSubmittedListener {

	private static String TAG = "CheckInActivity";
	private static int PAIN_ID = 0;
	private static int EATING_ID = 1;
	
	private ViewPager pager = null;
	private PagerAdapter mPagerAdapter = null;
	private List<Fragment> fragments = null;
	
	private String server = null;
	private String username = null;
	private String password = null;
	
	private Patient patient = null;
	private CheckIn checkIn = null;
	private ArrayList<Medication> medicationList = null;
	private Time time = null;
	
	private boolean submitted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_check_in);
		
		SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
		String hostname = sharedPrefs.getString("preference_server_hostname", "NULL");
		String port  = sharedPrefs.getString("preference_server_port", "NULL");
		server = hostname+":"+port;
		
		// Creation de la liste de Fragments que fera d�filer le PagerAdapter
		fragments = new Vector<Fragment>();

		// Ajout des Fragments dans la liste
		fragments.add(CheckInQuestionFragment.newInstance(PAIN_ID, CheckIn.PAIN_QUESTION, CheckIn.PainState.names()));
		fragments.add(CheckInQuestionFragment.newInstance(EATING_ID, CheckIn.EATING_QUESTION, CheckIn.Eating.names()));
	
		
		Bundle extras = getIntent().getExtras();
		username = extras.getString("username");
		password = extras.getString("password");
		
		time = new Time();
		time.setToNow();
		
		checkIn = new CheckIn();
		medicationList = new ArrayList<Medication>();
		
		final PatientSvcApi patientsvc = PatientSvc.init(server, username, password);
		
		// Get Patient's information
		CallableTask.invoke(new Callable<Collection<Patient>>() {

			@Override
			public Collection<Patient> call() throws Exception {
				
				return patientsvc.findByLogin(username);
			}
		}, new TaskCallback<Collection<Patient>>() {

			@Override
			public void success(Collection<Patient> result) {
		
				if(!result.isEmpty()){
					final Patient me = result.iterator().next();
					Log.i(TAG, "I'm "+me.getName()+" "+me.getLastname()+". My doctor is "+me.getDoctor().getLogin());
					//save Patient entity in Activity
					setPatient(me);
					setMedicationList(me);
					
					// we create a fragment for each element in the medication prescription for this patient
					if(medicationList.size()<1){
						fragments.add(CheckInMedicationFragment.newInstance(2, "pain medication"));
					}else{
						for (int i=0;i<medicationList.size();i++){
							fragments.add(CheckInMedicationFragment.newInstance((i+2), medicationList.get(i).getName()));
						}
					}
					mPagerAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void error(Exception e) {
				Log.e(TAG, "Error in communication with the Server", e);
				
				Toast.makeText(
						CheckInActivity.this,
						"Communication with the Server failed.",
						Toast.LENGTH_SHORT).show();
			}
		});
		

		
		
		// Cr�ation de l'adapter qui s'occupera de l'affichage de la liste de
		// Fragments
		this.mPagerAdapter = new CheckInPagerAdapter(super.getSupportFragmentManager(), fragments);

		pager = (ViewPager) super.findViewById(R.id.checkInPager);
		// Affectation de l'adapter au ViewPager
		pager.setAdapter(this.mPagerAdapter);
	}

	
	@Override
	public void answer(int id, String answ) {
		
		// save the given answer depending on the ID received
		if(id==PAIN_ID){
			Log.i(TAG, "how is your pain : "+answ);
			checkIn.setPainstate(PainState.valueOf(answ));
		
		}else if(id==EATING_ID){
			Log.i(TAG, "can you eat some ? "+answ);
			checkIn.setEating(Eating.valueOf(answ));
		
		}else{
			// there is no case to come here.
		}
		
		Log.i(TAG, "move to next view. from "+pager.getCurrentItem()+" to "+pager.getCurrentItem()+1);
		// move to the next page
		pager.setCurrentItem(pager.getCurrentItem()+1);
		
		
	}

	@Override
	public void medicationTaken(int index, String med, boolean isTaken,
			String date) {
		Medication medication = new Medication(med, isTaken, date);
		medicationList.set(index, medication);
		
		//If the user is on the last page... We need to save its Check-in.
		if(fragments.size()-1==pager.getCurrentItem()){
			if(!submitted){
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
				
				checkIn.setPatient(patient);
				checkIn.setMedicationlist(new HashSet<Medication>(medicationList));
				checkIn.setDate(df.format(new Date()));
				checkIn.setId(checkIn.hashCode());
				
				Log.i(TAG, "Checkin created ate the date : " + df.format(new Date()));
				
				final CheckInSvcApi checkInSvc = CheckInSvc.init(server, username, password);
				
				// Get Patient's information
				CallableTask.invoke(new Callable<Boolean>() {
	
					@Override
					public Boolean call() throws Exception {
						
						return checkInSvc.addCheckIn(checkIn);
					}
				}, new TaskCallback<Boolean>() {
	
					@Override
					public void success(Boolean result) {
						Toast.makeText(
								CheckInActivity.this,
								"Check-in added to the server.",
								Toast.LENGTH_SHORT).show();
						
						// the user don't need to save his check-in twice.
						submitted = true;
					}
					
					@Override
					public void error(Exception e) {
						Log.e(TAG, "Error in communication with the Server", e);
						
						Toast.makeText(
								CheckInActivity.this,
								"Communication with the Server failed.",
								Toast.LENGTH_SHORT).show();
					}
				});
			
			}else{	// already submitted his check-in
				Toast.makeText(
						CheckInActivity.this,
						"You have already save this record.",
						Toast.LENGTH_SHORT).show();
			}
		}
		
		//move to the next page
		Log.i(TAG, "mmove to next view. from "+pager.getCurrentItem()+" to "+(pager.getCurrentItem()+1));
		
		pager.setCurrentItem(pager.getCurrentItem()+1);
		Log.i(TAG, "move to the next medication. fragment size :"+fragments.size()+" nd current page : "+pager.getCurrentItem());
		

	}
	
	private void setPatient(Patient p){
		this.patient = p;
	}
	
	private void setMedicationList(Patient p){
		ArrayList<String> medList = p.getMedicationlist();
		
		if(medList.isEmpty()){
			Log.e(TAG, "There are no medication for this patient !");
		}else{
			for(int i =0;i<medList.size();i++){
				String name = medList.get(i);
				Log.i(TAG,"Create a new Medication with the name : "+name);
				Medication med = new Medication(name);
				medicationList.add(med);
			}
		}
	}
}