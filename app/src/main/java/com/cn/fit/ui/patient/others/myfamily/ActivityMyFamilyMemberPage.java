///**
// * @version: 2015-4-6 下午4:58:49
// * @author kuangtiecheng
// * 我家庭成员的列表，点击进去是成员的健康详情,用的是listview
// */
//
//package com.cn.aihu.ui.patient.others.myfamily;
//
//import java.util.HashMap;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.View;
//import android.webkit.WebView;
//import android.widget.TextView;
//
//import com.cn.aihu.R;
//import com.cn.aihu.ui.AppMain;
//import com.cn.aihu.ui.basic.ActivityBasic;
//import com.cn.aihu.util.Constant;
//import com.cn.aihu.util.StringUtil;
//
//public class ActivityMyFamilyMemberPage extends ActivityBasic{
//	private WebView mapView;
////    private String name;
//    private int selectNum = 0;
//	
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.my_res_my_familymember);
//		initView();
//	}
//
//	private void initView() {
//		selectNum = getIntent().getIntExtra(Constant.SELECT_MEMBER,0);
//		TextView title = (TextView) findViewById(R.id.middle_tv);
//		title.setText(StringUtil.displayName(AppMain.allFamilyMemberList.get(selectNum)));
//		mapView = (WebView) findViewById(R.id.datamap);
//		mapView.getSettings().setJavaScriptEnabled(true); 
////		mapView.setWebViewClient(new WebViewClient() {
////			public boolean shouldOverrideUrlLoading(WebView view, String url) {
////				view.loadUrl(url);
////				return true;
////			}
////		});
//		mapView.loadUrl(AppMain.mapIP+"/control/realtime.jsp?a=admin,admin123&n="+AppMain.allFamilyMemberList.get(selectNum).getMemberGknumber());
//	}
//
//	//查询家庭成员
//	private class QueryExpertTask extends AsyncTask<Integer, Integer, String> {
//		String result;
//
//		@Override
//		protected String doInBackground(Integer... params) {
//			HashMap<String, String> param = new HashMap<String, String>();
//			param.put("patientID", "1");
//			param.put("pageSize", 10 + "");
//			param.put("pageNum", 10 + "");
//			try {
////				String a = AbsParam.getBaseUrl()  + "/app/" + queryNurse;//"/yyzx/app/" + queryNurse;
////				Log.i("result", a);
////				result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
////						+ "/app/" + queryNurse, param, "utf-8");
////				Log.i("result", result);
////				JsonArrayToList(result);
//			} catch (Exception e) {
//				hideProgressBar();
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//
//		}
//	}
//
//
//	public void onClick(View v) {
//		super.onClick(v);
////		switch (v.getId()) {
////		case R.id.new_member://添加家庭成员
////			showDialog();
////			break;
////		case R.id.right_tv://消息
////			Intent intent = new Intent();
////			intent.setClass(ActivityMyFamilyMemberPage.this, ActivityAddNews.class);
////			startActivity(intent);
////			break;
////		default:
////			break;
////		}
//
//	}
//
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//
//	}
//
//
//
//    @Override
//    public void onDestroy() {
//              super.onDestroy();
//    }
//
//}
