//package com.cn.aihu.ui.patient.main.mynurse;
//package com.cn.aihu.ui.patient.main.mynurse;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cn.aihu.R;
//import com.cn.aihu.http.NetTool;
//import com.cn.aihu.model.nurse.BeanExpert;
//import com.cn.aihu.model.nurse.BeanRecommendInfo;
//import com.cn.aihu.model.nurse.BeanRequestAddInfo;
//import com.cn.aihu.ui.AppMain;
//import com.cn.aihu.ui.basic.ActivityBasic;
//import com.cn.aihu.ui.patient.main.TabActivityMain;
//import com.cn.aihu.ui.patient.main.mynurse.asynctask.AscynRecommendInfo;
//import com.cn.aihu.ui.patient.main.mynurse.asynctask.AscyncGetMyNurseList;
//import com.cn.aihu.ui.paychoose.ActivityPayChoose;
//import com.cn.aihu.util.AbsParam;
//import com.cn.aihu.util.Constant;
//import com.cn.aihu.util.FButton;
//import com.cn.aihu.util.UtilsSharedData;
//import com.cn.aihu.util.coverflow.FancyCoverFlow;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
///**
// * 健康秘书
// * 
// * @author kuangtiecheng
// * 
// */
//public class ActivityMyCareExpertAddMsg extends ActivityBasic {
//
//	private List<BeanRequestAddInfo> listMember;
//	private DisplayImageOptions options;
//	private ImageLoader imageLoader;
////	private View rootView;// 缓存Fragment view
//
//	// 健康服务
//	private FancyCoverFlow fancyCoverFlow;
//	private ImageAdapterNurse adapter;
//
//	private TextView startTourTips;
//	private FButton startTour;
//
//	private LinearLayout layout_chat,layout_addtips;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.mycareasist);
//		initial();
//		//初始化聊天窗
////		initViewchat(view);
//		initData();// 聊天	
//		//同时获取保健秘书的添加消息
//		getAssistAddInfo();
//		//同时获取潘总关爱信息
//		getPaliNews();
//	}
//
//	protected void initial() {
////		layout_chat = (LinearLayout) view.findViewById(R.id.layout_chat);
////		layout_addtips = (LinearLayout) view.findViewById(R.id.add_tips);
////		startTour = (FButton) view.findViewById(R.id.start_tour);
////		startTour.setCornerRadius(10);
////		startTourTips = (TextView) view.findViewById(R.id.start_tour_tips);
//		options = AppMain.initImageOptions(R.drawable.default_user_icon, false);
//		imageLoader = ImageLoader.getInstance();
//	}
//
//	// 初始化要显示的数据
//	private void initData() {
//		UtilsSharedData.initDataShare(this);
//	}
//
//	private void getPaliNews() {
//		showProgressBar();
//		// 推送的消息
//		AscynRecommendInfo arc = new AscynRecommendInfo(getActivity()) {
//			@Override
//			protected void onPostExecute(BeanRecommendInfo result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//
//				hideProgressBar();
//				
//			}
//
//		};
//		arc.execute();
//	}
//
//	/*
//	 * 获取保健秘书添加消息
//	 * 
//	 */
//	private void getAssistAddInfo() {
//		AscynRequestAddInfo ascynRequestAddInfo = new AscynRequestAddInfo(
//				this);
//		ascynRequestAddInfo.execute();
//	}
//	
//	
//	/*
//	 * 获取当前的添加消息（包含专家主动添加患者以及患者添加专家的确认消息）
//	 * 
//	 */
//	private class AscynRequestAddInfo extends AsyncTask<Integer, Integer, String> {
//
//		String result = "";
//		private Context act;
//		private String requestAddInfo = "/member/request/addlist";
//		private long userId;
//
//		public AscynRequestAddInfo(Context act) {
//			this.act = act;
//			listMember= new ArrayList<BeanRequestAddInfo>();
//			UtilsSharedData.initDataShare(act);// ////////
//			userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
//			showProgressBar();
//		}
//
//		@Override
//		protected  String doInBackground(Integer... params) {
//			HashMap<String, String> param = new HashMap<String, String>();
//			param.put("userId", userId + "");
//			param.put("pageSize", "10");
//			param.put("pageNum", "1");
//			param.put("userType", "0");
//			try {
//				String url = AbsParam.getBaseUrl() + requestAddInfo;
//				Log.i("input", url+param.toString());
//				result = NetTool.sendPostRequest(url, param, "utf-8");
//				Log.i("result", result);
//				JsonArrayToList(result);
//			} catch (Exception e) {
//				hideProgressBar();
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			hideProgressBar();
//			//组装数据，将添加消息也放入上层滑动头像中
//			//先remove掉原有的expert信息
//			for(int i=AppMain.allExpertList.size()-2;i>=0;i--){
//				if(AppMain.allExpertList.get(i).getResult()==0){
//					AppMain.allExpertList.remove(i);
//				}
//			}
//
//			for(BeanRequestAddInfo bean :listMember){
//				BeanExpert beanExpert = new BeanExpert();
//				beanExpert.setExpertId(bean.getNurseID());
//				beanExpert.setExpertName(bean.getNurseName());
//				beanExpert.setTxlj(bean.getImgUrl());
//				beanExpert.setResult(0);
//				AppMain.allExpertList.add(AppMain.allExpertList.size()-1, beanExpert);
//			}
//			adapter.notifyDataSetChanged();
//			if(AppMain.allExpertList.size()==1){
//				TabActivityMain.selectNurse = 0;
//			}else{
//				layout_chat.setVisibility(View.VISIBLE);
//				if(AppMain.allExpertList.size()!=0){
//					setTipsStatus(AppMain.allExpertList.get(TabActivityMain.selectNurse));
//				}
//			}
//		}
//
//		/**
//		 * 解析返回来的Json数组
//		 * 
//		 * @param jsonString
//		 * @return
//		 * @throws Exception
//		 */
//		private void JsonArrayToList(String jsonString) throws Exception {
//			Gson gson = new Gson();
//			// 添加我自己的信息
//			if (jsonString != null) {
//				if (!(jsonString.equals(-1))) {
//					listMember = gson.fromJson(jsonString,
//							new TypeToken<List<BeanRequestAddInfo>>() {
//							}.getType());
//					if(listMember.size()==1){
//						if(listMember.get(0).getIfhave()==0){
//							listMember.clear();
//						}
//					}
//				}
//			}
//		}
//
//	}
//
//	/*
//	 * 禁用聊天界面
//	 */
//	private void disableChat(){
//
//	}
//	/*
//	 * 引导用户进行支付
//	 */
//	private void payExpert(BeanExpert tempExpert){
//		Intent intentwxpay = new Intent(getActivity(), ActivityPayChoose.class);
//		startActivity(intentwxpay);
//	}
//	
//	
//	/*
//	 * 判断当前是否应该显示提示窗口
//	 */
//	private void setTipsStatus(BeanExpert tempExpert){
//		if((tempExpert.getExpertName()==null)){
//			return;
//		}
//		if(tempExpert.getResult()==0){
//			//未支付秘书，需要进行支付
//			disableChat();
//			if(layout_addtips.getVisibility()!=View.VISIBLE){
//				setAnimation(0.0f,0.0f,-1.0f,0.0f);
//				layout_addtips.startAnimation(animation);  
//				layout_addtips.setVisibility(View.VISIBLE);
//			}
//			startTourTips.setText(String.format("您好!我是 %s 保健护师,请点击下方的“开启健康之旅”,让我陪您一起享受健康生活吧！", tempExpert.getExpertName()));
//			startTour.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					//先不需要支付，后期需要支付
////					payExpert(tempExpert);
//					showProgressBar();
//					PayConfirmAsyncTask confirmExpectAsyncTask = new PayConfirmAsyncTask();
//					confirmExpectAsyncTask.execute();
//				}
//			});
//		}else{
//			if(layout_addtips.getVisibility()!=View.GONE){
//				setAnimation(0.0f,0.0f,0.0f,-1.0f);
//				layout_addtips.startAnimation(animation); 
//				layout_addtips.setVisibility(View.GONE);
//			}
//
//		}
//	}
//
//	// 人物选择滑动
//	private void choosePerson(View view) {	
//		if(AppMain.allExpertList.size()==0){
//			//做二次预防  防止闪退
//			AppMain.allExpertList.add(new BeanExpert());
//		}
//		adapter = new ImageAdapterNurse(getActivity(),imageLoader,options);
//		fancyCoverFlow = (FancyCoverFlow) view
//				.findViewById(R.id.fancyCoverFlow_service);
//		fancyCoverFlow.setSpacing(-50);
//		fancyCoverFlow.setAdapter(adapter);
//		fancyCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				TabActivityMain.selectNurse = position;
//				if(position<AppMain.allExpertList.size()-1){
//					//逻辑判断,如果是未支付确认专家则提示进行支付，否则显示聊天数据
//					final BeanExpert tempExpert = AppMain.allExpertList.get(TabActivityMain.selectNurse);
//					setTipsStatus(tempExpert);
//				}else{
//					if(layout_addtips.getVisibility()!=View.GONE){
//						setAnimation(0.0f,0.0f,0.0f,-1.0f);
//						layout_addtips.startAnimation(animation); 
//						layout_addtips.setVisibility(View.GONE);
//					}
//					//清空聊天界面，禁用发送按钮
//					disableChat();					
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//			}
//		});
//
//		// 点击事件
//		fancyCoverFlow.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				if (TabActivityMain.selectNurse == position) {
//					if(position == AppMain.allExpertList.size()-1){
//						//添加按钮
//						Intent intent = new Intent(getActivity(),
//								ActivityChooseNurse.class);
//						startActivity(intent);
//					}else{
//						if(AppMain.allExpertList.get(position).getResult()!=0){
//						//专家主页
//						Intent intent = new Intent(getActivity(),ActivityCoachPage.class);
//						intent.putExtra("ExpertId", AppMain.allExpertList.get(position).getExpertId());
//						intent.putExtra("image", AppMain.allExpertList.get(position).getTxlj());
//						startActivity(intent);
//						}
//					}
//					
//				}
//			}
//		});
//	}
//
//	/**
//	 * 获取健康秘书
//	 * 
//	 * @author kuangtiecheng
//	 * 
//	 */
//	private void getExperts() {
//		showProgressBar();
//		AscyncGetMyNurseList async = new AscyncGetMyNurseList(getActivity()) {
//
//			@Override
//			protected String doInBackground(Integer... params) {
//				// TODO Auto-generated method stub
//				return super.doInBackground(params);
//			}
//
//			@Override
//			protected void onPostExecute(String result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				hideProgressBar();
//				getAssistAddInfo();
//			}
//
//		};
//		async.execute();
//	}
//
//	/**
//	 * 确认添加专家，可以同意也可以拒绝
//	 * 
//	 * @author kuangtiecheng
//	 * 
//	 */
////	private String operation= "agree";
////	private class ConfirmExpectAsyncTask extends
////			AsyncTask<Integer, Integer, String> {
////		String result = "";
////
////		@Override
////		protected String doInBackground(Integer... params) {
////			long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
////			HashMap<String, String> param = new HashMap<String, String>();
////			param.put("userId", userId + "");
////			param.put("nurseId",AppMain.allExpertList.get(TabActivityMain.selectNurse).getExpertId() + "");
////			param.put("type", operation);
////			Log.i("input", AbsParam.getBaseUrl() + "/member/confirm/add"
////					+ param.toString());
////			try {
////				result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
////						+ "/member/confirm/add", param, "utf-8");
////				Log.i("result", result);
////				JsonResolve(result);
////			} catch (Exception e) {
////				hideProgressBar();
////				e.printStackTrace();
////			}
////			return null;
////		}
////
////		@Override
////		protected void onPostExecute(String result) {
////			hideProgressBar();
////			if (operation.equals("agree")) {
////				if (consequent.equals("success")) {
////					getExperts();
////					if(layout_addtips.getVisibility()!=View.GONE){
////						setAnimation(0.0f,0.0f,0.0f,-1.0f);
////						layout_addtips.startAnimation(animation); 
////						layout_addtips.setVisibility(View.GONE);
////					}
//////					if(layout_tour.getVisibility()!=View.GONE){
//////						setAnimation(0.0f,0.0f,0.0f,1.0f);
//////						layout_tour.setAnimation(animation);
//////						layout_tour.setVisibility(View.GONE);
//////					}
////				} else {
////					Toast.makeText(getActivity(), "添加失败", 1000).show();
////				}
////
////			} else {
////				if (consequent.equals("success")) {
////					Toast.makeText(getActivity(), "拒接添加成功", 1000).show();
////				} else {
////					Toast.makeText(getActivity(), "拒绝失败", 1000).show();
////				}
////
////			}
////		}
////
////		private String consequent;
////
////		private void JsonResolve(String result) {
////			JSONObject jsonObject;
////			try {
////				// tempNurseList.clear();
////				jsonObject = new JSONObject(result);
////				consequent = jsonObject.getString("result");
////				Log.i("input", "consequent》》》》》》" + consequent);
////
////			} catch (JSONException e) {
////				e.printStackTrace();
////			}
////		}
////	}
//
//	/**
//	 * 支付确认，开启健康之旅
//	 * 
//	 * @author kuangtiecheng
//	 * 
//	 */
//	private class PayConfirmAsyncTask extends
//			AsyncTask<Integer, Integer, String> {
//		String result = "";
//
//		@Override
//		protected String doInBackground(Integer... params) {
//			long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
//			HashMap<String, String> param = new HashMap<String, String>();
//			param.put("patientID", userId + "");
//			param.put("expertID",AppMain.allExpertList.get(TabActivityMain.selectNurse).getExpertId() + "");
//			Log.i("input", AbsParam.getBaseUrl() + "/member/healthcare/pay"
//					+ param.toString());
//			try {
//				result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
//						+ "/member/healthcare/pay", param, "utf-8");
//				Log.i("result", result);
//			} catch (Exception e) {
//				hideProgressBar();
//				e.printStackTrace();
//			}
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			hideProgressBar();
//			JSONObject json;
//			try {
//				json = new JSONObject(result);
//				result=json.getString("result");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(result!=null){
//				if(result.equals("success")){
//					getExperts();
//					if(layout_addtips.getVisibility()!=View.GONE){
//						setAnimation(0.0f,0.0f,0.0f,-1.0f);
//						layout_addtips.startAnimation(animation); 
//						layout_addtips.setVisibility(View.GONE);
//					}
//
//				}else{
//					Toast.makeText(getActivity(), "您的健康之旅貌似没有开启成功哦！", 1000).show();
//				}
//			}
//		}
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		super.onClick(v);
//		switch (v.getId()) {
//
//
//		}
//	}
//
///*
// * 聊天界面开始	
// */
//	
//	
//}
