package com.sammy.tweetfeed.transfer;

public interface ServerBackendInterface {
    public void backendRequestSucceded(String result);
    public void backendRequestFailed(String error);
}
