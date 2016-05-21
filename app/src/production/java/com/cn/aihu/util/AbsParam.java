package com.cn.fit.util;

public class AbsParam {

//	private static String IP = "118.26.142.171";
//	private static String IP = "192.168.12.115";
//	private static String IP = "192.168.203.103";
	/** 後台通信端口 */
//	private static String port = "80";
	private static String port = "8086";
	/** 通用路径 */
	private static String commonPath = "/serviceplatform";//"/serviceplatform";
	/** 专家用户ID，护士登陆后回给一个唯一的专家ID */
	private static String DnurseID = "1";
	/**科大讯飞语音播报申请的ID*/
//    private static final String APPID = "appid=519328ab";//科大讯飞语音播报申请的ID
	/** 后台通信地址，正式环境 */
	private static String IP = "test.inurse.com.cn";
	/** 后台通信地址，测试环境 */
//	private static String IP = "118.26.142.167:"+port;
	public static String getDnurseID() {
		return DnurseID;
	}
///**科大讯飞语音播报申请的ID*/
//	public static String getAppid() {
//		return APPID;
//	}

	public static void setDnurseID(String dnurseID) {
		DnurseID = dnurseID;
	}

	public static String getCommonPath() {
		return commonPath;
	}

	public static String getIP() {
		return IP;
	}

	public static String getPort() {
		return port;
	}

	public static String getBaseUrl() {
//		return "http://" + getIP() ;
		return "http://101.200.90.103" ;
//		return "http://10.103.14.195:8080/serviceplatform" ;
	}

}
