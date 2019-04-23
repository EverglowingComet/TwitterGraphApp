package com.sammy.tweetfeed.data;

import java.util.ArrayList;

public class GraphData {

    public double mSamplingInterval;
    public ArrayList<Double> mData = new ArrayList<Double>();
    public ArrayList<Milestone> mMilestones = new ArrayList<Milestone>();

    public GraphData(int samplingInterval) {
        mSamplingInterval = samplingInterval;
    }
}
