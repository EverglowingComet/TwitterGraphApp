package com.sammy.tweetfeed.transfer;

import com.sammy.tweetfeed.data.TrendingPlayer;
import com.sammy.tweetfeed.data.TrendingTeam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrendingAdapter implements ServerBackendInterface {

    public static final String TEAM_REQUEST_URL = "http://meaningless.000webhostapp.com/kingkaun/getTrendingEntities.php?party=t";
    public static final String PLAYER_REQUEST_URL = "http://meaningless.000webhostapp.com/kingkaun/getTrendingEntities.php?party=p";
    public static final String INTERVAL_HEADER = "&interval=";

    private boolean mIsTeam;
    private int mInterval;

    private TrendingPostListener mListener;

    public static interface TrendingPostListener {
        public void trendingPostCompleted(TrendingPlayer[] result);
        public void trendingPostCompleted(TrendingTeam[] result);
        public void trendingPostFailed(String error);
    }

    public TrendingAdapter(int interval, boolean isTeam, TrendingPostListener listener) {
        mInterval = interval;
        mIsTeam = isTeam;
        mListener = listener;
    }

    public void setIsTeam(boolean isTeam) {
        mIsTeam = isTeam;
    }

    public void setInterval(int interval) {
        mInterval = interval;
    }

    public void postRequest() {
        String url = (mIsTeam ? TEAM_REQUEST_URL : PLAYER_REQUEST_URL) + INTERVAL_HEADER + mInterval;
        ServerBackend backend = new ServerBackend(url, this);
        backend.postRequest();
    }

    private TrendingTeam[] parseJSONTeam(String json_str) {
        try {
            JSONObject json = new JSONObject(json_str);
            JSONArray data = json.getJSONArray("data");
            TrendingTeam[] team_data = new TrendingTeam[data.length()];
            for (int i = 0; i < data.length(); i ++ ) {
                JSONObject record = data.getJSONObject(i);

                TrendingTeam item = new TrendingTeam(record.getString("name"),
                        record.getString("screenName"),
                        record.getString("profileImage"),
                        record.getString("twitterHandle"),
                        Float.parseFloat(record.getString("shareOfVoice")));

                team_data[i] = item;
            }
            return team_data;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private TrendingPlayer[] parseJSONPlayer(String json_str) {
        try {
            JSONObject json = new JSONObject(json_str);
            JSONArray data = json.getJSONArray("data");
            TrendingPlayer[] player_data = new TrendingPlayer[data.length()];
            for (int i = 0; i < data.length(); i ++ ) {
                JSONObject record = data.getJSONObject(i);

                TrendingPlayer item = new TrendingPlayer(record.getString("name"),
                        record.getString("screenName"),
                        record.getString("profileImage"),
                        record.getString("twitterHandle"),
                        Float.parseFloat(record.getString("shareOfVoice")));

                player_data[i] = item;
            }
            return player_data;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void backendRequestSucceded(String result) {
        if (mIsTeam) {
            mListener.trendingPostCompleted(parseJSONTeam(result));
        } else {
            mListener.trendingPostCompleted(parseJSONPlayer(result));
        }
    }

    @Override
    public void backendRequestFailed(String error) {
        mListener.trendingPostFailed(error);
    }
}
