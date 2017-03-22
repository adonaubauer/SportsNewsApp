package com.adonaubauer.sportsnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Austin on 2/25/2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar actionBar;
    TabLayout tabLayout;
    TabItem news;
    TabItem teams;
    TabItem standings;
    TabItem stats;
    int numTabSelected;
    TextView welcomeMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(actionBar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, actionBar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);

        drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer_view);

        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        news = (TabItem) findViewById(R.id.news);

        teams = (TabItem) findViewById(R.id.teams);

        standings = (TabItem) findViewById(R.id.standings);

        stats = (TabItem) findViewById(R.id.stats);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        welcomeMessage = (TextView) findViewById(R.id.welcome_message);

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.NFL_NEWS:

                getSupportActionBar().setTitle(R.string.nfl_sports_news);

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.NFL_TEAMS:

                getSupportActionBar().setTitle(R.string.nfl_teams);

                NflTeamsFragment nflTeamsFragment = new NflTeamsFragment();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, nflTeamsFragment).commit();

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nflTeamsFragment).commit();

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.NFL_STATS:

                getSupportActionBar().setTitle(R.string.nfl_stats);

                NflPlayerStatsFragment nflPlayerStatsFragment = new NflPlayerStatsFragment();

                nflPlayerStatsFragment.setArguments(getIntent().getExtras());

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, nflPlayerStatsFragment).commit();

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nflPlayerStatsFragment).commit();

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.NFL_STANDINGS:

                getSupportActionBar().setTitle(R.string.nfl_standings);

                NflDivisionStandingsFragment nflDivisionStandingsFragment = new NflDivisionStandingsFragment();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, nflDivisionStandingsFragment).commit();

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nflDivisionStandingsFragment).commit();

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.NBA_NEWS:

                getSupportActionBar().setTitle(R.string.nba_sports_news);

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.NBA_TEAMS:

                getSupportActionBar().setTitle(R.string.nba_teams);

                NbaTeamsFragment nbaTeamsFragment = new NbaTeamsFragment();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, nbaTeamsFragment).commit();

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nbaTeamsFragment).commit();

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.NBA_STATS:

                getSupportActionBar().setTitle(R.string.nba_stats);

                NbaPlayerStatsFragment nbaPlayerStatsFragment = new NbaPlayerStatsFragment();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, nbaPlayerStatsFragment).commit();

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nbaPlayerStatsFragment).commit();

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.NBA_STANDINGS:

                getSupportActionBar().setTitle(R.string.nba_standings);

                NbaDivisionStandingsFragment nbaDivisionStandingsFragment = new NbaDivisionStandingsFragment();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, nbaDivisionStandingsFragment).commit();

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nbaDivisionStandingsFragment).commit();

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.MLB_NEWS:

                getSupportActionBar().setTitle(R.string.mlb_sports_news);

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.MLB_TEAMS:

                getSupportActionBar().setTitle(R.string.mlb_teams);

                MlbTeamsFragment mlbTeamsFragment = new MlbTeamsFragment();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mlbTeamsFragment).commit();

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mlbTeamsFragment).commit();

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.MLB_STATS:

                getSupportActionBar().setTitle(R.string.mlb_stats);

                MlbPlayerStatsFragment mlbPlayerStatsFragment = new MlbPlayerStatsFragment();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mlbPlayerStatsFragment).commit();

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mlbPlayerStatsFragment).commit();

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.MLB_STANDINGS:

                getSupportActionBar().setTitle(R.string.mlb_standings);

                MlbDivisionStandingsFragment mlbDivisionStandingsFragment = new MlbDivisionStandingsFragment();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mlbDivisionStandingsFragment).commit();

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mlbDivisionStandingsFragment).commit();

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;

    }

}
