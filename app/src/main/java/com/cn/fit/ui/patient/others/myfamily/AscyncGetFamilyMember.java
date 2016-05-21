//package com.cn.aihu.ui.patient.others.myfamily;
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
//import com.cn.aihu.model.family.BeanFamilyMemberInfo;
//import com.cn.aihu.model.family.BeanFamilyResult;
//import com.cn.aihu.model.family.BeanResultUtils;
//import com.cn.aihu.ui.AppMain;
//import com.cn.aihu.ui.basic.ActivityBasic;
//import com.cn.aihu.util.AbsParam;
//import com.cn.aihu.util.Constant;
//import com.cn.aihu.util.UtilsSharedData;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//public class AscyncGetFamilyMember extends AsyncTask<Integer, Integer, String>{
//
//	String result;
//	private Activity act;
//	private static String family = "/family/member/";
//	private long userId;
//	public AscyncGetFamilyMember(Activity act) {
//		this.act = act;
//		AppMain.allFamilyMemberList = new ArrayList<BeanFamilyMemberInfo>();
//		AppMain.memberList = new ArrayList<BeanFamilyMemberInfo>();
//		UtilsSharedData.initDataShare(act);// ////////
//		userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
//	}
//
//	@Override
//	protected String doInBackground(Integer... params) {
//		HashMap<String, String> param = new HashMap<String, String>();
//		param.put("userId", userId+"");
//		param.put("pageSize", "8");
//		param.put("pageNo", "1");
//		try {
//			String url = AbsParam.getBaseUrl()  +family + "list";
//			result = NetTool.sendPostRequest(url, param, "utf-8");
//			Log.i("result", result);
//			JsonArrayToList(result);
//		} catch (Exception e) {
//			((ActivityBasic)act).hideProgressBar();
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//		((ActivityBasic)act).hideProgressBar();
////		updateUI();
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
//	BeanFamilyResult bean = BeanResultUtils.parseResult(jsonString);
//	//添加我自己的信息
//	BeanFamilyMemberInfo myInfo = new BeanFamilyMemberInfo();
//	myInfo.setImageUrl("");
//	myInfo.setRemarkName("我");
//	myInfo.setMemberId(userId);
//	myInfo.setMemberLoginName(UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT));
//	myInfo.setMemberGknumber(Integer.parseInt(UtilsSharedData.getValueByKey(Constant.USER_GKMUM).equals("")?"0":UtilsSharedData.getValueByKey(Constant.USER_GKMUM)));
//	myInfo.setImageUrl(UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL));
//	AppMain.allFamilyMemberList.add(0, myInfo);
//	if(bean!=null){
//	if(bean.getStatusCode()==-1){
//		AppMain.memberList = gson.fromJson(bean.getData().toString(), new TypeToken<List<BeanFamilyMemberInfo>>(){}.getType()); 
//		if(AppMain.memberList != null){
//			for(BeanFamilyMemberInfo beanInfo : AppMain.memberList){
//				AppMain.allFamilyMemberList.add(beanInfo);
//			}
//		}
//	}
//	}
//}
//
//}
