package com.adonaubauer.sportsnewsapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Austin on 3/7/2017.
 */

public class NflPlayerStatsFragment extends ListFragment {

    public static final String ns = null;

    ArrayList<NflPlayerInfo> playerStats;

    ListView listView;

    private static final String uRL = "https://www.mysportsfeeds.com/api/feed/pull/nfl/2016-2017-regular/cumulative_player_stats.xml";

    public NflPlayerStatsFragment() {

        playerStats = new ArrayList<>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncTaskNflPlayerStats asyncTaskNflPlayerStats = new AsyncTaskNflPlayerStats();

        asyncTaskNflPlayerStats.execute(uRL);

    }

    private class AsyncTaskNflPlayerStats extends AsyncTask<String, Void, ArrayList<NflPlayerInfo>> {

        HttpURLConnection httpURLConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<NflPlayerInfo> strings) {
            super.onPostExecute(strings);

            int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

            setListAdapter(new ArrayAdapter<>(getContext(),layout, strings));
        }

        @Override
        protected ArrayList<NflPlayerInfo> doInBackground(String... params) {
            return sendRequest(params[0]);
        }

        private ArrayList<NflPlayerInfo> sendRequest(String apiUrl) {

            InputStream stream;
            ArrayList<NflPlayerInfo> playerStats = new ArrayList<>();

            try {

                stream = downloadUrl(apiUrl);
                playerStats = parse(stream);

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

        private InputStream downloadUrl(String stringUrl) throws IOException {

            String username = "adonaubauer";

            String password = "Mustang95";

            URL url = new URL(stringUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP));

            httpURLConnection.setDoOutput(false);

            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            return inputStream;

        }

    }

    private static class NflPlayerInfo {

        String playerLastName;
        String playerFirstName;
        String playerPosition;
        String teamCity;
        String teamName;
        String playerGamesPlayed;
        String playerPassingAttempts;
        String playerPassingCompletions;
        String playerPassingPercentage;
        String playerPassingYards;
        String playerPassingAverage;
        String playerPassingYardsPerAttempt;
        String playerPassingTouchdowns;
        String playerPassingTouchdownPercentage;
        String playerPassingInterceptions;
        String playerPassingInterceptionPercentage;
        String playerPassingLong;
        String playerPassing20Plus;
        String playerPassing40Plus;
        String playerPassingSacks;
        String playerPassingSackYards;
        String playerQBRating;
        String playerRushingAttempts;
        String playerRushingYards;
        String playerRushingAverage;
        String playerRushingTouchdowns;
        String playerRushingLong;
        String playerRushing20Plus;
        String playerRushing40Plus;
        String playerRushingFumbles;
        String playerTargets;
        String playerReceptions;
        String playerReceivingYards;
        String playerReceivingAverage;
        String playerReceivingTouchdowns;
        String playerReceivingLong;
        String playerReceiving20Plus;
        String playerReceiving40Plus;
        String playerReceivingFumbles;
        String playerTacklesSolo;
        String playerTacklesTotal;
        String playerTacklesAssist;
        String playerSacks;
        String playerSackYards;
        String playerSafeties;
        String playerTacklesForLoss;
        String playerInterceptions;
        String playerInterceptionYards;
        String playerInterceptionAverage;
        String playerInterceptionLong;
        String playerPassesDefended;
        String playerStuffs;
        String playerStuffYards;
        String playerKB;
        String playerFumbles;
        String playerFumblesLost;
        String playerFumblesForced;
        String playerFumblesOwnReceived;
        String playerFumblesOppReceived;
        String playerFumblesReceivingYards;
        String playerFumbleTouchdowns;
        String playerKickReturn;
        String playerKickReturnYards;
        String playerKickReturnAverage;
        String playerKickReturnLong;
        String playerKickReturnTouchdowns;
        String playerKickReturn20Plus;
        String playerKickReturn40Plus;
        String playerKickReturnFC;
        String playerKickReturnFumbles;
        String playerPuntReturn;
        String playerPuntReturnYards;
        String playerPuntReturnAverage;
        String playerPuntReturnLong;
        String playerPuntReturnTouchdowns;
        String playerPuntReturn20Plus;
        String playerPuntReturn40Plus;
        String playerPuntReturnFC;
        String playerPuntReturnFumbles;
        String playerTwoPointAttempts;
        String playerTwoPointMakes;
        String playerTwoPointPassAttempts;
        String playerTwoPointPassMakes;
        String playerTwoPointPassReceived;
        String playerTwoPointRushAttempts;
        String playerTwoPointRushMakes;

