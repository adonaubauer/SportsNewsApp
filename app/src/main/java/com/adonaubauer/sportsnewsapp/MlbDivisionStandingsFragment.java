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
 * Created by Austin on 3/21/2017.
 */

public class MlbDivisionStandingsFragment extends ListFragment {

    ArrayList<String> mlbDivisionStandingsList;

    String url = "https://www.mysportsfeeds.com/api/feed/pull/mlb/2016-regular/division_team_standings.xml?teamstats=W,L,RF,RA";

    public MlbDivisionStandingsFragment() {

        mlbDivisionStandingsList = new ArrayList<>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncTaskMlbDivisionStandings asyncTaskMlbDivisionStandings = new AsyncTaskMlbDivisionStandings();

        asyncTaskMlbDivisionStandings.execute(url);

    }


    private class AsyncTaskMlbDivisionStandings extends AsyncTask<String, Void, ArrayList<String>> {

        HttpURLConnection httpURLConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {

            int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

            setListAdapter(new ArrayAdapter<String>(getActivity(), layout, mlbDivisionStandingsList));

            super.onPostExecute(strings);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            return sendRequest(params[0]);
        }

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

                    mlbDivisionStandingsList.add(line);

                }

            } catch (Exception e) {

                httpURLConnection.getErrorStream();

                e.printStackTrace();

            } finally {

                if (httpURLConnection != null) {

                    httpURLConnection.disconnect();

                }

            }

            return mlbDivisionStandingsList;

        }

    }

}