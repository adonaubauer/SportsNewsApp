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
 * Created by Austin on 3/7/2017.
 */

public class NflPlayerStatsFragment extends ListFragment {

    public static final String ns = null;

    ArrayList<String> playerStats;

    String uRL = "https://www.mysportsfeeds.com/api/feed/pull/nfl/2016-2017-regular/cumulative_player_stats.xml";

    public NflPlayerStatsFragment() {

        playerStats = new ArrayList<>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncTaskNflPlayerStats asyncTaskNflPlayerStats = new AsyncTaskNflPlayerStats();

        asyncTaskNflPlayerStats.execute(uRL);

    }

    private class AsyncTaskNflPlayerStats extends AsyncTask<String, Void, ArrayList<String>> {

        HttpURLConnection httpURLConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

            setListAdapter(new ArrayAdapter<String>(getActivity(), layout, playerStats));
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

                    playerStats.add(line);

                }

            } catch (Exception e) {

                httpURLConnection.getErrorStream();

                e.printStackTrace();

            } finally {

                if (httpURLConnection != null) {

                    httpURLConnection.disconnect();

                }

            }

            return playerStats;

        }

    }

    public static class NflPlayerInfo {

        String playerId;
        String playerLastName;
        String playerFirstName;
        String playerJerseyNumber;
        String playerPosition;

        private NflPlayerInfo(String playerId, String playerLastName, String playerFirstName, String playerJerseyNumber, String playerPosition) {

            this.playerId = playerId;
            this.playerLastName = playerLastName;
            this.playerFirstName = playerFirstName;
            this.playerJerseyNumber = playerJerseyNumber;
            this.playerPosition = playerPosition;

        }

    }

    private NflPlayerInfo readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "player");
        String playerId = null;
        String playerLastName = null;
        String playerFirstName = null;
        String playerJerseyNumber = null;
        String playerPosition = null;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("ID")) {

                playerId = readPlayerId(parser);

            } else if (name.equals("LastName")) {

                playerLastName = readPlayerLastName(parser);

            } else if (name.equals("FirstName")) {

                playerFirstName = readPlayerFirstName(parser);

            } else if (name.equals("JerseyNumber")) {

                playerJerseyNumber = readPlayerJerseyNumber(parser);

            } else if (name.equals("Position")) {

                playerPosition = readPlayerPosition(parser);

            } else {

                skip(parser);

            }

        }

        return new NflPlayerInfo(playerId, playerLastName, playerFirstName, playerJerseyNumber, playerPosition);

    }

    private String readPlayerId(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "ID");
        String playerId = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "ID");
        return playerId;

    }

    private String readPlayerLastName(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "LastName");
        String playerLastName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "LastName");
        return playerLastName;

    }

    private String readPlayerFirstName(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "FirstName");
        String playerFirstName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "FirstName");
        return playerFirstName;

    }

    private String readPlayerJerseyNumber(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "JerseyNumber");
        String playerJerseyNumber = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "JerseyNumber");
        return playerJerseyNumber;

    }

    private String readPlayerPosition(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "Position");
        String playerPosition = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Position");
        return playerPosition;

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

    public static class NflTeamInfo {

        int teamId;
        String teamCity;
        String teamName;
        String teamAbbreviation;

        private NflTeamInfo(int teamId, String teamCity, String teamName, String teamAbbreviation) {

            this.teamId = teamId;
            this.teamCity = teamCity;
            this.teamName = teamName;
            this.teamAbbreviation = teamAbbreviation;

        }

    }

    public static class NflPlayerStatsInfo {

        int playerGamesPlayed;
        int playerPassingAttempts;
        int playerPassingCompletions;
        int playerPassingPercentage;
        int playerPassingYards;
        int playerPassingAverage;
        int playerPassingYardsPerAttempt;
        int playerPassingTouchdowns;
        int playerPassingTouchdownPercentage;
        int playerPassingInterceptions;
        int playerPassingInterceptionPercentage;
        int playerPassingLong;
        int playerPassing20Plus;
        int playerPassing40Plus;
        int playerPassingSacks;
        int playerPassingSackYards;
        int playerQBRating;
        int playerRushingAttempts;
        int playerRushingYards;
        int playerRushingAverage;
        int playerRushingTouchdowns;
        int playerRushingLong;
        int playerRushing20Plus;
        int playerRushing40Plus;
        int playerRushingFumbles;
        int playerTargets;
        int playerReceptions;
        int playerReceivingYards;
        int playerReceivingAverage;
        int playerReceivingTouchdowns;
        int playerReceivingLong;
        int playerReceiving20Plus;
        int playerReceiving40Plus;
        int playerReceivingFumbles;
        int playerTacklesSolo;
        int playerTacklesTotal;
        int playerTacklesAssist;
        int playerSacks;
        int playerSackYards;
        int playerSafeties;
        int playerTacklesForLoss;
        int playerInterceptions;
        int playerInterceptionYards;
        int playerInterceptionAverage;
        int playerInterceptionLong;
        int playerPassesDefended;
        int playerStuffs;
        int playerStuffYards;
        int playerKB;
        int playerFumbles;
        int playerFumblesLost;
        int playerFumblesForced;
        int playerFumblesOwnReceived;
        int playerFumblesOppReceived;
        int playerFumblesReceivingYards;
        int playerFumbleTouchdowns;
        int playerKickReturn;
        int playerKickReturnYards;
        int playerKickReturnAverage;
        int playerKickReturnLong;
        int playerKickReturnTouchdowns;
        int playerKickReturn20Plus;
        int playerKickReturn40Plus;
        int playerKickReturnFC;
        int playerKickReturnFumbles;
        int playerPuntReturn;
        int playerPuntReturnYards;
        int playerPuntReturnAverage;
        int playerPuntReutrnLong;
        int playerPuntReturnTouchdowns;
        int playerPuntReturn20Plus;
        int playerPuntReturn40Plus;
        int playerPuntReturnFC;
        int playerPuntReturnFumbles;
        int playerTwoPointAttempts;
        int playerTwoPointMakes;
        int playerTwoPointPassAttempts;
        int playerTwoPointPassMakes;
        int playerTwoPointPassReceived;
        int playerTwoPointRushAttempts;
        int playerTwoPointRushMakes;

        public NflPlayerStatsInfo(int playerGamesPlayed, int playerPassingAttempts, int playerPassingCompletions, int playerPassingPercentage, int playerPassingYards, int playerPassingAverage, int playerPassingYardsPerAttempt, int playerPassingTouchdowns, int playerPassingTouchdownPercentage, int playerPassingInterceptions, int playerPassingInterceptionPercentage, int playerPassingLong, int playerPassing20Plus, int playerPassing40Plus, int playerPassingSacks, int playerPassingSackYards, int playerQBRating, int playerRushingAttempts, int playerRushingYards, int playerRushingAverage, int playerRushingTouchdowns, int playerRushingLong, int playerRushing20Plus, int playerRushing40Plus, int playerRushingFumbles, int playerTargets, int playerReceptions, int playerReceivingYards, int playerReceivingAverage, int playerReceivingTouchdowns, int playerReceivingLong, int playerReceiving20Plus, int playerReceiving40Plus, int playerReceivingFumbles, int playerTacklesSolo, int playerTacklesTotal, int playerTacklesAssist, int playerSacks, int playerSackYards, int playerSafeties, int playerTacklesForLoss, int playerInterceptions, int playerInterceptionYards, int playerInterceptionAverage, int playerInterceptionLong, int playerPassesDefended, int playerStuffs, int playerStuffYards, int playerKB, int playerFumbles, int playerFumblesLost, int playerFumblesForced, int playerFumblesOwnReceived, int playerFumblesOppReceived, int playerFumblesReceivingYards, int playerFumbleTouchdowns, int playerKickReturn, int playerKickReturnYards, int playerKickReturnAverage, int playerKickReturnLong, int playerKickReturnTouchdowns, int playerKickReturn20Plus, int playerKickReturn40Plus, int playerKickReturnFC, int playerKickReturnFumbles, int playerPuntReturn, int playerPuntReturnYards, int playerPuntReturnAverage, int playerPuntReutrnLong, int playerPuntReturnTouchdowns, int playerPuntReturn20Plus, int playerPuntReturn40Plus, int playerPuntReturnFC, int playerPuntReturnFumbles, int playerTwoPointAttempts, int playerTwoPointMakes, int playerTwoPointPassAttempts, int playerTwoPointPassMakes, int playerTwoPointPassReceived, int playerTwoPointRushAttempts, int playerTwoPointRushMakes) {
            this.playerGamesPlayed = playerGamesPlayed;
            this.playerPassingAttempts = playerPassingAttempts;
            this.playerPassingCompletions = playerPassingCompletions;
            this.playerPassingPercentage = playerPassingPercentage;
            this.playerPassingYards = playerPassingYards;
            this.playerPassingAverage = playerPassingAverage;
            this.playerPassingYardsPerAttempt = playerPassingYardsPerAttempt;
            this.playerPassingTouchdowns = playerPassingTouchdowns;
            this.playerPassingTouchdownPercentage = playerPassingTouchdownPercentage;
            this.playerPassingInterceptions = playerPassingInterceptions;
            this.playerPassingInterceptionPercentage = playerPassingInterceptionPercentage;
            this.playerPassingLong = playerPassingLong;
            this.playerPassing20Plus = playerPassing20Plus;
            this.playerPassing40Plus = playerPassing40Plus;
            this.playerPassingSacks = playerPassingSacks;
            this.playerPassingSackYards = playerPassingSackYards;
            this.playerQBRating = playerQBRating;
            this.playerRushingAttempts = playerRushingAttempts;
            this.playerRushingYards = playerRushingYards;
            this.playerRushingAverage = playerRushingAverage;
            this.playerRushingTouchdowns = playerRushingTouchdowns;
            this.playerRushingLong = playerRushingLong;
            this.playerRushing20Plus = playerRushing20Plus;
            this.playerRushing40Plus = playerRushing40Plus;
            this.playerRushingFumbles = playerRushingFumbles;
            this.playerTargets = playerTargets;
            this.playerReceptions = playerReceptions;
            this.playerReceivingYards = playerReceivingYards;
            this.playerReceivingAverage = playerReceivingAverage;
            this.playerReceivingTouchdowns = playerReceivingTouchdowns;
            this.playerReceivingLong = playerReceivingLong;
            this.playerReceiving20Plus = playerReceiving20Plus;
            this.playerReceiving40Plus = playerReceiving40Plus;
            this.playerReceivingFumbles = playerReceivingFumbles;
            this.playerTacklesSolo = playerTacklesSolo;
            this.playerTacklesTotal = playerTacklesTotal;
            this.playerTacklesAssist = playerTacklesAssist;
            this.playerSacks = playerSacks;
            this.playerSackYards = playerSackYards;
            this.playerSafeties = playerSafeties;
            this.playerTacklesForLoss = playerTacklesForLoss;
            this.playerInterceptions = playerInterceptions;
            this.playerInterceptionYards = playerInterceptionYards;
            this.playerInterceptionAverage = playerInterceptionAverage;
            this.playerInterceptionLong = playerInterceptionLong;
            this.playerPassesDefended = playerPassesDefended;
            this.playerStuffs = playerStuffs;
            this.playerStuffYards = playerStuffYards;
            this.playerKB = playerKB;
            this.playerFumbles = playerFumbles;
            this.playerFumblesLost = playerFumblesLost;
            this.playerFumblesForced = playerFumblesForced;
            this.playerFumblesOwnReceived = playerFumblesOwnReceived;
            this.playerFumblesOppReceived = playerFumblesOppReceived;
            this.playerFumblesReceivingYards = playerFumblesReceivingYards;
            this.playerFumbleTouchdowns = playerFumbleTouchdowns;
            this.playerKickReturn = playerKickReturn;
            this.playerKickReturnYards = playerKickReturnYards;
            this.playerKickReturnAverage = playerKickReturnAverage;
            this.playerKickReturnLong = playerKickReturnLong;
            this.playerKickReturnTouchdowns = playerKickReturnTouchdowns;
            this.playerKickReturn20Plus = playerKickReturn20Plus;
            this.playerKickReturn40Plus = playerKickReturn40Plus;
            this.playerKickReturnFC = playerKickReturnFC;
            this.playerKickReturnFumbles = playerKickReturnFumbles;
            this.playerPuntReturn = playerPuntReturn;
            this.playerPuntReturnYards = playerPuntReturnYards;
            this.playerPuntReturnAverage = playerPuntReturnAverage;
            this.playerPuntReutrnLong = playerPuntReutrnLong;
            this.playerPuntReturnTouchdowns = playerPuntReturnTouchdowns;
            this.playerPuntReturn20Plus = playerPuntReturn20Plus;
            this.playerPuntReturn40Plus = playerPuntReturn40Plus;
            this.playerPuntReturnFC = playerPuntReturnFC;
            this.playerPuntReturnFumbles = playerPuntReturnFumbles;
            this.playerTwoPointAttempts = playerTwoPointAttempts;
            this.playerTwoPointMakes = playerTwoPointMakes;
            this.playerTwoPointPassAttempts = playerTwoPointPassAttempts;
            this.playerTwoPointPassMakes = playerTwoPointPassMakes;
            this.playerTwoPointPassReceived = playerTwoPointPassReceived;
            this.playerTwoPointRushAttempts = playerTwoPointRushAttempts;
            this.playerTwoPointRushMakes = playerTwoPointRushMakes;
        }
    }

}
