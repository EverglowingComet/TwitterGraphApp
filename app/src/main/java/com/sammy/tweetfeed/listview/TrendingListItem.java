package com.sammy.tweetfeed.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.AppDataCache;
import com.sammy.tweetfeed.data.TrendingPlayer;
import com.sammy.tweetfeed.data.TrendingTeam;

import java.io.InputStream;

public class TrendingListItem extends RelativeLayout {

    public String mIconURL = null;
    public Bitmap mImageBitmap = null;
    public static String TAG = "TendingListItem";

    private ImageView mImageView = null;
    private TextView mTitle = null;
    private TextView mGameInfo = null;

    public DownloadIconTask mDownloadTask = null;

    public TrendingListItem(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public TrendingListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDefaultIcon() {
        mImageView.setImageResource(R.drawable.team);
    }

    public void bindView(TrendingTeam team, Bitmap iconbitmap) {
        mImageView = (ImageView) findViewById(R.id.tending_list_item_icon);
        mTitle = (TextView) findViewById(R.id.tending_list_title);
        mGameInfo = (TextView) findViewById(R.id.tending_list_item_success);
        if (mIconURL == null || !mIconURL.equals(team.mImageURL)) {
            mIconURL = team.mImageURL;
            mImageBitmap = null;
        }
        if (mImageView != null && mTitle != null && mGameInfo != null) {
            mImageView.setImageResource(R.drawable.team);
            if(team.mImageURL != null && (iconbitmap == null || mImageBitmap == null)) {
                if (mDownloadTask != null)
                    mDownloadTask.cancel(true);
                mDownloadTask = new DownloadIconTask(this, R.drawable.team);
                mDownloadTask.execute(team.mImageURL.toString());
            } else if (mImageBitmap != null) {
                mImageView.setImageBitmap(mImageBitmap);
            } else if (iconbitmap != null) {
                mImageBitmap = iconbitmap;
                mImageView.setImageBitmap(iconbitmap);
            }
            mTitle.setText(team.mName);
            mGameInfo.setText(team.mShareOfVoice + "%");
        } else {
            Log.e(TAG, "missing elements");
        }
    }

    public void bindView(TrendingPlayer player, Bitmap iconbitmap) {
        mImageView = (ImageView) findViewById(R.id.tending_list_item_icon);
        mTitle = (TextView) findViewById(R.id.tending_list_title);
        mGameInfo = (TextView) findViewById(R.id.tending_list_item_success);
        if (mIconURL == null || !mIconURL.equals(player.mImageURL)) {
            mIconURL = player.mImageURL;
            mImageBitmap = null;
        }
        if (mImageView != null && mTitle != null && mGameInfo != null) {
            mImageView.setImageResource(R.drawable.player);
            if(player.mImageURL != null && (iconbitmap == null || mImageBitmap == null)) {
                if (mDownloadTask != null)
                    mDownloadTask.cancel(true);
                mDownloadTask = new DownloadIconTask(this, R.drawable.team);
                mDownloadTask.execute(player.mImageURL.toString());
            } else if (mImageBitmap != null) {
                mImageView.setImageBitmap(mImageBitmap);
            } else if (iconbitmap != null) {
                mImageBitmap = iconbitmap;
                mImageView.setImageBitmap(iconbitmap);
            }
            mTitle.setText(player.mName);
            mGameInfo.setText(player.mShareOfVoice + "%");
        } else {
            Log.e(TAG, "missing elements");
        }
    }

    public class DownloadIconTask extends AsyncTask<String, Void, Bitmap> {
        private TrendingListItem item;
        private int mDefaultResId;
        private String imgUrl;

        public DownloadIconTask(TrendingListItem bmImage, int defaultResId) {
            item = bmImage;
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
                AppDataCache.sIconCash.put(imgUrl, result);
                mImageBitmap = result;
                if (item.mIconURL != null && item.mIconURL.equals(imgUrl))
                    item.mImageView.setImageBitmap(result);
            } else {
                item.mImageView.setImageResource(mDefaultResId);
            }
        }
    }

}
