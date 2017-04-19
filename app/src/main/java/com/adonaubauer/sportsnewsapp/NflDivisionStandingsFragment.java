package com.adonaubauer.sportsnewsapp;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Austin on 3/21/2017.
 */

public class NflDivisionStandingsFragment extends ListFragment {

    public static final String ns = null;

    ArrayList<NflDivisionalInfo> nflDivisionStandingsList;

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

            setListAdapter(new ArrayAdapter<>(getActivity(), layout, strings));

            super.onPostExecute(strings);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            return sendRequest(params[0]);
        }

        private ArrayList<String> sendRequest(String apiUrl) {

            InputStream stream;
            ArrayList<String> nflDivisionalInfo = new ArrayList<>();

            try {

                stream = downloadUrl(apiUrl);
                nflDivisionalInfo = parse(stream);

            } catch (Exception e) {

                httpURLConnection.getErrorStream();

                e.printStackTrace();

            } finally {

                if (httpURLConnection != null) {

                    httpURLConnection.disconnect();

                }

            }

            return nflDivisionalInfo;

        }

        private InputStream downloadUrl(String stringUrl) throws IOException {

            String username = "adonaubauer";

            String password = "Mustang95";

            URL url = new URL(stringUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP));

            httpURLConnection.setDoOutput(false);

            httpURLConnection.connect();

            Log.d("Response Code",  String.valueOf(httpURLConnection.getResponseCode()));

            InputStream inputStream = httpURLConnection.getInputStream();

            return inputStream;

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

    public ArrayList<String> parse(InputStream in) throws IOException, XmlPullParserException {

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

    private ArrayList<String> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {

        ArrayList<String> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "div:divisionteamstandings");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;

            }

            String name = parser.getName();

            if (name.equals("div:lastUpdatedOn")) {

                skip(parser);

            } else if (name.equals("div:division")) {

                entries.add(readEntry(parser).toString());

            } else {

                skip(parser);

            }

        }

        return entries;

    }

    private ArrayList<NflDivisionalInfo> readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {

        ArrayList<NflDivisionalInfo> nflDivisionalInfo = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "div:division");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;

            }

            String name = parser.getName();

            if (name.equals("div:teamentry")) {

                nflDivisionalInfo.add(readTeamEntry(parser));

            } else {

                skip(parser);

            }

        }

        return nflDivisionalInfo;

    }

    private NflDivisionalInfo readTeamEntry(XmlPullParser parser) throws IOException, XmlPullParserException {

        NflDivisionalInfo nflDivisionalInfo = null;

        parser.require(XmlPullParser.START_TAG, ns, "div:teamentry");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;

            }

            String name = parser.getName();

            if (name.equals("div:team")) {

                nflDivisionalInfo = readTeamInfo(parser);

            } else if (name.equals("div:rank")) {

                skip(parser);

            } else if (name.equals("div:stats")) {

                readTeamStats(parser, nflDivisionalInfo);

            } else {

                skip(parser);

            }

        }

        return nflDivisionalInfo;

    }

    private NflDivisionalInfo readTeamInfo(XmlPullParser parser) throws IOException, XmlPullParserException {

        NflDivisionalInfo nflDivisionalInfo = new NflDivisionalInfo();

        String teamCity = null;
        String teamName = null;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;

            }

            String name = parser.getName();

            if (name.equals("div:ID")) {

                skip(parser);

            } else if (name.equals("div:City")) {

                teamCity = readTeamCityName(parser);

                nflDivisionalInfo.setTeamCity(teamCity);

            } else if (name.equals("div:Name")) {

                teamName = readTeamNameName(parser);

                nflDivisionalInfo.setTeamName(teamName);

            } else if (name.equals("div:Abbreviation")) {

                skip(parser);

            } else {

                skip(parser);

            }

        }

        return nflDivisionalInfo;

    }

    private NflDivisionalInfo readTeamStats(XmlPullParser parser, NflDivisionalInfo nflDivisionalInfo) throws IOException, XmlPullParserException {

        String teamWins;
        String teamLosses;
        String teamTies;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;

            }

            String name = parser.getName();

            if (name.equals("div:GamesPlayed")) {

                skip(parser);

            } else if (name.equals("div:Wins")) {

                teamWins = readTeamWins(parser);

                nflDivisionalInfo.setTeamWins(teamWins);

            } else if (name.equals("div:Losses")) {

                teamLosses = readTeamLosses(parser);

                nflDivisionalInfo.setTeamLosses(teamLosses);

            } else if (name.equals("div:Ties")) {

                teamTies = readTeamTies(parser);

                nflDivisionalInfo.setTeamTies(teamTies);

            } else {

                skip(parser);

            }

        }

        return nflDivisionalInfo;

    }

    private String readTeamCityName(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "div:City");
        String teamCityName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "div:City");
        return teamCityName;

    }

    private String readTeamNameName(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "div:Name");
        String teamNameName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "div:Name");
        return teamNameName;

    }

    private String readTeamWins(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "div:Wins");
        String teamWins = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "div:Wins");
        return teamWins;

    }

    private String readTeamLosses(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "div:Losses");
        String teamLosses = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "div:Losses");
        return teamLosses;

    }

    private String readTeamTies(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "div:Ties");
        String teamTies = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "div:Ties");
        return teamTies;

    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {

        String result = "";

        if (parser.next() == XmlPullParser.TEXT) {

            result = parser.getText();
            parser.nextTag();

        }

        return result;

    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
