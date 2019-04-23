package com.sammy.tweetfeed.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.AppDataCache;
import com.sammy.tweetfeed.data.DownloadIconTask;
import com.sammy.tweetfeed.data.DownloadImageTask;
import com.sammy.tweetfeed.data.TweetItem;

import java.io.InputStream;

public class TweetListItem extends RelativeLayout implements View.OnClickListener{

    private TweetItem mItem;
    private Context mContext;
    public String mIconURL = null;
    public Bitmap mImageBitmap = null;

    private TextView mTextView;
    private TextView mUserName;
    private ImageView mUserIcon;

    private ImageView mRetweetBtn;
    private ImageView mFavoriteBtn;
    private TextView mRetweet;
    private TextView mFavorite;

    public DownloadIconTask mDownloadTask = null;

    public TweetListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TweetListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void bindView(TweetItem item, Bitmap iconbitmap) {

        mTextView = (TextView) findViewById(R.id.tweet_item_text);
        mUserName = (TextView) findViewById(R.id.tweet_user_name);
        mUserIcon = (ImageView) findViewById(R.id.tweet_user_icon);

        mRetweet = (TextView) findViewById(R.id.tweet_retweet);
        mFavorite = (TextView) findViewById(R.id.tweet_favorite);

        mRetweetBtn = (ImageView) findViewById(R.id.tweet_retweet_btn);
        mRetweetBtn.setOnClickListener(this);
        mFavoriteBtn = (ImageView) findViewById(R.id.tweet_favorite_btn);
        mFavoriteBtn.setOnClickListener(this);

        mItem = item;
        mTextView.setText(item.mText);
        mUserName.setText(item.mUserName);
        mRetweet.setText(String.valueOf(item.mRetweetCount));
        mFavorite.setText(String.valueOf(item.mFavoriteCount));
        mUserIcon.setImageResource(R.drawable.player);
        if (mIconURL == null || !mIconURL.equals(item.mUserImageURL)) {
            mIconURL = item.mUserImageURL;
            mImageBitmap = null;
        }
        if(item.mUserImageURL != null && (iconbitmap == null || mImageBitmap == null)) {
            if (mDownloadTask != null)
                mDownloadTask.cancel(true);
            mDownloadTask = new DownloadIconTask(this, R.drawable.player);
            mDownloadTask.execute(item.mUserImageURL.toString());
        } else if (mImageBitmap != null) {
            mUserIcon.setImageBitmap(mImageBitmap);
        } else if (iconbitmap != null) {
            mImageBitmap = iconbitmap;
            mUserIcon.setImageBitmap(iconbitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tweet_retweet:
                postRetweet(mItem.mTweetId);
                break;
            case R.id.tweet_favorite:
                postFavorite(mItem.mTweetId);
                break;
        }
    }

    public void postRetweet(String tweet_id) {

    }

    public void postFavorite(String tweet_id) {

    }

    public class DownloadIconTask extends AsyncTask<String, Void, Bitmap> {
        private TweetListItem item;
        private int mDefaultResId;
        private String imgUrl;

        public DownloadIconTask(TweetListItem bmImage, int defaultResId) {
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
                AppDataCache.sTweetIconCash.put(imgUrl, result);
                mImageBitmap = result;
                if (item.mIconURL != null && item.mIconURL.equals(imgUrl))
                    item.mUserIcon.setImageBitmap(result);
            } else {
                item.mUserIcon.setImageResource(mDefaultResId);
            }
        }
    }

}