        private NflPlayerInfo(String playerLastName, String playerFirstName, String playerPosition) {

            this.playerLastName = playerLastName;
            this.playerFirstName = playerFirstName;
            this.playerPosition = playerPosition;

        }

        public void setTeamCity(String teamCity) {

            this.teamCity = teamCity;

        }

        public void setTeamName(String teamName) {

            this.teamName = teamName;

        }

        public void setPlayerLastName(String playerLastName) {
            this.playerLastName = playerLastName;
        }

        public void setPlayerFirstName(String playerFirstName) {
            this.playerFirstName = playerFirstName;
        }

        public void setPlayerPosition(String playerPosition) {
            this.playerPosition = playerPosition;
        }

        public void setPlayerGamesPlayed(String playerGamesPlayed) {
            this.playerGamesPlayed = playerGamesPlayed;
        }

        public void setPlayerPassingAttempts(String playerPassingAttempts) {
            this.playerPassingAttempts = playerPassingAttempts;
        }

        public void setPlayerPassingCompletions(String playerPassingCompletions) {
            this.playerPassingCompletions = playerPassingCompletions;
        }

        public void setPlayerPassingPercentage(String playerPassingPercentage) {
            this.playerPassingPercentage = playerPassingPercentage;
        }

        public void setPlayerPassingYards(String playerPassingYards) {
            this.playerPassingYards = playerPassingYards;
        }

        public void setPlayerPassingAverage(String playerPassingAverage) {
            this.playerPassingAverage = playerPassingAverage;
        }

        public void setPlayerPassingYardsPerAttempt(String playerPassingYardsPerAttempt) {
            this.playerPassingYardsPerAttempt = playerPassingYardsPerAttempt;
        }

        public void setPlayerPassingTouchdowns(String playerPassingTouchdowns) {
            this.playerPassingTouchdowns = playerPassingTouchdowns;
        }

        public void setPlayerPassingTouchdownPercentage(String playerPassingTouchdownPercentage) {
            this.playerPassingTouchdownPercentage = playerPassingTouchdownPercentage;
        }

        public void setPlayerPassingInterceptions(String playerPassingInterceptions) {
            this.playerPassingInterceptions = playerPassingInterceptions;
        }

        public void setPlayerPassingInterceptionPercentage(String playerPassingInterceptionPercentage) {
            this.playerPassingInterceptionPercentage = playerPassingInterceptionPercentage;
        }

        public void setPlayerPassingLong(String playerPassingLong) {
            this.playerPassingLong = playerPassingLong;
        }

        public void setPlayerPassing20Plus(String playerPassing20Plus) {
            this.playerPassing20Plus = playerPassing20Plus;
        }

        public void setPlayerPassing40Plus(String playerPassing40Plus) {
            this.playerPassing40Plus = playerPassing40Plus;
        }

        public void setPlayerPassingSacks(String playerPassingSacks) {
            this.playerPassingSacks = playerPassingSacks;
        }

        public void setPlayerPassingSackYards(String playerPassingSackYards) {
            this.playerPassingSackYards = playerPassingSackYards;
        }

        public void setPlayerQBRating(String playerQBRating) {
            this.playerQBRating = playerQBRating;
        }

        public void setPlayerRushingAttempts(String playerRushingAttempts) {
            this.playerRushingAttempts = playerRushingAttempts;
        }

        public void setPlayerRushingYards(String playerRushingYards) {
            this.playerRushingYards = playerRushingYards;
        }

        public void setPlayerRushingAverage(String playerRushingAverage) {
            this.playerRushingAverage = playerRushingAverage;
        }

        public void setPlayerRushingTouchdowns(String playerRushingTouchdowns) {
            this.playerRushingTouchdowns = playerRushingTouchdowns;
        }

        public void setPlayerRushingLong(String playerRushingLong) {
            this.playerRushingLong = playerRushingLong;
        }

        public void setPlayerRushing20Plus(String playerRushing20Plus) {
            this.playerRushing20Plus = playerRushing20Plus;
        }

