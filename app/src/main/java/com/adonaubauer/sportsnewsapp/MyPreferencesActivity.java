package com.adonaubauer.sportsnewsapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Austin on 3/7/2017.
 */

public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment())
                .commit();


    }

}
