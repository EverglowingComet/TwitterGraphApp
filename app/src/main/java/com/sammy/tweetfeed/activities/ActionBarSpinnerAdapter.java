package com.sammy.tweetfeed.activities;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import com.sammy.tweetfeed.R;

public class ActionBarSpinnerAdapter implements SpinnerAdapter {

    public int[] mImageResources;
    public int[] mSpinnerValues;

    public ActionBarSpinnerItem[] mItems;

    private Context mContext;

    public ActionBarSpinnerAdapter(Context context, int[] res, int[] values) {
        mContext = context;
        mImageResources = res;
        mSpinnerValues = values;
        mItems = new ActionBarSpinnerItem[res.length];
        for (int i = 0; i < res.length; i ++) {
            mItems[i] = new ActionBarSpinnerItem(res[i], values[i]);
        }
    }

    public static class ActionBarSpinnerItem {
        public int mImageResource;
        public int mSpinnerValue;

        public ActionBarSpinnerItem(int res, int value) {
            mImageResource = res;
            mSpinnerValue = value;
        }
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.icon_spinner, null);
        }
        ImageView icon = (ImageView) convertView;
        icon.setImageResource(mImageResources[position]);
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return mSpinnerValues[position];
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.icon_spinner, null);
        }
        ImageView icon = (ImageView) convertView;
        icon.setImageResource(mImageResources[position]);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mItems == null || mItems.length == 0;
    }

}