        public void setPlayerRushing40Plus(String playerRushing40Plus) {
            this.playerRushing40Plus = playerRushing40Plus;
        }

        public void setPlayerRushingFumbles(String playerRushingFumbles) {
            this.playerRushingFumbles = playerRushingFumbles;
        }

        public void setPlayerTargets(String playerTargets) {
            this.playerTargets = playerTargets;
        }

        public void setPlayerReceptions(String playerReceptions) {
            this.playerReceptions = playerReceptions;
        }

        public void setPlayerReceivingYards(String playerReceivingYards) {
            this.playerReceivingYards = playerReceivingYards;
        }

        public void setPlayerReceivingAverage(String playerReceivingAverage) {
            this.playerReceivingAverage = playerReceivingAverage;
        }

        public void setPlayerReceivingTouchdowns(String playerReceivingTouchdowns) {
            this.playerReceivingTouchdowns = playerReceivingTouchdowns;
        }

        public void setPlayerReceivingLong(String playerReceivingLong) {
            this.playerReceivingLong = playerReceivingLong;
        }

        public void setPlayerReceiving20Plus(String playerReceiving20Plus) {
            this.playerReceiving20Plus = playerReceiving20Plus;
        }

        public void setPlayerReceiving40Plus(String playerReceiving40Plus) {
            this.playerReceiving40Plus = playerReceiving40Plus;
        }

        public void setPlayerReceivingFumbles(String playerReceivingFumbles) {
            this.playerReceivingFumbles = playerReceivingFumbles;
        }

        public void setPlayerTacklesSolo(String playerTacklesSolo) {
            this.playerTacklesSolo = playerTacklesSolo;
        }

        public void setPlayerTacklesTotal(String playerTacklesTotal) {
            this.playerTacklesTotal = playerTacklesTotal;
        }

        public void setPlayerTacklesAssist(String playerTacklesAssist) {
            this.playerTacklesAssist = playerTacklesAssist;
        }

        public void setPlayerSacks(String playerSacks) {
            this.playerSacks = playerSacks;
        }

        public void setPlayerSackYards(String playerSackYards) {
            this.playerSackYards = playerSackYards;
        }

        public void setPlayerSafeties(String playerSafeties) {
            this.playerSafeties = playerSafeties;
        }

        public void setPlayerTacklesForLoss(String playerTacklesForLoss) {
            this.playerTacklesForLoss = playerTacklesForLoss;
        }

        public void setPlayerInterceptions(String playerInterceptions) {
            this.playerInterceptions = playerInterceptions;
        }

        public void setPlayerInterceptionYards(String playerInterceptionYards) {
            this.playerInterceptionYards = playerInterceptionYards;
        }

        public void setPlayerInterceptionAverage(String playerInterceptionAverage) {
            this.playerInterceptionAverage = playerInterceptionAverage;
        }

        public void setPlayerInterceptionLong(String playerInterceptionLong) {
            this.playerInterceptionLong = playerInterceptionLong;
        }

        public void setPlayerPassesDefended(String playerPassesDefended) {
            this.playerPassesDefended = playerPassesDefended;
        }

        public void setPlayerStuffs(String playerStuffs) {
            this.playerStuffs = playerStuffs;
        }

        public void setPlayerStuffYards(String playerStuffYards) {
            this.playerStuffYards = playerStuffYards;
        }

        public void setPlayerKB(String playerKB) {
            this.playerKB = playerKB;
        }

        public void setPlayerFumbles(String playerFumbles) {
            this.playerFumbles = playerFumbles;
        }

        public void setPlayerFumblesLost(String playerFumblesLost) {
            this.playerFumblesLost = playerFumblesLost;
        }

        public void setPlayerFumblesForced(String playerFumblesForced) {
            this.playerFumblesForced = playerFumblesForced;
        }

        public void setPlayerFumblesOwnReceived(String playerFumblesOwnReceived) {
            this.playerFumblesOwnReceived = playerFumblesOwnReceived;
        }

        public void setPlayerFumblesOppReceived(String playerFumblesOppReceived) {
            this.playerFumblesOppReceived = playerFumblesOppReceived;
        }

        public void setPlayerFumblesReceivingYards(String playerFumblesReceivingYards) {
            this.playerFumblesReceivingYards = playerFumblesReceivingYards;
        }

