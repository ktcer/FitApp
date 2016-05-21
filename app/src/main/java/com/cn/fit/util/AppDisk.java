package com.cn.fit.util;

import android.os.Environment;

import java.io.File;

/**
 * 系统目录名称、路径
 *
 * @author kuangtiecheng
 */
public class AppDisk {

    /**
     * SD卡目录
     */
    public static final String diskLocal = Environment.getExternalStorageDirectory() + File.separator;

    public final static String appName = "Aihu";

    /**
     * inurse根目录
     */
    public final static String appInursePath = diskLocal + appName + File.separator;

//	/** 资格证照片目录 */
//	public static final String Photo = "/mnt/internalsd/AppTaxi/photo/";
//	
//	/** 交接班地点的配置文件目录 */
//	public static final String PathHandover = "/mnt/internalsd/AppTaxi/Address/";
//	
//	/** 日志文件目录 */
//	public static final String LogPath = "/mnt/internalsd/AppTaxi/Logs/";
//	
//	/** 增量文件夹相对路径 */
//	public static final String patchpath = "/mnt/internalsd/AppTaxi/";	//8M
//	
//	/** 存储分包数据用的地址*/	
//	public static final String updatePackagePath = "/mnt/extsd/AppTaxi/TaxiTerminalAppDownload/";	//8M////////////////////
//	
//	/** 外部存储卡路径 */
//	public static final String EXTSD = "/mnt/extsd/";
//	
//	/** 监控路径 */
//	public static final String DCIM = "/mnt/extsd/dcim/";
//	
//	/** 图片的路径 */
//	public static final String DCIM_CAMERA = "/mnt/extsd/dcim/camera/";
//	
    /**
     * 录音的路径
     */
    public static final String DCIM_RECORD = "record" + File.separator;

    /**
     * 视频的路径
     */
    public static final String DCIM_VIDEO = "video" + File.separator;

    /**
     * 缓存的路径
     */
    public static final String PIC_CACHE = "cache" + File.separator;

    /**
     * 缓存图片、裁剪图片的最终路径
     */
    public static String PICTUREFOLDER = "";
    /**
     * 头像的目录
     */
    public static final String MYHEAD = "myHead" + File.separator;
//	
//	/** 高德地图所在目录 */
//	public static final String mapABC = "/mnt/extsd/MapABC/";
//	
//	/** 图片的的路径 */
//	public static final String Media_IMAGE = "/mnt/extsd/Media/Image/";
//	
//	/** 录音的的路径 */
//	public static final String Media_MUSIC = "/mnt/extsd/Media/Music/";
//	
//	/** 录像的的路径 */
//	public static final String Media_VIDEO = "/mnt/extsd/Media/Video/";

    /**
     * 是否能读写外存储盘
     *
     * @return boolean
     */
    public static boolean canReadDisk() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
//	
//	/** OTG外界存储盘符  */
//	public static final String diskStorage = "/mnt/usbhost1/";
//	
//	/** 更新目录  */
//	public static final String otgTaxiTerRec = "/mnt/usbhost1/AppTaxi/";
//	
//	/** 更新目录  */
//	public static final String otgMapABC = "/mnt/usbhost1/MapABC/";
//	
//	/** 更新目录 */
//	public static final String otgDCIM = "/mnt/usbhost1/dcim/";
//	
//	/** OTG更新目录（可多个）  */
//	public static final String[] otgDirDown = {"/mnt/usbhost1/AppTaxi/", "/mnt/usbhost1/MapABC/"};

}