/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.ui.chatting.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.FileAccessor;
import com.cn.fit.ui.chat.common.utils.ResourceHelper;
import com.cn.fit.ui.chat.storage.ImgInfoSqlManager;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.holder.BaseHolder;
import com.cn.fit.ui.chat.ui.chatting.holder.ImageRowViewHolder;
import com.cn.fit.ui.chat.ui.chatting.view.ChattingItemContainer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.yuntongxun.ecsdk.ECMessage;

import java.io.File;


/**
 * <p>Title: ImageRxRow.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-4-17
 */
public class ImageRxRow extends BaseChattingRow {

    public ImageRxRow(int type) {
        super(type);
    }


    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {

        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null) {
            convertView = new ChattingItemContainer(inflater, R.layout.chatting_item_from_picture);

            //use the view holder pattern to save of already looked up subviews
            ImageRowViewHolder holder = new ImageRowViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));

        }
        //actually setup the view
        return convertView;
    }

    @Override
    public void buildChattingData(Context context, BaseHolder baseHolder,
                                  ECMessage detail, int position) {

        final ImageRowViewHolder holder = (ImageRowViewHolder) baseHolder;
        String userData = detail.getUserData();
        ViewHolderTag holderTag = ViewHolderTag.createTag(detail, ViewHolderTag.TagType.TAG_VIEW_PICTURE, position);
        View.OnClickListener onClickListener = ((ChattingActivity) context).getChattingAdapter().getOnClickListener();
        holder.chattingContentIv.setTag(holderTag);
        holder.chattingContentIv.setOnClickListener(onClickListener);
        holder.chattingContentIv.setImageBitmap(null);
        if (TextUtils.isEmpty(userData)) {
            return;
        }
        int start = userData.indexOf("THUMBNAIL://");
        int gif = userData.indexOf(",PICGIF://");
        boolean isGif = userData.contains("PICGIF://true");
        if (start != -1) {
            String thumbnail;
            if (gif == -1) {
                thumbnail = userData.substring(start);
            } else {
                thumbnail = userData.substring(start, gif);
            }
            Bitmap thumbBitmap = ImgInfoSqlManager.getInstance().getThumbBitmap(thumbnail, 2);
            final ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(detail.getMsgId());
            DisplayImageOptions.Builder optionsBuilder = DemoUtils.getChatDisplayImageOptionsBuilder();
            optionsBuilder.showImageOnLoading(new BitmapDrawable(thumbBitmap));
            if (imgInfo != null && !TextUtils.isEmpty(imgInfo.getBigImgPath())) {

                if (imgInfo.getBigImgPath().startsWith("http:")) {
                    ImageLoader.getInstance().displayImage(imgInfo.getBigImgPath(), holder.chattingContentIv, optionsBuilder.build(), new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            if (imageUri.startsWith("http:")) {
                                File imgCacheFile = DiskCacheUtils.findInCache(imageUri, ImageLoader.getInstance().getDiskCache());
                                if (imgCacheFile != null) {
                                    imgInfo.setBigImgPath(imgCacheFile.getAbsolutePath().substring(imgCacheFile.getAbsolutePath().lastIndexOf("/")));
                                    ImgInfoSqlManager.getInstance().updateImageInfo(imgInfo);
                                }
                            }
                        }
                    });
                } else {
                    ImageLoader.getInstance().displayImage("file://" + FileAccessor.getImagePathName() + "/" + imgInfo.getBigImgPath(), holder.chattingContentIv, optionsBuilder.build());
                }
            }
        }

        if (isGif) {
            boolean showGif = detail.getMsgStatus() == ECMessage.MessageStatus.SUCCESS || detail.getMsgStatus() == ECMessage.MessageStatus.RECEIVE;
            if (holder.mGifIcon != null) {
                holder.mGifIcon.setVisibility(!showGif ? View.GONE : View.VISIBLE);
            }
        } else {
            holder.mGifIcon.setVisibility(View.GONE);
        }
        int startWidth = userData.indexOf("outWidth://");
        int startHeight = userData.indexOf(",outHeight://");
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.chattingContentIv.getLayoutParams();
        if (startWidth != -1 && startHeight != -1 && start != -1) {
            int imageMinWidth = ResourceHelper.fromDPToPix(context, isGif ? 200 : 100);
            int width = DemoUtils.getInt(userData.substring(startWidth + "outWidth://".length(), startHeight), imageMinWidth);
            int height = DemoUtils.getInt(userData.substring(startHeight + ",outHeight://".length(), start - 1), imageMinWidth);
            holder.chattingContentIv.setMinimumWidth(imageMinWidth);
            params.width = imageMinWidth;
            int _height = height * imageMinWidth / width;
            if (_height > ResourceHelper.fromDPToPix(context, 230)) {
                _height = ResourceHelper.fromDPToPix(context, 230);
                holder.chattingContentIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            if (width != 0) {
                holder.chattingContentIv.setMinimumHeight(_height);
                params.height = _height;
            } else {
                holder.chattingContentIv.setMinimumHeight(imageMinWidth);
                params.height = imageMinWidth;
            }
            holder.chattingContentIv.setLayoutParams(params);

        }
        //}
    }

    @Override
    public int getChatViewType() {

        return ChattingRowType.IMAGE_ROW_RECEIVED.ordinal();
    }


    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu,
                                          View targetView, ECMessage detail) {
        // TODO Auto-generated method stub
        return false;
    }

}