        public void setPlayerFumbleTouchdowns(String playerFumbleTouchdowns) {
            this.playerFumbleTouchdowns = playerFumbleTouchdowns;
        }

        public void setPlayerKickReturn(String playerKickReturn) {
            this.playerKickReturn = playerKickReturn;
        }

        public void setPlayerKickReturnYards(String playerKickReturnYards) {
            this.playerKickReturnYards = playerKickReturnYards;
        }

        public void setPlayerKickReturnAverage(String playerKickReturnAverage) {
            this.playerKickReturnAverage = playerKickReturnAverage;
        }

        public void setPlayerKickReturnLong(String playerKickReturnLong) {
            this.playerKickReturnLong = playerKickReturnLong;
        }

        public void setPlayerKickReturnTouchdowns(String playerKickReturnTouchdowns) {
            this.playerKickReturnTouchdowns = playerKickReturnTouchdowns;
        }

        public void setPlayerKickReturn20Plus(String playerKickReturn20Plus) {
            this.playerKickReturn20Plus = playerKickReturn20Plus;
        }

        public void setPlayerKickReturn40Plus(String playerKickReturn40Plus) {
            this.playerKickReturn40Plus = playerKickReturn40Plus;
        }

        public void setPlayerKickReturnFC(String playerKickReturnFC) {
            this.playerKickReturnFC = playerKickReturnFC;
        }

        public void setPlayerKickReturnFumbles(String playerKickReturnFumbles) {
            this.playerKickReturnFumbles = playerKickReturnFumbles;
        }

        public void setPlayerPuntReturn(String playerPuntReturn) {
            this.playerPuntReturn = playerPuntReturn;
        }

        public void setPlayerPuntReturnYards(String playerPuntReturnYards) {
            this.playerPuntReturnYards = playerPuntReturnYards;
        }

        public void setPlayerPuntReturnAverage(String playerPuntReturnAverage) {
            this.playerPuntReturnAverage = playerPuntReturnAverage;
        }

        public void setPlayerPuntReturnLong(String playerPuntReturnLong) {
            this.playerPuntReturnLong = playerPuntReturnLong;
        }

        public void setPlayerPuntReturnTouchdowns(String playerPuntReturnTouchdowns) {
            this.playerPuntReturnTouchdowns = playerPuntReturnTouchdowns;
        }

        public void setPlayerPuntReturn20Plus(String playerPuntReturn20Plus) {
            this.playerPuntReturn20Plus = playerPuntReturn20Plus;
        }

        public void setPlayerPuntReturn40Plus(String playerPuntReturn40Plus) {
            this.playerPuntReturn40Plus = playerPuntReturn40Plus;
        }

        public void setPlayerPuntReturnFC(String playerPuntReturnFC) {
            this.playerPuntReturnFC = playerPuntReturnFC;
        }

        public void setPlayerPuntReturnFumbles(String playerPuntReturnFumbles) {
            this.playerPuntReturnFumbles = playerPuntReturnFumbles;
        }

        public void setPlayerTwoPointAttempts(String playerTwoPointAttempts) {
            this.playerTwoPointAttempts = playerTwoPointAttempts;
        }

        public void setPlayerTwoPointMakes(String playerTwoPointMakes) {
            this.playerTwoPointMakes = playerTwoPointMakes;
        }

        public void setPlayerTwoPointPassAttempts(String playerTwoPointPassAttempts) {
            this.playerTwoPointPassAttempts = playerTwoPointPassAttempts;
        }

        public void setPlayerTwoPointPassMakes(String playerTwoPointPassMakes) {
            this.playerTwoPointPassMakes = playerTwoPointPassMakes;
        }

        public void setPlayerTwoPointPassReceived(String playerTwoPointPassReceived) {
            this.playerTwoPointPassReceived = playerTwoPointPassReceived;
        }

        public void setPlayerTwoPointRushAttempts(String playerTwoPointRushAttempts) {
            this.playerTwoPointRushAttempts = playerTwoPointRushAttempts;
        }

        public void setPlayerTwoPointRushMakes(String playerTwoPointRushMakes) {
            this.playerTwoPointRushMakes = playerTwoPointRushMakes;
        }

