package com.adonaubauer.sportsnewsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Austin on 4/7/2017.
 */

public class NbaViewPagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public NbaViewPagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);

        this.numberOfTabs = numberOfTabs;

    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:

                return new NbaTeamsFragment();

            case 1:

                return new NbaDivisionStandingsFragment();

            case 2:

                return new NbaPlayerStatsFragment();

            default:

                return null;


        }

    }

    @Override
    public int getCount() {

        return numberOfTabs;

    }

}
