package com.sammy.tweetfeed.transfer;

import com.sammy.tweetfeed.data.TweetItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TweetAdapter  implements ServerBackendInterface {

    public static final String TEAM_REQUEST_URL = "http://meaningless.000webhostapp.com/kingkaun/getTweets.php?party=t";
    public static final String PLAYER_REQUEST_URL = "http://meaningless.000webhostapp.com/kingkaun/getTweets.php?party=p";
    public static final String INTERVAL_HEADER = "&interval=";
    public static final String NAME_HEADER = "&name=";
    public static final String PAGE_HEADER = "&page=";

    private int mTweetCount;
    private int mFollowingCount;
    private int mFollowerCount;

    private String mName;
    private int mPage;
    private boolean mIsTeam;
    private int mInterval;

    private TweetPostListener mListener;

    public static interface TweetPostListener {
        public void tweetPostCompleted(ArrayList<TweetItem> result, int tweet_count, int follower_count, int following_count);
        public void tweetPostFailed(String error);
    }

    public TweetAdapter(String name, int page, int interval, boolean isTeam, TweetPostListener listener) {
        mName = name;
        mPage = page;
        mInterval = interval;
        mIsTeam = isTeam;
        mListener = listener;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public void setIsTeam(boolean isTeam) {
        mIsTeam = isTeam;
    }

    public void setInterval(int interval) {
        mInterval = interval;
    }

    public void postRequest() {
        String url = (mIsTeam ? TEAM_REQUEST_URL : PLAYER_REQUEST_URL) +
                INTERVAL_HEADER + mInterval + NAME_HEADER + mName + PAGE_HEADER + mPage;
        ServerBackend backend = new ServerBackend(url, this);
        backend.postRequest();
    }

    private ArrayList<TweetItem> parseJSON(String json_str) {
        try {
            mTweetCount = 0;
            mFollowerCount = 0;
            mFollowingCount = 0;

            JSONObject json = new JSONObject(json_str);
            JSONArray data = json.getJSONArray("data");
            ArrayList<TweetItem> team_data = new ArrayList<TweetItem>();
            for (int i = 0; i < data.length(); i ++ ) {
                JSONObject record = data.getJSONObject(i);
                mTweetCount += record.getInt("tweetCount");
                mFollowerCount += record.getInt("followers");
                mFollowingCount += record.getInt("following");
            }

            JSONArray tweets = json.getJSONArray("tweets");

            for (int i = 0; i < tweets.length(); i ++ ) {
                JSONObject record = tweets.getJSONObject(i);
                TweetItem item = new TweetItem(
                        record.getString("originalText"),
                        record.getString("tweetId"),
                        record.getString("userName"),
                        record.getString("userScreenName"),
                        record.getString("userImage"),
                        record.getString("createdAt"),
                        Integer.parseInt(record.getString("retweetCount")),
                        Integer.parseInt(record.getString("favoriteCount"))
                );
                team_data.add(item);
            }

            return team_data;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void backendRequestSucceded(String result) {
        mListener.tweetPostCompleted(parseJSON(result), mTweetCount, mFollowerCount, mFollowingCount);
    }

    @Override
    public void backendRequestFailed(String error) {
        mListener.tweetPostFailed(error);
    }
}