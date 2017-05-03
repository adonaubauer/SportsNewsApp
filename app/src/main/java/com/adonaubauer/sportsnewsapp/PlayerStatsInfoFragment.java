package com.adonaubauer.sportsnewsapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Austin on 4/28/2017.
 */

public class PlayerStatsInfoFragment extends ListFragment {

    ArrayList<NflPlayerInfo> playerStatsInfo;

    public PlayerStatsInfoFragment() {

        playerStatsInfo = new ArrayList<>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        setListAdapter(new ArrayAdapter<>(getContext(),layout, playerStatsInfo));

    }

}
