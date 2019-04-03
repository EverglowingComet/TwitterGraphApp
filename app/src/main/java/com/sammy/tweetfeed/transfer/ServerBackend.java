package com.sammy.tweetfeed.transfer;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServerBackend {

    private String mRequestUrl = null;
    private ServerBackendInterface mDeelegate;

    public ServerBackend(String url, ServerBackendInterface delegate) {
        mRequestUrl = url;
        mDeelegate = delegate;
    }

    public void postRequest() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(mRequestUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mDeelegate.backendRequestFailed(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    mDeelegate.backendRequestSucceded(response.body().string());
                } else {
                    mDeelegate.backendRequestFailed("Server Response is not successful.");
                }
            }
        });
    }
}
