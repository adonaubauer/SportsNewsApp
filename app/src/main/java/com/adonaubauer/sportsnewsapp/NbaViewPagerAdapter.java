package com.adonaubauer.sportsnewsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 *
 * The NbaViewPagerAdapter will get and create the NbaFragments
 *
 */

public class NbaViewPagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    /**
     *
     * The NbaViewPagerAdapter constructor will get the number of tabs
     *
     * @param fragmentManager
     * @param numberOfTabs
     */
    public NbaViewPagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
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

                return new NbaTeamsFragment();

            case 1:

                return new NbaDivisionStandingsFragment();

            case 2:

                return new NbaPlayerStatsFragment();

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
