package com.sammy.tweetfeed.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.Constants;
import com.sammy.tweetfeed.data.TrendingPlayer;
import com.sammy.tweetfeed.data.TrendingTeam;
import com.sammy.tweetfeed.listview.TrendingPlayerListAdapter;
import com.sammy.tweetfeed.listview.TrendingTeamListAdapter;
import com.sammy.tweetfeed.transfer.TrendingAdapter;

import java.util.ArrayList;

public class TrendingListActivity extends AppCompatActivity implements View.OnClickListener, TrendingAdapter.TrendingPostListener {

    private ListView mListView;
    private TrendingTeamListAdapter mTeamAdapter;
    private TrendingPlayerListAdapter mPlayerAdapter;

//    private ImageView mSwitchTeamPlayer;
//    private Spinner mIntervalSwitcher;

    private TrendingAdapter mServerDataAdapter;
    private TrendingPlayer[] mPlayerData = new TrendingPlayer[]{};
    private TrendingTeam[] mTeamData = new TrendingTeam[]{};

    private boolean isTeamList = true;
    private int mInterval = 24;

    // Action Bar and Menu
    private ActionBar mActionBar;

    private MenuItem mCatMenu;
    private MenuItem mIntervalMenu;

    private Spinner mCatSpinner;
    private Spinner mIntervalSpinner;

    private SpinnerAdapter mCatAdapter;
    private SpinnerAdapter mIntervalAdapter;

    private static final int[] interval_spinner_images = {
            R.drawable.interval_6hrs,
            R.drawable.interval_12hrs,
            R.drawable.interval_1day,
            R.drawable.interval_2days,
            R.drawable.interval_3days,
            R.drawable.interval_1week,
            R.drawable.interval_15days
    };
    private static final int[] interval_spinner_values = {
            6, 12, 24, 48, 72, 168, 360
    };

    private static final int[] category_spinner_items = {
            R.drawable.team,
            R.drawable.player
    };

    private static final int[] category_spinner_values = {0,1};
    private Context mContext;
    // Action Bar and Menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tending_list);
        mContext = this;

        // List View
//        mSwitchTeamPlayer = (ImageView) findViewById(R.id.category_switcher);
//        mSwitchTeamPlayer.setOnClickListener(this);
//        mIntervalSwitcher = (Spinner) findViewById(R.id.timespan_switcher);
        mListView = (ListView) findViewById(R.id.tending_list_view);
        mListView.setEmptyView(findViewById(R.id.empty_view));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detail_intent = new Intent();
                detail_intent.setClass(mContext, TrendingDetailActivity.class);
                detail_intent.putExtra(Constants.KEY_TEAM, isTeamList);
                if (isTeamList) {
                    TrendingTeam item = (TrendingTeam) parent.getItemAtPosition(position);
                    detail_intent.putExtra(Constants.KEY_NAME, item.mName);
                    detail_intent.putExtra(Constants.KEY_NICKNAME, item.mScreenName);
                    detail_intent.putExtra(Constants.KEY_IMAGE_URL, item.mImageURL);
                    detail_intent.putExtra(Constants.KEY_TWITTER, item.mTwitterHandle);
                    detail_intent.putExtra(Constants.KEY_SHARE_VOICE, item.mShareOfVoice);
                } else {
                    TrendingPlayer item = (TrendingPlayer) parent.getItemAtPosition(position);
                    detail_intent.putExtra(Constants.KEY_NAME, item.mName);
                    detail_intent.putExtra(Constants.KEY_NICKNAME, item.mScreenName);
                    detail_intent.putExtra(Constants.KEY_IMAGE_URL, item.mImageURL);
                    detail_intent.putExtra(Constants.KEY_TWITTER, item.mTwitterHandle);
                    detail_intent.putExtra(Constants.KEY_SHARE_VOICE, item.mShareOfVoice);
                }
                mContext.startActivity(detail_intent);
            }
        });

        mServerDataAdapter = new TrendingAdapter(mInterval, isTeamList, this);
        mServerDataAdapter.postRequest();
        mTeamAdapter = new TrendingTeamListAdapter(this, 0, new ArrayList<TrendingTeam>());
        mPlayerAdapter = new TrendingPlayerListAdapter(this, 0, new ArrayList<TrendingPlayer>());
        // List View

        // Action Bar and Menu
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayUseLogoEnabled(true);
        mActionBar.setLogo(R.drawable.team);
        mActionBar.setTitle(R.string.trending_list_title_team);

        // Action Bar and Menu

        updateListView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.category_switcher:
