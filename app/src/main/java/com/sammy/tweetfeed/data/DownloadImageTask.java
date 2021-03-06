package com.sammy.tweetfeed.data;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    int mDefaultResId;
    String urldisplay;

    public DownloadImageTask(ImageView bmImage, int defaultResId) {
        this.bmImage = bmImage;
        mDefaultResId = defaultResId;
    }

    protected Bitmap doInBackground(String... urls) {
        urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            AppDataCache.sIconCash.put(urldisplay, result);
            bmImage.setImageBitmap(result);
        } else {
            bmImage.setImageResource(mDefaultResId);
        }
    }
}