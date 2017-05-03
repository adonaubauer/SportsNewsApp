package com.adonaubauer.sportsnewsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * MlbViewPagerAdapter is a class that will get each mlbfragments when the tabs are
 * clicked in the mainactivty
 *
 * @see FragmentStatePagerAdapter
 *
 */

public class MlbViewPagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    /**
     *
     * MlbViewPagerAdapter constructor will set the number of tabs
     *
     *
     * @param fragmentManager
     * @param numberOfTabs
     */
    public MlbViewPagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);

        this.numberOfTabs = numberOfTabs;

    }

    /**
     *
     * @see #getItem(int)
     *
     * @param position
     * @return
     */
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

    /**
     *
     * @see #getCount()
     *
     * @return
     */
    @Override
    public int getCount() {

        return numberOfTabs;

    }

}
