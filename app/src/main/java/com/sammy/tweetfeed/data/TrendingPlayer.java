package com.sammy.tweetfeed.data;

import java.net.URL;

public class TrendingPlayer {
    public String mName = null;
    public String mScreenName = null;
    public String mTwitterHandle = null;
    public String mImageURL = null;
    public float mShareOfVoice = 0;


    public TrendingPlayer(String name, String screen_name, String image_url, String twitter_handle, float shareOfvoice) {
        mName = name;
        mScreenName = screen_name;
        mImageURL = image_url;
        mTwitterHandle = twitter_handle;
        mShareOfVoice = shareOfvoice;
    }
}
