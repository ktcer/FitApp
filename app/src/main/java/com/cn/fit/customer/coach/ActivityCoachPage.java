package com.cn.fit.customer.coach;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.coach.comment.ActivityQueryCoachMomment;
import com.cn.fit.coach.comment.AdaperQureyMomment;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanCoachPage;
import com.cn.fit.model.customer.BeanQureyMomment;
import com.cn.fit.model.nurse.BeanEassy;
import com.cn.fit.model.nurse.BeanServeTime;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.core.ArrayLists;
import com.cn.fit.ui.chat.core.ContactsCache;
import com.cn.fit.ui.chat.storage.IMessageSqlManager;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.ui.patient.main.mynurse.AdapterEassy;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.UtilsImage;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityCoachPage extends ActivityBasicListView {
    //    private ListView listView;
    private TextView summary, delete, titile, coachAddress;
    private AdapterEassy adapter;
    private List<BeanEassy> eassyLis;// BeanEassy
    private DisplayImageOptions options;
    //	private HorizontalPicAdapter mAdapter;
    private CircleImageView imageView;
    // private ImageView wchatcode;
    private String name = "";
    //	private GridViewInScrollView gridview;
//	private ArrayList<String> picList;
    private long expertId = 1;
    // 头像
    private String image = "";
    private int type;// 0代表查看教练主页，1代表添加教练时使用
    private List<BeanServeTime> listServeTime = new ArrayLists<BeanServeTime>();
    private int selectServeType;
    private BeanCoachPage beanHealthSecretaryInfo;
    //	private String secretaryDdh = "0";
    private byte payType = 1;
    //	private int mRemainTime;
    private TextView secretary_name;
    private ImageView secretary_gender;
    private TextView fansamount;
    private TextView secretary_staff;
    private TextView secretary_hosptial;
    private TextView secretary_goodat;
    private String latitude;//
    private String longitude;
    private LinearLayout llMucn;
    private List<BeanQureyMomment> tempInfoList, infoList;
    private AdaperQureyMomment listViewAdapter;
    private long classID;
//	public static String expertSession = "0";//用于将管理员发送的消息强制转化为教练发送的消息

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        enableBanner = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hearth_secretary_homeppage);
        initView();
