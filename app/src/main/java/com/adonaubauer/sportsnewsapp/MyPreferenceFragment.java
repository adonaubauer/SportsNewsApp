package com.adonaubauer.sportsnewsapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * MyPreferenceFragment adds user preferences from xml preferences
 *
 * @see PreferenceFragment
 *
 */

public class MyPreferenceFragment extends PreferenceFragment {

    /**
     *
     * @see #onCreate(Bundle)
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


    }

}
