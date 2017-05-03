package com.adonaubauer.sportsnewsapp;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Austin on 3/7/2017.
 */

public class NflPlayerStatsFragment extends ListFragment {

    int mNum;

    public static final String ns = null;

    ArrayList<NflPlayerInfo> playerStats;

    private static final String uRL = "https://www.mysportsfeeds.com/api/feed/pull/nfl/2016-2017-regular/cumulative_player_stats.xml";

    public NflPlayerStatsFragment() {

        playerStats = new ArrayList<>();

    }

    static NflPlayerStatsFragment newInstance(int num) {

        NflPlayerStatsFragment nflPlayerStatsFragment = new NflPlayerStatsFragment();

        Bundle args = new Bundle();

        args.putInt("num", num);

        nflPlayerStatsFragment.setArguments(args);

        return nflPlayerStatsFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNum = getArguments() != null ? getArguments().getInt("num") : 1;

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

                readPlayerTeamInfo(parser, nflPlayerInfo);

            } else if (name.equals("cum:stats")) {

                readPlayerStatsInfo(parser, nflPlayerInfo);

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

    private NflPlayerInfo readPlayerTeamInfo(XmlPullParser parser, NflPlayerInfo teamInfo) throws IOException, XmlPullParserException {

        String playerTeamCity;
        String playerTeamName;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;

            }

            String name = parser.getName();

            if (name.equals("cum:ID")) {

                skip(parser);

            } else if (name.equals("cum:City")) {

                playerTeamCity = readPlayerTeamCity(parser);

                teamInfo.setTeamCity(playerTeamCity);

            } else if (name.equals("cum:Name")) {

                playerTeamName = readPlayerTeamName(parser);

                teamInfo.setTeamName(playerTeamName);

            } else if (name.equals("cum:Abbreviation")) {

                skip(parser);

            } else {

                skip(parser);

            }

        }

