package com.sammy.tweetfeed.data;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Iterator;

public class AppDataCache {
    public static HashMap<String, Bitmap> sIconCash = new HashMap<>();
    public static HashMap<String, Bitmap> sTweetIconCash = new HashMap<>();
    public static HashMap<String, Boolean> sDownloadingURL = new HashMap<>();
    public static HashMap<String, Boolean> sDownloadedURL = new HashMap<>();
    public static HashMap<String, Boolean> sTweetDownloadingURL = new HashMap<>();
    public static HashMap<String, Boolean> sTweetDownloadedURL = new HashMap<>();

    public static void cleanTweetCache() {
        Iterator<Bitmap> iterator = sTweetIconCash.values().iterator();
        while (iterator.hasNext()) {
            Bitmap icon_bitmap = iterator.next();
            if (icon_bitmap != null && !icon_bitmap.isRecycled()) {
                icon_bitmap.recycle();
            }
        }
        sTweetIconCash.clear();
        sTweetDownloadedURL.clear();
    }
}