//		getSecretaryTask();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getSecretaryTask();
        showProgressBar();
        QueryMomment task = new QueryMomment();
        task.execute();
    }

    private void initView() {
        tempInfoList = new ArrayList<BeanQureyMomment>();
        infoList = new ArrayList<BeanQureyMomment>();
        UtilsSharedData.initDataShare(this);
        eassyLis = new ArrayList<BeanEassy>();
        expertId = getIntent().getExtras().getLong("ExpertId");
        image = getIntent().getExtras().getString("image");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        classID = getIntent().getLongExtra("classID", 0);
        delete = (TextView) findViewById(R.id.right_tv);
        secretary_name = (TextView) findViewById(R.id.homepage_secretaryName);
        secretary_gender = (ImageView) findViewById(R.id.homepage_gender);
        fansamount = (TextView) findViewById(R.id.homepage_mountOfFans);
        secretary_staff = (TextView) findViewById(R.id.homepage_secretarystaff);
        secretary_hosptial = (TextView) findViewById(R.id.homepage_hospital);
        secretary_goodat = (TextView) findViewById(R.id.homepage_secretary_goodat);
        coachAddress = (TextView) findViewById(R.id.address_coach_text);
        llMucn = (LinearLayout) findViewById(R.id.ll_much);
        llMucn.setOnClickListener(this);
        if (getIntent().getExtras().getString("pageType") != null) {
            type = 1;
            delete.setText("添加");
        } else {
            type = 0;
            delete.setText("删除");
        }
        titile = (TextView) findViewById(R.id.middle_tv);
        delete.setOnClickListener(this);
//		delete.setVisibility(View.VISIBLE);//等到请求到护士信息的时候再判断是否显示，如果请求不到，则不显示，如果请求到，则显示
//		listView = (ListView) findViewById(R.id.listview_secretary_essay);
        summary = (TextView) findViewById(R.id.et_summary);
        imageView = (CircleImageView) findViewById(R.id.secretary_head);
        imageView.setOnClickListener(this);

        listViewAdapter = new AdaperQureyMomment(this, infoList);
        listView = (XListView) this.findViewById(R.id.comment_list);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listView.setXListViewListener(this);
        listView.setAdapter(listViewAdapter);
//		gridview = (GridViewInScrollView) findViewById(R.id.gridView_Picture);
//		picList = new ArrayList<String>();
        // TODO
        // 添加几张默认照片
//		for (int i = 0; i < 6; i++) {
//			picList.add("");
//		}
//		mAdapter = new HorizontalPicAdapter(this, picList);
//		gridview.setAdapter(mAdapter);
//		gridview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				// getSecretaryTask(nurseInfoList.get(arg2).getNurseID());
//				UtilsImage utilsImage = new UtilsImage();
//				utilsImage.displayBigPics(ActivityCoachPage.this,picList, arg2);
//			}
//
//		});
        // wchatcode = (ImageView) findViewById(R.id.wchat_code);
        adapter = new AdapterEassy(this, eassyLis);
        options = AppMain.initImageOptions(R.drawable.default_user_icon, true);// 构建完成
        ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + image,
                imageView, options);// 头像
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_much:
                intent = new Intent(this, ActivityQueryCoachMomment.class);
                intent.putExtra("coachID", expertId);
                intent.putExtra("classID", classID);
                startActivity(intent);
                break;
            case R.id.right_tv:
                if (type == 0) {
                    // 删除
                    deletSshowAlertDialog("确定删除教练吗");
                } else if (type == 1) {
                    showDialog();
                }

                break;
            case R.id.secretary_head:
                UtilsImage.displayBigPic(ActivityCoachPage.this, image);
                break;
            default:
                break;
        }
    }

    private void deletSshowAlertDialog(String title) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (type == 0) {
                    // 删除
                    dialog.dismiss();
                    AscyncDeleteExpert mTask = new AscyncDeleteExpert();
                    mTask.execute();

                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void getSecretaryTask() {
        AscynsCoachPage ascynsCoachPage = new AscynsCoachPage(this, expertId, latitude, longitude) {
            @Override
            protected void onPostExecute(BeanCoachPage beanCoachPage) {
                super.onPostExecute(beanCoachPage);
                if (beanCoachPage != null) {
                    delete.setVisibility(View.VISIBLE);
                    beanHealthSecretaryInfo = beanCoachPage;
                    fillTheView();
                } else {
                    delete.setVisibility(View.GONE);
                    summary.setText(R.string.network_error);
                }


            }
        };
        ascynsCoachPage.execute();

    }

    @SuppressLint("ResourceAsColor")
    private void fillTheView() {
        eassyLis.clear();
        String picLife = "";
        titile.setText(R.string.title_homepage);
        name = beanHealthSecretaryInfo.getName();
        if (beanHealthSecretaryInfo.getName() != null) {
            secretary_name.setText(name);
        } else {

        }
        if (beanHealthSecretaryInfo.getResume() != null) {
            summary.setText(beanHealthSecretaryInfo.getResume());
        }
        if (beanHealthSecretaryInfo.getSex() != null) {
            if (beanHealthSecretaryInfo.getSex().equals("男")) {
                secretary_gender.setBackgroundResource(R.drawable.iconfont_nan);
            } else if (beanHealthSecretaryInfo.getSex().equals("女")) {
                secretary_gender.setBackgroundResource(R.drawable.iconfont_nv);
                ;
            }
        } else {
            secretary_gender.setVisibility(View.INVISIBLE);
        }

        fansamount.setText(beanHealthSecretaryInfo.getMembers() + "");

        coachAddress.setText(beanHealthSecretaryInfo.getAddress() + "距离您" + beanHealthSecretaryInfo.getDistance() + "m");
        if (beanHealthSecretaryInfo.getCoachProgramType() != null) {
            secretary_staff.setText(beanHealthSecretaryInfo.getCoachProgramType());
        } else {
            secretary_staff.setText("暂无擅长");
        }
        if (beanHealthSecretaryInfo.getTeachTime() != null) {
            secretary_hosptial.setText(beanHealthSecretaryInfo.getTeachTime());
        } else {
            secretary_hosptial.setText("暂无执教时间");
        }

        if (beanHealthSecretaryInfo.getRewards() != null) {
            secretary_goodat.setText(beanHealthSecretaryInfo.getRewards());
        } else {
            secretary_goodat.setText("");
        }
        // 文章
//		if (beanHealthSecretaryInfo.getEssayList() != null) {
//			for (BeanEassy ea : beanHealthSecretaryInfo.getEssayList()) {
//				eassyLis.add(ea);
//			}
//		}
//		listView.setAdapter(adapter);
        // fixListViewHeight(listView);

//		String[] a = {};
//		if (picLife.length() != 0) {
//			a = picLife.split("\\&");
//		}
//		for (int i = 0; i < 6; i++) {
//			if (i < a.length) {
//				picList.set(i, a[i]);
//			} else {
//				picList.set(i, "");
//			}
//		}
//		mAdapter.notifyDataSetChanged();
    }


    private class AscyncAddCoach extends
            AsyncTask<Integer, Integer, String> {

        private String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
//			expertSession = expertId+"1";
            String retString = "";
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userID", UtilsSharedData.getLong(Constant.USER_ID, 0)
                    + "");
            param.put("coachID", expertId + "");

            try {
                retString = NetTool.sendPostRequest(
                        AbsParam.getBaseUrl() + "/coach/app/follownew",
                        param, "utf-8");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                hideProgressBar();
            }
            return retString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int operate = 0;
            String detail = "";
            if (result == null) {
                return;
            }
            try {
                JSONObject json = new JSONObject(result);
                operate = json.getInt("result");
                detail = json.getString("detail");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            hideProgressBar();
            if (operate == 1) {
                String old = TabActivityMain.class.getName();
                backTo(old);
                Intent intent = new Intent(ActivityCoachPage.this,
                        ChattingActivity.class);
                intent.putExtra(Constant.AUTO_SENDMASSAGE, true);
                intent.putExtra(ChattingActivity.RECIPIENTS, expertId
                        + "1");
                ECContacts tempECContacts = null;
                for (int i = 0; i < ContactsCache.getInstance()
                        .getContacts().size(); i++) {
                    if (ContactsCache.getInstance().getContacts()
                            .get(i).getContactid()
                            .equals(expertId + "1")) {
                        tempECContacts = ContactsCache.getInstance()
                                .getContacts().get(i);
                    }
                }
                if (tempECContacts != null) {
                    intent.putExtra(ChattingActivity.CONTACT_USER,
                            tempECContacts.getNickname());
                    intent.putExtra(ChattingActivity.CONTACT_IMGURL,
                            tempECContacts.getImgUrl());
                    intent.putExtra(ChattingActivity.CONTACT_REMAINTIME,
                            7);
                } else {
                    intent.putExtra(ChattingActivity.CONTACT_USER, name);
                    intent.putExtra(ChattingActivity.CONTACT_IMGURL,
                            image);
                    intent.putExtra(ChattingActivity.CONTACT_REMAINTIME,
                            7);

                }
                startActivity(intent);
            } else {
                ToastUtil.showMessage("添加失败，请重试！");
            }
        }


    }

    /**
     * 删除教练
     */
    private class AscyncDeleteExpert extends
            AsyncTask<Integer, Integer, String> {

        private String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            String retString = "";
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userID", UtilsSharedData.getLong(Constant.USER_ID, 0)
                    + "");
            param.put("coachID", expertId + "");

            try {
                retString = NetTool.sendPostRequest(
                        AbsParam.getBaseUrl() + "/coach/app/unfollow",
                        param, "utf-8");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                hideProgressBar();
            }
            return retString;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            int operate = 0;
            String detail = "";
            if (result == null) {
                return;
            }
            try {
                JSONObject json = new JSONObject(result);
                operate = json.getInt("result");
                detail = json.getString("detail");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (operate == 0) {
                ToastUtil.showMessage("删除成功");
                //同时清除与该教练的聊天记录
                cleanChatHistory();
            } else {
                ToastUtil.showMessage("删除失败，请重试！");
                hideProgressBar();
            }
        }


        private void cleanChatHistory() {
            ECHandlerHelper handlerHelper = new ECHandlerHelper();
            handlerHelper.postRunnOnThead(new Runnable() {
                @Override
                public void run() {
                    IMessageSqlManager.deleteChattingMessage(expertId + "1");
                    ToastUtil.showMessage(R.string.clear_msg_success);
                    ActivityCoachPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressBar();
                            String old = TabActivityMain.class.getName();
                            backTo(old);
                        }
                    });
                }
            });
        }

    }

    /*
     *
     */
    public void showDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        String content = "";
        content = "您确认添加 " + name + "教练提供的 " + "服务？";

        builder.setMessage(content);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showProgressBar();
                payType = 1;
                AscyncAddCoach asc = new AscyncAddCoach();
                asc.execute();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                payType = 0;
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private class QueryMomment extends AsyncTask<Integer, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("coachID", expertId + "");
            param.put("state", "" + 2);//
            param.put("pageSize", 3 + "");
            param.put("pageNum", 1 + "");
            try {
                String url = AbsParam.getBaseUrl() + "/home/app/getcomments";
                Log.i("result", url + param.toString());
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                jsonToArray(result);
                if (tempInfoList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = false;
                }
            } catch (Exception e) {
                canLoadMore = false;
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            for (BeanQureyMomment tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(false);
            } else {
                listView.setPullLoadEnable(false);
            }

            listViewAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
    }


    private void jsonToArray(String json) {
        Gson gson = new Gson();
        tempInfoList = gson.fromJson(json, new TypeToken<List<BeanQureyMomment>>() {
        }.getType());
    }

}