//                toggleListMode();
//                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trending_screen, menu);

        mCatMenu = menu.findItem(R.id.category_switch);
        mCatSpinner = (Spinner) MenuItemCompat.getActionView(mCatMenu);

        mCatAdapter = new ActionBarSpinnerAdapter(this, category_spinner_items, category_spinner_values);
        mCatSpinner.setAdapter(mCatAdapter);
        mCatSpinner.setBackgroundColor(Color.TRANSPARENT);
        mCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ActionBarSpinnerAdapter.ActionBarSpinnerItem item = (ActionBarSpinnerAdapter.ActionBarSpinnerItem) parent.getSelectedItem();
                setListMode(item.mSpinnerValue == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mIntervalMenu = menu.findItem(R.id.interval_switch);
        mIntervalSpinner = (Spinner) MenuItemCompat.getActionView(mIntervalMenu);

        mIntervalAdapter = new ActionBarSpinnerAdapter(this, interval_spinner_images, interval_spinner_values);
        mIntervalSpinner.setAdapter(mIntervalAdapter);
        mIntervalSpinner.setBackgroundColor(Color.TRANSPARENT);
        mIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ActionBarSpinnerAdapter.ActionBarSpinnerItem item = (ActionBarSpinnerAdapter.ActionBarSpinnerItem) parent.getSelectedItem();
                setInterval(item.mSpinnerValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setDefaultItems();
        return true;
    }

    public void setDefaultItems() {
        for (int i = 0; i < mIntervalAdapter.getCount(); i ++) {
            if (mInterval == (int) mIntervalAdapter.getItemId(i)) {
                mIntervalSpinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < mCatAdapter.getCount(); i ++) {
            if (0 == (int) mCatAdapter.getItemId(i)) {
                mCatSpinner.setSelection(i);
                break;
            }
        }
    }

    private void setInterval(int interval) {
        mInterval = interval;
        postServerData(mInterval);
        updateListView();
    }
    private void setListMode(boolean team) {
        isTeamList = team;
//        mSwitchTeamPlayer.setImageResource(isTeamList ? R.drawable.team : R.drawable.player);
        mActionBar.setLogo(isTeamList ? R.drawable.team : R.drawable.player);
        mActionBar.setTitle(isTeamList ? R.string.trending_list_title_team : R.string.trending_list_title_player);
        postServerData(mInterval);
        updateListView();
    }

    private void toggleListMode() {
        isTeamList = !isTeamList;
//        mSwitchTeamPlayer.setImageResource(isTeamList ? R.drawable.team : R.drawable.player);
        mActionBar.setLogo(isTeamList ? R.drawable.team : R.drawable.player);
        mActionBar.setTitle(isTeamList ? R.string.trending_list_title_team : R.string.trending_list_title_player);
        postServerData(mInterval);
        updateListView();
    }

    private void updateListView() {
        mListView.setAdapter(isTeamList ? mTeamAdapter : mPlayerAdapter);
    }

    private void postServerData(int interval) {
        mServerDataAdapter.setInterval(interval);
        mServerDataAdapter.setIsTeam(isTeamList);
        mServerDataAdapter.postRequest();
    }

    @Override
    public void trendingPostCompleted(final TrendingPlayer[] result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPlayerAdapter.changeData(result);
                mPlayerAdapter.notifyDataSetChanged();
                mListView.setAdapter(mPlayerAdapter);
            }
        });
    }

    @Override
    public void trendingPostCompleted(final TrendingTeam[] result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTeamAdapter.changeData(result);
                mTeamAdapter.notifyDataSetChanged();
                mListView.setAdapter(mTeamAdapter);
            }
        });
    }

    @Override
    public void trendingPostFailed(String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListView.setAdapter(null);
            }
        });
    }
}
