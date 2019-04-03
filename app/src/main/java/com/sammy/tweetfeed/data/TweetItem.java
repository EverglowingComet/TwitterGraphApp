package com.sammy.tweetfeed.data;


public class TweetItem {
    public String mText;
    public String mTweetId;
    public String mUserName;
    public String mUserImageURL;
    public String mUserNameScreen;
    public String mCreatedAt;
    public int mRetweetCount;
    public int mFavoriteCount;

    public TweetItem(String text, String tweet_id, String user_name, String user_screen_name,
                     String user_url, String create_date, int retweet, int favorite) {
        mTweetId = tweet_id;
        mText = text;
        mUserName = user_name;
        mUserImageURL = user_url;
        mUserNameScreen = user_screen_name;
        mCreatedAt = create_date;
        mRetweetCount = retweet;
        mFavoriteCount = favorite;
    }
}