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
package com.cn.fit.ui.chat.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;

import com.cn.fit.ui.chat.common.utils.DateUtil;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.FileAccessor;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.ui.chatting.ViewImageInfo;
import com.cn.fit.ui.chat.ui.chatting.model.ImgInfo;
import com.cn.fit.util.FileUtils;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;


/**
 * 图片保存
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2015-1-4
 */
public class ImgInfoSqlManager extends AbstractSQLManager {

    public HashMap<String, Bitmap> imgThumbCache = new HashMap<String, Bitmap>(20);
    private static int column_index = 1;

    public static ImgInfoSqlManager mInstance;

    public static ImgInfoSqlManager getInstance() {
        if (mInstance == null) {
            mInstance = new ImgInfoSqlManager();
        }
        return mInstance;
    }

    static final String TABLES_NAME_IMGINFO = "imginfo";


    public List<ViewImageInfo> getViewImageInfos(List<String> msgids) {
        StringBuilder where = new StringBuilder();
        if (msgids != null && !msgids.isEmpty()) {
            where.append(" where " + ImgInfoColumn.MSG_LOCAL_ID + " IN (");
            for (int i = 0; i < msgids.size(); i++) {
                if (msgids.get(i) == null) {
                    continue;
                }
                String id = msgids.get(i);
                where.append("'" + id + "'");
                if (i != msgids.size() - 1) {
                    where.append(",");
                }
            }
            where.append(") ");
        }
        String sql = "select id , msglocalid ,bigImgPath , thumbImgPath from " + TABLES_NAME_IMGINFO + where.toString() + " ORDER BY id ,msglocalid ASC";
        Cursor cursor = sqliteDB().rawQuery(sql, null);
        List<ViewImageInfo> urls = null;
        if (cursor != null && cursor.getCount() > 0) {
            urls = new ArrayList<ViewImageInfo>();
            while (cursor.moveToNext()) {
                urls.add(new ViewImageInfo(cursor));
            }
        }
        return urls;
    }

    public String getAllmsgid() {
        return null;
    }

    public class ImgInfoColumn extends BaseColumn {

        public static final String MSGSVR_ID = "msgSvrId";
        public static final String OFFSET = "offset";
        public static final String TOTALLEN = "totalLen";
        public static final String BIG_IMGPATH = "bigImgPath";
        public static final String THUMBIMG_PATH = "thumbImgPath";
        public static final String CREATE_TIME = "createtime";
        public static final String STATUS = "status";
        public static final String MSG_LOCAL_ID = "msglocalid";
        public static final String NET_TIMES = "nettimes";

    }

