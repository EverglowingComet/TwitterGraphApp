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
import com.sammy.tweetfeed.data.TrendingPlayer;
import com.sammy.tweetfeed.data.TrendingTeam;

import java.io.InputStream;

public class TrendingListItem extends RelativeLayout {

    public Bitmap mIconBitmap = null;
    public String mIconURL = null;
    public static String TAG = "TendingListItem";

    private ImageView mIcon = null;
    private TextView mTitle = null;
    private TextView mGameInfo = null;

    public TrendingListItem(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public TrendingListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDefaultIcon() {
        mIcon.setImageResource(R.drawable.team);
    }

    public void bindView(TrendingTeam team, Bitmap iconbitmap) {
        mIcon = (ImageView) findViewById(R.id.tending_list_item_icon);
        mTitle = (TextView) findViewById(R.id.tending_list_title);
        mGameInfo = (TextView) findViewById(R.id.tending_list_item_success);
        if (mIcon != null && mTitle != null && mGameInfo != null) {
            if(team.mImageURL != null && iconbitmap == null) {
                new DownloadImageTask(mIcon, R.drawable.team).execute(team.mImageURL.toString());
            } else if (iconbitmap != null) {
                mIconBitmap = iconbitmap;
                mIcon.setImageBitmap(iconbitmap);
            } else {
                mIcon.setImageResource(R.drawable.team);
            }
            mTitle.setText(team.mName);
            mGameInfo.setText(team.mShareOfVoice + "%");
        } else {
            Log.e(TAG, "missing elements");
        }
    }

    public void bindView(TrendingPlayer player, Bitmap iconbitmap) {
        mIcon = (ImageView) findViewById(R.id.tending_list_item_icon);
        mTitle = (TextView) findViewById(R.id.tending_list_title);
        mGameInfo = (TextView) findViewById(R.id.tending_list_item_success);
        mIconURL = player.mImageURL.toString();
        if (mIcon != null && mTitle != null && mGameInfo != null) {
            mIconBitmap = null;
            if(player.mImageURL != null && iconbitmap == null) {
                new DownloadImageTask(mIcon, R.drawable.team).execute(player.mImageURL);
            } else if (iconbitmap != null) {
                mIconBitmap = iconbitmap;
                mIcon.setImageBitmap(iconbitmap);
            } else {
                mIcon.setImageResource(R.drawable.team);
            }
            mTitle.setText(player.mName);
            mGameInfo.setText(player.mShareOfVoice + "%");
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        int mDefaultResId;

        public DownloadImageTask(ImageView bmImage, int defaultResId) {
            this.bmImage = bmImage;
            mDefaultResId = defaultResId;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
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
            mIconBitmap = result;
            if (result != null) {
                bmImage.setImageBitmap(result);
            } else {
                bmImage.setImageResource(mDefaultResId);
            }
        }
    }
}
