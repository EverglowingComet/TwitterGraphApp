package com.sammy.tweetfeed.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.AppDataCache;
import com.sammy.tweetfeed.data.TrendingPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class TrendingPlayerListAdapter extends ArrayAdapter<TrendingPlayer> {

    private Context mContext;

    public TrendingPlayerListAdapter(Context context, int resource, ArrayList<TrendingPlayer> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrendingPlayer item = getItem(position);
        TrendingListItem item_view;
        if (position == 0) {
            Log.e("comet____", "url: " + item.mImageURL);
        }
        if (convertView != null) {
            item_view = (TrendingListItem) convertView;
        } else {
            item_view = (TrendingListItem) LayoutInflater.from(mContext).inflate(R.layout.tending_list_item, null);
        }

        item_view.bindView(item, AppDataCache.sIconCash.containsKey(item.mImageURL)? AppDataCache.sIconCash.get(item.mImageURL) : null);

        return item_view;
    }

    public void changeData(TrendingPlayer[] contents) {
        clear();
        addAll(getList(contents));
        notifyDataSetChanged();
    }

    public ArrayList<TrendingPlayer> getList(TrendingPlayer[] contents) {
        ArrayList<TrendingPlayer> list = new ArrayList<>();
        for (int i = 0; i < contents.length; i ++) {
            list.add(contents[i]);
        }
        return list;
    }
}
