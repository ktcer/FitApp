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
//import lecho.lib.hellocharts.view.LineChartView;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.style.ForegroundColorSpan;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnLongClickListener;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cn.aihu.R;
//import com.cn.aihu.http.NetTool;
//import com.cn.aihu.model.family.BeanFamilyResult;
//import com.cn.aihu.model.family.BeanResultUtils;
//import com.cn.aihu.ui.AppMain;
//import com.cn.aihu.ui.basic.ActivityBasic;
//import com.cn.aihu.util.AbsParam;
//import com.cn.aihu.util.CircleImageView;
//import com.cn.aihu.util.Constant;
//import com.cn.aihu.util.CustomDialog;
//import com.cn.aihu.util.StringUtil;
//import com.cn.aihu.util.UtilsSharedData;
//import com.cn.aihu.util.segmentbutton.AndroidSegmentedControlView;
//import com.cn.aihu.util.segmentbutton.AndroidSegmentedControlView.OnSelectionChangedListener;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//
//public class ActivityMyFamily extends ActivityBasic implements OnLongClickListener{
//	private static String family = "/family/member/";
//	private TextView title;
//	private TextView search,name;
//	private int	resultID;
//	private DisplayImageOptions options;
//	private ImageLoader imageLoader = ImageLoader.getInstance();
//	private WebView mapView;
//	private CircleImageView myImg;
//	private CircleImageView[] imgMember = new CircleImageView[8];
//	private TextView[] tvMember = new TextView[8];
//	private BeanFamilyResult beanFamilyResult;
//	private SlideShowView slideShowView;
//	
//	private FrameLayout dataPhotos;
//	private LineChartView dataChart;
//	private int flag=0;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.my_res_my_family);
//		initView();
//	}
//
//	private void initView() {
//		UtilsSharedData.initDataShare(this);
//		search = (TextView) findViewById(R.id.right_tv);
//		title = (TextView) findViewById(R.id.middle_tv);
//		title.setText("我的家庭");
//		search.setVisibility(View.VISIBLE);
//		search.setText("消息");
//		search.setOnClickListener(this);
//		name = (TextView) findViewById(R.id.tv_name);
//		myImg = (CircleImageView) findViewById(R.id.myimg);
//		imgMember[0] = (CircleImageView) findViewById(R.id.person1);
//		imgMember[1] = (CircleImageView) findViewById(R.id.person2);
//		imgMember[2] = (CircleImageView) findViewById(R.id.person3);
//		imgMember[3] = (CircleImageView) findViewById(R.id.person4);
//		
//		imgMember[4] = (CircleImageView) findViewById(R.id.person5);
//		imgMember[5] = (CircleImageView) findViewById(R.id.person6);
//		imgMember[6] = (CircleImageView) findViewById(R.id.person7);
//		imgMember[7] = (CircleImageView) findViewById(R.id.person8);
//		
//		tvMember[0] = (TextView) findViewById(R.id.title1);
//		tvMember[1] = (TextView) findViewById(R.id.title2);
//		tvMember[2] = (TextView) findViewById(R.id.title3);
//		tvMember[3] = (TextView) findViewById(R.id.title4);
//		
//		tvMember[4] = (TextView) findViewById(R.id.title5);
//		tvMember[5] = (TextView) findViewById(R.id.title6);
//		tvMember[6] = (TextView) findViewById(R.id.title7);
//		tvMember[7] = (TextView) findViewById(R.id.title8);
//		
//		myImg.setOnClickListener(this);
//		for(int i = 0;i<8;i++){
//			imgMember[i].setOnClickListener(this);
//		}
//
//		mapView = (WebView) findViewById(R.id.datamap);
//		mapView.getSettings().setJavaScriptEnabled(true); 
//		mapView.setWebViewClient(new WebViewClient() {
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
//				return true;
//			}
//		});
//		options = new DisplayImageOptions.Builder() 
//    	.showImageOnLoading(R.drawable.default_user_icon) //设置图片在下载期间显示的图片  
//    	.showImageForEmptyUri(R.drawable.default_user_icon)//设置图片Uri为空或是错误的时候显示的图片  
//    	.showImageOnFail(R.drawable.default_user_icon)  //设置图片加载/解码过程中错误时候显示的图片
//    	.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
//    	.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
//    	.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//    	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
//    	.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
//    	//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//    	//设置图片加入缓存前，对bitmap进行设置  
//    	//.preProcessor(BitmapProcessor preProcessor)  
//    	.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
//    	.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少  
//    	.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
//    	.build();//构建完成  
//		
//		imageLoader.displayImage(AbsParam.getBaseUrl()+UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL), myImg, options);
//		myImg.setBorderColor(getResources()
//				.getColor(R.color.blue_second));
//		myImg.setBorderWidth(20);
//		name.setText("我");
//		
//		dataPhotos = (FrameLayout)findViewById(R.id.dataphotots);
//		dataChart = (LineChartView)findViewById(R.id.datachart);
////		mapView.loadUrl("http://118.26.142.171:8080/control/alarm.jsp?a=admin,admin123");
//		mapView.loadUrl(AppMain.mapIP+ "/control/realtime.jsp?a=admin,admin123&n="+UtilsSharedData.getValueByKey(Constant.USER_GKMUM));
//		slideShowView = (SlideShowView)findViewById(R.id.slideshowView);
//		
////		TextView tv1 = new TextView(ActivityMyFamily.this);
////		tv1.setId(1);
////		tv1.setBackgroundColor(getResources().getColor(R.color.white));
////		tv1.setTextColor(getResources().getColor(R.color.gray));
////		tv1.setText("奋力开发中");
//		int[] imageList = new int[]{
//                R.drawable.lunbo1,
//                R.drawable.lunbo2,
//                R.drawable.lunbo3,   
//                R.drawable.lunbo4,
//        };
//		slideShowView.setImageList(imageList);
//		AndroidSegmentedControlView datePicker = (AndroidSegmentedControlView)findViewById(R.id.choose_display);
//		datePicker.setOnSelectionChangedListener(new OnSelectionChangedListener() {
//			
//			@Override
//			public void newSelection(String identifier, String value) {
//				// TODO Auto-generated method stub
//				if(value.equals("家庭相册")){
//					dataPhotos.setVisibility(View.VISIBLE);
//					dataChart.setVisibility(View.GONE);
//					mapView.setVisibility(View.GONE);
//				}else if(value.equals("地理位置")){
//					flag=0;
//					mapView.loadUrl(AppMain.mapIP+"/control/realtime.jsp?a=admin,admin123&n="+UtilsSharedData.getValueByKey(Constant.USER_GKMUM));
//					dataPhotos.setVisibility(View.GONE);
//					dataChart.setVisibility(View.GONE);
//					mapView.setVisibility(View.VISIBLE);
//				}else if(value.equals("报警信息")){
//					flag=1;
//					mapView.loadUrl(AppMain.mapIP+"/control/alarm.jsp?a=admin,admin123");
//					dataPhotos.setVisibility(View.GONE);
//					dataChart.setVisibility(View.GONE);
//					mapView.setVisibility(View.VISIBLE);
//				}
//				else{
//					dataPhotos.setVisibility(View.GONE);
//					dataChart.setVisibility(View.VISIBLE);
//					mapView.setVisibility(View.GONE);
//				}
//			}
//		});
//	}
//	
//
//	private void updateUI(){
//		//将选中圈重新置于我的位置上
//		myImg.setBorderColor(getResources()
//				.getColor(R.color.blue_second));
//		myImg.setBorderWidth(20);
//		name.setText("我");
//		if(flag==1){
//			
//		}else{
//			mapView.loadUrl(AppMain.mapIP+"/control/realtime.jsp?a=admin,admin123&n="+UtilsSharedData.getValueByKey(Constant.USER_GKMUM));
//		}
////		mapView.loadUrl("http://118.26.142.171:8080/control/realtime.jsp?a=admin,admin123&n="+UtilsSharedData.getValueByKey(Constant.USER_GKMUM));
//		for (int i = 0; i < 8; i++) {
//			if (i < AppMain.memberList.size()) {
//				imageLoader.displayImage(AbsParam.getBaseUrl()
//						+ AppMain.memberList.get(i).getImageUrl(),
//						imgMember[i], options);
//				imgMember[i].setOnLongClickListener(this);
//				tvMember[i].setText(StringUtil.displayName(AppMain.memberList.get(i)));
//			} else {
//				//清掉所有的照片和文字
//				imgMember[i].setImageResource(R.drawable.add_user_icon);
//				tvMember[i].setText("");
//			}
//			imgMember[i]
//					.setBorderColor(getResources().getColor(
//							R.color.whitesmoke));
//			imgMember[i].setBorderWidth(2);
//			selectPicNum = 0;
//		}
//	}
//
//	int selectPicNum = 0;//选中的成员照片编号
//	public void onClick(View v) {
//		super.onClick(v);
//		switch (v.getId()) {
//
//		case R.id.myimg:
//			if(selectPicNum == 0){
//				//直接跳转到我的地图页面
//				Intent intent = new Intent(this, ActivityMyFamilyMemberPage.class);
//				intent.putExtra(Constant.SELECT_MEMBER, selectPicNum);
//	            startActivity(intent);
//			}else{
//				selectPicNum = 0;
//				for (int i = 0; i < AppMain.memberList.size(); i++) {
//					imgMember[i].setBorderColor(getResources()
//							.getColor(R.color.whitesmoke));
//					imgMember[i].setBorderWidth(2);
//				}
//				myImg.setBorderColor(getResources()
//						.getColor(R.color.blue_second));
//				myImg.setBorderWidth(20);
//				name.setText("我");
//				if(flag==1){
//					
//				}else{
//				mapView.loadUrl(AppMain.mapIP+"/control/realtime.jsp?a=admin,admin123&n="+UtilsSharedData.getValueByKey(Constant.USER_GKMUM));
//				}
//			}
//			break;
//		case R.id.person1:
//			jumpToSearchPage(1);
//			break;
//		case R.id.person2:
//			jumpToSearchPage(2);
//			break;
//		case R.id.person3:
//			jumpToSearchPage(3);
//			break;
//		case R.id.person4:
//			jumpToSearchPage(4);
//			break;
//		case R.id.person5:
//			jumpToSearchPage(5);
//			break;
//		case R.id.person6:
//			jumpToSearchPage(6);
//			break;
//		case R.id.person7:
//			jumpToSearchPage(7);
//			break;
//		case R.id.person8:
//			jumpToSearchPage(8);
//			break;	
//		case R.id.right_tv://消息
//			Intent intent = new Intent();
//			intent.setClass(ActivityMyFamily.this, ActivityAddNews.class);
//			startActivity(intent);
//			break;
//		default:
//			break;
//		}
//
//	}
//	
//	private void jumpToSearchPage(int j){
//		if(j > AppMain.memberList.size()){
//			Intent intent = new Intent();
//			intent.putExtra(Constant.FAMILY_SELECTNUM, j);
//			intent.setClass(ActivityMyFamily.this, ActivityAddFamilyMember.class);
//			startActivity(intent);
//		}else{
//			if(selectPicNum == j){
//				//直接跳转到我的地图页面
//				Intent intent = new Intent(this, ActivityMyFamilyMemberPage.class);
//				intent.putExtra(Constant.SELECT_MEMBER, selectPicNum);
//	            startActivity(intent);
//			}else{
//				selectPicNum = j;
//				// 点击成员头像显示成员名称
//				for (int i = 0; i < AppMain.memberList.size(); i++) {
//					if (i == selectPicNum - 1) {
//						imgMember[i]
//								.setBorderColor(getResources().getColor(
//										R.color.blue_second));
//						imgMember[i].setBorderWidth(20);
//					} else {
//						imgMember[i]
//								.setBorderColor(getResources().getColor(
//										R.color.whitesmoke));
//						imgMember[i].setBorderWidth(2);
//					}
//					myImg.setBorderColor(getResources().getColor(
//							R.color.whitesmoke));
//					myImg.setBorderWidth(2);
//				}
//			}
//			
//			name.setText(StringUtil.displayName(AppMain.memberList.get(selectPicNum-1)));
//			
//		   //加载当前成员地理位置
//			if(flag==1){
//				
//			}else{
//				mapView.loadUrl(AppMain.mapIP+ "/control/realtime.jsp?a=admin,admin123&n="+AppMain.memberList.get(selectPicNum-1).getMemberGknumber());
//			}
//		
//		}
//	}
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		getFamilyMember();
//	}
//	
//	private void getFamilyMember(){
//		showProgressBar();
//		AscyncGetFamilyMember async = new AscyncGetFamilyMember(this){
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
//				updateUI();
//			}
//			
//		};
//		async.execute();
//	}
//	
//	
//	//删除家庭成员
//	private class DeleteTask extends AsyncTask<Integer, Integer, String> {
//		String result;
//
//		@Override
//		protected String doInBackground(Integer... params) {
//			HashMap<String, String> param = new HashMap<String, String>();
//			UtilsSharedData.initDataShare(ActivityMyFamily.this);// ////////
//			long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
//			param.put("userId", userId+"");
//			param.put("relationId", AppMain.memberList.get(selectPicNum-1).getRelationId()+"");
//			try {
//				String url = AbsParam.getBaseUrl()  + family + "delete";
//				result = NetTool.sendPostRequest(url, param, "utf-8");
//				Log.i("result", result);
//				JsonResult(result);
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
//			if(beanFamilyResult!=null){
//				if(beanFamilyResult.getStatusCode() == -1){
//					Toast.makeText(ActivityMyFamily.this, "删除成功", 1000).show();
//					getFamilyMember();
//				}else{
//					Toast.makeText(ActivityMyFamily.this, BeanResultUtils.getPropertyFromResult(beanFamilyResult, "msg"), 1000).show();
//				}
//			
//			}
//		}
//	}
//	/**
//	 * 解析返回来的Json数组
//	 * 
//	 * @param jsonString
//	 * @return
//	 * @throws Exception
//	 */
//	
//	private void JsonResult(String jsonString){
//		beanFamilyResult = BeanResultUtils.parseResult(jsonString);
//	}
//
//
//
//    @Override
//    public void onDestroy() {
//		super.onDestroy();
//		imageLoader.clearMemoryCache();
//		imageLoader.clearDiscCache();
//    }
//
//	@Override
//	public boolean onLongClick(View arg0) {
//		// TODO Auto-generated method stub
//		switch (arg0.getId()) {
//		case R.id.myimg:
//			break;
//		case R.id.person1:
//			selectPicNum = 1;
//			showDialog();
//			break;
//		case R.id.person2:
//			selectPicNum = 2;
//			showDialog();
//			break;
//		case R.id.person3:
//			selectPicNum = 3;
//			showDialog();
//			break;
//		case R.id.person4:
//			selectPicNum = 4;
//			showDialog();
//			break;
//		case R.id.person5:
//			selectPicNum = 5;
//			showDialog();
//			break;
//		case R.id.person6:
//			selectPicNum = 6;
//			showDialog();
//			break;
//		case R.id.person7:
//			selectPicNum = 7;
//			showDialog();
//			break;
//		case R.id.person8:
//			selectPicNum = 8;
//			showDialog();
//			break;
//		}
//		return true;
//	}
//	
//	public void showDialog() {
//		 
//		CustomDialog.Builder builder = new CustomDialog.Builder(
//				ActivityMyFamily.this);
//		builder.setTitle("提示");
//		String userName = StringUtil.displayName(AppMain.memberList.get(selectPicNum-1));
//		String content = "您确认删除  "+" "+userName+" "+"？";
//		SpannableStringBuilder style=new SpannableStringBuilder(content);             
//	    style.setSpan(new ForegroundColorSpan(Color.RED),5,userName.length()+8,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
//		builder.setMessage(style);
//		builder.setPositiveButton("确定",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog,
//							int which) {
//						showProgressBar();
//						DeleteTask deleteTask = new DeleteTask();
//						deleteTask.execute();
//						dialog.dismiss();
//
//					}
//				});
//		builder.setNegativeButton("取消",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog,
//							int which) {
//						dialog.dismiss();
//					}
//				});
//		builder.create().show();
//
//	}
//
//}
