package com.adonaubauer.sportsnewsapp;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.widget.ArrayAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Austin on 3/21/2017.
 */

public class NflDivisionStandingsFragment extends ListFragment {

    public static final String ns = null;

    ArrayList<String> nflDivisionStandingsList;

    private static final String url = "https://www.mysportsfeeds.com/api/feed/pull/nfl/2016-2017-regular/division_team_standings.xml?teamstats=W,L,T";

    public NflDivisionStandingsFragment() {

        nflDivisionStandingsList = new ArrayList<>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncTaskNflDivisionStandings asyncTaskNflPlayerStats = new AsyncTaskNflDivisionStandings();

        asyncTaskNflPlayerStats.execute(url);

    }

    private class AsyncTaskNflDivisionStandings extends AsyncTask<String, Void, ArrayList<String>> {

        HttpURLConnection httpURLConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {

            int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

            setListAdapter(new ArrayAdapter<String>(getActivity(), layout, nflDivisionStandingsList));

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

                    nflDivisionStandingsList.add(line);

                }

            } catch (Exception e) {

                httpURLConnection.getErrorStream();

                e.printStackTrace();

            } finally {

                if (httpURLConnection != null) {

                    httpURLConnection.disconnect();

                }

            }

            return nflDivisionStandingsList;

        }

    }

    private static class NflDivisionalInfo {

        String divisionalName;
        String teamCity;
        String teamName;
        String divisionalRank;
        String teamWins;
        String teamLosses;
        String teamTies;

        public void setDivisionalName(String divisionalName) {
            this.divisionalName = divisionalName;
        }

        public void setTeamCity(String teamCity) {
            this.teamCity = teamCity;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public void setDivisionalRank(String divisionalRank) {
            this.divisionalRank = divisionalRank;
        }

        public void setTeamWins(String teamWins) {
            this.teamWins = teamWins;
        }

        public void setTeamLosses(String teamLosses) {
            this.teamLosses = teamLosses;
        }

        public void setTeamTies(String teamTies) {
            this.teamTies = teamTies;
        }

        public String toString() {

            String divisionalInfoToString = teamCity + " " + teamName + " " + teamWins + " " + teamLosses + " " + teamTies;

            return divisionalInfoToString;

        }

    }

    public ArrayList<NflDivisionalInfo> parse(InputStream in) throws IOException, XmlPullParserException {

        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);

        } finally {

            in.close();

        }

    }

    private ArrayList<NflDivisionalInfo> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {

        ArrayList<NflDivisionalInfo> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "div:divisionteamstandings");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;

            }

            String name = parser.getName();

            /*if (name.equals("div:lastUpdatedOn")) {

                skip(parser);

            } else if (name.equals("div:division")) {

                entries.add(readEntry(parser));

            } else {

                skip(parser);

            }*/

        }

        return entries;

    }

    private NflDivisionalInfo readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {

        NflDivisionalInfo nflDivisionalInfo = null;

        parser.require(XmlPullParser.START_TAG, ns, "div:");

        return nflDivisionalInfo;

    }

}
