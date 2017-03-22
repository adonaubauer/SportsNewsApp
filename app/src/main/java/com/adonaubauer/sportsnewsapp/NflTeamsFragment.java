package com.adonaubauer.sportsnewsapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

/**
 * Created by Austin on 3/15/2017.
 */

public class NflTeamsFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, getResources().getStringArray(R.array.nfl_team_names)));

    }

}
