package com.adonaubauer.sportsnewsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Set;

/**
 * MainActivity is the main activity class for Sportnews app. This is the launcher class
 * it contains a navigation drawer and a tab menu
 *
 * @author Austin
 * @see android.support.v7.app.AppCompatActivity
 * @see android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
 *
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar actionBar;
    TabLayout tabLayout;
    ViewPager viewPager;
    NflViewPagerAdapter nflViewPagerAdapter;
    NbaViewPagerAdapter nbaViewPagerAdapter;
    MlbViewPagerAdapter mlbViewPagerAdapter;
    TextView welcomeMessage;

    int tabPosition;

    /**
     *
     * @see #onCreate(Bundle)
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
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

        tabLayout.addTab(tabLayout.newTab().setText("TEAMS"));

        tabLayout.addTab(tabLayout.newTab().setText("STANDINGS"));

        tabLayout.addTab(tabLayout.newTab().setText("STATS"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);

        welcomeMessage = (TextView) findViewById(R.id.welcome_message);

        if (savedInstanceState != null) {

            welcomeMessage.setText("");

            viewPager.getAdapter();

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(final TabLayout.Tab tab) {

                    viewPager.setCurrentItem(tabPosition);

                    tabLayout.getTabAt(tabPosition);

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }

    }

    /**
     *
     * @see #onBackPressed()
     *
     */
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

        }

    }

    /**
     *
     * @see #onCreateOptionsMenu(Menu)
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_action_bar_menu, menu);

        return true;

    }

    /**
     *
     * @see #onOptionsItemSelected(MenuItem)
     *
     * @param menuItem
     * @return
     */
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

    /**
     *
     * @see #onSaveInstanceState(Bundle)
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("TAB SELECTED POSITION", tabLayout.getSelectedTabPosition());

    }

    /**
     *
     * @see #onRestoreInstanceState(Bundle)
     *
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        tabPosition = savedInstanceState.getInt("TAB SELECTED POSITION");

    }

    /**
     *
     * @see #onNavigationItemSelected(MenuItem)
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.NFL:

                nflViewPagerAdapter = new NflViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

                viewPager.setAdapter(nflViewPagerAdapter);

                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, nflViewPagerAdapter.getItem(0)).commit();

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        if (tab.getPosition() == 0) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nflViewPagerAdapter.getItem(0)).commit();

                        } else if (tab.getPosition() == 1) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nflViewPagerAdapter.getItem(1)).commit();

                        } else if (tab.getPosition() == 2) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nflViewPagerAdapter.getItem(2)).commit();

                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                        if (tab.getPosition() == 0) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nflViewPagerAdapter.getItem(0)).commit();

                        } else if (tab.getPosition() == 1) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nflViewPagerAdapter.getItem(1)).commit();

                        } else if (tab.getPosition() == 2) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nflViewPagerAdapter.getItem(2)).commit();

                        }

                    }
                });

                getSupportActionBar().setTitle(R.string.nfl);

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.NBA:

                nbaViewPagerAdapter = new NbaViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

                viewPager.setAdapter(nbaViewPagerAdapter);

                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nbaViewPagerAdapter.getItem(0)).commit();

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        if (tab.getPosition() == 0) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nbaViewPagerAdapter.getItem(0)).commit();

                        } else if (tab.getPosition() == 1) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nbaViewPagerAdapter.getItem(1)).commit();

                        } else if (tab.getPosition() == 2) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nbaViewPagerAdapter.getItem(2)).commit();

                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

                getSupportActionBar().setTitle(R.string.nba);

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

            case R.id.MLB:

                mlbViewPagerAdapter = new MlbViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

                viewPager.setAdapter(mlbViewPagerAdapter);

                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mlbViewPagerAdapter.getItem(0)).commit();

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        if (tab.getPosition() == 0) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mlbViewPagerAdapter.getItem(0)).commit();

                        } else if (tab.getPosition() == 1) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mlbViewPagerAdapter.getItem(1)).commit();

                        } else if (tab.getPosition() == 2) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mlbViewPagerAdapter.getItem(2)).commit();

                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

                getSupportActionBar().setTitle(R.string.mlb);

                drawerLayout.closeDrawer(GravityCompat.START);

                welcomeMessage.setText("");

                return true;

        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;

    }

}
