//package com.cn.aihu.ui.patient.main.mynurse.asynctask;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import android.app.Activity;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.cn.aihu.http.NetTool;
//import com.cn.aihu.model.nurse.BeanExpert;
//import com.cn.aihu.util.AbsParam;
//import com.cn.aihu.util.Constant;
//import com.cn.aihu.util.UtilsSharedData;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//public class AscyncGetMyNurseList extends AsyncTask<Integer, Integer, ArrayList<BeanExpert>>{
//
//	String result;
//	private Activity act;
//	private static String family = "/member/patient/";
//	private long userId;
//	private ArrayList<BeanExpert> allExpertList = new ArrayList<BeanExpert>();
//	public AscyncGetMyNurseList(Activity act) {
//		this.act = act;
//		allExpertList = new ArrayList<BeanExpert>();
//		UtilsSharedData.initDataShare(act);// ////////
//		userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
//	}
//
//	@Override
//	protected ArrayList<BeanExpert> doInBackground(Integer... params) {
//		HashMap<String, String> param = new HashMap<String, String>();
//		param.put("patientID", userId + "");
//		try {
//			String url = AbsParam.getBaseUrl()  +family + "mynurse";
//			result = NetTool.sendPostRequest(url, param, "utf-8");
//			Log.i("result", result);
//			JsonArrayToList(result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return allExpertList;
//	}
//
//	@Override
//	protected void onPostExecute(ArrayList<BeanExpert> result) {
//		
//	}
//
///**
// * 解析返回来的Json数组
// * 
// * @param jsonString
// * @return
// * @throws Exception
// */
//private void JsonArrayToList(String jsonString) throws Exception {
//	Gson gson = new Gson();
//	allExpertList = gson.fromJson(jsonString, new TypeToken<List<BeanExpert>>(){}.getType()); 
//	//新加一个空值，用于添加专家
//}
//
//}
