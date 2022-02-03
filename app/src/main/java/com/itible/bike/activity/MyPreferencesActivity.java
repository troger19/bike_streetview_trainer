package com.itible.bike.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.itible.bike.R;


public class MyPreferencesActivity extends PreferenceActivity {

    public static final String NUMBER_OF_SERIES_PREF = "number_of_series_preference";
    public static final String PAUSE_PREF = "pause_preference";
    public static final String MOBILE_NUMBER_PREF = "mobile_number_preference";
    final static String SENDING_SMS = "alarm_preference";
    public final static String USER_PREF = "user_preference";
    public final static String EXERCISE_NAME_PREF = "exercise_name_preference";
    public final static String BIKE_MULTIPLICATOR = "bike_paddle_multiplicator_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }
    }
}