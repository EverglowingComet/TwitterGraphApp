package com.sammy.tweetfeed.data;

public class IntervalItem {
    public String mLabel;
    public int mIntervalValue;
    public int mSamplingInterval;

    public IntervalItem(int value, String label, int sampling) {
        mLabel =label;
        mIntervalValue = value;
        mSamplingInterval = sampling;
    }

    public static int getSamplingInterval(IntervalItem[] items, int interval) {
        for(int i = 0; i < items.length; i ++) {
            if(items[i].mIntervalValue == interval)
                return items[i].mSamplingInterval;
        }
        return -1;
    }
}
