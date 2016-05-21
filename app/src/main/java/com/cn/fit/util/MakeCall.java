package com.cn.fit.util;
//package com.cn.aihu.util;
//
//import java.util.regex.Pattern;
//
//import android.content.Context;
//
//import com.butel.butelconnect.client.ButelCliManager;
//import com.butel.butelconnect.client.ButelClient;
///**
// * @Description:锟斤拷锟斤拷锟斤拷牛锟斤拷频锟界话
// * @author 锟铰匡拷锟斤拷
// */
//public class MakeCall {
//
//	static ButelClient Aclient = null;// butelConnect锟侥客伙拷锟剿撅拷锟�
//	/*
//	 * private static AutoCompleteTextView edt_appkey; private static
//	 * AutoCompleteTextView edt_uid; private static AutoCompleteTextView
//	 * edt_nubenumber; private static AutoCompleteTextView edt_nikename; private
//	 * static AutoCompleteTextView edt_dstnubenumber; private static
//	 * AutoCompleteTextView edt_dstnikename;
//	 */
//	private static String calltype = "102";
//
//	public static int makecall(Context context, String destNickName,
//			String destNum) {
//
//		ButelClient client = ButelCliManager.getClient(context);
//		return client.makeCall(Integer.parseInt(calltype.toString().trim()),
//				new String(destNum).toString().trim(), new String(destNickName)
//						.toString().trim());
//
//	}
//	
//	public static String register(Context context,String userId){
//		String nubeNum = "";
//		ButelClient client = ButelCliManager.getClient(context);
//		int tag = client.init();
//		if(tag == 0){
//			nubeNum = client.register(new String("82bf0c6d171942eba499f0397ab07dcf").toString().trim(), userId.toString().trim());
//		}
//		return nubeNum;
//	}
//	
//	public static int login(Context context,String userId,String nubeNum,String nickName){
//		ButelClient client = ButelCliManager.getClient(context);
//		int tag = client.init();
//		int tagLogin = -1;
//		if(tag == 0){
//			tagLogin = client.login(new String(
//					"82bf0c6d171942eba499f0397ab07dcf").toString().trim(),
//					new String(nubeNum).toString().trim(), new String(
//							userId).toString().trim(), new String(nickName)
//							.toString().trim());		
//       	}
//	    return tagLogin;
//	}
//	
//	
//	public static void logout(Context context){
//		ButelClient client = ButelCliManager.getClient(context);
//		client.logout();
//	}
//
//
//	public static boolean isNumeric(String str) {
//		Pattern pattern = Pattern.compile("[0-9]*");
//		return pattern.matcher(str).matches();
//	}
//	
//	
//	public static String getReturnMeaning(int msg){
//		if (msg==0) {
//			return "0  makecall 锟缴癸拷";
//		} else if (msg==-1) {
//			return "锟斤拷锟界不锟斤拷锟斤拷";
//		} else if (msg==-2) {
//			return "锟斤拷锟斤拷锟斤拷锟斤拷通锟斤拷锟斤拷";
//		} else if (msg ==-3) {
//			return "锟斤拷锟斤拷锟斤拷锟酵达拷锟斤拷";
//		} else if (msg==-4) {
//			return "锟斤拷锟叫猴拷为锟斤拷";
//		} else if (msg==-5) {
//			return "锟皆硷拷锟斤拷锟斤拷锟皆硷拷";
//		} else if (msg==-6) {
//			return "锟界话锟斤拷锟斤拷未锟斤拷锟斤拷";
//		} else if (msg==-7) {
//			return "锟斤拷支锟斤拷锟斤拷锟斤拷频通锟斤拷";
//		} else if (msg==-8) {// acd只锟斤拷锟�8锟斤拷锟斤拷
//			return "ACD锟斤拷询失锟斤拷";
//		} else if (msg==-10) {
//			return "ACD锟斤拷锟绞э拷锟�;
//		} else if (msg==-200) {
//			return "ACD锟斤拷询锟斤拷时";
//		} else if (msg== -909) {
//			return "nube锟斤拷锟诫不锟较凤拷:8位锟斤拷锟斤拷";
//		}
//		
//		return "";
//	}
//}
