package com.capstone.symptommanagement.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.capstone.symptommanagement.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckInMedicationFragment extends Fragment implements OnClickListener {

	// Callback interface to send data to the main Acitivty (CheckInActivity)
	public interface OnMedicationSubmittedListener {
        public void medicationTaken(int index, String medication, boolean isTaken, String date);
	}
	
	public static String MEDICATION = "medication";
	public static String INDEX = "index";
	public static String QUESTION = "Did you take \n your %s ?";
	
	private TextView tvQuestion;
	private TextView tvAnswerYES;
	private TextView tvAnswerNO;
	
	private TextView tvDate;
	private EditText etDate;
	
	private TextView tvTimeHour;
	private EditText etTimeHour;
	private TextView tvTimeMinute;
	private EditText etTimeMinute;
	private Button bSubmit;
	
	private OnMedicationSubmittedListener mCallback;
	private String medication;
	private int index;
	
	public static final CheckInMedicationFragment newInstance(int index, String medication) {
		CheckInMedicationFragment fragment = new CheckInMedicationFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(INDEX, index);
		bundle.putString(MEDICATION, medication);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_check_in_medication, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tvQuestion = (TextView)getView().findViewById(R.id.check_in_question_textView);
		tvAnswerYES = (TextView)getView().findViewById(R.id.check_in_answerYES_textView);
		tvAnswerNO = (TextView)getView().findViewById(R.id.check_in_answerNO_textView);
		
		// label for Date entry
		tvDate = (TextView)getView().findViewById(R.id.check_in_date_textView);
		tvDate.setVisibility(View.INVISIBLE);
		
		// entry for user to give the actual date
		etDate = (EditText)getView().findViewById(R.id.check_in_date_editText);
		etDate.setVisibility(View.INVISIBLE);
		
		// label for hours and minutes
		tvTimeHour = (TextView)getView().findViewById(R.id.check_in_time_textView);
		tvTimeHour.setVisibility(View.INVISIBLE);
		tvTimeMinute = (TextView)getView().findViewById(R.id.check_in_time_minute_textView);
		tvTimeMinute.setVisibility(View.INVISIBLE);
		
		//entries for the user to give hours et minutes when he take his medication 
		etTimeHour = (EditText)getView().findViewById(R.id.check_in_time_hour_editText);
		etTimeMinute = (EditText)getView().findViewById(R.id.check_in_time_minute_editText);
		etDate.setVisibility(View.INVISIBLE);
		etTimeHour.setVisibility(View.INVISIBLE);
		etTimeMinute.setVisibility(View.INVISIBLE);
		
		bSubmit = (Button)getView().findViewById(R.id.check_in_submit_button);
		
		tvAnswerYES.setOnClickListener(this);
		tvAnswerNO.setOnClickListener(this);
		bSubmit.setOnClickListener(this);
		
		index = getArguments().getInt(INDEX);
		medication = getArguments().getString(MEDICATION);
		setQuestion(medication);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnMedicationSubmittedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMedicationSubmittedListener");
        }
	}

	
	public void setQuestion(String medication){
		tvQuestion.setText(String.format(QUESTION, medication));
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case(R.id.check_in_answerYES_textView):
			
			// check the current time to fill automatically visual elements
			Time now = new Time();
			now.setToNow();
			
			// hide all Elements to select a date and a time
			tvDate.setVisibility(View.VISIBLE);	
			tvTimeHour.setVisibility(View.VISIBLE);
			tvTimeMinute.setVisibility(View.VISIBLE);
		
			etDate.setVisibility(View.VISIBLE);
			etTimeHour.setVisibility(View.VISIBLE);
			etTimeMinute.setVisibility(View.VISIBLE);
			
			etDate.setText(now.toString().substring(0, 4)+"-"+(now.toString().substring(4,6))+"-"+now.toString().substring(6, 8));
			etTimeHour.setText(now.toString().substring(9,11));
			etTimeMinute.setText(now.toString().substring(11, 13));
			
			break;
		
		case(R.id.check_in_answerNO_textView):
			
			// hide all Elements to select a date and a time
			tvDate.setVisibility(View.INVISIBLE);	
			tvTimeHour.setVisibility(View.INVISIBLE);
			tvTimeMinute.setVisibility(View.INVISIBLE);
		
			etDate.setVisibility(View.INVISIBLE);
			etTimeHour.setVisibility(View.INVISIBLE);
			etTimeMinute.setVisibility(View.INVISIBLE);
			
			// call the callback to store the user answer directly
			mCallback.medicationTaken(index-2, medication, false, null);
			break;
			
		case(R.id.check_in_submit_button):
			String date = etDate.getText().toString();
			
			if(Pattern.matches("\\d{4}(-\\d{2}){2}", date)){
	
				String time = etTimeHour.getText()+":"+etTimeMinute.getText();
				Log.i("MedicationFragment", "date given = "+date+" and time : "+time);
				mCallback.medicationTaken(index-2, medication, true, date.concat("T"+time+"Z"));
			
			}else{				
				Toast.makeText(getActivity(),
					"the date you enter is invalid", 
					Toast.LENGTH_SHORT)
					.show();
			}
			break;
		}
	}
}
