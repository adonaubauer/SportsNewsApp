package com.adonaubauer.sportsnewsapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Austin on 3/7/2017.
 */

public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


    }

}
