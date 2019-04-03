package com.sammy.tweetfeed.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.AppDataCache;
import com.sammy.tweetfeed.data.TrendingTeam;
import com.sammy.tweetfeed.data.TweetItem;

import java.util.ArrayList;
import java.util.List;

public class TweetListAdapter extends ArrayAdapter<TweetItem> {

    private Context mContext;

    public TweetListAdapter(Context context, int resource, List<TweetItem> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TweetItem item = getItem(position);
        TweetListItem item_view;
        if (convertView != null) {
            item_view = (TweetListItem) convertView;
        } else {
            item_view = (TweetListItem) LayoutInflater.from(mContext).inflate(R.layout.tweet_item, null);
        }

        item_view.bindView(item, AppDataCache.sIconCash.containsKey(item.mUserImageURL)? AppDataCache.sIconCash.get(item.mUserImageURL) : null);

        return item_view;
    }

    public void changeData(ArrayList<TweetItem> contents) {
        clear();
        addAll(contents);
        notifyDataSetChanged();
    }


}
