package com.adonaubauer.sportsnewsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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

                return new NflTeamsFragment();

            case 1:

                return new NflDivisionStandingsFragment();

            case 2:

                return new NflPlayerStatsFragment();

            default:

                return null;


        }

        /*} else if (sportTitle.equals("NBA")) {

            switch (position) {

                case 0:

                    NbaTeamsFragment nbaTeamsFragment = new NbaTeamsFragment();
                    return nbaTeamsFragment;

                case 1:

                    NbaDivisionStandingsFragment nbaDivisionStandingsFragment = new NbaDivisionStandingsFragment();
                    return nbaDivisionStandingsFragment;

                case 2:

                    NbaPlayerStatsFragment nbaPlayerStatsFragment = new NbaPlayerStatsFragment();
                    return nbaPlayerStatsFragment;

                default:

                    return null;

            }

        } else if (sportTitle.equals("MLB")) {

            switch (position) {

                case 0:

                    MlbTeamsFragment mlbTeamsFragment = new MlbTeamsFragment();
                    return mlbTeamsFragment;

                case 1:

                    MlbDivisionStandingsFragment mlbDivisionStandingsFragment = new MlbDivisionStandingsFragment();
                    return mlbDivisionStandingsFragment;

                case 2:

                    MlbPlayerStatsFragment mlbPlayerStatsFragment = new MlbPlayerStatsFragment();
                    return mlbPlayerStatsFragment;

                default:

                    return null;

            }

        }

        return null;

        */

    }

    @Override
    public int getCount() {

        return numberOfTabs;

    }

}