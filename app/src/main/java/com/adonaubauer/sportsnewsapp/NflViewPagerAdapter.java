package com.adonaubauer.sportsnewsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;

/**
 * The NflViewPagerAdapter class will create new instances of
 * the nfl fragments
 *
 * @see FragmentStatePagerAdapter
 */

public class NflViewPagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    /**
     *
     * The NflViewPagerAdapter constructor will set the number of tabs
     *
     * @param fragmentManager
     * @param numberOfTabs
     */
    public NflViewPagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
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

                return NflTeamsFragment.newInstance(position);

            case 1:

                return NflDivisionStandingsFragment.newInstance(position);

            case 2:

                return NflPlayerStatsFragment.newInstance(position);

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