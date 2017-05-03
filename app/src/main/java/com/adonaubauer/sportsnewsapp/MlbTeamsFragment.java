package com.adonaubauer.sportsnewsapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * MlbTeamsFragment is a list of MlbTeams
 *
 * @see ListFragment
 */

public class MlbTeamsFragment extends ListFragment {

    /**
     *
     * @see #onCreate(Bundle)
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, getResources().getStringArray(R.array.mlb_team_names)));

    }

    /**
     *
     * @see #onListItemClick(ListView, View, int, long)
     *
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String clickedTeam = (String) l.getItemAtPosition(position);

        Intent mlbTeamActivity = new Intent(getActivity().getApplicationContext(), TeamActivity.class);

        mlbTeamActivity.putExtra("Clicked Team", clickedTeam);

        startActivity(mlbTeamActivity);

    }

}
