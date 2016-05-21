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
package com.cn.fit.ui.chat.common.utils;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import com.cn.fit.R;
import com.cn.fit.util.FileUtils;


/**
 * <p>Title: MimeTypesTools.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2015-1-4
 */
public class MimeTypesTools {

    private static boolean hasLoadMimeType = false;

    public static String getMimeType(Context context, String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            fileName = fileName.toLowerCase();

            MimeTypes mimeTypes = getMimeTypes(context);
            String extension = FileUtils.getExtension(fileName);
            return mimeTypes.getMimeType(extension);
        }

        return null;
    }

    private static MimeTypes getMimeTypes(Context context) {
        return loadMimeTypes(context);
    }

    private static MimeTypes loadMimeTypes(Context context) {
        MimeTypeParser parser = null;
        XmlResourceParser xmlResourceParser = null;
        if (!hasLoadMimeType) {
            try {
                parser = new MimeTypeParser(context, context.getPackageName());
                xmlResourceParser = context.getResources().getXml(R.xml.mimetypes);

                return parser.fromXmlResource(xmlResourceParser);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            hasLoadMimeType = true;
        }

        return null;
    }
}
