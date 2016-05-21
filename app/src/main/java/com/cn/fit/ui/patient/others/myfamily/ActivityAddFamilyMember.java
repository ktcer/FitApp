/**
 * @version: 2015-4-6 下午4:58:49
 * @author kuangtiecheng
 * 我家庭成员的列表，点击进去是成员的健康详情,用的是listview
 */

package com.cn.fit.ui.patient.others.myfamily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.family.BeanFamilyResult;
import com.cn.fit.model.family.BeanMemberSearchInfo;
import com.cn.fit.model.family.BeanResultUtils;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityAddFamilyMember extends ActivityBasicListView {
    private static String family = "/family/member/";
    private List<BeanMemberSearchInfo> memberList, tempMemberList;
    private MemberListAdapter memberListAdapter;
    private EditText searchEdit;
    private String phoneNum = "";
    private String remarkName = "";
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private int selectColume;
    private BeanFamilyResult beanFamilyResult;
    private int selectNum = 0;
    protected int pageNum = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_res_add_familymember);
        initView();
    }

    private void initView() {
        selectNum = getIntent().getIntExtra(Constant.FAMILY_SELECTNUM, 1);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        searchEdit
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            // 校验输入的手机号长度是否正确;
                            if (searchEdit.length() != 11) {
                                Toast.makeText(ActivityAddFamilyMember.this, "用户名长度不对", 1000).show();
                                searchEdit.setText("");
                            } else {
                                //开始搜索
                                phoneNum = searchEdit.getText().toString();
                                showProgressBar();
                                QueryMemberTask memberTask = new QueryMemberTask();
                                memberTask.execute();
                            }
                            return true;
                        }
                        return false;
                    }
                });
        memberListAdapter = new MemberListAdapter(this);
        memberList = new ArrayList<BeanMemberSearchInfo>();
        tempMemberList = new ArrayList<BeanMemberSearchInfo>();
        listView = (XListView) findViewById(R.id.member_list);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                selectColume = arg2;
                showDialog();
            }
        });
        listView.setAdapter(memberListAdapter);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_user_icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_user_icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_user_icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                        //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                        //设置图片加入缓存前，对bitmap进行设置
                        //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        imageLoader.clearMemoryCache();
        imageLoader.clearDiscCache();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    private class MemberListAdapter extends BaseAdapter {
        private Context context;

        public MemberListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return memberList.size();
        }

        @Override
        public Object getItem(int position) {
            return memberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.list_item_circle, null);
                holder.memberPhoneTV = (TextView) convertView
                        .findViewById(R.id.list_item_title);
                holder.memberAvatarIV = (CircleImageView) convertView
                        .findViewById(R.id.list_item_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//			holder.memberAvatarIV.setBackgroundResource(R.drawable.user_icon);
            imageLoader.displayImage(AbsParam.getBaseUrl() + memberList.get(position).getImageUrl(),
                    holder.memberAvatarIV, options);
            holder.memberPhoneTV.setText(memberList.get(position)
                    .getPhone());
            return convertView;
        }
    }

    private class ViewHolder {
        CircleImageView memberAvatarIV;
        TextView memberPhoneTV;
    }

    //添加家庭成员
    private class AddTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            UtilsSharedData.initDataShare(ActivityAddFamilyMember.this);// ////////
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("userId", userId + "");
            param.put("memberId", memberList.get(selectColume - 1).getUserId() + "");
            param.put("relationName", remarkName);
            param.put("index", selectNum + "");
            try {
                String url = AbsParam.getBaseUrl() + family + "add";
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                JsonResult(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            if (beanFamilyResult != null) {
                if (beanFamilyResult.getStatusCode() == -1) {
                    Toast.makeText(ActivityAddFamilyMember.this, "添加成功", 1000).show();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            finish();
                        }
                    }, 1000);

                } else {
                    Toast.makeText(ActivityAddFamilyMember.this, BeanResultUtils.getPropertyFromResult(beanFamilyResult, "msg"), 1000).show();
                }

            }
        }
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */

    private void JsonResult(String jsonString) {
//		Gson gson = new Gson(); 
//		beanFamilyResult = gson.fromJson(jsonString, BeanFamilyResult.class); 
        beanFamilyResult = BeanResultUtils.parseResult(jsonString);
    }

    private class QueryMemberTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            UtilsSharedData.initDataShare(ActivityAddFamilyMember.this);// ////////
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("userId", userId + "");
            param.put("phone", phoneNum);
            Log.i("input", AbsParam.getBaseUrl()
                    + family + "search" + param.toString());
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + family + "search", param, "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (tempMemberList == null) {
                return;
            }
            if (pageNum == 1) {
                memberList.clear();
            }

            for (BeanMemberSearchInfo tmp : tempMemberList) {
                memberList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }
            memberListAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @SuppressLint("ResourceAsColor")
    public void showDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setHint("请输入与本人关系添加家庭成员（例如：爸爸）");
        inputServer.setMinLines(8);
        inputServer.setHintTextColor(R.color.font_gray);
        inputServer.setTextColor(Color.BLACK);
        inputServer.setGravity(Gravity.TOP);
        inputServer.setSingleLine(false);
        inputServer.setHorizontalScrollBarEnabled(false);
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setContentView(inputServer);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                remarkName = inputServer.getText().toString();
                showProgressBar();
                AddTask addTask = new AddTask();
                addTask.execute();
                dialog.dismiss();
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

    private void JsonArrayToList(String result) {
        tempMemberList.clear();
        Gson gson = new Gson();
        BeanMemberSearchInfo bean = gson.fromJson(result, BeanMemberSearchInfo.class);
        if (bean == null) {
            canLoadMore = false;
            return;
        }
        tempMemberList.add(bean);//new TypeToken<List<BeanFamilyMemberInfo>>(){}.getType());
        if (tempMemberList != null) {
            if (tempMemberList.size() > 0) {
                if (tempMemberList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            }
        }

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        pageNum = 1;
        QueryMemberTask memberTask = new QueryMemberTask();
        memberTask.execute();
    }

    QueryMemberTask memberTask;

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        super.onLoadMore();
        if (canLoadMore) {
            if (memberTask != null && memberTask.getStatus() == AsyncTask.Status.RUNNING) {
                memberTask.cancel(true);  //  如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }
            memberTask = new QueryMemberTask();
            memberTask.execute();
        }

    }

}