        return teamInfo;

    }

    private NflPlayerInfo readPlayerStatsInfo(XmlPullParser parser, NflPlayerInfo statsInfo) throws IOException, XmlPullParserException {

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
        String playerInterceptionTouchdowns;
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
        String playerFumblesTotalReceivingYards;
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
        String playerFieldGoalBlock;
        String playerFieldGoalMakes;
        String playerFieldGoalAttempts;
        String playerFieldGoalPercentage;
        String playerFieldGoalMade1to19;
        String playerFieldGoalAttempts1to19;
        String playerFieldGoal1to19Percent;
        String playerFieldGoalMade20to29;
        String playerFieldGoalAttempts20to29;
        String playerFieldGoal20to29Percent;
        String playerFieldGoalMade30to39;
        String playerFieldGoalAttempts30to39;
        String playerFieldGoal30to39Percent;
        String playerFieldGoalMade40to49;
        String playerFieldGoalAttempts40to49;
        String playerFieldGoal40to49Percent;
        String playerFieldGoalMade50Plus;
        String playerFieldGoalAttempts50Plus;
        String playerFieldGoal50PlusPercent;
        String playerFieldGoalLong;
        String playerExtraPointBlock;
        String playerExtraPointMakes;
        String playerExtraPointAttempts;
        String playerExtraPointPercentage;
        String playerFieldGoalAndExtraPointPoints;
        String playerKickoffs;
        String playerKickoffYards;
        String playerKickoffOOB;
        String playerKickoffAverage;
        String playerKickoffTouchback;
        String playerKickoffPercent;
        String playerKickoffReturns;
        String playerKickoffReturnYards;
        String playerKickoffReturnYardAverage;
        String playerKickoffTouchdowns;
        String playerKickoffOS;
        String playerKickoffOSR;
        String playerPunts;
        String playerPuntYards;
        String playerPuntNetYards;
        String playerPuntLong;
        String playerPuntAverage;
        String playerPuntNetAverage;
        String playerPuntBlock;
        String playerPuntOOB;
        String playerPuntDown;
        String playerPuntInside20;
        String playerPuntInside20Percent;
        String playerPuntTouchback;
        String playerPuntTouchbackPercent;
        String playerPuntFC;
        String playerPuntsReturn;
        String playerPuntsReturnYards;
        String playerPuntsReturnAverage;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;

            }

            String name = parser.getName();

            if (name.equals("cum:GamesPlayed")) {

                playerGamesPlayed = readPlayerGamesPlayed(parser);

                statsInfo.setPlayerGamesPlayed(playerGamesPlayed);

            } else if (name.equals("cum:PassAttempts")) {

                playerPassingAttempts = readPlayerPassingAttempts(parser);

                statsInfo.setPlayerPassingAttempts(playerPassingAttempts);

            } else if (name.equals("cum:PassCompletions")) {

                playerPassingCompletions = readPlayerPassingCompletions(parser);

                statsInfo.setPlayerPassingCompletions(playerPassingCompletions);

            } else if (name.equals("cum:PassPct")) {

                playerPassingPercentage = readPlayerPassingPercentage(parser);

                statsInfo.setPlayerPassingPercentage(playerPassingPercentage);

            } else if (name.equals("cum:PassYards")) {

                playerPassingYards = readPlayerPassingYards(parser);

                statsInfo.setPlayerPassingYards(playerPassingYards);

            } else if (name.equals("cum:PassAvg")) {

                playerPassingAverage = readPlayerPassingAverage(parser);

                statsInfo.setPlayerPassingAverage(playerPassingAverage);

            } else if (name.equals("cum:PassYardsPerAtt")) {

                playerPassingYardsPerAttempt = readPlayerPassingYardsPerAttempt(parser);

                statsInfo.setPlayerPassingYardsPerAttempt(playerPassingYardsPerAttempt);

            } else if (name.equals("cum:PassTD")) {

                playerPassingTouchdowns = readPlayerPassingTouchdowns(parser);

                statsInfo.setPlayerPassingTouchdowns(playerPassingTouchdowns);

            } else if (name.equals("cum:PassTDPct")) {

                playerPassingTouchdownPercentage = readPlayerPassingTouchdownPercentage(parser);

                statsInfo.setPlayerPassingTouchdownPercentage(playerPassingTouchdownPercentage);

            } else if (name.equals("cum:PassInt")) {

                playerPassingInterceptions = readPlayerPassingInterceptions(parser);

                statsInfo.setPlayerPassingInterceptions(playerPassingInterceptions);

            } else if (name.equals("cum:PassIntPct")) {

                playerPassingInterceptionPercentage = readPlayerPassingInterceptionPercentage(parser);

                statsInfo.setPlayerPassingInterceptionPercentage(playerPassingInterceptionPercentage);

            } else if (name.equals("cum:PassLng")) {

                playerPassingLong = readPlayerPassingLong(parser);

                statsInfo.setPlayerPassingLong(playerPassingLong);

            } else if (name.equals("cum:Pass20Plus")) {

                playerPassing20Plus = readPlayerPassing20Plus(parser);

                statsInfo.setPlayerPassing20Plus(playerPassing20Plus);

            } else if (name.equals("cum:Pass40Plus")) {

                playerPassing40Plus = readPlayerPassing40Plus(parser);

                statsInfo.setPlayerPassing40Plus(playerPassing40Plus);

            } else if (name.equals("cum:PassSacks")) {

                playerPassingSacks = readPlayerPassingSacks(parser);

                statsInfo.setPlayerPassingSacks(playerPassingSacks);

            } else if (name.equals("cum:PassSackY")) {

                playerPassingSackYards = readPlayerPassingSacksYards(parser);

                statsInfo.setPlayerPassingSackYards(playerPassingSackYards);

            } else if (name.equals("cum:QBRating")) {

                playerQBRating = readPlayerQBRating(parser);

                statsInfo.setPlayerQBRating(playerQBRating);

            } else if (name.equals("cum:RushAttempts")) {

                playerRushingAttempts = readPlayerRushingAttempts(parser);

                statsInfo.setPlayerRushingAttempts(playerRushingAttempts);

            } else if (name.equals("cum:RushYards")) {

                playerRushingYards = readPlayerRushingYards(parser);

                statsInfo.setPlayerRushingYards(playerRushingYards);

            } else if (name.equals("cum:RushAverage")) {

                playerRushingAverage = readPlayerRushingAverage(parser);

                statsInfo.setPlayerRushingAverage(playerRushingAverage);

            } else if (name.equals("cum:RushTD")) {

                playerRushingTouchdowns = readPlayerRushingTouchdowns(parser);

                statsInfo.setPlayerRushingTouchdowns(playerRushingTouchdowns);

            } else if (name.equals("cum:RushLng")) {

                playerRushingLong = readPlayerRushingLong(parser);

                statsInfo.setPlayerRushingLong(playerRushingLong);

            } else if (name.equals("cum:Rush20Plus")) {

                playerRushing20Plus = readPlayerRushing20Plus(parser);

                statsInfo.setPlayerRushing20Plus(playerRushing20Plus);

            } else if (name.equals("cum:Rush40Plus")) {

                playerRushing40Plus = readPlayerRushing40Plus(parser);

                statsInfo.setPlayerRushing40Plus(playerRushing40Plus);

            } else if (name.equals("cum:RushFumbles")) {

                playerRushingFumbles = readPlayerRushingFumbles(parser);

                statsInfo.setPlayerRushingFumbles(playerRushingFumbles);

            } else if (name.equals("cum:Targets")) {

                playerTargets = readPlayerTargets(parser);

                statsInfo.setPlayerTargets(playerTargets);

            } else if (name.equals("cum:Receptions")) {

                playerReceptions = readPlayerReceptions(parser);

                statsInfo.setPlayerReceptions(playerReceptions);

            } else if (name.equals("cum:RecYards")) {

                playerReceivingYards = readPlayerReceivingYards(parser);

                statsInfo.setPlayerReceivingYards(playerReceivingYards);

            } else if (name.equals("cum:RecAverage")) {

                playerReceivingAverage = readPlayerReceivingAverage(parser);

                statsInfo.setPlayerReceivingAverage(playerReceivingAverage);

            } else if (name.equals("cum:RecTD")) {

                playerReceivingTouchdowns = readPlayerReceivingTouchdowns(parser);

                statsInfo.setPlayerReceivingTouchdowns(playerReceivingTouchdowns);

            } else if (name.equals("cum:RecLng")) {

                playerReceivingLong = readPlayerReceivingLong(parser);

                statsInfo.setPlayerReceivingLong(playerReceivingLong);

            } else if (name.equals("cum:Rec20Plus")) {

                playerReceiving20Plus = readPlayerReceiving20Plus(parser);

                statsInfo.setPlayerReceiving20Plus(playerReceiving20Plus);

            } else if (name.equals("cum:Rec40Plus")) {

                playerReceiving40Plus = readPlayerReceiving40Plus(parser);

                statsInfo.setPlayerReceiving40Plus(playerReceiving40Plus);

            } else if (name.equals("cum:RecFumbles")) {

                playerReceivingFumbles = readPlayerReceivingFumbles(parser);

                statsInfo.setPlayerReceivingFumbles(playerReceivingFumbles);

            } else if (name.equals("cum:TackleSolo")) {

                playerTacklesSolo = readPlayerTacklesSolo(parser);

                statsInfo.setPlayerTacklesSolo(playerTacklesSolo);

            } else if (name.equals("cum:TackleTotal")) {

                playerTacklesTotal = readPlayerTacklesTotal(parser);

                statsInfo.setPlayerTacklesTotal(playerTacklesTotal);

            } else if (name.equals("cum:TackleAst")) {

                playerTacklesAssist = readPlayerTacklesAssist(parser);

                statsInfo.setPlayerTacklesAssist(playerTacklesAssist);

            } else if (name.equals("cum:Sacks")) {

                playerSacks = readPlayerSacks(parser);

                statsInfo.setPlayerSacks(playerSacks);

            } else if (name.equals("cum:SackYds")) {

                playerSackYards = readPlayerSackYards(parser);

                statsInfo.setPlayerSackYards(playerSackYards);

            } else if (name.equals("cum:Safeties")) {

                playerSafeties = readPlayerSafeties(parser);

                statsInfo.setPlayerSafeties(playerSafeties);

            } else if (name.equals("cum:TacklesForLoss")) {

                playerTacklesForLoss = readPlayerTacklesForLoss(parser);

                statsInfo.setPlayerTacklesForLoss(playerTacklesForLoss);

            } else if (name.equals("cum:Interceptions")) {

                playerInterceptions = readPlayerInterceptions(parser);

                statsInfo.setPlayerInterceptions(playerInterceptions);

            } else if (name.equals("cum:IntTD")) {

                playerInterceptionTouchdowns = readPlayerInterceptionTouchdowns(parser);

                statsInfo.setPlayerInterceptionTouchdowns(playerInterceptionTouchdowns);

            } else if (name.equals("cum:IntYds")) {

                playerInterceptionYards = readPlayerInterceptionYards(parser);

                statsInfo.setPlayerInterceptionYards(playerInterceptionYards);

            } else if (name.equals("cum:IntAverage")) {

                playerInterceptionAverage = readPlayerInterceptionAverage(parser);

                statsInfo.setPlayerInterceptionAverage(playerInterceptionAverage);

            } else if (name.equals("cum:IntLng")) {

                playerInterceptionLong = readPlayerInterceptionLong(parser);

                statsInfo.setPlayerInterceptionLong(playerInterceptionLong);

            } else if (name.equals("cum:PassesDefended")) {

                playerPassesDefended = readPlayerPassesDefended(parser);

                statsInfo.setPlayerPassesDefended(playerPassesDefended);

            } else if (name.equals("cum:Stuffs")) {

                playerStuffs = readPlayerStuffs(parser);

                statsInfo.setPlayerStuffs(playerStuffs);

            } else if (name.equals("cum:StuffYds")) {

                playerStuffYards = readPlayerStuffYards(parser);

                statsInfo.setPlayerStuffYards(playerStuffYards);

            } else if (name.equals("cum:KB")) {

                playerKB = readPlayerKB(parser);

                statsInfo.setPlayerKB(playerKB);

            } else if (name.equals("cum:Fumbles")) {

                playerFumbles = readPlayerFumbles(parser);

                statsInfo.setPlayerFumbles(playerFumbles);

            } else if (name.equals("cum:FumLost")) {

                playerFumblesLost = readPlayerFumblesLost(parser);

                statsInfo.setPlayerFumblesLost(playerFumblesLost);

            } else if (name.equals("cum:FumForced")) {

                playerFumblesForced = readPlayerFumblesForced(parser);

                statsInfo.setPlayerFumblesForced(playerFumblesForced);

            } else if (name.equals("cum:FumOwnRec")) {

                playerFumblesOwnReceived = readPlayerFumblesOwnReceived(parser);

                statsInfo.setPlayerFumblesOwnReceived(playerFumblesOwnReceived);

            } else if (name.equals("cum:FumOppRec")) {

                playerFumblesOppReceived = readPlayerFumblesOppReceived(parser);

                statsInfo.setPlayerFumblesOppReceived(playerFumblesOppReceived);

            } else if (name.equals("cum:FumRecYds")) {

                playerFumblesReceivingYards = readPlayerFumblesReceivingYards(parser);

                statsInfo.setPlayerFumblesReceivingYards(playerFumblesReceivingYards);

            } else if (name.equals("cum:FumTotalRec")) {

                playerFumblesTotalReceivingYards = readPlayerFumblesTotalReceivingYards(parser);

                statsInfo.setPlayerFumblesTotalReceivingYards(playerFumblesTotalReceivingYards);

            } else if (name.equals("cum:FumTD")) {

                playerFumbleTouchdowns = readPlayerFumbleTouchdowns(parser);

                statsInfo.setPlayerFumbleTouchdowns(playerFumbleTouchdowns);

            } else if (name.equals("cum:KrRet")) {

                playerKickReturn = readPlayerKickReturn(parser);

                statsInfo.setPlayerKickReturn(playerKickReturn);

            } else if (name.equals("cum:KrYds")) {

                playerKickReturnYards = readPlayerKickReturnYards(parser);

                statsInfo.setPlayerKickReturnYards(playerKickReturnYards);

            } else if (name.equals("cum:KrAvg")) {

                playerKickReturnAverage = readPlayerKickReturnAverage(parser);

                statsInfo.setPlayerKickReturnAverage(playerKickReturnAverage);

            } else if (name.equals("cum:KrLng")) {

                playerKickReturnLong = readPlayerKickReturnLong(parser);

                statsInfo.setPlayerKickReturnLong(playerKickReturnLong);

            } else if (name.equals("cum:KrTD")) {

                playerKickReturnTouchdowns = readPlayerKickReturnTouchdown(parser);

                statsInfo.setPlayerKickReturnTouchdowns(playerKickReturnTouchdowns);

            } else if (name.equals("cum:Kr20Plus")) {

                playerKickReturn20Plus = readPlayerKickReturn20Plus(parser);

                statsInfo.setPlayerKickReturn20Plus(playerKickReturn20Plus);

            } else if (name.equals("cum:Kr40Plus")) {

                playerKickReturn40Plus = readPlayerKickReturn40Plus(parser);

                statsInfo.setPlayerKickReturn40Plus(playerKickReturn40Plus);

            } else if (name.equals("cum:KrFC")) {

                playerKickReturnFC = readPlayerKickReturnFC(parser);

                statsInfo.setPlayerKickReturnFC(playerKickReturnFC);

            } else if (name.equals("cum:KrFum")) {

                playerKickReturnFumbles = readPlayerKickReturnFumbles(parser);

                statsInfo.setPlayerKickReturnFumbles(playerKickReturnFumbles);

            } else if (name.equals("cum:PrRet")) {

                playerPuntReturn = readPlayerPuntReturn(parser);

                statsInfo.setPlayerPuntReturn(playerPuntReturn);

            } else if (name.equals("cum:PrYds")) {

                playerPuntReturnYards = readPlayerPuntReturnYards(parser);

                statsInfo.setPlayerPuntReturnYards(playerPuntReturnYards);

            } else if (name.equals("cum:PrAvg")) {

                playerPuntReturnAverage = readPlayerPuntReturnAverage(parser);

                statsInfo.setPlayerPuntReturnAverage(playerPuntReturnAverage);

            } else if (name.equals("cum:PrLng")) {

                playerPuntReturnLong = readPlayerPuntReturnLong(parser);

                statsInfo.setPlayerPuntReturnLong(playerPuntReturnLong);

            } else if (name.equals("cum:PrTD")) {

                playerPuntReturnTouchdowns = readPlayerPuntReturnTouchdowns(parser);

                statsInfo.setPlayerPuntReturnTouchdowns(playerPuntReturnTouchdowns);

            } else if (name.equals("cum:Pr20Plus")) {

                playerPuntReturn20Plus = readPlayerPuntReturn20Plus(parser);

                statsInfo.setPlayerPuntReturn20Plus(playerPuntReturn20Plus);

            } else if (name.equals("cum:Pr40Plus")) {

                playerPuntReturn40Plus = readPlayerPuntReturn40Plus(parser);

                statsInfo.setPlayerPuntReturn40Plus(playerPuntReturn40Plus);

            } else if (name.equals("cum:PrFC")) {

                playerPuntReturnFC = readPlayerPuntReturnFC(parser);

                statsInfo.setPlayerPuntReturnFC(playerPuntReturnFC);

            } else if (name.equals("cum:PrFum")) {

                playerPuntReturnFumbles = readPlayerPuntReturnFumbles(parser);

                statsInfo.setPlayerPuntReturnFumbles(playerPuntReturnFumbles);

            } else if (name.equals("cum:TwoPtAtt")) {

                playerTwoPointAttempts = readPlayerTwoPointAttempts(parser);

                statsInfo.setPlayerTwoPointAttempts(playerTwoPointAttempts);

            } else if (name.equals("cum:TwoPtMade")) {

                playerTwoPointMakes = readPlayerTwoPointMakes(parser);

                statsInfo.setPlayerTwoPointMakes(playerTwoPointMakes);

            } else if (name.equals("cum:TwoPtPassAtt")) {

                playerTwoPointPassAttempts = readPlayerTwoPointPassAttempts(parser);

                statsInfo.setPlayerTwoPointPassAttempts(playerTwoPointPassAttempts);

            } else if (name.equals("cum:TwoPtPassMade")) {

                playerTwoPointPassMakes = readPlayerTwoPointPassMakes(parser);

                statsInfo.setPlayerTwoPointPassMakes(playerTwoPointPassMakes);

            } else if (name.equals("cum:TwoPtPassRec")) {

                playerTwoPointPassReceived = readPlayerTwoPointPassReceived(parser);

                statsInfo.setPlayerTwoPointPassReceived(playerTwoPointPassReceived);

            } else if (name.equals("cum:TwoPtRushAtt")) {

                playerTwoPointRushAttempts = readPlayerTwoPointRushAttempts(parser);

                statsInfo.setPlayerTwoPointRushAttempts(playerTwoPointRushAttempts);

            } else if (name.equals("cum:TwoPtRushMade")) {

                playerTwoPointRushMakes = readPlayerTwoPointRushMakes(parser);

                statsInfo.setPlayerTwoPointRushMakes(playerTwoPointRushMakes);

            } else if (name.equals("cum:FgBlk")) {

                playerFieldGoalBlock = readPlayerFieldGoalBlock(parser);

                statsInfo.setPlayerFieldGoalBlock(playerFieldGoalBlock);

            } else if (name.equals("cum:FgMade")) {

                playerFieldGoalMakes = readPlayerFieldGoalMade(parser);

                statsInfo.setPlayerFieldGoalMakes(playerFieldGoalMakes);

            } else if (name.equals("cum:FgAtt")) {

                playerFieldGoalAttempts = readPlayerFieldGoalAttempts(parser);

                statsInfo.setPlayerFieldGoalAttempts(playerFieldGoalAttempts);

            } else if (name.equals("cum:FgPct")) {

                playerFieldGoalPercentage = readPlayerFieldGoalPercent(parser);

                statsInfo.setPlayerFieldGoalPercentage(playerFieldGoalPercentage);

            } else if (name.equals("cum:FgMade1_19")) {

                playerFieldGoalMade1to19 = readPlayerFieldGoalMade1to19(parser);

                statsInfo.setPlayerFieldGoalMade1to19(playerFieldGoalMade1to19);

            } else if (name.equals("cum:FgAtt1_19")) {

                playerFieldGoalAttempts1to19 = readPlayerFieldGoalAttempts1to19(parser);

                statsInfo.setPlayerFieldGoalAttempts1to19(playerFieldGoalAttempts1to19);

            } else if (name.equals("cum:Fg1_19Pct")) {

                playerFieldGoal1to19Percent = readPlayerFieldGoalPercent1to19(parser);

                statsInfo.setPlayerFieldGoal1to19Percent(playerFieldGoal1to19Percent);

            } else if (name.equals("cum:FgMade20_29")) {

                playerFieldGoalMade20to29 = readPlayerFieldGoalMade20to29(parser);

                statsInfo.setPlayerFieldGoalMade20to29(playerFieldGoalMade20to29);

            } else if (name.equals("cum:FgAtt20_29")) {

                playerFieldGoalAttempts20to29 = readPlayerFieldGoalAttempts20to29(parser);

                statsInfo.setPlayerFieldGoalAttempts20to29(playerFieldGoalAttempts20to29);

            } else if (name.equals("cum:Fg20_29Pct")) {

                playerFieldGoal20to29Percent = readPlayerFieldGoalPercent20to29(parser);

                statsInfo.setPlayerFieldGoal20to29Percent(playerFieldGoal20to29Percent);

            } else if (name.equals("cum:FgMade30_39")) {

                playerFieldGoalMade30to39 = readPlayerFieldGoalMade30to39(parser);

                statsInfo.setPlayerFieldGoalMade30to39(playerFieldGoalMade30to39);

            } else if (name.equals("cum:FgAtt30_39")) {

                playerFieldGoalAttempts30to39 = readPlayerFieldGoalAttempts30to39(parser);

                statsInfo.setPlayerFieldGoalAttempts30to39(playerFieldGoalAttempts30to39);

            } else if (name.equals("cum:Fg30_39Pct")) {

                playerFieldGoal30to39Percent = readPlayerFieldGoalPercent30to39(parser);

                statsInfo.setPlayerFieldGoal30to39Percent(playerFieldGoal30to39Percent);

            } else if (name.equals("cum:FgMade40_49")) {

                playerFieldGoalMade40to49 = readPlayerFieldGoalMade40to49(parser);

                statsInfo.setPlayerFieldGoalMade40to49(playerFieldGoalMade40to49);

            } else if (name.equals("cum:FgAtt40_49")) {

                playerFieldGoalAttempts40to49 = readPlayerFieldGoalAttempts40to49(parser);

                statsInfo.setPlayerFieldGoalAttempts40to49(playerFieldGoalAttempts40to49);

            } else if (name.equals("cum:Fg40_49Pct")) {

                playerFieldGoal40to49Percent = readPlayerFieldGoalPercent40to49(parser);

                statsInfo.setPlayerFieldGoal40to49Percent(playerFieldGoal40to49Percent);

            } else if (name.equals("cum:FgMade50Plus")) {

                playerFieldGoalMade50Plus = readPlayerFieldGoalMade50Plus(parser);

                statsInfo.setPlayerFieldGoalMade50Plus(playerFieldGoalMade50Plus);

            } else if (name.equals("cum:FgAtt50Plus")) {

                playerFieldGoalAttempts50Plus = readPlayerFieldGoalAttempts50Plus(parser);

                statsInfo.setPlayerFieldGoalAttempts50Plus(playerFieldGoalAttempts50Plus);

            } else if (name.equals("cum:Fg50PlusPct")) {

                playerFieldGoal50PlusPercent = readPlayerFieldGoalPercent50Plus(parser);

                statsInfo.setPlayerFieldGoal50PlusPercent(playerFieldGoal50PlusPercent);

            } else if (name.equals("cum:FgLng")) {

                playerFieldGoalLong = readPlayerFieldGoalLong(parser);

                statsInfo.setPlayerFieldGoalLong(playerFieldGoalLong);

            } else if (name.equals("cum:XpBlk")) {

                playerExtraPointBlock = readPlayerExtraPointBlock(parser);

                statsInfo.setPlayerExtraPointBlock(playerExtraPointBlock);

            } else if (name.equals("cum:XpMade")) {

                playerExtraPointMakes = readPlayerExtraPointMade(parser);

                statsInfo.setPlayerExtraPointMakes(playerExtraPointMakes);

            } else if (name.equals("cum:XpAtt")) {

                playerExtraPointAttempts = readPlayerExtraPointAttempts(parser);

                statsInfo.setPlayerExtraPointAttempts(playerExtraPointAttempts);

            } else if (name.equals("cum:XpPct")) {

                playerExtraPointPercentage = readPlayerExtraPointPercent(parser);

                statsInfo.setPlayerExtraPointPercentage(playerExtraPointPercentage);

            } else if (name.equals("cum:FgAndXpPts")) {

                playerFieldGoalAndExtraPointPoints = readPlayerFieldGoalAndExtraPointPoints(parser);

                statsInfo.setPlayerFieldGoalAndExtraPointPoints(playerFieldGoalAndExtraPointPoints);

            } else if (name.equals("cum:Kickoffs")) {

                playerKickoffs = readPlayerKickoffs(parser);

                statsInfo.setPlayerKickoffs(playerKickoffs);

            } else if (name.equals("cum:KoYds")) {

                playerKickoffYards = readPlayerKickoffYards(parser);

                statsInfo.setPlayerKickoffYards(playerKickoffYards);

            } else if (name.equals("cum:KoOOB")) {

                playerKickoffOOB = readPlayerKickoffOOB(parser);

                statsInfo.setPlayerKickoffOOB(playerKickoffOOB);

            } else if (name.equals("cum:KoAvg")) {

                playerKickoffAverage = readPlayerKickoffAverage(parser);

                statsInfo.setPlayerKickoffAverage(playerKickoffAverage);

            } else if (name.equals("cum:KoTB")) {

                playerKickoffTouchback = readPlayerKickofTouchback(parser);

                statsInfo.setPlayerKickoffTouchback(playerKickoffTouchback);

            } else if (name.equals("cum:KoPct")) {

                playerKickoffPercent = readPlayerKickoffPercent(parser);

                statsInfo.setPlayerKickoffPercent(playerKickoffPercent);

            } else if (name.equals("cum:KoRet")) {

                playerKickoffReturns = readPlayerKickoffReturn(parser);

                statsInfo.setPlayerKickoffReturns(playerKickoffReturns);

            } else if (name.equals("cum:KoRetYds")) {

                playerKickoffReturnYards = readPlayerKickoffReturnYards(parser);

                statsInfo.setPlayerKickoffReturnYards(playerKickoffReturnYards);

            } else if (name.equals("cum:KoRetAvgYds")) {

                playerKickoffReturnYardAverage = readPlayerKickoffReturnAverageYards(parser);

                statsInfo.setPlayerKickoffReturnYardAverage(playerKickoffReturnYardAverage);

            } else if (name.equals("cum:KoTD")) {

                playerKickoffTouchdowns = readPlayerKickoffReturnTouchdown(parser);

                statsInfo.setPlayerKickoffTouchdowns(playerKickoffTouchdowns);

            } else if (name.equals("cum:KoOS")) {

                playerKickoffOS = readPlayerKickoffOS(parser);

                statsInfo.setPlayerKickoffOS(playerKickoffOS);

            } else if (name.equals("cum:KoOSR")) {

                playerKickoffOSR = readPlayerKickoffOSR(parser);

                statsInfo.setPlayerKickoffOSR(playerKickoffOSR);

            } else if (name.equals("cum:Punts")) {

                playerPunts = readPlayerPunts(parser);

                statsInfo.setPlayerPunts(playerPunts);

            } else if (name.equals("cum:PuntYds")) {

                playerPuntYards = readPlayerPuntYards(parser);

                statsInfo.setPlayerPuntYards(playerPuntYards);

            } else if (name.equals("cum:PuntNetYds")) {

                playerPuntNetYards = readPlayerPuntNetYards(parser);

                statsInfo.setPlayerPuntNetYards(playerPuntNetYards);
            } else if (name.equals("cum:PuntLng")) {

                playerPuntLong = readPlayerPuntLong(parser);

                statsInfo.setPlayerPuntLong(playerPuntLong);

            } else if (name.equals("cum:PuntAvg")) {

                playerPuntAverage = readPlayerPuntAverage(parser);

                statsInfo.setPlayerPuntAverage(playerPuntAverage);

            } else if (name.equals("cum:PuntNetAvg")) {

                playerPuntNetAverage = readPlayerPuntNetAverage(parser);

                statsInfo.setPlayerPuntNetAverage(playerPuntNetAverage);

            } else if (name.equals("cum:PuntBlk")) {

                playerPuntBlock = readPlayerPuntBlock(parser);

                statsInfo.setPlayerPuntBlock(playerPuntBlock);

            } else if (name.equals("cum:PuntOOB")) {

                playerPuntOOB = readPlayerPuntOOB(parser);

                statsInfo.setPlayerPuntOOB(playerPuntOOB);

            } else if (name.equals("cum:PuntDown")) {

                playerPuntDown = readPlayerPuntDown(parser);

                statsInfo.setPlayerPuntDown(playerPuntDown);

            } else if (name.equals("cum:PuntIn20")) {

                playerPuntInside20 = readPlayerPuntIn20(parser);

                statsInfo.setPlayerPuntInside20(playerPuntInside20);

            } else if (name.equals("cum:PuntIn20Pct")) {

                playerPuntInside20Percent = readPlayerPuntIn20Percent(parser);

                statsInfo.setPlayerPuntInside20Percent(playerPuntInside20Percent);

            } else if (name.equals("cum:PuntTB")) {

                playerPuntTouchback = readPlayerPuntTB(parser);

                statsInfo.setPlayerPuntTouchback(playerPuntTouchback);

            } else if (name.equals("cum:PuntTBPct")) {

                playerPuntTouchbackPercent = readPlayerPuntTBPercent(parser);

                statsInfo.setPlayerPuntTouchbackPercent(playerPuntTouchbackPercent);

            } else if (name.equals("cum:PuntFC")) {

                playerPuntFC = readPlayerPuntFC(parser);

                statsInfo.setPlayerPuntFC(playerPuntFC);

            } else if (name.equals("cum:PuntRet")) {

                playerPuntsReturn = readPlayerPuntPuntReturn(parser);

                statsInfo.setPlayerPuntsReturn(playerPuntsReturn);

            } else if (name.equals("cum:PuntRetYds")) {

                playerPuntsReturnYards = readPlayerPuntPuntReturnYards(parser);

                statsInfo.setPlayerPuntsReturnYards(playerPuntsReturnYards);

            } else if (name.equals("cum:PuntRetAvg")) {

                playerPuntsReturnAverage = readPlayerPuntPuntReturnAverage(parser);

                statsInfo.setPlayerPuntsReturnAverage(playerPuntsReturnAverage);

            } else {

                skip(parser);

            }

        }

        return statsInfo;

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

    private String readPlayerGamesPlayed(XmlPullParser parser) throws IOException, XmlPullParserException {

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

    private String readPlayerPassingPercentage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassPct");
        String playerPassPercentage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassPct");
        return playerPassPercentage;

    }

    private String readPlayerPassingYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassYards");
        String playerPassingYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassYards");
        return playerPassingYards;

    }

    private String readPlayerPassingAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassAvg");
        String playerPassingAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassAvg");
        return playerPassingAverage;

    }

    private String readPlayerPassingYardsPerAttempt(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassYardsPerAtt");
        String playerPassingYardsPerAttempt = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassYardsPerAtt");
        return playerPassingYardsPerAttempt;

    }

    private String readPlayerPassingTouchdowns(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassTD");
        String playerPassingTouchdowns = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassTD");
        return playerPassingTouchdowns;

    }

    private String readPlayerPassingTouchdownPercentage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassTDPct");
        String playerPassingTouchdownPercentage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassTDPct");
        return playerPassingTouchdownPercentage;

    }

    private String readPlayerPassingInterceptions(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassInt");
        String playerPassingInterceptions = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassInt");
        return playerPassingInterceptions;

    }

    private String readPlayerPassingInterceptionPercentage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassIntPct");
        String playerPassingInterceptionPercentage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassIntPct");
        return playerPassingInterceptionPercentage;

    }

    private String readPlayerPassingLong(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassLng");
        String playerPassingLong = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassLng");
        return playerPassingLong;

    }

    private String readPlayerPassing20Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Pass20Plus");
        String playerPassing20Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Pass20Plus");
        return playerPassing20Plus;

    }

    private String readPlayerPassing40Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Pass40Plus");
        String playerPassing40Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Pass40Plus");
        return playerPassing40Plus;

    }

    private String readPlayerPassingSacks(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassSacks");
        String playerPassingSacks = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassSacks");
        return playerPassingSacks;

    }

    private String readPlayerPassingSacksYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassSackY");
        String playerPassingSacksYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassSackY");
        return playerPassingSacksYards;

    }

    private String readPlayerQBRating(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:QBRating");
        String playerQBRating = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:QBRating");
        return playerQBRating;

    }

    private String readPlayerRushingAttempts(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RushAttempts");
        String playerRushingAttempts = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RushAttempts");
        return playerRushingAttempts;

    }

    private String readPlayerRushingYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RushYards");
        String playerRushingYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RushYards");
        return playerRushingYards;

    }

    private String readPlayerRushingAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RushAverage");
        String playerRushingAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RushAverage");
        return playerRushingAverage;

    }

    private String readPlayerRushingTouchdowns(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RushTD");
        String playerRushingTouchdowns = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RushTD");
        return playerRushingTouchdowns;

    }

    private String readPlayerRushingLong(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RushLng");
        String playerRushingLong = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RushLng");
        return playerRushingLong;

    }

    private String readPlayerRushing20Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Rush20Plus");
        String playerRushing20Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Rush20Plus");
        return playerRushing20Plus;

    }

    private String readPlayerRushing40Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Rush40Plus");
        String playerRushing40Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Rush40Plus");
        return playerRushing40Plus;

    }

    private String readPlayerRushingFumbles(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RushFumbles");
        String playerRushingFumbles = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RushFumbles");
        return playerRushingFumbles;

    }

    private String readPlayerTargets(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Targets");
        String playerTargets = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Targets");
        return playerTargets;

    }

    private String readPlayerReceptions(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Receptions");
        String playerReceptions = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Receptions");
        return playerReceptions;

    }

    private String readPlayerReceivingYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RecYards");
        String playerReceivingYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RecYards");
        return playerReceivingYards;

    }

    private String readPlayerReceivingAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RecAverage");
        String playerReceivingAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RecAverage");
        return playerReceivingAverage;

    }

    private String readPlayerReceivingTouchdowns(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RecTD");
        String playerReceivingTouchdowns = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RecTD");
        return playerReceivingTouchdowns;

    }

    private String readPlayerReceivingLong(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RecLng");
        String playerReceivingLong = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RecLng");
        return playerReceivingLong;

    }

    private String readPlayerReceiving20Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Rec20Plus");
        String playerReceiving20Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Rec20Plus");
        return playerReceiving20Plus;

    }

    private String readPlayerReceiving40Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Rec40Plus");
        String playerReceiving40Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Rec40Plus");
        return playerReceiving40Plus;

    }

    private String readPlayerReceivingFumbles(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:RecFumbles");
        String playerReceivingFumbles = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:RecFumbles");
        return playerReceivingFumbles;

    }

    private String readPlayerTacklesSolo(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TackleSolo");
        String playerTacklesSolo = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TackleSolo");
        return playerTacklesSolo;

    }

    private String readPlayerTacklesTotal(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TackleTotal");
        String playerTacklesTotal = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TackleTotal");
        return playerTacklesTotal;

    }

    private String readPlayerTacklesAssist(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TackleAst");
        String playerTackleAssist = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TackleAst");
        return playerTackleAssist;

    }

    private String readPlayerSacks(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Sacks");
        String playerSacks = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Sacks");
        return playerSacks;

    }

    private String readPlayerSackYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:SackYds");
        String playerSackYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:SackYds");
        return playerSackYards;

    }

    private String readPlayerSafeties(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Safeties");
        String playerSafeties = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Safeties");
        return playerSafeties;

    }

    private String readPlayerTacklesForLoss(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TacklesForLoss");
        String playerTacklesForLoss = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TacklesForLoss");
        return playerTacklesForLoss;

    }

    private String readPlayerInterceptions(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Interceptions");
        String playerInterceptions = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Interceptions");
        return playerInterceptions;

    }

    private String readPlayerInterceptionTouchdowns(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:IntTD");
        String playerInterceptionTouchdowns = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:IntTD");
        return playerInterceptionTouchdowns;

    }

    private String readPlayerInterceptionYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:IntYds");
        String playerInterceptionYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:IntYds");
        return playerInterceptionYards;

    }

    private String readPlayerInterceptionAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:IntAverage");
        String playerInterceptionAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:IntAverage");
        return playerInterceptionAverage;

    }

    private String readPlayerInterceptionLong(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:IntLng");
        String playerInterceptionLong = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:IntLng");
        return playerInterceptionLong;

    }

    private String readPlayerPassesDefended(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PassesDefended");
        String passesDefendedPasses = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PassesDefended");
        return passesDefendedPasses;

    }

    private String readPlayerStuffs(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Stuffs");
        String playerStuffs = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Stuffs");
        return playerStuffs;

    }

    private String readPlayerStuffYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:StuffYds");
        String playerStuffYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:StuffYds");
        return playerStuffYards;

    }

    private String readPlayerKB(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KB");
        String playerKB = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KB");
        return playerKB;

    }

    private String readPlayerFumbles(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Fumbles");
        String playerFumbles = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Fumbles");
        return playerFumbles;

    }

    private String readPlayerFumblesLost(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FumLost");
        String playerFumblesLost = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FumLost");
        return playerFumblesLost;

    }

    private String readPlayerFumblesForced(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FumForced");
        String playerFumblesForced = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FumForced");
        return playerFumblesForced;

    }

    private String readPlayerFumblesOwnReceived(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FumOwnRec");
        String playerFumblesOwnRec = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FumOwnRec");
        return playerFumblesOwnRec;

    }

    private String readPlayerFumblesOppReceived(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FumOppRec");
        String playerFumblesOppRec = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FumOppRec");
        return playerFumblesOppRec;

    }

    private String readPlayerFumblesReceivingYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FumRecYds");
        String playerFumblesRecYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FumRecYds");
        return playerFumblesRecYards;

    }

    private String readPlayerFumblesTotalReceivingYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FumTotalRec");
        String playerFumblesTotalReceivingYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FumTotalRec");
        return playerFumblesTotalReceivingYards;

    }

    private String readPlayerFumbleTouchdowns(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FumTD");
        String playerFumTD = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FumTD");
        return playerFumTD;

    }

    private String readPlayerKickReturn(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KrRet");
        String playerKickReturn = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KrRet");
        return playerKickReturn;

    }

    private String readPlayerKickReturnYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KrYds");
        String playerKickReturnYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KrYds");
        return playerKickReturnYards;

    }

    private String readPlayerKickReturnAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KrAvg");
        String playerKickReturnAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KrAvg");
        return playerKickReturnAverage;

    }

    private String readPlayerKickReturnLong(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KrLng");
        String playerKickReturnLong = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KrLng");
        return playerKickReturnLong;

    }

    private String readPlayerKickReturnTouchdown(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KrTD");
        String playerKickReturnTouchdown = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KrTD");
        return playerKickReturnTouchdown;

    }

    private String readPlayerKickReturn20Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Kr20Plus");
        String playerKickReturn20Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Kr20Plus");
        return playerKickReturn20Plus;

    }

    private String readPlayerKickReturn40Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Kr40Plus");
        String playerKickReturn40Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Kr40Plus");
        return playerKickReturn40Plus;

    }

    private String readPlayerKickReturnFC(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KrFC");
        String playerKickReturnFC = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KrFC");
        return playerKickReturnFC;

    }

    private String readPlayerKickReturnFumbles(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KrFum");
        String playerKickReturnFumbles = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KrFum");
        return playerKickReturnFumbles;

    }

    private String readPlayerPuntReturn(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PrRet");
        String playerPuntReturn = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PrRet");
        return playerPuntReturn;

    }

    private String readPlayerPuntReturnYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PrYds");
        String playerPuntReturnYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PrYds");
        return playerPuntReturnYards;

    }

    private String readPlayerPuntReturnAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PrAvg");
        String playerPuntReturnAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PrAvg");
        return playerPuntReturnAverage;

    }

    private String readPlayerPuntReturnLong(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns , "cum:PrLng");
        String playerPuntReturnLong = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PrLng");
        return playerPuntReturnLong;

    }

    private String readPlayerPuntReturnTouchdowns(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PrTD");
        String playerPuntReturnTouchdown = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PrTD");
        return playerPuntReturnTouchdown;

    }

    private String readPlayerPuntReturn20Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Pr20Plus");
        String playerPuntReturn20Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Pr20Plus");
        return playerPuntReturn20Plus;

    }

    private String readPlayerPuntReturn40Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Pr40Plus");
        String playerPuntReturn40Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Pr40Plus");
        return playerPuntReturn40Plus;

    }

    private String readPlayerPuntReturnFC(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PrFC");
        String playerPuntReturnFC = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PrFC");
        return playerPuntReturnFC;

    }

    private String readPlayerPuntReturnFumbles(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PrFum");
        String playerPuntReturnFumbles = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PrFum");
        return playerPuntReturnFumbles;

    }

    private String readPlayerTwoPointAttempts(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TwoPtAtt");
        String playerTwoPointAttempts = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TwoPtAtt");
        return playerTwoPointAttempts;

    }

    private String readPlayerTwoPointMakes(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TwoPtMade");
        String playerTwoPointMade = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TwoPtMade");
        return playerTwoPointMade;

    }

    private String readPlayerTwoPointPassAttempts(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TwoPtPassAtt");
        String playerTwoPointPassAttempts = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TwoPtPassAtt");
        return playerTwoPointPassAttempts;

    }

    private String readPlayerTwoPointPassMakes(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TwoPtPassMade");
        String playerTwoPointPassMakes = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TwoPtPassMade");
        return playerTwoPointPassMakes;

    }

    private String readPlayerTwoPointPassReceived(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TwoPtPassRec");
        String playerTwoPointPassRec = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TwoPtPassRec");
        return playerTwoPointPassRec;

    }

    private String readPlayerTwoPointRushAttempts(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TwoPtRushAtt");
        String playerTwoPointRushAttempts = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TwoPtRushAtt");
        return playerTwoPointRushAttempts;

    }

    private String readPlayerTwoPointRushMakes(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:TwoPtRushMade");
        String playerTwoPointRushMakes = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:TwoPtRushMade");
        return playerTwoPointRushMakes;

    }

    private String readPlayerFieldGoalBlock(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgBlk");
        String playerFieldGoalBlock = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgBlk");
        return playerFieldGoalBlock;

    }

    private String readPlayerFieldGoalMade(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgMade");
        String playerFieldGoalMade = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgMade");
        return playerFieldGoalMade;

    }

    private String readPlayerFieldGoalAttempts(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgAtt");
        String playerFieldGoalAttempts = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgAtt");
        return playerFieldGoalAttempts;

    }

    private String readPlayerFieldGoalPercent(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgPct");
        String playerFieldGoalPercent = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgPct");
        return playerFieldGoalPercent;

    }

    private String readPlayerFieldGoalMade1to19(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgMade1_19");
        String playerFieldGoalMade1to19 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgMade1_19");
        return playerFieldGoalMade1to19;

    }

    private String readPlayerFieldGoalAttempts1to19(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgAtt1_19");
        String playerFieldGoalAttempts1to19 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgAtt1_19");
        return playerFieldGoalAttempts1to19;

    }

    private String readPlayerFieldGoalPercent1to19(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Fg1_19Pct");
        String playerFieldGoalPercent1to19 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Fg1_19Pct");
        return playerFieldGoalPercent1to19;

    }

    private String readPlayerFieldGoalMade20to29(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgMade20_29");
        String playerFieldGoalMade20to29 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgMade20_29");
        return playerFieldGoalMade20to29;

    }

    private String readPlayerFieldGoalAttempts20to29(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgAtt20_29");
        String playerFieldGoalAttempts20to29 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgAtt20_29");
        return playerFieldGoalAttempts20to29;

    }

    private String readPlayerFieldGoalPercent20to29(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Fg20_29Pct");
        String playerFieldGoalPercent20to29 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Fg20_29Pct");
        return playerFieldGoalPercent20to29;

    }

    private String readPlayerFieldGoalMade30to39(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgMade30_39");
        String playerFieldGoalMade30to39 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgMade30_39");
        return playerFieldGoalMade30to39;

    }

    private String readPlayerFieldGoalAttempts30to39(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgAtt30_39");
        String playerFieldGoalAttempts30to39 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgAtt30_39");
        return playerFieldGoalAttempts30to39;

    }

    private String readPlayerFieldGoalPercent30to39(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Fg30_39Pct");
        String playerFieldGoalPercent30to39 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Fg30_39Pct");
        return playerFieldGoalPercent30to39;

    }

    private String readPlayerFieldGoalMade40to49(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgMade40_49");
        String playerFieldGoalMade40to49 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgMade40_49");
        return playerFieldGoalMade40to49;

    }

    private String readPlayerFieldGoalAttempts40to49(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgAtt40_49");
        String playerFieldGoalAttempts40to49 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgAtt40_49");
        return playerFieldGoalAttempts40to49;

    }

    private String readPlayerFieldGoalPercent40to49(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Fg40_49Pct");
        String playerFieldGoalPercent40to49 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Fg40_49Pct");
        return playerFieldGoalPercent40to49;

    }

    private String readPlayerFieldGoalMade50Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgMade50Plus");
        String playerFieldGoalMade40to49 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgMade50Plus");
        return playerFieldGoalMade40to49;

    }

    private String readPlayerFieldGoalAttempts50Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgAtt50Plus");
        String playerFieldGoalAttempts50Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgAtt50Plus");
        return playerFieldGoalAttempts50Plus;

    }

    private String readPlayerFieldGoalPercent50Plus(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Fg50PlusPct");
        String playerFieldGoalPercent50Plus = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Fg50PlusPct");
        return playerFieldGoalPercent50Plus;

    }

    private String readPlayerFieldGoalLong(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgLng");
        String playerFieldGoalLong = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgLng");
        return playerFieldGoalLong;

    }

    private String readPlayerExtraPointBlock(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:XpBlk");
        String playerExtraPointBlock = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:XpBlk");
        return playerExtraPointBlock;

    }

    private String readPlayerExtraPointMade(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:XpMade");
        String playerExtraPointMade = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:XpMade");
        return playerExtraPointMade;

    }

    private String readPlayerExtraPointAttempts(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:XpAtt");
        String playerExtraPointAttempts = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:XpAtt");
        return playerExtraPointAttempts;

    }

    private String readPlayerExtraPointPercent(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:XpPct");
        String playerExtraPointPercent = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:XpPct");
        return playerExtraPointPercent;

    }

    private String readPlayerFieldGoalAndExtraPointPoints(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:FgAndXpPts");
        String playerFieldGoalAndExtraPointPoints = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:FgAndXpPts");
        return playerFieldGoalAndExtraPointPoints;

    }

    private String readPlayerKickoffs(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Kickoffs");
        String playerKickoffs = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Kickoffs");
        return playerKickoffs;

    }

    private String readPlayerKickoffYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoYds");
        String playerKickoffYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoYds");
        return playerKickoffYards;

    }

    private String readPlayerKickoffOOB(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoOOB");
        String playerKickoffOOB = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoOOB");
        return playerKickoffOOB;

    }

    private String readPlayerKickoffAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoAvg");
        String playerKickoffAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoAvg");
        return playerKickoffAverage;

    }

    private String readPlayerKickofTouchback(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoTB");
        String playerKickoffTouchback = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoTB");
        return playerKickoffTouchback;

    }

    private String readPlayerKickoffPercent(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoPct");
        String playerKickoffPercent = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoPct");
        return playerKickoffPercent;

    }

    private String readPlayerKickoffReturn(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoRet");
        String playerKickoffReturn = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoRet");
        return playerKickoffReturn;

    }

    private String readPlayerKickoffReturnYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoRetYds");
        String playerKickoffReturnYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoRetYds");
        return playerKickoffReturnYards;

    }

    private String readPlayerKickoffReturnAverageYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoRetAvgYds");
        String playerKickoffReturnAverageYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoRetAvgYds");
        return playerKickoffReturnAverageYards;

    }

    private String readPlayerKickoffReturnTouchdown(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoTD");
        String playerKickoffReturnTouchdown = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoTD");
        return playerKickoffReturnTouchdown;

    }

    private String readPlayerKickoffOS(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoOS");
        String playerKickoffOS = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoOS");
        return playerKickoffOS;

    }

    private String readPlayerKickoffOSR(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:KoOSR");
        String playerKickoffOSR = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:KoOSR");
        return playerKickoffOSR;

    }

    private String readPlayerPunts(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:Punts");
        String playerPunts = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:Punts");
        return playerPunts;

    }

    private String readPlayerPuntYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntYds");
        String playerPuntYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntYds");
        return playerPuntYards;

    }

    private String readPlayerPuntNetYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntNetYds");
        String playerPuntNetYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntNetYds");
        return playerPuntNetYards;

    }

    private String readPlayerPuntLong(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntLng");
        String playerPuntLong = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntLng");
        return playerPuntLong;

    }

    private String readPlayerPuntAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntAvg");
        String playerPuntAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntAvg");
        return playerPuntAverage;

    }

    private String readPlayerPuntNetAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntNetAvg");
        String playerPuntNetAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntNetAvg");
        return playerPuntNetAverage;

    }

    private String readPlayerPuntBlock(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntBlk");
        String playerPuntBlock = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntBlk");
        return playerPuntBlock;

    }

    private String readPlayerPuntOOB(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntOOB");
        String playerPuntOOB = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntOOB");
        return playerPuntOOB;

    }

    private String readPlayerPuntDown(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntDown");
        String playerPuntDown = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntDown");
        return playerPuntDown;

    }

    private String readPlayerPuntIn20(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntIn20");
        String playerPuntIn20 = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntIn20");
        return playerPuntIn20;

    }

    private String readPlayerPuntIn20Percent(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntIn20Pct");
        String playerPuntIn20Percent = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntIn20Pct");
        return playerPuntIn20Percent;

    }

    private String readPlayerPuntTB(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntTB");
        String playerPuntTB = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntTB");
        return playerPuntTB;

    }

    private String readPlayerPuntTBPercent(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntTBPct");
        String playerPuntTBPercent = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntTBPct");
        return playerPuntTBPercent;

    }

    private String readPlayerPuntFC(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntFC");
        String playerPuntFC = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntFC");
        return playerPuntFC;

    }

    private String readPlayerPuntPuntReturn(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntRet");
        String playerPuntReturn = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntRet");
        return playerPuntReturn;

    }

    private String readPlayerPuntPuntReturnYards(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntRetYds");
        String playerPuntReturnYards = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntRetYds");
        return playerPuntReturnYards;

    }

    private String readPlayerPuntPuntReturnAverage(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "cum:PuntRetAvg");
        String playerPuntReturnAverage = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cum:PuntRetAvg");
        return playerPuntReturnAverage;

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
            this.playerPassing40Plus = playerPassing40PlayerKickReturnYards;
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
            this.playerPassing40Plus = playerPassing40Pl  this.playerTwoPointRushAttempts = playerTwoPointRushAttempts;
            this.playerTwoPointRushMakes = playerTwoPointRushMakes;
        }
    }*/

}
