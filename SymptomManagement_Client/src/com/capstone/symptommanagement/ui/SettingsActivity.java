package com.capstone.symptommanagement.ui;

import com.capstone.symptommanagement.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.settings, false);
			 // Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.settings);
	}
}
