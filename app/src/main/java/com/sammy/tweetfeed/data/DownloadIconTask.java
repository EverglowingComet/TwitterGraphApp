package com.sammy.tweetfeed.data;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadIconTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;
    private int mDefaultResId;
    private String imgUrl;
    private Context mContext;

    public DownloadIconTask(Context activity, ImageView bmImage, int defaultResId) {
        mContext = activity;
        imageView = bmImage;
        mDefaultResId = defaultResId;
    }

    protected Bitmap doInBackground(String... urls) {
        imgUrl = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(imgUrl).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            AppDataCache.sTweetIconCash.put(imgUrl, result);
            AppDataCache.sDownloadingURL.remove(imgUrl);
            AppDataCache.sDownloadedURL.put(imgUrl, true);
            imageView.setImageBitmap(result);
        } else {
            AppDataCache.sDownloadingURL.remove(imgUrl);
            AppDataCache.sDownloadedURL.put(imgUrl, false);
            imageView.setImageResource(mDefaultResId);
        }
    }
}
