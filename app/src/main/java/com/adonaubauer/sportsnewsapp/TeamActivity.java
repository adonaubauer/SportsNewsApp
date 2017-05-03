package com.adonaubauer.sportsnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;

/**
 * The TeamActivity class is a new activity that hold team data info
 *
 * @see AppCompatActivity
 *
 */

public class TeamActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    String[] nflTeamNames;
    String[] nbaTeamNames;
    String[] mlbTeamNames;
    TextView teamNameTextView;

    /**
     *
     * @see #onCreate(Bundle)
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        toolbar = (Toolbar) findViewById(R.id.toolbar_team);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_team);

        viewPager = (ViewPager) findViewById(R.id.pager);

        teamNameTextView = (TextView) findViewById(R.id.teamName);

        setSupportActionBar(toolbar);

        nflTeamNames = getResources().getStringArray(R.array.nfl_team_names);

        nbaTeamNames = getResources().getStringArray(R.array.nba_team_names);

        mlbTeamNames = getResources().getStringArray(R.array.mlb_team_names);

        Intent getTeamNameIntent = getIntent();

        String clickedTeam = getTeamNameIntent.getStringExtra("Clicked Team");

        for (String team : nflTeamNames) {

            if (clickedTeam.equals(team)) {

                for (String teamName : nflTeamNames) {

                    tabLayout.addTab(tabLayout.newTab().setText(teamName));

                }

                switchToTabNfl(team);

                final int clickedTeamIndex = Arrays.asList(nflTeamNames).indexOf(team);

                tabLayout.setScrollPosition(clickedTeamIndex, 0f, true);

                teamNameTextView.setText(team);

            }

        }
        for (String team : nbaTeamNames) {

            if (clickedTeam.equals(team)) {

                for (String teamName : nbaTeamNames) {

                    tabLayout.addTab(tabLayout.newTab().setText(teamName));



                }

                switchToTabNba(team);

                teamNameTextView.setText(team);

            }

        }
        for (String team : mlbTeamNames) {

            if (clickedTeam.equals(team)) {

                for (String teamName : mlbTeamNames) {

                    tabLayout.addTab(tabLayout.newTab().setText(teamName));

                }

                switchToTabMlb(team);

                teamNameTextView.setText(team);

            }

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
     * The switchToTabNfl method will get the tab
     * and set the viewPager to the tab item
     *
     * @param tab
     */
    public void switchToTabNfl(String tab) {

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

    /**
     *
     * The switchToTabNba method will get the tab
     * and set the viewPager to the tab item
     *
     * @param tab
     */
    public void switchToTabNba(String tab) {

        if(tab.equals("Atlanta Hawks")){
            viewPager.setCurrentItem(0);
        }else if(tab.equals("Boston Celtics")){
            viewPager.setCurrentItem(1);
        }else if(tab.equals("Brooklyn Nets")){
            viewPager.setCurrentItem(2);
        }else if(tab.equals("Charlotte Hornets")){
            viewPager.setCurrentItem(3);
        }else if(tab.equals("Chicago Bulls")){
            viewPager.setCurrentItem(4);
        }else if(tab.equals("Cleveland Cavaliers")){
            viewPager.setCurrentItem(5);
        }else if(tab.equals("Dallas Mavericks")){
            viewPager.setCurrentItem(6);
        }else if(tab.equals("Denver Nuggets")){
            viewPager.setCurrentItem(7);
        }else if(tab.equals("Detroit Pistons")){
            viewPager.setCurrentItem(8);
        }else if(tab.equals("Golden State Warriors")){
            viewPager.setCurrentItem(9);
        }else if(tab.equals("Houston Rockets")){
            viewPager.setCurrentItem(10);
        }else if(tab.equals("Indiana Pacers")){
            viewPager.setCurrentItem(11);
        }else if(tab.equals("Los Angeles Clippers")){
            viewPager.setCurrentItem(12);
        }else if(tab.equals("Los Angeles Lakers")){
            viewPager.setCurrentItem(13);
        }else if(tab.equals("Memphis Grizzlies")){
            viewPager.setCurrentItem(14);
        }else if(tab.equals("Miami Heat")){
            viewPager.setCurrentItem(15);
        }else if(tab.equals("Los Angeles Chargers")){
            viewPager.setCurrentItem(16);
        }else if(tab.equals("Los Angeles Rams")){
            viewPager.setCurrentItem(17);
        }else if(tab.equals("Milwaukee Bucks")){
            viewPager.setCurrentItem(18);
        }else if(tab.equals("Minnesota Timberwolves")){
            viewPager.setCurrentItem(19);
        }else if(tab.equals("New Orleans Pelicans")){
            viewPager.setCurrentItem(20);
        }else if(tab.equals("New York Knicks")){
            viewPager.setCurrentItem(21);
        }else if(tab.equals("Oklahoma City Thunder")){
            viewPager.setCurrentItem(22);
        }else if(tab.equals("Orlando Magic")){
            viewPager.setCurrentItem(23);
        }else if(tab.equals("Philadelphia 76ers")){
            viewPager.setCurrentItem(24);
        }else if(tab.equals("Phoenix Suns")){
            viewPager.setCurrentItem(25);
        }else if(tab.equals("Portland Trail Blazers")){
            viewPager.setCurrentItem(26);
        }else if(tab.equals("Sacramento Kings")){
            viewPager.setCurrentItem(27);
        }else if(tab.equals("San Antonio Spurs")){
            viewPager.setCurrentItem(28);
        }else if(tab.equals("Toronto Rapters")){
            viewPager.setCurrentItem(29);
        }else if(tab.equals("Utah Jazz")){
            viewPager.setCurrentItem(28);
        }else if(tab.equals("Washington Wizards")){
            viewPager.setCurrentItem(29);
        }

    }

    /**
     *
     * The switchToTabMlb method will get the tab
     * and set the viewPager to the tab item
     *
     * @param tab
     */
    public void switchToTabMlb(String tab) {

        if(tab.equals("Arizona Diamondbacks")){
            viewPager.setCurrentItem(0);
        }else if(tab.equals("Atlanta Braves")){
            viewPager.setCurrentItem(1);
        }else if(tab.equals("Baltimore Orioles")){
            viewPager.setCurrentItem(2);
        }else if(tab.equals("Boston Red Sox")){
            viewPager.setCurrentItem(3);
        }else if(tab.equals("Chicago Cubs")){
            viewPager.setCurrentItem(4);
        }else if(tab.equals("Chicago White Sox")){
            viewPager.setCurrentItem(5);
        }else if(tab.equals("Cincinnati Reds")){
            viewPager.setCurrentItem(6);
        }else if(tab.equals("Cleveland Indians")){
            viewPager.setCurrentItem(7);
        }else if(tab.equals("Colorado Rockies")){
            viewPager.setCurrentItem(8);
        }else if(tab.equals("Detroit Tigers")){
            viewPager.setCurrentItem(9);
        }else if(tab.equals("Houston Astros")){
            viewPager.setCurrentItem(10);
        }else if(tab.equals("Kansas City Royals")){
            viewPager.setCurrentItem(11);
        }else if(tab.equals("Los Angeles Angels")){
            viewPager.setCurrentItem(12);
        }else if(tab.equals("Los Angeles Dodgers")){
            viewPager.setCurrentItem(13);
        }else if(tab.equals("Miami Marlins")){
            viewPager.setCurrentItem(14);
        }else if(tab.equals("Milwaukee Brewers")){
            viewPager.setCurrentItem(15);
        }else if(tab.equals("Minnesota Twins")){
            viewPager.setCurrentItem(16);
        }else if(tab.equals("New York Mets")){
            viewPager.setCurrentItem(17);
        }else if(tab.equals("New York Yankees")){
            viewPager.setCurrentItem(18);
        }else if(tab.equals("Oakland Athletics")){
            viewPager.setCurrentItem(19);
        }else if(tab.equals("Philadelphia Phillies")){
            viewPager.setCurrentItem(20);
        }else if(tab.equals("Pittsburgh Pirates")){
            viewPager.setCurrentItem(21);
        }else if(tab.equals("St. Louis Cardinals")){
            viewPager.setCurrentItem(22);
        }else if(tab.equals("San Diego Padres")){
            viewPager.setCurrentItem(23);
        }else if(tab.equals("San Francisco Giants")){
            viewPager.setCurrentItem(24);
        }else if(tab.equals("Seattle Mariners")){
            viewPager.setCurrentItem(25);
        }else if(tab.equals("Tampa Bay Rays")){
            viewPager.setCurrentItem(26);
        }else if(tab.equals("Texas Rangers")){
            viewPager.setCurrentItem(27);
        }else if(tab.equals("Toronto Blue Jays")){
            viewPager.setCurrentItem(28);
        }else if(tab.equals("Washington Nationals")){
            viewPager.setCurrentItem(29);
        }

    }

}