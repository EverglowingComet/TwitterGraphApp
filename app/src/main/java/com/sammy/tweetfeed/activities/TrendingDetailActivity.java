package com.sammy.tweetfeed.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.AppDataCache;
import com.sammy.tweetfeed.data.Constants;
import com.sammy.tweetfeed.data.DownloadImageTask;

import java.io.InputStream;

public class TrendingDetailActivity extends AppCompatActivity implements View.OnClickListener {

    // detail infos
    private String mName = null;
    private String mScreenName = null;
    private String mTwitterHandle = null;
    private String mImageURL = null;
    private float mShareOfVoice = 0;
    private boolean mIsTeam = true;
    // detail infos

    // detail ui
    private ImageView mImage;
    private TextView mNameView;
    private TextView mNickNameView;
    private TextView mShareVoiceView;

    private Button mShowTweets;
    private Button mShowActivity;
    private Button mShowSentiment;
    private Button mShowTopics;
    private Context mContext;
    // detail ui

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trending_details);

        mContext = this;

        mName = getIntent().getStringExtra(Constants.KEY_NAME);
        mScreenName = getIntent().getStringExtra(Constants.KEY_NICKNAME);
        mTwitterHandle = getIntent().getStringExtra(Constants.KEY_TWITTER);
        mImageURL = getIntent().getStringExtra(Constants.KEY_IMAGE_URL);
        mShareOfVoice = getIntent().getFloatExtra(Constants.KEY_SHARE_VOICE, 0.0f);
        mIsTeam = getIntent().getBooleanExtra(Constants.KEY_TEAM, true);

        mImage = (ImageView) findViewById(R.id.trending_detail_image);
        mNameView = (TextView) findViewById(R.id.trending_detail_name);
        mNickNameView = (TextView) findViewById(R.id.trending_detail_nickname);
        mShareVoiceView = (TextView) findViewById(R.id.trending_detail_rating);
        if (AppDataCache.sIconCash.containsKey(mImageURL)) {
            mImage.setImageBitmap(AppDataCache.sIconCash.get(mImageURL));
        } else {
            new DownloadImageTask(mImage, mIsTeam ? R.drawable.team : R.drawable.player).execute(mImageURL);
            mImage.setImageResource(mIsTeam ? R.drawable.team : R.drawable.player);
        }
        mNameView.setText(mName);
        mNickNameView.setText(mScreenName);
        mShareVoiceView.setText(String.valueOf(mShareOfVoice));

        mShowTweets = (Button) findViewById(R.id.trending_detail_show_tweets);
        mShowTweets.setOnClickListener(this);
        mShowActivity = (Button) findViewById(R.id.trending_detail_show_activity);
        mShowActivity.setOnClickListener(this);
        mShowSentiment = (Button) findViewById(R.id.trending_detail_show_sentiment);
        mShowSentiment.setOnClickListener(this);
        mShowTopics = (Button) findViewById(R.id.trending_detail_show_tweets);
        mShowTopics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trending_detail_show_tweets:

                Intent tweet_intent = new Intent();
                tweet_intent.setClass(mContext, TweetListActivity.class);
                tweet_intent.putExtra(Constants.KEY_TEAM, mIsTeam);

                tweet_intent.putExtra(Constants.KEY_NAME, mName);
                tweet_intent.putExtra(Constants.KEY_NICKNAME, mScreenName);
                tweet_intent.putExtra(Constants.KEY_IMAGE_URL, mImageURL);
                tweet_intent.putExtra(Constants.KEY_TWITTER, mTwitterHandle);
                tweet_intent.putExtra(Constants.KEY_SHARE_VOICE, mShareOfVoice);
                mContext.startActivity(tweet_intent);
                break;
        }
    }

}
