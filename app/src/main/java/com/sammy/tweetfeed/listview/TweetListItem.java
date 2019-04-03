package com.sammy.tweetfeed.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sammy.tweetfeed.R;
import com.sammy.tweetfeed.data.DownloadIconTask;
import com.sammy.tweetfeed.data.DownloadImageTask;
import com.sammy.tweetfeed.data.TweetItem;

public class TweetListItem extends RelativeLayout implements View.OnClickListener{

    private TweetItem mItem;
    private Context mContext;

    private TextView mTextView;
    private TextView mUserName;
    private ImageView mUserIcon;

    private ImageView mRetweetBtn;
    private ImageView mFavoriteBtn;
    private TextView mRetweet;
    private TextView mFavorite;

    public Bitmap mImageBitmap;

    public TweetListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TweetListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        if(item.mUserImageURL != null && iconbitmap == null) {
            new DownloadIconTask(mUserIcon, R.drawable.team).execute(item.mUserImageURL);
        } else if (iconbitmap != null) {
            mUserIcon.setImageBitmap(iconbitmap);
        } else {
            mUserIcon.setImageResource(R.drawable.team);
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
}
