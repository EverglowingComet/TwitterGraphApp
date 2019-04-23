package com.sammy.tweetfeed.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.AppDataCache;
import com.sammy.tweetfeed.data.Constants;
import com.sammy.tweetfeed.data.DownloadImageTask;
import com.sammy.tweetfeed.data.GraphData;
import com.sammy.tweetfeed.data.IntervalItem;
import com.sammy.tweetfeed.data.TweetItem;
import com.sammy.tweetfeed.data.UserInfo;
import com.sammy.tweetfeed.transfer.ActivityAdapter;
import com.sammy.tweetfeed.transfer.TweetAdapter;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

public class ActivityGraphAnalysis extends AppCompatActivity implements View.OnClickListener, ActivityAdapter.ActivityPostListener {

    private UserInfo mUserInfo;
    private boolean mTweetBy = false;

    private ImageView mIconView;
    private TextView mNameView;
    private TextView mTweetHandleView;
    private TextView mTweetByView;
    private TextView mTweetOnView;
    private LinearLayout mTweetSwitch;
    private LinearLayout mFavoriteSwitch;
    private LinearLayout mMessageSwitch;

    private DiscreteScrollView mIntervalScroll;
    private IntervalPickerAdapter mIntervalAdapter;

    private ActivityAdapter mServerDataAdapter;
    private IntervalItem[] pastIntervals;
    private int mInterval = Constants.DEFAULT_INTERVAL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        pastIntervals = new IntervalItem[] {
                new IntervalItem(6, getString(R.string.past_interval_6_hours), 5),
                new IntervalItem(12, getString(R.string.past_interval_12_hours), 10),
                new IntervalItem(24, getString(R.string.past_interval_24_hours), 20),
                new IntervalItem(48, getString(R.string.past_interval_2_days), 40),
                new IntervalItem(72, getString(R.string.past_interval_3_days), 60),
                new IntervalItem(168, getString(R.string.past_interval_1_week), 140),
                new IntervalItem(360, getString(R.string.past_interval_15_days), 300)
        };
        mUserInfo = UserInfo.fromIntent(getIntent());

        mIconView = (ImageView) findViewById(R.id.user_icon);
        mNameView = (TextView) findViewById(R.id.user_name);
        mTweetHandleView = (TextView) findViewById(R.id.user_id);
        mTweetByView = (TextView) findViewById(R.id.tweets_by);
        mTweetOnView = (TextView) findViewById(R.id.tweets_on);
        mTweetByView.setOnClickListener(this);
        mTweetOnView.setOnClickListener(this);

        mTweetSwitch = (LinearLayout) findViewById(R.id.per_tweet);
        mFavoriteSwitch = (LinearLayout) findViewById(R.id.per_favorite);
        mMessageSwitch = (LinearLayout) findViewById(R.id.per_message);
        mTweetSwitch.setOnClickListener(this);
        mFavoriteSwitch.setOnClickListener(this);
        mMessageSwitch.setOnClickListener(this);

        mIntervalScroll = (DiscreteScrollView) findViewById(R.id.interval_picker);
        mIntervalAdapter = new IntervalPickerAdapter(this, pastIntervals);
        mIntervalScroll.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));

        mIntervalScroll.setAdapter(mIntervalAdapter);

        mIntervalScroll.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<IntervalPickerAdapter.IntervalViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable IntervalPickerAdapter.IntervalViewHolder viewHolder, int adapterPosition) {
                changeInterval(pastIntervals[adapterPosition].mIntervalValue);
            }
        });
        for(int i = 0; i < pastIntervals.length; i ++) {
            if (pastIntervals.length == mInterval) {
                mIntervalScroll.scrollToPosition(i);
                break;
            }
        }

        if (AppDataCache.sIconCash.containsKey(mUserInfo.mImageURL)) {
            mIconView.setImageBitmap(AppDataCache.sIconCash.get(mUserInfo.mImageURL));
        } else {
            new DownloadImageTask(mIconView, mUserInfo.mIsTeam ? R.drawable.team : R.drawable.player).execute(mUserInfo.mImageURL);
            mIconView.setImageResource(mUserInfo.mIsTeam ? R.drawable.team : R.drawable.player);
        }

        mNameView.setText(mUserInfo.mName);
        mTweetHandleView.setText(mUserInfo.mTwitterHandle);
        mTweetByView.setText(String.format(getString(R.string.tweet_screen_tweets_by), mUserInfo.mName));
        mTweetOnView.setText(String.format(getString(R.string.tweet_screen_tweets_on), mUserInfo.mName));

        mServerDataAdapter = new ActivityAdapter(mUserInfo.mName, mUserInfo.mTwitterHandle, mTweetBy, mInterval,
                IntervalItem.getSamplingInterval(pastIntervals, mInterval), mUserInfo.mIsTeam, this);

        postUpdate();

        initGraphView();
        //updateGraphView();
    }

    private void changeInterval(int interval) {
        mInterval = interval;
    }

    @Override
    public void onClick(View v) {

    }

    private GraphView mGraphView;
    private LineGraphSeries<DataPoint> mGraphData;

    private void initGraphView() {
        mGraphView = (GraphView) findViewById(R.id.graph_view);

        mGraphData = new LineGraphSeries<DataPoint>();
        mGraphData.setColor(0xff33ee00);
        mGraphData.setBackgroundColor(0x3344ff22);
        mGraphData.setDrawBackground(true);
    }

    private void updateGraphView() {
        double x = 0, y;
        for (int i = 0; i < 500; i ++) {
            x = x + 0.1;
            y = x + Math.sin(x) - 0.02 * x * x + 3;
            mGraphData.appendData(new DataPoint(x, y), true, 500);
        }
        mGraphView.addSeries(mGraphData);
    }

    public void postUpdate() {
        mServerDataAdapter.setTweetBy(mTweetBy);
        mServerDataAdapter.postRequest();

    }

    @Override
    public void postCompleted(GraphData result, int tweet_count, int follower_count, int following_count) {
        mGraphView.removeAllSeries();
        LineGraphSeries<DataPoint> points = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < result.mData.size(); i ++) {
            double value = result.mData.get(i);
            points.appendData(new DataPoint(i * result.mSamplingInterval, value), true, result.mData.size() + 1);
        }

        mGraphView.addSeries(points);
    }

    @Override
    public void postFailed(String error) {

    }
}
