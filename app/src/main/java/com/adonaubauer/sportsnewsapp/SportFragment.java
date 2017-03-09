package com.adonaubauer.sportsnewsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Austin on 3/7/2017.
 */

public class SportFragment extends Fragment {

    public static final String ARG_SPORT_NUMBER = "sport_number";

    public SportFragment() {



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_main, container, false);

        int i = getArguments().getInt(ARG_SPORT_NUMBER);

        String sport = getResources().getStringArray(R.array.sports_array)[i];

        getActivity().setTitle(sport);

        return rootView;

    }

}
