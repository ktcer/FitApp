package com.cn.fit.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class CreateFolder {

    public static File updateDir = null;
    public static File updateFile = null;
    public static boolean isCreateFileSucess;

    /**
     * 方法描述：createFile方法
     *
     * @param String app_name
     * @return
     * @see FileUtil
     */
    public static void createApkFile(String app_name) {

        if (hasSDCard()) {
            isCreateFileSucess = true;
            updateDir = new File(AppDisk.appInursePath);
            updateFile = new File(updateDir + "/" + app_name + ".apk");

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    isCreateFileSucess = false;
                    e.printStackTrace();
                }
            }

        } else {
            isCreateFileSucess = false;
        }
    }

    public static void createFolder(String userNum) {
        if (hasSDCard()) {
            //有sd卡才进行以下创建
            if (createRootFolder()) {
                //应用根目录创建成功之后再创建下级目录
                if (createUserFolder(userNum)) {
                    //用户目录创建成功则创建用户下面的目录
                    createRecordFolder(userNum);
                    createVideoFolder(userNum);
                    createCacheFolder(userNum);
                }

            }
        }
    }

    public static boolean hasSDCard() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }

    /*
     * 创建应用根目录
     */
    private static boolean createRootFolder() {
        try {
            File dirFirstFile = new File(AppDisk.appInursePath);//新建一级主目录
            if (!dirFirstFile.exists()) {//判断文件夹目录是否存在
                dirFirstFile.mkdir();//如果不存在则创建
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;

    }

    /*
     * 创建用户目录
     */
    private static boolean createUserFolder(String userNum) {
        try {
            File dirFirstFile = new File(AppDisk.appInursePath + userNum + File.separator);//新建一级主目录
            if (!dirFirstFile.exists()) {//判断文件夹目录是否存在
                dirFirstFile.mkdir();//如果不存在则创建
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;

    }

    /*
     * 创建录音目录
     */
    private static void createRecordFolder(String userNum) {
        File dirFirstFile = new File(AppDisk.appInursePath + userNum + File.separator + AppDisk.DCIM_RECORD);//新建一级主目录
        if (!dirFirstFile.exists()) {//判断文件夹目录是否存在
            dirFirstFile.mkdir();//如果不存在则创建
        }
    }

    /*
     * 创建录像 目录
     */
    private static void createVideoFolder(String userNum) {
        File dirFirstFile = new File(AppDisk.appInursePath + userNum + File.separator + AppDisk.DCIM_VIDEO);//新建一级主目录
        if (!dirFirstFile.exists()) {//判断文件夹目录是否存在
            dirFirstFile.mkdir();//如果不存在则创建
        }
    }

    /*
     * 创建缓存目录
     */
    private static void createCacheFolder(String userNum) {
        AppDisk.PICTUREFOLDER = AppDisk.appInursePath + userNum
                + File.separator + AppDisk.PIC_CACHE;
        File dirFirstFile = new File(AppDisk.PICTUREFOLDER);// 新建缓存目录
        if (!dirFirstFile.exists()) {// 判断文件夹目录是否存在
            dirFirstFile.mkdir();// 如果不存在则创建
        }
    }
    /*
	 * 创建图片缓存目录目录
	 */
//	public static void createCacheFolder(){
//		
//		if(hasSDCard()){
//			//有sd卡才进行以下创建
//			if(createRootFolder()){
//				//应用根目录创建成功之后再创建下级目录
//					//用户目录创建成功则创建用户下面的目录
//			         File dirFirstFile=new File(AppDisk.appInursePath+AppDisk.PIC_CACHE);//新建一级主目录  
//			         if(!dirFirstFile.exists()){//判断文件夹目录是否存在  
//			              dirFirstFile.mkdir();//如果不存在则创建  
//			         }  			
//			}
//		}
//
//	}

    /*
     * 获取录音目录下所有文件名
     */
    public static List<String> getAllFileNameInFolder(String folderPath, String name) {
        File file = new File(folderPath);//新建一级主目录
        File[] array = file.listFiles();
        if (array == null) {
            return null;
        }
        List<String> fileList = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                // only take file name
                fileList.add(array[i].getName());
            }
        }
        return fileList;
    }

    /*
     * 获取录音目录下所有文件名
     */
    public static List<String> getAllFileNameInFolder(String folderPath) {
        File file = new File(folderPath);//新建一级主目录
        File[] array = file.listFiles();
        if (array == null) {
            return null;
        }
        List<String> fileList = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                // only take file name
                fileList.add(array[i].getName());
            }
        }
        return fileList;
    }

    /*
     * 获取录音目录下指定的文件名
     */
    public static String getFileNameInFolder(String folderPath, String name) {
        File file = new File(folderPath);//新建一级主目录
        File[] array = file.listFiles();
        if (array == null) {
            return null;
        }
        String filename = new String();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                // only take file name
                if (name.equals(array[i].getName())) {
                    filename = array[i].getName();
                }
                ;
            }
        }
        return filename;
    }
}
