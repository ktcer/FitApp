package com.cn.fit.ui.patient.main.healthdiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.others.myorders.FragmentOrdersList;
import com.cn.fit.util.pagertable.PagerSlidingTabStrip;

public class ActivityRecoveryRemind extends ActivityBasic {
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    //	private RecorderAndPlaybackInterface audioRecorderAndPlaybackInterface;
//	private ImageView addImage;
//	private TextView submitbtn;
//    private ListView listView;
//    private static String audioDirector;
//    private static String audioName;
//	private List<String> contentString = new ArrayList<String>();
//	private ArrayAdapter<String> adapter;
    private MyPagerAdapter adapterremind;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_res_add_recovery_remind);
        inital();
    }

    private void inital() {
        // TODO Auto-generated method stub
//		audioDirector = AppDisk.appInursePath+UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)+File.separator+AppDisk.DCIM_RECORD;
//		audioName = "new.wav";
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_remind);
        pager = (ViewPager) findViewById(R.id.pager_remind);
        adapterremind = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapterremind);

        tabs.setIndicatorColor(getResources().getColor(R.color.blue_second));
        tabs.setViewPager(pager);
//		addImage = (ImageView) findViewById(R.id.new_remind);
//		addImage.setVisibility(View.GONE);
//		submitbtn = (TextView) findViewById(R.id.right_tv);
//		submitbtn.setText("选择");
//		submitbtn.setVisibility(View.VISIBLE);
//		submitbtn.setOnClickListener(this);
        TextView titile = (TextView) findViewById(R.id.middle_tv);
        titile.setText("专家提醒");
//		addImage.setOnClickListener(this);
//        audioRecorderAndPlaybackInterface = new RecorderAndPlaybackMediaRecorderImpl(
//                getApplicationContext());
    }

//	public void showMessageDialog(final int index) {
//		 
//		CustomDialog.Builder builder = new CustomDialog.Builder(
//				ActivityRecoveryRemind.this);
//		builder.setTitle("提示");
//		String content = "您确认删除  "+" "+contentString.get(index)+" "+"？";
//		SpannableStringBuilder style=new SpannableStringBuilder(content);             
//	    style.setSpan(new ForegroundColorSpan(Color.RED),5,contentString.get(index).length()+8,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
//		builder.setMessage(style);
//		builder.setPositiveButton("确定",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog,
//							int which) {
//						File file = new File(audioDirector+contentString.get(index));
//						file.delete();
//						setAdapter();
//						listView.invalidate();
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
//	public void showRecordOptionsDialog() {
//		 
//		CustomDialog.Builder builder = new CustomDialog.Builder(
//				ActivityRecoveryRemind.this);
//		builder.setTitle("提示");
//		String content = "请您选择您需要录制提醒的方式"; 
//		builder.setMessage(content);
//		builder.setPositiveButton("语音",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog,
//							int which) {
//						startActivity(ActivityRecordAudio.class);
//						dialog.dismiss();
//					}
//				});
//		builder.setNegativeButton("视频",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog,
//							int which) {
//						startActivity(ActivityVideoRecord.class);
//						dialog.dismiss();
//					}
//				});
//		builder.create().show();
//
//	}
//
//	public void onClick(View v) {
//		super.onClick(v);
//		switch (v.getId()) {
//		case R.id.right_tv:
//			Intent intent = new Intent(this, ActivitySetNotificationTime.class);
//			startActivity(intent);
//			break;
//		case R.id.new_remind:
////			showRecordOptionsDialog();
//			break;
//		default:
//			break;
//		}
//	}

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		setAdapter();
    }

//	private void setAdapter(){
//		contentString = CreateFolder.getAllFileNameInFolder(audioDirector);	
//		if(contentString==null){
//			return;
//		}
//		if(contentString.size()==0){
//			submitbtn.setVisibility(View.INVISIBLE);
//		}else{
//			submitbtn.setVisibility(View.VISIBLE);
//		}
//		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, 
//				contentString) {
//
//			@Override 
//			public View getView(int position, View convertView, ViewGroup parent) 
//			{ 
//				CheckedTextView textView = (CheckedTextView) super.getView(position, convertView, parent); 
//				int textColor = R.color.black; 
//				if(position == 0){
//					textColor = R.color.blue_second;
//				}
//				textView.setTextColor(ActivityRecoveryRemind.this.getResources().getColor(textColor)); 
//				textView.setTextSize(16);
//				return textView; 
//			} 
//			
//		};
//		listView = (ListView)findViewById(R.id.remind_list);
//		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				showMessageDialog(arg2);
//				return true;
//			}
//			
//		});
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View view,
//					int position, long itemId) {
//
//				CheckedTextView textView = (CheckedTextView) view;
//				for (int i = 0; i < listView.getCount(); i++) {
//					textView = (CheckedTextView) listView.getChildAt(i);
//					if (textView != null) {
//						textView.setTextColor(getResources().getColor(R.color.black));
//						textView.setTextSize(16);
//					}
//
//				}
//				listView.invalidate();
//				textView = (CheckedTextView) view;
//				if (textView != null) {
//					textView.setTextColor(getResources().getColor(R.color.blue_second));
//				}
//
//				audioRecorderAndPlaybackInterface.startPlayback(audioDirector+contentString.get(position));
//
//			}
//		});
//		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);  
//		listView.setItemChecked(0, true); 
//		listView.setAdapter(adapter);
//	}

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"  全部     ", "  待支付    ", "  已付款    ", "  退款    "};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentOrdersList.newInstance(position);
        }

    }

}