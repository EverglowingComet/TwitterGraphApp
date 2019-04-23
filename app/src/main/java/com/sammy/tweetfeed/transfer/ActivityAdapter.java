package com.sammy.tweetfeed.transfer;

import com.sammy.tweetfeed.data.GraphData;
import com.sammy.tweetfeed.data.IntervalItem;
import com.sammy.tweetfeed.data.TweetItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityAdapter implements ServerBackendInterface {

    public static final String TEAM_REQUEST_URL = "http://meaningless.000webhostapp.com/kingkaun/getTweetCount.php?party=t";
    public static final String PLAYER_REQUEST_URL = "http://meaningless.000webhostapp.com/kingkaun/getTweetCount.php?party=p";
    public static final String INTERVAL_HEADER = "&interval=";
    public static final String NAME_HEADER = "&name=";
    public static final String TWITTER_HEADER = "&twitterHandle=";

    private String mName;
    private String mTweetHandle;
    private boolean mTweetBy;
    private boolean mIsTeam;
    private int mInterval;
    private int mSamplingInterval;

    private ActivityPostListener mListener;

    public static interface ActivityPostListener {
        public void postCompleted(GraphData result, int tweet_count, int follower_count, int following_count);
        public void postFailed(String error);
    }

    public ActivityAdapter(String name, String handle, boolean tweetby, int interval, int sample, boolean isTeam, ActivityPostListener listener) {
        mName = name;
        mTweetHandle = handle;
        mTweetBy = tweetby;
        mInterval = interval;
        mIsTeam = isTeam;
        mListener = listener;
        mSamplingInterval = sample;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setTweetHandle(String tweethandle) {
        mTweetHandle = tweethandle;
    }

    public void setIsTeam(boolean isTeam) {
        mIsTeam = isTeam;
    }

    public void setTweetBy(boolean tweetBy) {
        mTweetBy = tweetBy;
    }

    public void setInterval(int interval) {
        mInterval = interval;
    }

    public void setSamplingInterval(int interval) {
        mSamplingInterval = interval;
    }

    public void postRequest() {
        String url = (mIsTeam ? TEAM_REQUEST_URL : PLAYER_REQUEST_URL) +
                INTERVAL_HEADER + mInterval + (mTweetBy ? TWITTER_HEADER + mTweetHandle : NAME_HEADER + mName);
        ServerBackend backend = new ServerBackend(url, this);
        backend.postRequest();
    }

    private GraphData parseJSON(String json_str) {
        try {

            GraphData result = new GraphData(mSamplingInterval);
            JSONObject json = new JSONObject(json_str);
            JSONArray data = json.getJSONArray("data");
            ArrayList<TweetItem> team_data = new ArrayList<TweetItem>();
            for (int i = 0; i < data.length(); i ++ ) {
                JSONObject record = data.getJSONObject(i);
                result.mData.add(record.getDouble("value"));
            }

            return result;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public void backendRequestSucceded(String result) {
        mListener.postCompleted(parseJSON(result), 0, 0, 0);
    }

    @Override
    public void backendRequestFailed(String error) {
        mListener.postFailed(error);
    }
}
