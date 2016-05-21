/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.ui.chatting;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.ChattingAysnImageLoader;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.FileAccessor;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.ui.ECSuperActivity;


/**
 * 图片预览大图
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2015-1-4
 */
public class ImageGalleryActivity extends ECSuperActivity implements View.OnClickListener {


    private Bitmap mThumbnailBitmap;

    private ChattingAysnImageLoader mAsynImageDownload;

    private ImageMsgInfoEntry mMsgEntry;

    private ImageView mImageView;
    private ProgressBar mpProgressBar;

    private String mImageUrl;

    /**
     *
     */
    private boolean mFullscreen = true;

    /**
     * keys for extras and icicles
     */
    public final static String CHATTING_MESSAGE = "ccp@chatting_message";


    final private ChattingAysnImageLoader.ChattingImageCallBack mChattingImageCallBack
            = new ChattingAysnImageLoader.ChattingImageCallBack() {

        @Override
        public void onChattingImageLoaded() {
            if (null != mMsgEntry.getRemoteUrl() && !TextUtils.isEmpty(mMsgEntry.getRemoteUrl())) {// load
                String saveName = DemoUtils.md5(mMsgEntry.getRemoteUrl()) + ".jpg";
                LogUtil.d("this image saveName " + saveName);
                if (new File(FileAccessor.getImagePathName(), saveName).exists()) {
                    System.gc();
                    Drawable fromPath = Drawable.createFromPath(FileAccessor.getImagePathName() + "/" + saveName);
                    if (fromPath == null) {
                        finish();
                    }
                    if (mAsynImageDownload != null && mMsgEntry != null) {
                        mAsynImageDownload.removeKeyCache(mMsgEntry.getId());
                    }
                    LogUtil.d("loading from sdcard " + saveName + ".jpg");
                    if (fromPath != null && mImageView != null) {
                        mImageView.setImageDrawable(fromPath);
                        mpProgressBar.setVisibility(View.GONE);
                    }
                } else {
                    mpProgressBar.setVisibility(View.VISIBLE);
                }
            }
        }
    };


    private final Handler mHandlerCallbck = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            mFullscreen = !mFullscreen;
            setTitleFooterVisible(mFullscreen);
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Bring up the softkeyboard so the user can immediately enter msg. This
        // call won't do anything on devices with a hard keyboard.
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAsynImageDownload = ChattingAysnImageLoader.getInstance(this);
        LogUtil.d(LogUtil.getLogUtilsTag(ImageGalleryActivity.class), "load is  " + mAsynImageDownload.toString());

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            Object o = mBundle.getParcelable(CHATTING_MESSAGE);
            if (o != null && o instanceof ImageMsgInfoEntry) {
                mMsgEntry = (ImageMsgInfoEntry) o;
            }
        }

        if (mMsgEntry == null) {
            finish();
            return;
        }
        if (mThumbnailBitmap == null) {
            mThumbnailBitmap = BitmapFactory.decodeFile(FileAccessor.getImagePathName() + "/" + mMsgEntry.getThumbnailurl());
        }

        // Initialize members for UI elements.
        initResourceRefs();

        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1, "1 / 1", this);
        setActionBarTitle("1 / 1");
        setTitleFooterVisible(true);
    }

    @Override
    public void onBaseContentViewAttach(View contentView) {
        View activityLayoutView = getActivityLayoutView();
        ((ViewGroup) activityLayoutView.getParent()).removeView(activityLayoutView);
        ((ViewGroup) getWindow().getDecorView()).addView(activityLayoutView, 1);

    }

    /**
     * @param request
     */
    private void requestStatusbars(boolean request) {
        if (request) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            return;
        }
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "request custom title");
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Full screen, hidden actionBar
     *
     * @param visible
     */
    void setTitleFooterVisible(boolean visible) {
        if (visible) {
            requestStatusbars(false);
            showTitleView();
            return;
        }

        requestStatusbars(true);
        hideTitleView();
    }

    /**
     *
     */
    private void initResourceRefs() {
        mImageView = (ImageView) findViewById(R.id.image_photo);
        mImageView.setOnClickListener(this);
        mpProgressBar = (ProgressBar) findViewById(R.id.footLoading);
        mImageView.setAdjustViewBounds(true);

        // 查看大图是否已经在本地


        mImageUrl = mMsgEntry.getPicurl();
        if (null != mImageUrl && !TextUtils.isEmpty(mImageUrl) && !mImageUrl.startsWith("http")) {// load
            Drawable drawable = null;
            if (new File(FileAccessor.getImagePathName(), mImageUrl).exists()) {
                System.gc();
                drawable = Drawable.createFromPath(FileAccessor.getImagePathName() + "/" + mImageUrl);
                mThumbnailBitmap = BitmapFactory.decodeFile(FileAccessor.getImagePathName() + "/" + mMsgEntry.getThumbnailurl());
                if (drawable == null) {
                    finish();
                }
                LogUtil.d("loading from sdcard " + mImageUrl);
                mImageView.setImageDrawable(drawable);
                mpProgressBar.setVisibility(View.GONE);
            }
            // 本地文件已经损坏
        } else {
            String thumbnailurl = mMsgEntry.getThumbnailurl();
            if (new File(thumbnailurl).exists()) {
                mImageView.setImageBitmap(BitmapFactory.decodeFile(thumbnailurl));
                return;
            }
            mImageView.setImageBitmap(mThumbnailBitmap);
            mAsynImageDownload.addTask(null, mMsgEntry, false);
            mpProgressBar.setVisibility(View.VISIBLE);
        }

    }

    /* (non-Javadoc)
     * @see com.hisun.cas.ui.CASActivity#onResume()
     */
    @Override
    public void onResume() {
        if (mAsynImageDownload != null) {
            mAsynImageDownload.setCallBack(mChattingImageCallBack);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImageView != null && mImageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            mImageView.setImageDrawable(null);
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }

        mImageView = null;
        mpProgressBar = null;
        //mAsynImageDownload.stop();

        System.gc();
    }

    @Override
    protected int getLayoutId() {

        return R.layout.slide_image;
    }

    @Override
    public void onClick(View v) {
        mHandlerCallbck.sendEmptyMessageDelayed(1, 350L);
        switch (v.getId()) {
            case R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;

            default:
                break;
        }
    }


}
