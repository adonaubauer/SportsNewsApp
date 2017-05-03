package com.adonaubauer.sportsnewsapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Austin on 3/15/2017.
 */

public class NflTeamsFragment extends ListFragment {

    int mNum;

    static NflTeamsFragment newInstance(int num) {

        NflTeamsFragment nflTeamsFragment = new NflTeamsFragment();

        Bundle args = new Bundle();

        args.putInt("num", num);

        nflTeamsFragment.setArguments(args);

        return nflTeamsFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNum = getArguments() != null ? getArguments().getInt("num") : 1;

        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, getResources().getStringArray(R.array.nfl_team_names)));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String clickedTeam = (String) l.getItemAtPosition(position);

        Intent nflTeamActivity = new Intent(getActivity().getApplicationContext(), TeamActivity.class);

        nflTeamActivity.putExtra("Clicked Team", clickedTeam);

        startActivity(nflTeamActivity);

    }
}
