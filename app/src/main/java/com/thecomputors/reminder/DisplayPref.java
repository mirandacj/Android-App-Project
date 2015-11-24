package com.thecomputors.reminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;

/**
 * Created by mitchem on 11/15/2015.
 */
public class DisplayPref extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        // TODO update preferences
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(key);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DisplayActivity.class);
        startActivity(i);
        finish();
        super.onStop();
    }

    private void updatePreference(String key){
        Preference pref = findPreference(key);

        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
            return;
        }

        if (pref instanceof EditTextPreference){
            EditTextPreference editPref =  (EditTextPreference) pref;
            editPref.setSummary(editPref.getText());
            return;
        }

        if (pref instanceof RingtonePreference) {
            Uri ringtoneUri = Uri.parse(Reminder.getRingtone());
            Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
            if (ringtone != null) pref.setSummary(ringtone.getTitle(this));
        }
    }
}
