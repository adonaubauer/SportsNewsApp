package com.adonaubauer.sportsnewsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Austin on 4/7/2017.
 */

public class MlbViewPagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public MlbViewPagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);

        this.numberOfTabs = numberOfTabs;

    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:

                return new MlbTeamsFragment();

            case 1:

                return new MlbDivisionStandingsFragment();

            case 2:

                return new MlbPlayerStatsFragment();

            default:

                return null;


        }

    }

    @Override
    public int getCount() {

        return numberOfTabs;

    }

}