        public String toString() {

            String nflPlayerInfoToString = playerLastName + " " + playerFirstName + " " + playerPosition
                    + " " + teamCity + " " + teamName + " " + playerGamesPlayed + " " + playerPassingAttempts
                    + " " + playerPassingCompletions + " " + playerPassingPercentage
                    + " " + playerPassingYards + " " + playerPassingAverage + " " + playerPassingYardsPerAttempt
                    + " " + playerPassingTouchdowns + " " + playerPassingTouchdownPercentage
                    + " " + playerPassingInterceptions + " " + playerPassingInterceptionPercentage
                    + " " + playerPassingLong + " " + playerPassing20Plus + " " + playerPassing40Plus
                    + " " + playerPassingSacks + " " + playerQBRating + " " + playerRushingAttempts
                    + " " + playerRushingYards + " " + playerRushingAverage + " " + playerRushingTouchdowns
                    + " " + playerRushingLong + " " + playerRushing20Plus + " " + playerRushing40Plus
                    + " " + playerRushingFumbles + " " + playerTargets + " " + playerReceptions
                    + " " + playerReceivingYards + " " + playerReceivingAverage + " " + playerReceivingTouchdowns
                    + " " + playerReceivingLong + " " + playerReceiving20Plus + " " + playerReceiving40Plus
                    + " " + playerReceivingFumbles + " " + playerTacklesSolo + " " + playerTacklesTotal
                    + " " + playerTacklesAssist + " " + playerSacks + " " + playerSacks
                    + " " + playerSackYards + " " + playerSafeties + " " + playerTacklesForLoss
                    + " " + playerInterceptions + " " + playerInterceptionYards + " " + playerInterceptionAverage
                    + " " + playerInterceptionLong + " " + playerPassesDefended + " " + playerStuffs
                    + " " + playerStuffYards + " " + playerKB + " " + playerFumbles + " " + playerFumblesLost
                    + " " + playerFumblesForced + " " + playerFumblesOwnReceived + " " + playerFumblesOppReceived
                    + " " + playerFumblesReceivingYards + " " + playerKickReturn + " " + playerKickReturnYards
                    + " " + playerKickReturnAverage + " " + playerKickReturnAverage + " " + playerKickReturnLong
                    + " " + playerKickReturnTouchdowns + " " + playerKickReturn20Plus + " " + playerKickReturn40Plus
                    + " " + playerKickReturnFC + " " + playerKickReturnFumbles + " " + playerPuntReturn
                    + " " + playerPuntReturnYards + " " + playerPuntReturnAverage + " " + playerPuntReturnLong
                    + " " + playerPuntReturnTouchdowns + " " + playerPuntReturn20Plus + " " +playerPuntReturn40Plus
                    + " " + playerPuntReturnFC + " " + playerPuntReturnFumbles + " " + playerTwoPointAttempts
                    + " " + playerTwoPointMakes + " " + playerTwoPointPassAttempts + " " + playerTwoPointPassMakes
                    + " " + playerTwoPointPassReceived + " " + playerTwoPointRushAttempts + " " + playerTwoPointRushMakes;


            return nflPlayerInfoToString;

        }

    }

