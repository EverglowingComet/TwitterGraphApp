package com.sammy.tweetfeed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.AppDataCache;
import com.sammy.tweetfeed.data.Constants;
import com.sammy.tweetfeed.data.DownloadImageTask;
import com.sammy.tweetfeed.data.TweetItem;
import com.sammy.tweetfeed.listview.TweetListAdapter;
import com.sammy.tweetfeed.transfer.TweetAdapter;

import java.util.ArrayList;


public class TweetListActivity extends AppCompatActivity implements View.OnClickListener, TweetAdapter.TweetPostListener {

    private ImageView mIconView;
    private TextView mNameView;
    private TextView mTweetHandleView;
    private TextView mTweetCountView;
    private TextView mTweetFollowingView;
    private TextView mTweetFollowerView;
    private TextView mTweetByView;
    private TextView mTweetOnView;
    private ListView mListView;
    private TextView mEmptyView;
    private TextView mNavPage;
    private ImageButton mNavNext;
    private ImageButton mNavPrev;

    private String mUserName;
    private String mImageURL;
    private String mUserTwitter;
    private boolean mIsTweetBy = true;
    private int mTweetCount = 0;
    private int mTweetFollowingCount = 0;
    private int mTweetFollowerCount = 0;
    private boolean mIsTeam = true;
    private int mInterval = 24;
    private int mPage = 1;
    private int mPageCount = 1;

    private TweetAdapter mServerDataAdapter;
    private ArrayList<TweetItem> mItems = new ArrayList<>();
    private TweetListAdapter mListAdapter;

    private class UIHandler extends Handler {
        public static final int UPDATE_COUNTS = 1000;
        public static final int UPDATE_LIST = 1001;
        public static final int POST_DATA_FAILED = 1002;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_COUNTS:
                    mTweetCountView.setText(String.valueOf(mTweetCount));
                    mTweetFollowerView.setText(String.valueOf(mTweetFollowerCount));
                    mTweetFollowingView.setText(String.valueOf(mTweetFollowingCount));
                    break;
                case UPDATE_LIST:
                    mListAdapter.changeData(mTweetData);
                    mListAdapter.notifyDataSetChanged();
                    mEmptyView.setText(R.string.empty_tweet);
                    break;
                case POST_DATA_FAILED:
                    mEmptyView.setText(R.string.loading_failed);
                    break;
            }
        }
    }

    private UIHandler mHandler;
    private ArrayList<TweetItem> mTweetData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_screen);

        mHandler = new UIHandler();

        mIconView = (ImageView) findViewById(R.id.user_icon);
        mNameView = (TextView) findViewById(R.id.user_name);
        mTweetHandleView = (TextView) findViewById(R.id.user_id);
        mTweetCountView = (TextView) findViewById(R.id.tweet_count);
        mTweetFollowingView = (TextView) findViewById(R.id.following_count);
        mTweetFollowerView = (TextView) findViewById(R.id.follower_count);
        mTweetByView = (TextView) findViewById(R.id.tweets_by);
        mTweetOnView = (TextView) findViewById(R.id.tweets_on);
        mTweetByView.setOnClickListener(this);
        mTweetOnView.setOnClickListener(this);
        mNavPage = (TextView) findViewById(R.id.tweet_nav_page);
        mNavNext = (ImageButton) findViewById(R.id.tweet_nav_next);
        mNavPrev = (ImageButton) findViewById(R.id.tweet_nav_prev);
        mNavPage.setText(String.valueOf(mPage));
        mNavNext.setOnClickListener(this);
        mNavPrev.setOnClickListener(this);

        mEmptyView = (TextView) findViewById(R.id.tweet_empty_view);
        mListView = (ListView) findViewById(R.id.tweet_list_view);
        mListView.setEmptyView(mEmptyView);

        Intent intent = getIntent();
        mUserName = intent.getStringExtra(Constants.KEY_NAME);
        mImageURL = intent.getStringExtra(Constants.KEY_IMAGE_URL);
        mUserTwitter = intent.getStringExtra(Constants.KEY_TWITTER);
        mIsTeam = intent.getBooleanExtra(Constants.KEY_TEAM, true);
        mInterval = intent.getIntExtra(Constants.KEY_INTERVAL, 24);

        if (AppDataCache.sIconCash.containsKey(mImageURL)) {
            mIconView.setImageBitmap(AppDataCache.sIconCash.get(mImageURL));
        } else {
            new DownloadImageTask(mIconView, mIsTeam ? R.drawable.team : R.drawable.player).execute(mImageURL);
            mIconView.setImageResource(mIsTeam ? R.drawable.team : R.drawable.player);
        }

        mNameView.setText(mUserName);
        mTweetHandleView.setText(mUserTwitter);
        mTweetByView.setText(String.format(getString(R.string.tweet_screen_tweets_by), mUserName));
        mTweetOnView.setText(String.format(getString(R.string.tweet_screen_tweets_on), mUserName));

        mServerDataAdapter = new TweetAdapter(mUserName, mInterval, mPage, mIsTeam, this);
        postUpdate(mPage);

        mListAdapter = new TweetListAdapter(this, 0, mItems);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tweets_by:
                mIsTweetBy = true;
                updateTweetSwitcher();
                break;
            case R.id.tweets_on:
                mIsTweetBy = false;
                updateTweetSwitcher();
                break;
            case R.id.tweet_nav_next:
                progressPage(true);
                break;
            case R.id.tweet_nav_prev:
                progressPage(false);
                break;
        }
    }

    // UI functions
    private void progressPage(boolean next) {
        boolean changed = false;
        if (mPage > 1 && !next) {
            mPage--;
            changed = true;
        } else if (mPage < mPageCount && next) {
            mPage++;
            changed = true;
        }
        if (changed) {
            mNavPage.setText(String.valueOf(mPage));
            postUpdate(mPage);
        }
    }

    private void updateTweetSwitcher() {
        mTweetByView.setBackgroundResource(mIsTweetBy ?
                R.drawable.tweeter_switcher_bg_selected : R.drawable.tweeter_switcher_bg_unselected);
        mTweetOnView.setBackgroundResource(mIsTweetBy ?
                R.drawable.tweeter_switcher_right_bg_unselected : R.drawable.tweeter_switcher_right_bg_selected);
    }

    @Override
    public void tweetPostCompleted(ArrayList<TweetItem> result, int tweet_count, int follower_count, int following_count) {
        mTweetCount = tweet_count;
        mPageCount = tweet_count / Constants.TWEET_PAGE_SIZE + (tweet_count % Constants.TWEET_PAGE_SIZE == 0 ? 0 : 1);

        mTweetFollowerCount = follower_count;
        mTweetFollowingCount = following_count;

        mHandler.sendEmptyMessage(UIHandler.UPDATE_COUNTS);

        mTweetData = result;
        mHandler.sendEmptyMessage(UIHandler.UPDATE_LIST);
    }

    @Override
    public void tweetPostFailed(String error) {
        mHandler.sendEmptyMessage(UIHandler.POST_DATA_FAILED);
    }
    // UI functions


    public void postUpdate(int page) {
        mServerDataAdapter.setPage(page);
        mServerDataAdapter.postRequest();

        mTweetCountView.setText(R.string.loading);
        mTweetFollowingView.setText(R.string.loading);
        mTweetFollowerView.setText(R.string.loading);

        mEmptyView.setText(R.string.empty_tweet);
    }

}
