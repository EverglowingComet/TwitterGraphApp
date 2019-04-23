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
import com.sammy.tweetfeed.data.UserInfo;

import java.io.InputStream;

public class TrendingDetailActivity extends AppCompatActivity implements View.OnClickListener {

    // detail infos
    private UserInfo mUserInfo;
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

        mUserInfo = UserInfo.fromIntent(getIntent());

        mImage = (ImageView) findViewById(R.id.trending_detail_image);
        mNameView = (TextView) findViewById(R.id.trending_detail_name);
        mNickNameView = (TextView) findViewById(R.id.trending_detail_nickname);
        mShareVoiceView = (TextView) findViewById(R.id.trending_detail_rating);
        if (AppDataCache.sIconCash.containsKey(mUserInfo.mImageURL)) {
            mImage.setImageBitmap(AppDataCache.sIconCash.get(mUserInfo.mImageURL));
        } else {
            new DownloadIconTask(mImage, mUserInfo.mIsTeam ? R.drawable.team : R.drawable.player).execute(mUserInfo.mImageURL);
            mImage.setImageResource(mUserInfo.mIsTeam ? R.drawable.team : R.drawable.player);
        }
        mNameView.setText(mUserInfo.mName);
        mNickNameView.setText(mUserInfo.mScreenName);
        mShareVoiceView.setText(String.valueOf(mUserInfo.mShareOfVoice));

        mShowTweets = (Button) findViewById(R.id.trending_detail_show_tweets);
        mShowTweets.setOnClickListener(this);
        mShowActivity = (Button) findViewById(R.id.trending_detail_show_activity);
        mShowActivity.setOnClickListener(this);
        mShowSentiment = (Button) findViewById(R.id.trending_detail_show_sentiment);
        mShowSentiment.setOnClickListener(this);
        mShowTopics = (Button) findViewById(R.id.trending_detail_show_topics);
        mShowTopics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trending_detail_show_tweets:

                Intent tweet_intent = new Intent();
                tweet_intent.setClass(mContext, TweetListActivity.class);
                collectInfo(tweet_intent);
                mContext.startActivity(tweet_intent);
                break;
            case R.id.trending_detail_show_activity:

                Intent activity_intent = new Intent();
                activity_intent.setClass(mContext, ActivityGraphAnalysis.class);
                collectInfo(activity_intent);
                mContext.startActivity(activity_intent);
                break;
            case R.id.trending_detail_show_sentiment:

                Intent sentimental_intent = new Intent();
                sentimental_intent.setClass(mContext, SentimentGraphAnalysis.class);
                collectInfo(sentimental_intent);
                mContext.startActivity(sentimental_intent);
                break;
            case R.id.trending_detail_show_topics:

                Intent topics_intent = new Intent();
                topics_intent.setClass(mContext, TrendingTopicsActivity.class);
                collectInfo(topics_intent);
                mContext.startActivity(topics_intent);
                break;
        }
    }

    private void collectInfo(Intent intent) {
        intent.putExtra(Constants.KEY_TEAM, mUserInfo.mIsTeam);

        intent.putExtra(Constants.KEY_NAME, mUserInfo.mName);
        intent.putExtra(Constants.KEY_NICKNAME, mUserInfo.mScreenName);
        intent.putExtra(Constants.KEY_IMAGE_URL, mUserInfo.mImageURL);
        intent.putExtra(Constants.KEY_TWITTER, mUserInfo.mTwitterHandle);
        intent.putExtra(Constants.KEY_SHARE_VOICE, mUserInfo.mShareOfVoice);
    }

    public class DownloadIconTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        private int mDefaultResId;
        private String imgUrl;

        public DownloadIconTask(ImageView bmImage, int defaultResId) {
            imageView = bmImage;
            mDefaultResId = defaultResId;
        }

        protected Bitmap doInBackground(String... urls) {
            imgUrl = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(imgUrl).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            AppDataCache.sTweetIconCash.put(imgUrl, result);
            if (result != null) {
                AppDataCache.sDownloadingURL.remove(imgUrl);
                AppDataCache.sDownloadedURL.put(imgUrl, true);
                imageView.setImageBitmap(result);
            } else {
                AppDataCache.sDownloadingURL.remove(imgUrl);
                AppDataCache.sDownloadedURL.put(imgUrl, false);
                imageView.setImageResource(mDefaultResId);
            }
        }
    }

}
