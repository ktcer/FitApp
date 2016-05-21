package com.cn.fit.util;

import java.util.ArrayList;

import android.app.Activity;

import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.ui.chatting.ViewImageInfo;

public class UtilsImage {
    /*
	 * 显示大图(单图)
	 */

    public static void displayBigPic(Activity mContext, String url) {

        if (!url.equals("")) {
            String fullUrl = AbsParam.getBaseUrl() + url;
            int position = 0;
            ViewImageInfo beanPic = new ViewImageInfo(fullUrl, fullUrl);
            ArrayList<ViewImageInfo> urls = new ArrayList<ViewImageInfo>();
            urls.add(beanPic);
            if (urls == null || urls.isEmpty()) {
                return;
            }
            CCPAppManager
                    .startChattingImageViewAction(mContext, position, urls);
        }
    }

	/*
	 * 显示大图(多图)
	 */

    public static void displayBigPics(Activity mContext, ArrayList<String> urls,
                                      int position) {

        if (urls == null) {
            return;
        }
        if (urls.size() == 0) {
            return;
        }
        ArrayList<ViewImageInfo> imageUrls = new ArrayList<ViewImageInfo>();
        for (String url : urls) {
            ViewImageInfo beanPic = new ViewImageInfo(AbsParam.getBaseUrl() + url, AbsParam.getBaseUrl() + url);
            imageUrls.add(beanPic);
        }

        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }
        CCPAppManager.startChattingImageViewAction(mContext, position,
                imageUrls);
    }
}
