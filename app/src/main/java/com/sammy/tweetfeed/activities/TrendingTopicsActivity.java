package com.sammy.tweetfeed.activities;

import android.graphics.Color;
import android.graphics.Rect;
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
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.AppDataCache;
import com.sammy.tweetfeed.data.Constants;
import com.sammy.tweetfeed.data.DownloadImageTask;
import com.sammy.tweetfeed.data.IntervalItem;
import com.sammy.tweetfeed.data.UserInfo;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

public class TrendingTopicsActivity extends AppCompatActivity implements View.OnClickListener {

    private UserInfo mUserInfo;

    private DiscreteScrollView mIntervalScroll;
    private IntervalPickerAdapter mIntervalAdapter;

    private IntervalItem[] pastIntervals;
    private int mInterval = Constants.DEFAULT_INTERVAL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_screen);
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

        initGraphView();
        updateGraphView();
    }

    private void changeInterval(int interval) {
        mInterval = interval;
    }

    @Override
    public void onClick(View v) {

    }

    private GraphView mGraphView;
    private BarGraphSeries<DataPoint> mGraphData;

    private void initGraphView() {
        mGraphView = (GraphView) findViewById(R.id.graph_view);
        mGraphData = new BarGraphSeries<DataPoint>();
        mGraphData.setColor(0xff33ee00);
        mGraphData.setDataWidth(0.5);
        mGraphData.setSpacing(50);
        mGraphData.setTitle("Reporte de Fecha #1 a Fecha #2");

// draw values on top
        mGraphData.setDrawValuesOnTop(true);
        mGraphData.setValuesOnTopColor(Color.RED);
    }

    private void updateGraphView() {
        double x = 0, y;
        for (int i = 1; i < 10; i ++) {
            x = i;
            y = x + Math.sin(x) - 0.02 * x * x + 3;
            mGraphData.appendData(new DataPoint(x, y), true, 500);
        }
        mGraphView.addSeries(mGraphData);
    }

}
