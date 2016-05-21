//package com.cn.aihu.service;
//
//import java.util.List;
//
//import android.app.ActivityManager;
//import android.app.ActivityManager.RunningTaskInfo;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.cn.aihu.ui.AppMain;
//
//public class ServiceNotification extends Service {
////	private WakeLock mWakeLock;
//	/**0：语音1：视频2:文字*/
////	private int flag;
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		//判断应用是否在运行 
//		ActivityManager am = (ActivityManager) ServiceNotification.this
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		List<RunningTaskInfo> list = am.getRunningTasks(100);
//		String MY_PKG_NAME = "com.cn.aihu";
//		for (RunningTaskInfo info : list) {
//			if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
//					|| info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
//				AppMain.isAppRunning = true;
//				break;
//			}
//		}
//		Log.d("FROM SERVICE" , "FROM SERVICE");
//	}

//	/**
//	 * @author kuangtiecheng
//	 * 判别类型
//	 *
//	 */
//	public int getRemindType(BeanHealthDiaryLocal bean){
//		int remindType = -1;
//		if(bean!=null){
//			String path = bean.getPath();
//			if (path != null && path.endsWith(".mp4")) {
//				remindType = 1; 
//			}else if(path != null && path.endsWith(".wav")){				
//				remindType = 0; 
//			}else if(path == null||path.length()==0){
//				remindType = 2; 				
//			}			
//		}
//		return remindType;
//	}
//	
//	/**
//	 * 按id查询响铃的闹钟,确定显示的内容
//	 * 
//	 * */
//	public BeanHealthDiaryLocal getDiaryBean(String id){			
//		BeanHealthDiaryLocal beanHealthDiaryLocal = new BeanHealthDiaryLocal();
//		beanHealthDiaryLocal.setUserid((int) UtilsSharedData.getLong(
//				Constant.USER_ID, 0));
//		beanHealthDiaryLocal.setDiaryid(Integer.valueOf(id));
//		beanHealthDiaryLocal.setValid(DataBaseHelper.VALID_FLAG); 
//		
//		Log.e("=-=-=beanHealthDiaryLocal =-=-=",""+beanHealthDiaryLocal);
//		ArrayList<BeanHealthDiaryLocal> list = DataBaseHelper.onQuery(
//				this, beanHealthDiaryLocal);
//		Log.e("=-=-=list getDiaryBean()=-=-=", ""+list.size());
//		if(list.size()>0){
//			Log.e("=-=-=list.get(list.size()-1) =-=-=",""+list.get(list.size()-1));
//			return list.get(list.size()-1);
//		}
//		return beanHealthDiaryLocal;		
//	}

//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		long id = intent.getLongExtra(AlarmManagerReceiver.ID, -1);
//		Log.e("=-=-=id=-=-=", ""+id);
//		int timeHour = intent.getIntExtra(AlarmManagerReceiver.TIME_HOUR, -1);
//		int timeMinute = intent.getIntExtra(AlarmManagerReceiver.TIME_MINUTE, -1);
//		String time = timeFormat(timeHour,timeMinute);
//		Log.e("=-=-=time=-=-=", ""+time);
//		Log.d("FROM SERVICE" , "FROM SERVICE");
//		BeanHealthDiaryLocal beanHealthDiaryLocal = getDiaryBean(""+id);
//		flag = getRemindType(beanHealthDiaryLocal);
////		UtilsSharedData.initDataShare(this);
////		flag = UtilsSharedData.getLong("chooseFalg", 0);
//		switch (flag) {
//		case 1://视频
////			String url=AppDisk.appInursePath+UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)+File.separator+AppDisk.DCIM_VIDEO+"1.mp4";
////			String url=AppDisk.appInursePath+UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)+File.separator+AppDisk.DCIM_VIDEO+"视频提醒.mp4";
////			this.startActivity(AlarmScreen.class,Constant.VEDIO_URL,url);
//			this.startActivity(AlarmScreen.class,Constant.ALARM_TIME,""+id);
//			AlarmManagerReceiver.setAlarms(this);
//			stopSelf();
//			break;
//		case 0://语音
////			this.startActivity(AlarmScreenVoice.class,Constant.ALARM_TIME,time);
//			this.startActivity(AlarmScreenVoice.class,Constant.ALARM_TIME,""+id);
//			AlarmManagerReceiver.setAlarms(this);
//			stopSelf();
//			break;
//		case 2://文字
////			this.startActivity(AlarmScreenText.class,Constant.ALARM_TIME,time);
//			this.startActivity(AlarmScreenText.class,Constant.ALARM_TIME,""+id);
//			AlarmManagerReceiver.setAlarms(this);
//			stopSelf();
//			break;
//
//		default:
//			break;
//		}
//	
//		return super.onStartCommand(intent, flags, startId);
//		
//	}
//	
//	protected void startActivity(Class<?> cls, String key, String value) {
//		Intent alarmIntent = new Intent(getBaseContext(), cls);
//		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		alarmIntent.putExtra(key, value);
//		getApplication().startActivity(alarmIntent);
//	}
//	protected void startActivity(Class<?> cls) {
//		Intent alarmIntent = new Intent(getBaseContext(), cls);
//		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		getApplication().startActivity(alarmIntent);
//	}

//	/**
//	 * @author kuangtiecheng
//	 * 格式化输出时间："HH:mm"
//	 */
//	public String timeFormat(int hour, int minute){
//		StringBuilder builder = new StringBuilder();
//		if (hour < 10) {
//			builder.append("0").append(hour);
//			builder.append(":");
//			if (minute < 10)
//				builder.append("0").append(minute);
//			else
//				builder.append(minute);
//		} else {
//			builder.append(hour).append(":");
//			if (minute < 10)
//				builder.append("0").append(minute);
//			else
//				builder.append(minute);
//		}
//		return builder.toString();
//	}	

//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		Log.d("FROM SERVICE" , "Service Destroyed");
//	}
//
//}
