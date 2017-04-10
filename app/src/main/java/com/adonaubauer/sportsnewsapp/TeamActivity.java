package com.adonaubauer.sportsnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Austin on 4/9/2017.
 */

public class TeamActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    String[] nflTeamNames;
    String[] nbaTeamNames;
    String[] mlbTeamNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        toolbar = (Toolbar) findViewById(R.id.toolbar_team);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_team);

        viewPager = (ViewPager) findViewById(R.id.pager);

        setSupportActionBar(toolbar);

        nflTeamNames = getResources().getStringArray(R.array.nfl_team_names);

        nbaTeamNames = getResources().getStringArray(R.array.nba_team_names);

        mlbTeamNames = getResources().getStringArray(R.array.mlb_team_names);

        Intent getTeamNameIntent = getIntent();

        String clickedTeam = getTeamNameIntent.getStringExtra("Clicked Team");

        for (String team : nflTeamNames) {

            if (clickedTeam.equals(team)) {

                for (String teamName : getResources().getStringArray(R.array.nfl_team_names)) {

                    tabLayout.addTab(tabLayout.newTab().setText(teamName));

                }

                switchToTab(team);

            }

        }

        /*for (String teamName : getResources().getStringArray(R.array.nba_team_names)) {

            tabLayout.addTab(tabLayout.newTab().setText(teamName));

        }

        for (String teamName : getResources().getStringArray(R.array.mlb_team_names)) {

            tabLayout.addTab(tabLayout.newTab().setText(teamName));

        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_action_bar_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.action_settings:

                Intent userPreferencesIntent = new Intent(this, MyPreferencesActivity.class);
                startActivity(userPreferencesIntent);

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }

    public void switchToTab(String tab) {

        if(tab.equals("Arizona Cardinals")){
            viewPager.setCurrentItem(0);
        }else if(tab.equals("Atlanta Falcons")){
            viewPager.setCurrentItem(1);
        }else if(tab.equals("Baltimore Ravens")){
            viewPager.setCurrentItem(2);
        }else if(tab.equals("Buffalo Bills")){
            viewPager.setCurrentItem(3);
        }else if(tab.equals("Carolina Panthers")){
            viewPager.setCurrentItem(4);
        }else if(tab.equals("Chicago Bears")){
            viewPager.setCurrentItem(5);
        }else if(tab.equals("Cincinnati Bengals")){
            viewPager.setCurrentItem(6);
        }else if(tab.equals("Cleveland Browns")){
            viewPager.setCurrentItem(7);
        }else if(tab.equals("Dallas Cowboys")){
            viewPager.setCurrentItem(8);
        }else if(tab.equals("Denver Broncos")){
            viewPager.setCurrentItem(9);
        }else if(tab.equals("Detroit Lions")){
            viewPager.setCurrentItem(10);
        }else if(tab.equals("Green Bay Packers")){
            viewPager.setCurrentItem(11);
        }else if(tab.equals("Houston Texans")){
            viewPager.setCurrentItem(12);
        }else if(tab.equals("Indianapolis Colts")){
            viewPager.setCurrentItem(13);
        }else if(tab.equals("Jacksonville Jaguars")){
            viewPager.setCurrentItem(14);
        }else if(tab.equals("Kansas City Chiefs")){
            viewPager.setCurrentItem(15);
        }else if(tab.equals("Los Angeles Chargers")){
            viewPager.setCurrentItem(16);
        }else if(tab.equals("Los Angeles Rams")){
            viewPager.setCurrentItem(17);
        }else if(tab.equals("Miami Dolphins")){
            viewPager.setCurrentItem(18);
        }else if(tab.equals("Minnesota Vikings")){
            viewPager.setCurrentItem(19);
        }else if(tab.equals("New England Patriots")){
            viewPager.setCurrentItem(20);
        }else if(tab.equals("New Orleans Saints")){
            viewPager.setCurrentItem(21);
        }else if(tab.equals("New York Giants")){
            viewPager.setCurrentItem(22);
        }else if(tab.equals("New York Jets")){
            viewPager.setCurrentItem(23);
        }else if(tab.equals("Oakland Raiders")){
            viewPager.setCurrentItem(24);
        }else if(tab.equals("Philadelphia Eagles")){
            viewPager.setCurrentItem(25);
        }else if(tab.equals("Pittsburgh Steelers")){
            viewPager.setCurrentItem(26);
        }else if(tab.equals("San Francisco 49ers")){
            viewPager.setCurrentItem(27);
        }else if(tab.equals("Seattle Seahawks")){
            viewPager.setCurrentItem(28);
        }else if(tab.equals("Tampa Bay Buccaneers")){
            viewPager.setCurrentItem(29);
        }else if(tab.equals("Tennessee Titans")){
            viewPager.setCurrentItem(28);
        }else if(tab.equals("Washington Redskins")){
            viewPager.setCurrentItem(29);
        }

    }

}