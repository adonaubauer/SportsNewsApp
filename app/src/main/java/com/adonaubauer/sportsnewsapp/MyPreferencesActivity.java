package com.adonaubauer.sportsnewsapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * MyPreferencesActivity will get the preference fragment
 *
 * @see PreferenceActivity
 *
 */

public class MyPreferencesActivity extends PreferenceActivity {

    /**
     *
     * @see #onCreate(Bundle)
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment())
                .commit();


    }

}
