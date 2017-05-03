package com.adonaubauer.sportsnewsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;

/**
 * Created by Austin on 4/7/2017.
 */

public class NflViewPagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public NflViewPagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);

        this.numberOfTabs = numberOfTabs;

    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:

                return NflTeamsFragment.newInstance(position);

            case 1:

                return NflDivisionStandingsFragment.newInstance(position);

            case 2:

                return NflPlayerStatsFragment.newInstance(position);

            default:

                return null;


        }

    }

    @Override
    public int getCount() {

        return numberOfTabs;

    }
}