    private ImgInfoSqlManager() {
        Cursor cursor = sqliteDB().query(TABLES_NAME_IMGINFO, null, null, null, null, null, ImgInfoColumn.ID + " ASC ");
        if ((cursor.getCount() > 0) && (cursor.moveToLast())) {
            column_index = 1 + cursor.getInt(cursor.getColumnIndex(ImgInfoColumn.ID));
        }
        cursor.close();
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "loading new img id:" + column_index);
    }

    public long insertImageInfo(ImgInfo imgInfo) {
        if (imgInfo == null) {
            return -1;
        }
        ContentValues buildContentValues = imgInfo.buildContentValues();
        if (buildContentValues.size() == 0) {
            return -1;
        }
        try {
            return sqliteDB().insert(TABLES_NAME_IMGINFO, null, buildContentValues);
        } catch (Exception e) {
            LogUtil.e(TAG, "insert imgInfo error = " + e.getMessage());
        }
        return -1;
    }

    /**
     * @param imgInfo
     * @return
     */
    public long updateImageInfo(ImgInfo imgInfo) {
        if (imgInfo == null) {
            return -1;
        }
        ContentValues buildContentValues = imgInfo.buildContentValues();
        if (buildContentValues.size() == 0) {
            return -1;
        }
        try {
            String where = ImgInfoColumn.ID + " = " + imgInfo.getId();
            return sqliteDB().update(TABLES_NAME_IMGINFO, buildContentValues, where, null);
        } catch (Exception e) {
            LogUtil.e(TAG, "insert imgInfo error = " + e.getMessage());
        }
        return -1;
    }

    /**
     * @param filePath
     * @return
     */
    public ImgInfo createImgInfo(String filePath) {

        if (!FileUtils.checkFile(filePath) || FileAccessor.getImagePathName() == null) {
            return null;
        }

        int bitmapDegrees = DemoUtils.getBitmapDegrees(filePath);
        String fileNameMD5 = DemoUtils.md5(System.currentTimeMillis() + filePath);
        String bigFileFullName = fileNameMD5 + ".jpg";
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "original img path = " + filePath);

        Options bitmapOptions = DemoUtils.getBitmapOptions(filePath);
        String authorityDir = FileAccessor.getImagePathName().getAbsolutePath();
        if ((FileUtils.decodeFileLength(filePath) > 204800)
                || (bitmapOptions != null && (((bitmapOptions.outHeight > 960) || (bitmapOptions.outWidth > 960))))) {
            File file = new File(authorityDir);
            if (!file.exists()) {
                file.mkdirs();
            }

            if (!DemoUtils.createThumbnailFromOrig(filePath, 960, 960, Bitmap.CompressFormat.JPEG, 70, FileAccessor.getImagePathName().getAbsolutePath(), fileNameMD5)) {
                return null;
            }
            FileAccessor.renameTo(authorityDir + File.separator, fileNameMD5, bigFileFullName);
        } else {
            // file size small.
            FileUtils.copyFile(authorityDir, fileNameMD5, ".jpg", FileUtils.readFlieToByte(filePath, 0, FileUtils.decodeFileLength(filePath)));
        }
        if (bitmapDegrees != 0 && !DemoUtils.rotateCreateBitmap(authorityDir + File.separator + bigFileFullName, bitmapDegrees, Bitmap.CompressFormat.JPEG, authorityDir, bigFileFullName)) {
            return null;
        }
        LogUtil.d(TAG, "insert: compressed bigImgPath = " + bigFileFullName);
        String thumbName = DemoUtils.md5(fileNameMD5 + System.currentTimeMillis());
        File file = new File(authorityDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!DemoUtils.createThumbnailFromOrig(authorityDir + File.separator + bigFileFullName, 100, 100, Bitmap.CompressFormat.JPEG, 60, authorityDir, thumbName)) {
            return null;
        }
        LogUtil.d(TAG, "insert: thumbName = " + thumbName);
        ImgInfo imgInfo = new ImgInfo();
        column_index += 1;
        imgInfo.setId(column_index);
        imgInfo.setBigImgPath(bigFileFullName);
        imgInfo.setThumbImgPath(thumbName);
        imgInfo.setCreatetime((int) DateUtil.getCurrentTime());
        imgInfo.setTotalLen(FileUtils.decodeFileLength(filePath));
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "insert: compress img size = " + imgInfo.getTotalLen());
        return imgInfo;
    }

    /**
     * @param filePath
     * @return
     */
    public ImgInfo createGIFImgInfo(String filePath) {

        if (!FileUtils.checkFile(filePath) || FileAccessor.getImagePathName() == null) {
            return null;
        }
        String fileNameMD5 = DemoUtils.md5(System.currentTimeMillis() + filePath);
        String bigFileFullName = fileNameMD5 + ".gif";
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "original img path = " + filePath);
        String authorityDir = FileAccessor.getImagePathName().getAbsolutePath();
        FileUtils.copyFile(authorityDir, fileNameMD5, ".gif", FileUtils.readFlieToByte(filePath, 0, FileUtils.decodeFileLength(filePath)));
        LogUtil.d(TAG, "insert: compressed bigImgPath = " + bigFileFullName);
        String thumbName = DemoUtils.md5(fileNameMD5 + System.currentTimeMillis());
        File file = new File(authorityDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!DemoUtils.createThumbnailFromOrig(authorityDir + File.separator + bigFileFullName, 100, 100, Bitmap.CompressFormat.JPEG, 60, authorityDir, thumbName)) {
            return null;
        }
        LogUtil.d(TAG, "insert: thumbName = " + thumbName);
        ImgInfo imgInfo = new ImgInfo();
        column_index += 1;
        imgInfo.setId(column_index);
        imgInfo.setBigImgPath(bigFileFullName);
        imgInfo.setThumbImgPath(thumbName);
        imgInfo.setCreatetime((int) DateUtil.getCurrentTime());
        imgInfo.setTotalLen(FileUtils.decodeFileLength(filePath));
        imgInfo.isGif = true;
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "insert: compress img size = " + imgInfo.getTotalLen());
        return imgInfo;
    }

    /**
     * 接收图片生成缩略图
     *
     * @param msg
     * @return
     */
    public ImgInfo getThumbImgInfo(ECMessage msg) {
        ECImageMessageBody body = (ECImageMessageBody) msg.getBody();
        if (TextUtils.isEmpty(body.getLocalUrl()) || !new File(body.getLocalUrl()).exists()) {
            return null;
        }
        LogUtil.d(TAG, "insert: thumbName = " + body.getFileName());
        ImgInfo imgInfo = new ImgInfo();
        column_index += 1;
        imgInfo.setId(column_index);
        if (!TextUtils.isEmpty(body.getThumbnailFileUrl())) {
            imgInfo.setBigImgPath(body.getRemoteUrl());
            imgInfo.setThumbImgPath(new File(body.getLocalUrl()).getName());
        } else {
            imgInfo.setBigImgPath(new File(body.getLocalUrl()).getName());
            String filePath = body.getLocalUrl();
            String imageName = filePath.substring(filePath.lastIndexOf("/") + 1);
            String thumbName = DemoUtils.md5((imageName + System.currentTimeMillis()));
            String thumbNameDir = FileAccessor.getImagePathName().getAbsolutePath();
            if (!DemoUtils.createThumbnailFromOrig(filePath, 100, 100, Bitmap.CompressFormat.JPEG, 60, thumbNameDir, thumbName)) {
                return null;
            }
            imgInfo.setThumbImgPath(thumbName);
        }
        /*if(body.getRemoteUrl().contains("_thumbnail")) {
			imgInfo.setBigImgPath(body.getRemoteUrl().replace("_thumbnail", ""));
		} else {
			imgInfo.setBigImgPath(null);
		}*/
        imgInfo.isGif = body.getRemoteUrl().endsWith(".gif");
        imgInfo.setMsglocalid(msg.getMsgId());
        imgInfo.setCreatetime((int) DateUtil.getCurrentTime());
        imgInfo.setTotalLen(FileUtils.decodeFileLength(body.getLocalUrl()));
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "insert: compress img size = " + imgInfo.getTotalLen());
        return imgInfo;
    }

    /**
     * 接收图片生成缩略图
     *
     * @return
     */
    public ImgInfo getThumbImgInfo2(ECMessage msg) {

        ECFileMessageBody body = (ECFileMessageBody) msg.getBody();
        if (TextUtils.isEmpty(body.getLocalUrl()) || !new File(body.getLocalUrl()).exists()) {
            return null;
        }
        String filePath = body.getLocalUrl();
        String imageName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String thumbName = DemoUtils.md5((imageName + System.currentTimeMillis()));
        String thumbNameDir = FileUtils.getMD5FileDir(FileAccessor.IMESSAGE_IMAGE, thumbName);
        File file = new File(thumbNameDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!DemoUtils.createThumbnailFromOrig(filePath, 100, 100, Bitmap.CompressFormat.JPEG, 60, thumbNameDir, thumbName)) {
            return null;
        }
        LogUtil.d(TAG, "insert: thumbName = " + thumbName);
        ImgInfo imgInfo = new ImgInfo();
        column_index += 1;
        imgInfo.setId(column_index);
        imgInfo.setBigImgPath(imageName);
        imgInfo.setThumbImgPath(thumbName);
        imgInfo.setMsglocalid(msg.getMsgId());
        imgInfo.setCreatetime((int) DateUtil.getCurrentTime());
        imgInfo.setTotalLen(FileUtils.decodeFileLength(body.getLocalUrl()));
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "insert: compress img size = " + imgInfo.getTotalLen());
        return imgInfo;
    }

    public String getThumbUrlAndDel(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        if (fileName.trim().startsWith("THUMBNAIL://")) {
            String fileId = fileName.substring("THUMBNAIL://".length());
            String imgName = getImgInfo(fileId).getThumbImgPath();
            if (imgName == null) {
                return null;
            }
            String fileUrlByFileName = FileAccessor.getImagePathName() + "/" + imgName;
            delImgInfo(fileId);
            return fileUrlByFileName;
        }
        return null;
    }

    public Bitmap getThumbBitmap(String fileName, float scale) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        if (fileName.trim().startsWith("THUMBNAIL://")) {
            String fileId = fileName.substring("THUMBNAIL://".length());
            String imgName = getImgInfo(fileId).getThumbImgPath();
            if (imgName == null) {
                return null;
            }
            String fileUrlByFileName = FileAccessor.getImagePathName() + "/" + imgName;
            ;
            //String fileUrlByFileName = FileAccessor.getFileUrlByFileName(imgName);
            Bitmap bitmap = imgThumbCache.get(fileUrlByFileName);
            if (bitmap == null || bitmap.isRecycled()) {
                Options options = new Options();
                float density = 160.0F * scale;
                options.inDensity = (int) density;
                bitmap = BitmapFactory.decodeFile(fileUrlByFileName, options);
                if (bitmap != null) {
                    bitmap.setDensity((int) density);
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (scale * bitmap.getWidth()), (int) (scale * bitmap.getHeight()), true);
                    imgThumbCache.put(fileUrlByFileName, bitmap);
                    LogUtil.d(TAG, "cached file " + fileName);
                }
            }

            if (bitmap != null) {
                return DemoUtils.processBitmap(bitmap, /*bitmap.getWidth() / 15*/0);
            }

        }
        return null;
    }

    /**
     * @param msgId
     * @return
     */
    public ImgInfo getImgInfo(String msgId) {
        ImgInfo imgInfo = new ImgInfo();
        String where = ImgInfoColumn.MSG_LOCAL_ID + "='" + msgId + "'";
        Cursor cursor = sqliteDB().query(TABLES_NAME_IMGINFO, null, where, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            imgInfo.setCursor(cursor);
        }
        cursor.close();
        return imgInfo;
    }

    public ImgInfo getImgInfo(int id) {
        ImgInfo imgInfo = new ImgInfo();
        String where = ImgInfoColumn.ID + "=" + id;
        Cursor cursor = sqliteDB().query(TABLES_NAME_IMGINFO, null, where, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            imgInfo.setCursor(cursor);
        }
        cursor.close();
        return imgInfo;
    }


    public long delImgInfo(String msgId) {
        String where = ImgInfoColumn.MSG_LOCAL_ID + "='" + msgId + "'";
        return getInstance().sqliteDB().delete(TABLES_NAME_IMGINFO, where, null);
    }

    public static void reset() {
        getInstance().release();
    }

    @Override
    protected void release() {
        super.release();
        mInstance = null;
    }
}
