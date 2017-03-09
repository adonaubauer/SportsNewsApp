package com.adonaubauer.sportsnewsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Austin on 2/25/2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar actionBar;

    CharSequence title;
    String[] sportsTitles;

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

            /*case R.id.action_websearch:

                SearchView searchView = (SearchView) menuItem.getActionView();

                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

                if (searchManager != null) {

                    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

                }

                searchView.setIconifiedByDefault(false);

                return true;

            */

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

                return true;

            case R.id.NFL_TEAMS:

                getSupportActionBar().setTitle(R.string.nfl_teams);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.NFL_STATS:

                getSupportActionBar().setTitle(R.string.nfl_stats);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.NFL_STANDINGS:

                getSupportActionBar().setTitle(R.string.nfl_standings);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.NBA_NEWS:

                getSupportActionBar().setTitle(R.string.nba_sports_news);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.NBA_TEAMS:

                getSupportActionBar().setTitle(R.string.nba_teams);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.NBA_STATS:

                getSupportActionBar().setTitle(R.string.nba_stats);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.NBA_STANDINGS:

                getSupportActionBar().setTitle(R.string.nba_standings);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.MLB_NEWS:

                getSupportActionBar().setTitle(R.string.mlb_sports_news);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.MLB_TEAMS:

                getSupportActionBar().setTitle(R.string.mlb_teams);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.MLB_STATS:

                getSupportActionBar().setTitle(R.string.mlb_stats);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

            case R.id.MLB_STANDINGS:

                getSupportActionBar().setTitle(R.string.mlb_standings);

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;

    }

}
