package com.sammy.tweetfeed.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.IntervalItem;

import java.util.List;

public class IntervalPickerAdapter extends RecyclerView.Adapter<IntervalPickerAdapter.IntervalViewHolder> {
    private IntervalItem[] mItems;
    private Context mContext;

    public static class IntervalViewHolder extends RecyclerView.ViewHolder {
        public TextView label;

        public IntervalViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.activity_interval_label);
        }
    }

    public IntervalPickerAdapter(Context context, IntervalItem[] items) {
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public IntervalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).
                inflate(R.layout.activity_interval_item, parent, false);

        return new IntervalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IntervalViewHolder holder, int position) {
        holder.label.setText(mItems[position].mLabel);
    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

}
