package com.cn.fit.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.view.Display;
import android.view.View;

import com.cn.fit.ui.Loger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 功能页面截屏
 *
 * @author kuangtiecheng
 */
public class Screenshot {

    private Activity activity;

    /**
     * 分享页面截屏
     */
    public static Bitmap screenImage;
    private static long imgBuildTime;    //图片创建时间
    private static long imgSaveTime;    //图片本地存储时间

    private Loger log = new Loger("[" + getClass().getSimpleName() + "]");
    ;

    public Screenshot(Activity activvity) {
        this.activity = activvity;
    }

    /**
     * 截屏后翻转90度
     *
     * @return boolean 是否成功
     */
    public boolean cutScreenHorizontal() {
        boolean result = cutScreen();
        if (result) {
            Matrix matrix = new Matrix();
            matrix.postRotate(0);    //翻转90度
            int width = screenImage.getWidth();
            int height = screenImage.getHeight();
            screenImage = Bitmap.createBitmap(screenImage, 0, 0, width, height, matrix, true);
        }
        return result;
    }

    /**
     * 截屏方法
     *
     * @return boolean 是否成功
     */
    public boolean cutScreen() {
        try {
            View view = activity.getWindow().getDecorView();
            Display display = activity.getWindowManager().getDefaultDisplay();
            view.layout(0, 0, display.getWidth(), display.getHeight());
            view.setDrawingCacheEnabled(true);//允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
            screenImage = Bitmap.createBitmap(view.getDrawingCache());
            int headH = getHeadHeight();
            screenImage = Bitmap.createBitmap(screenImage, 0, headH, display.getWidth(), display.getHeight() - headH);
            imgBuildTime = System.currentTimeMillis();
            return true;
        } catch (Exception e) {
            log.output("截取当前页面的屏幕失败，异常信息如下：", e);
        }
        return false;
    }

    /**
     * 返回系统顶部状态条的高度
     *
     * @return int
     */
    private int getHeadHeight() {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();

        if (height <= 320) {
            return 18;
        } else if (height <= 480) {
            return 25;
        } else if (height <= 800) {
            return 40;
        } else if (height <= 854) {
            return 44;
        } else if (height <= 960) {
            return 48;
        } else if (height <= 1024) {
            return 52;
        } else if (height <= 1280) {
            return 72;
        } else {
            return 0;
        }
    }

    /**
     * 返回存储设备状态（是否可用）
     *
     * @return boolean
     */
    public static boolean getExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取微博分享的本地图片存储地址
     *
     * @return String 返回图片本地存储地址
     * @throws Exception
     */
    public static String getShareLocalFilePath() throws Exception {
        String picRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/weiboShare/";
        String picPath = picRoot + "ShareImg.png";
        if (imgBuildTime == imgSaveTime) {
            return picPath;
        }

        File fileDir = new File(picRoot);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(picPath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (null == screenImage) {
            return null;
        }
        screenImage.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, baos);
        byte[] buf = baos.toByteArray();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(buf, 0, buf.length);
        fileOutputStream.close();
        imgSaveTime = imgBuildTime;
        return picPath;
    }
}