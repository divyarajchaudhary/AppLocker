package com.gueei.applocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class AppLockerPreferenceActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.addPreferencesFromResource(R.xml.preferences);
	    PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(serviceEnabledListener);
	}
	
	OnSharedPreferenceChangeListener serviceEnabledListener = new OnSharedPreferenceChangeListener(){
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (key.equals("service_enabled")){
				if (sharedPreferences.getBoolean(key, false))
					startService();
				else
					stopService();
			}
		}
	};

	private void stopService() {
		this.stopService(new Intent(this, DetectorService.class));
	}

	private void startService() {
		Intent startService = new Intent(this, DetectorService.class);
		this.startService(startService);
	}
}