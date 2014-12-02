package com.capstone.symptommanagement.ui;

import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.Callable;

import com.capstone.symptommanagement.CallableTask;
import com.capstone.symptommanagement.CheckInNotificationReceiver;
import com.capstone.symptommanagement.R;
import com.capstone.symptommanagement.TaskCallback;
import com.capstone.symptommanagement.client.PatientSvc;
import com.capstone.symptommanagement.client.PatientSvcApi;
import com.capstone.symptommanagement.core.Patient;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class PatientHomeActivity extends Activity implements OnClickListener {
	
	static final String TAG = "PatientHomeActivity";
	
	static final int ID_ALARM_1 = 0;
	static final int ID_ALARM_2 = 1;
	static final int ID_ALARM_3 = 2;
	static final int ID_ALARM_4 = 3;
	
	private int pHour;
	private int pMinute;

	private SharedPreferences sharedPrefs;
	private SharedPreferences.Editor editor;
	
	private String server;
	private String username;
	private String password;
	
	private Patient patient;
	
	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private PendingIntent mNotificationReceiverPendingIntent1,
							mNotificationReceiverPendingIntent2,
							mNotificationReceiverPendingIntent3,
							mNotificationReceiverPendingIntent4;
	
	private TextView tvPatientInfo;
	private TextView tvAlarm1;
	private TextView tvAlarm2;
	private TextView tvAlarm3;
	private TextView tvAlarm4;
	
	private Switch swAlarm1;
	private Switch swAlarm2;
	private Switch swAlarm3;
	private Switch swAlarm4;
	
	private Button createCheckInButton;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_patient);
		
		sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
		
		editor = sharedPrefs.edit();
		
		String hostname = sharedPrefs.getString("preference_server_hostname", "NULL");
		String port  = sharedPrefs.getString("preference_server_port", "NULL");
		server = hostname+":"+port;
		
		tvPatientInfo = (TextView)findViewById(R.id.patient_home_information);
		Bundle extras = getIntent().getExtras();
		username = extras.getString("username");
		password = extras.getString("password");
		
		mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		
		// Create PendingIntents to start the AlarmNotificationReceiver
		mNotificationReceiverIntent = new Intent(PatientHomeActivity.this,
				CheckInNotificationReceiver.class);
		mNotificationReceiverIntent.putExtra("username", username);
		mNotificationReceiverIntent.putExtra("password", password);
		
		mNotificationReceiverPendingIntent1 = PendingIntent.getBroadcast(
				PatientHomeActivity.this, ID_ALARM_1, mNotificationReceiverIntent, 0);
		mNotificationReceiverPendingIntent2 = PendingIntent.getBroadcast(
				PatientHomeActivity.this, ID_ALARM_2, mNotificationReceiverIntent, 0);
		mNotificationReceiverPendingIntent3 = PendingIntent.getBroadcast(
				PatientHomeActivity.this, ID_ALARM_3, mNotificationReceiverIntent, 0);
		mNotificationReceiverPendingIntent4 = PendingIntent.getBroadcast(
				PatientHomeActivity.this, ID_ALARM_4, mNotificationReceiverIntent, 0);
		
		tvAlarm1 = (TextView)findViewById(R.id.Alarm1ClockTextView);
		tvAlarm2 = (TextView)findViewById(R.id.Alarm2ClockTextView);
		tvAlarm3 = (TextView)findViewById(R.id.Alarm3ClockTextView);
		tvAlarm4 = (TextView)findViewById(R.id.Alarm4ClockTextView);
		
		tvAlarm1.setOnClickListener(this);
		tvAlarm2.setOnClickListener(this);
		tvAlarm3.setOnClickListener(this);
		tvAlarm4.setOnClickListener(this);
		
		swAlarm1 = (Switch)findViewById(R.id.Alarm1Switch);
		swAlarm2 = (Switch)findViewById(R.id.Alarm2Switch);
		swAlarm3 = (Switch)findViewById(R.id.Alarm3Switch);
		swAlarm4 = (Switch)findViewById(R.id.Alarm4Switch);
		
		swAlarm1.setOnClickListener(this);
		swAlarm2.setOnClickListener(this);
		swAlarm3.setOnClickListener(this);
		swAlarm4.setOnClickListener(this);
		
		createCheckInButton = (Button)findViewById(R.id.patient_home_create_check_in_button);
		createCheckInButton.setOnClickListener(this);
		
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
					tvPatientInfo.setText("Patient. "+ me.getName() +" "+ me.getLastname()+"\n"
										+"Birth date :" + me.getBirthdate()+"\n"
										+"Medical ID : "+me.getMedicalnumber()+"\n"
										+"Doctor : "+ me.getDoctor().getName()+" "+me.getDoctor().getLastname());
					
					//save Patient entity in Activity
					setPatient(me);
				}
			}
			
			@Override
			public void error(Exception e) {
				Log.e(TAG, "Error in communication with the Server", e);
				
				Toast.makeText(
						PatientHomeActivity.this,
						"Communication with the Server failed.",
						Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// check if activity was previously shown
		swAlarm1.setChecked(sharedPrefs.getBoolean("alarm1", false));
		swAlarm2.setChecked(sharedPrefs.getBoolean("alarm2", false));
		swAlarm3.setChecked(sharedPrefs.getBoolean("alarm3", false));
		swAlarm4.setChecked(sharedPrefs.getBoolean("alarm4", false));
	}

	@Override
	protected void onPause() {
		super.onPause();
		// save alarms defined by the user
		editor.putBoolean("alarm1",  swAlarm1.isChecked());
		editor.putBoolean("alarm2",  swAlarm2.isChecked());
		editor.putBoolean("alarm3",  swAlarm3.isChecked());
		editor.putBoolean("alarm4",  swAlarm4.isChecked());
		
		editor.commit();
	}

	@Override
	public void onClick(View v){
		
		switch(v.getId()){
		case (R.id.patient_home_create_check_in_button):
			Intent intent = new Intent(this, CheckInActivity.class);
			intent.putExtra("username", username);
			intent.putExtra("password", password);
			startActivity(intent);
			break;
		
		// click on TextView to select the date
		case (R.id.Alarm1ClockTextView):
			showDialog(ID_ALARM_1);
			swAlarm1.setChecked(true);
			break;
			
		case (R.id.Alarm2ClockTextView):
			showDialog(ID_ALARM_2);
			swAlarm2.setChecked(true);
			break;
			
		case (R.id.Alarm3ClockTextView):
			showDialog(ID_ALARM_3);
			swAlarm3.setChecked(true);
			break;
			
		case (R.id.Alarm4ClockTextView):
			showDialog(ID_ALARM_4);
			swAlarm4.setChecked(true);
			break;
	
		// click on swith to de/activate each alarm
		case (R.id.Alarm1Switch):
			if(swAlarm1.isChecked()){
            	setAlarm(mNotificationReceiverPendingIntent1);
            	Toast.makeText(getBaseContext(), "Create a new Alarm 1", Toast.LENGTH_SHORT).show();
			}else{
				mAlarmManager.cancel(mNotificationReceiverPendingIntent1);
            	Toast.makeText(getBaseContext(), "Deleting Alarm 1", Toast.LENGTH_SHORT).show();
			}
			break;
		
		case (R.id.Alarm2Switch):
			if(swAlarm2.isChecked()){
            	setAlarm(mNotificationReceiverPendingIntent2);
            	Toast.makeText(getBaseContext(), "Create a new Alarm 2", Toast.LENGTH_SHORT).show();
			}else{
				mAlarmManager.cancel(mNotificationReceiverPendingIntent2);
            	Toast.makeText(getBaseContext(), "Deleting Alarm 2", Toast.LENGTH_SHORT).show();
			}
			break;
		
		case (R.id.Alarm3Switch):
			if(swAlarm3.isChecked()){
            	setAlarm(mNotificationReceiverPendingIntent3);
            	Toast.makeText(getBaseContext(), "Create a new Alarm 3", Toast.LENGTH_SHORT).show();
			}else{
				mAlarmManager.cancel(mNotificationReceiverPendingIntent3);
            	Toast.makeText(getBaseContext(), "Deleting Alarm 3", Toast.LENGTH_SHORT).show();
			}
			break;
		
		case (R.id.Alarm4Switch):
			if(swAlarm4.isChecked()){
            	setAlarm(mNotificationReceiverPendingIntent4);
            	Toast.makeText(getBaseContext(), "Create a new Alarm 4", Toast.LENGTH_SHORT).show();
			}else{
				mAlarmManager.cancel(mNotificationReceiverPendingIntent1);
            	Toast.makeText(getBaseContext(), "Deleting Alarm 4", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener1 =
	        new TimePickerDialog.OnTimeSetListener() {
	            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            	pHour = hourOfDay;
	            	pMinute = minute;
	            	updateDisplay(tvAlarm1);
	            	setAlarm(mNotificationReceiverPendingIntent1);
	            }
	        };

	private TimePickerDialog.OnTimeSetListener mTimeSetListener2 =
	        new TimePickerDialog.OnTimeSetListener() {
	            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            	pHour = hourOfDay;
	            	pMinute = minute;
	            	updateDisplay(tvAlarm2);
	            }
	        };
	private TimePickerDialog.OnTimeSetListener mTimeSetListener3 =
	        new TimePickerDialog.OnTimeSetListener() {
	            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            	pHour = hourOfDay;
	            	pMinute = minute;
	            	updateDisplay(tvAlarm3);
	            }
	        };
	private TimePickerDialog.OnTimeSetListener mTimeSetListener4 =
	        new TimePickerDialog.OnTimeSetListener() {
	            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            	pHour = hourOfDay;
	            	pMinute = minute;
	            	updateDisplay(tvAlarm4);
	            }
	    	};
	
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case ID_ALARM_1:
            return new TimePickerDialog(this,
                    mTimeSetListener1, pHour, pMinute, false);
        case ID_ALARM_2:
            return new TimePickerDialog(this,
                    mTimeSetListener2, pHour, pMinute, false);
        case ID_ALARM_3:
            return new TimePickerDialog(this,
                    mTimeSetListener3, pHour, pMinute, false);
        case ID_ALARM_4:
            return new TimePickerDialog(this,
                    mTimeSetListener4, pHour, pMinute, false);
        }
        return null;
    }
    
    private void updateDisplay(TextView v){
    	v.setText(new StringBuilder().append(pHour).append(":").append(pMinute));
    }	
    
    private void setAlarm(PendingIntent intent){
    	// Set the alarm to start at the given time.
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(System.currentTimeMillis());
    	calendar.set(Calendar.HOUR_OF_DAY, pHour);
    	calendar.set(Calendar.MINUTE, pMinute);
    	
    	mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 
    								calendar.getTimeInMillis(), 
    								AlarmManager.INTERVAL_DAY,
    								intent);
    	
    	Log.i(TAG, "alarm created. It will ring in "+calendar.getTimeInMillis()+" sec.");
    	Log.i(TAG, "system is in "+System.currentTimeMillis()+" sec.");
    }
    
    public void setPatient(Patient pat){
    	this.patient = pat;
    }
}