    public ArrayList<NflPlayerInfo> parse(InputStream in) throws XmlPullParserException, IOException {

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

    private ArrayList<NflPlayerInfo> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        ArrayList<NflPlayerInfo> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "cum:cumulativeplayerstats");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("cum:lastUpdatedOn")) {

                skip(parser);

            } else if (name.equals("cum:playerstatsentry")) {

                entries.add(readEntry(parser));

            } else {

                skip(parser);

            }

        }

        return entries;

    }

    private NflPlayerInfo readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        NflPlayerInfo nflPlayerInfo = null;

        parser.require(XmlPullParser.START_TAG, ns, "cum:playerstatsentry");

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("cum:player")) {

                nflPlayerInfo = readPlayerInfo(parser);

            } else if (name.equals("cum:team")) {



            } else if (name.equals("cum:stats")) {



            } else {

                skip(parser);

            }

        }

        return nflPlayerInfo;

    }

    private NflPlayerInfo readPlayerInfo(XmlPullParser parser) throws XmlPullParserException, IOException {

        String playerLastName = null;
        String playerFirstName = null;
        String playerPosition = null;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName(); //cum:id

            if (name.equals("cum:ID")) {

                skip(parser);

            } else if (name.equals("cum:LastName")) {

                playerLastName = readPlayerLastName(parser);

            } else if (name.equals("cum:FirstName")) {

                playerFirstName = readPlayerFirstName(parser);

            } else if (name.equals("cum:JerseyNumber")) {

                skip(parser);

            } else if (name.equals("cum:Position")) {

                playerPosition = readPlayerPosition(parser);

            } else {

                skip(parser);

            }

        }

        return new NflPlayerInfo(playerLastName, playerFirstName, playerPosition);

    }

    private String readPlayerLastName(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:LastName");
        String playerLastName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:LastName");
        return playerLastName;

    }

    private String readPlayerFirstName(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FirstName");
        String playerFirstName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FirstName");
        return playerFirstName;

    }

    private String readPlayerPosition(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Position");
        String playerPosition = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Position");
        return playerPosition;

    }

    private String readPlayerTeamCity(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:City");
        String playerTeamCity = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:City");
        return playerTeamCity;

    }

    private String readPlayerTeamName(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Name");
        String playerTeamName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Name");
        return playerTeamName;


    }

    private String readPlayerGamesPlayer(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:GamesPlayed");
        String playerGamesPlayed = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:GamesPlayed");
        return playerGamesPlayed;

    }

    private String readPlayerPassingAttempts(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassAttempts");
        String playerPassAttempts = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassAttempts");
        return playerPassAttempts;

    }

    private String readPlayerPassingCompletions(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassCompletions");
        String playerPassCompletions = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassCompletions");
        return playerPassCompletions;

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

    /*public static class NflTeamInfo {

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

        String playerGamesPlayed;
        String playerPassingAttempts;
        String playerPassingCompletions;
        String playerPassingPercentage;
        String playerPassingYards;
        String playerPassingAverage;
        String playerPassingYardsPerAttempt;
        String playerPassingTouchdowns;
        String playerPassingTouchdownPercentage;
        String playerPassingInterceptions;
        String playerPassingInterceptionPercentage;
        String playerPassingLong;
        String playerPassing20Plus;
        String playerPassing40Plus;
        String playerPassingSacks;
        String playerPassingSackYards;
        String playerQBRating;
        String playerRushingAttempts;
        String playerRushingYards;
        String playerRushingAverage;
        String playerRushingTouchdowns;
        String playerRushingLong;
        String playerRushing20Plus;
        String playerRushing40Plus;
        String playerRushingFumbles;
        String playerTargets;
        String playerReceptions;
        String playerReceivingYards;
        String playerReceivingAverage;
        String playerReceivingTouchdowns;
        String playerReceivingLong;
        String playerReceiving20Plus;
        String playerReceiving40Plus;
        String playerReceivingFumbles;
        String playerTacklesSolo;
        String playerTacklesTotal;
        String playerTacklesAssist;
        String playerSacks;
        String playerSackYards;
        String playerSafeties;
        String playerTacklesForLoss;
        String playerInterceptions;
        String playerInterceptionYards;
        String playerInterceptionAverage;
        String playerInterceptionLong;
        String playerPassesDefended;
        String playerStuffs;
        String playerStuffYards;
        String playerKB;
        String playerFumbles;
        String playerFumblesLost;
        String playerFumblesForced;
        String playerFumblesOwnReceived;
        String playerFumblesOppReceived;
        String playerFumblesReceivingYards;
        String playerFumbleTouchdowns;
        String playerKickReturn;
        String playerKickReturnYards;
        String playerKickReturnAverage;
        String playerKickReturnLong;
        String playerKickReturnTouchdowns;
        String playerKickReturn20Plus;
        String playerKickReturn40Plus;
        String playerKickReturnFC;
        String playerKickReturnFumbles;
        String playerPuntReturn;
        String playerPuntReturnYards;
        String playerPuntReturnAverage;
        String playerPuntReutrnLong;
        String playerPuntReturnTouchdowns;
        String playerPuntReturn20Plus;
        String playerPuntReturn40Plus;
        String playerPuntReturnFC;
        String playerPuntReturnFumbles;
        String playerTwoPointAttempts;
        String playerTwoPointMakes;
        String playerTwoPointPassAttempts;
        String playerTwoPointPassMakes;
        String playerTwoPointPassReceived;
        String playerTwoPointRushAttempts;
        String playerTwoPointRushMakes;

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
    }*/

}
