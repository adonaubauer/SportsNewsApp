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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        toolbar = (Toolbar) findViewById(R.id.toolbar_team);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_team);

        viewPager = (ViewPager) findViewById(R.id.pager);

        setSupportActionBar(toolbar);

        for (String teamName : getResources().getStringArray(R.array.nfl_team_names)) {

            tabLayout.addTab(tabLayout.newTab().setText(teamName));

        }

        for (String teamName : getResources().getStringArray(R.array.nba_team_names)) {

            tabLayout.addTab(tabLayout.newTab().setText(teamName));

        }

        for (String teamName : getResources().getStringArray(R.array.mlb_team_names)) {

            tabLayout.addTab(tabLayout.newTab().setText(teamName));

        }

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

}