package com.sammy.tweetfeed.data;

import android.content.Intent;

public class UserInfo {

    public String mName = null;
    public String mScreenName = null;
    public String mTwitterHandle = null;
    public String mImageURL = null;
    public float mShareOfVoice = 0;
    public boolean mIsTeam = true;

    public static UserInfo fromIntent(Intent intent) {
        UserInfo info = new UserInfo();
        info.mName = intent.getStringExtra(Constants.KEY_NAME);
        info.mScreenName = intent.getStringExtra(Constants.KEY_NICKNAME);
        info.mTwitterHandle = intent.getStringExtra(Constants.KEY_TWITTER);
        info.mImageURL = intent.getStringExtra(Constants.KEY_IMAGE_URL);
        info.mShareOfVoice = intent.getFloatExtra(Constants.KEY_SHARE_VOICE, 0.0f);
        info.mIsTeam = intent.getBooleanExtra(Constants.KEY_TEAM, true);

        return info;
    }
}
