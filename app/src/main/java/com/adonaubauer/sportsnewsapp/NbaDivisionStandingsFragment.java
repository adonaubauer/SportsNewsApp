package com.adonaubauer.sportsnewsapp;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * NbaDivisionStandings will create a list of nba division standings
 *
 * @see ListFragment
 *
 */

public class NbaDivisionStandingsFragment extends ListFragment {

    ArrayList<String> nbaDivisionStandingsList;

    String url = "https://www.mysportsfeeds.com/api/feed/pull/nba/2015-2016-regular/division_team_standings.xml?teamstats=W,L,PTS,PTSA";

    /**
     *
     * The NbaDivisionStandingsFragment constructor will create a new ArrayList of strings
     *
     */
    public NbaDivisionStandingsFragment() {

        nbaDivisionStandingsList = new ArrayList<>();

    }

    /**
     *
     * @see #onCreate(Bundle)
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        AsyncTaskNbaDivisionStandings asyncTaskNbaDivisionStandings = new AsyncTaskNbaDivisionStandings();

        asyncTaskNbaDivisionStandings.execute(url);

    }

    /**
     *
     * AsyncTaskNbaDivisionStandings class will create a request to get the nba division standings from
     * the web api
     *
     * @see AsyncTask
     *
     */
    private class AsyncTaskNbaDivisionStandings extends AsyncTask<String, Void, ArrayList<String>> {

        HttpURLConnection httpURLConnection;

        /**
         *
         * @see #onPreExecute()
         *
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         *
         * @see #onPostExecute(ArrayList)
         *
         * @param strings
         */
        @Override
        protected void onPostExecute(ArrayList<String> strings) {

            int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

            setListAdapter(new ArrayAdapter<String>(getActivity(), layout, nbaDivisionStandingsList));

            super.onPostExecute(strings);
        }

        /**
         *
         * @see #doInBackground(String...)
         *
         * @param params
         * @return
         */
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            return sendRequest(params[0]);
        }

        /**
         *
         * The sendRequest method will get the api url and send a request
         *
         * @param apiUrl
         * @return
         */
        private ArrayList<String> sendRequest(String apiUrl) {

            try {

                String username = "adonaubauer";

                String password = "Mustang95";

                URL url = new URL(apiUrl);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("GET");

                httpURLConnection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP));

                httpURLConnection.setDoOutput(false);

                httpURLConnection.connect();

                Log.d("Response Code",  String.valueOf(httpURLConnection.getResponseCode()));

                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    nbaDivisionStandingsList.add(line);

                }

            } catch (Exception e) {

                httpURLConnection.getErrorStream();

                e.printStackTrace();

            } finally {

                if (httpURLConnection != null) {

                    httpURLConnection.disconnect();

                }

            }

            return nbaDivisionStandingsList;

        }

    }

}
