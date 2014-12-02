package com.capstone.symptommanagement.ui;

import java.util.ArrayList;
import com.capstone.symptommanagement.R;
import com.capstone.symptommanagement.core.Patient;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PatientArrayAdapter extends ArrayAdapter<Patient>{

	private Context mContext;
	private final ArrayList<Patient> patients;
	private ArrayList<Patient> originPatients = null;
	
	public PatientArrayAdapter(Context cont, ArrayList<Patient> patients){
		super(cont, R.layout.rowlayout_listview_doctor, new ArrayList<Patient>(patients));
		this.mContext = cont;
		this.patients = patients;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// define a custom element in the listview
		View rowView = inflater.inflate(R.layout.rowlayout_listview_doctor, parent, false);
		TextView tvPatientName = (TextView) rowView.findViewById(R.id.doctor_home_listview_row_identitity);
		TextView tvPatientLastCheck = (TextView) rowView.findViewById(R.id.doctor_home_listview_row_patient_state);
		TextView tvPatientID = (TextView) rowView.findViewById(R.id.doctor_home_listview_row_medical_number);
		
		// fill the layout with patient's information
		Patient p = patients.get(position);
		tvPatientName.setText(p.getName()+ " " +p.getLastname());
		tvPatientID.setText("Med ID:" +p.getMedicalnumber());
		tvPatientLastCheck.setText("");
		
		return rowView;
	}
	
	
	public int getCount() {
		return patients.size();
	}

	
	public void filter(String text){

		// copy the patient list
		if (originPatients ==null){
			originPatients = new ArrayList<Patient>(patients);
		}
		//notifyDataSetChanged();
		if(text.isEmpty()){
			Log.i("PatientArrayAdapter", "fiter is empty");
			patients.clear();
			patients.addAll(originPatients);
		}else{
			// clear the list used in the listview
			Log.i("PatientArrayAdapter", "here, we start to filter!");
			patients.clear();
			for(Patient patient:originPatients){
				// add patient with a name that matches with the filter
				String name = patient.getName().toLowerCase();
				if(name.indexOf(text.toLowerCase())>-1){
					//patients.add(patient);
					patients.add(patient);
					Log.i("PatientArrayAdapter","add one element");
				}
			}
			
			
			
		}
		if(patients.size()>0){
			notifyDataSetChanged();
		}else{
			notifyDataSetInvalidated();
		}
	}

	public Patient getItem(int position) {
		return patients.get(position);
	}

	public int getPosition(Patient item) {
		return patients.indexOf(item);
	}

	public long getItemId(int position) {
		return position;
	}
}
