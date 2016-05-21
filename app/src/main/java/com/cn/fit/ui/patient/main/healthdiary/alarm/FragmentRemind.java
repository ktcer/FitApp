package com.cn.fit.ui.patient.main.healthdiary.alarm;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.alarm.BeanDeletRemind;
import com.cn.fit.model.alarm.BeanRemindInfo;
import com.cn.fit.ui.basic.FragmentBasicListView;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscynDeletRemind;
import com.cn.fit.ui.record.audio.RecorderAndPlaybackInterface;
import com.cn.fit.ui.record.audio.RecorderAndPlaybackMediaRecorderImpl;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.AppDisk;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CreateFolder;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentRemind extends FragmentBasicListView {

    private static final String ARG_POSITION = "position";
    private List<BeanRemindInfo> infoList, tempInfoList;
    private int type;
    private RecorderAndPlaybackInterface audioRecorderAndPlaybackInterface;
    // private ImageView addImage;
    // private TextView submitbtn;
    private static String audioDirector;
    private static String audioName;
    private List<String> name = new ArrayList<String>();
    private List<String> contentString = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    //
    // public static FragmentRemind newInstance(int position) {
    // FragmentRemind f = new FragmentRemind();
    // Bundle b = new Bundle();
    // b.putInt(ARG_POSITION, position);
    // f.setArguments(b);
    // return f;
    // }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);
        inital();
        // final int margin = (int)
        // TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
        // getResources()
        // .getDisplayMetrics());
        // orderListViewAdapter = new OrdersListAdapter(getActivity());
        listView = new XListView(getActivity());
        // params.setMargins(margin, margin, margin, margin);
        params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);
        listView.setCacheColorHint(R.color.black);
        listView.setBackgroundResource(R.color.white);
        listView.setDividerHeight(1);
        listView.setDivider(getResources().getDrawable(R.color.lightgray));
        listView.setSelector(R.color.transparent);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        // listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        // {
        //
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1,
        // int position, long arg3) {
        // // Intent intent =new
        // Intent(getActivity(),ActivityMyOrderInfo.class);
        // // Bundle mBundle = new Bundle();
        // // mBundle.putString(Constant.ORDER_NO, infoList.get(position -
        // 1).getDdh());
        // // mBundle.putByte(Constant.ORDER_STATUS, infoList.get(position -
        // 1).getState());
        // // mBundle.putFloat(Constant.ORDER_PRICE, infoList.get(position -
        // 1).getMoney());
        // // intent.putExtras(mBundle);
        // // startActivity(intent);
        // }
        // });
        setAdapter();
        showProgressBar();
        QueryMyRemindTask task = new QueryMyRemindTask();
        task.execute(type);
        // infoList = new ArrayList<BeanHealthPostOrder>();
        // tempInfoList = new ArrayList<BeanHealthPostOrder>();
        // listView.setAdapter(orderListViewAdapter);
        fl.addView(listView);
        return fl;
    }

    private void inital() {
        // TODO Auto-generated method stub

        infoList = new ArrayList<BeanRemindInfo>();
        tempInfoList = new ArrayList<BeanRemindInfo>();
        if (type == 1) {
            audioDirector = AppDisk.appInursePath
                    + UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
                    + File.separator + "PATIENT" + File.separator
                    + AppDisk.DCIM_RECORD;
            audioName = "new.wav";

            File sampleDir = new File(audioDirector);
            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }

            contentString = CreateFolder.getAllFileNameInFolder(audioDirector);
            audioRecorderAndPlaybackInterface = new RecorderAndPlaybackMediaRecorderImpl(
                    getActivity());
        }
        if (type == 2) {
            audioDirector = AppDisk.appInursePath
                    + UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
                    + File.separator + "PATIENT" + File.separator
                    + AppDisk.DCIM_VIDEO;
            File sampleDir = new File(audioDirector);
            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }
        }

    }

    private void setAdapter() {
        // contentString = CreateFolder.getAllFileNameInFolder(audioDirector);
        if (name == null) {
            return;
        }
        if (name.size() == 0) {
            // submitbtn.setVisibility(View.INVISIBLE);
        } else {
            // submitbtn.setVisibility(View.VISIBLE);
        }
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_single_choice, name) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CheckedTextView textView = (CheckedTextView) super.getView(
                        position, convertView, parent);
                int textColor = R.color.black;
                if (position == 0) {
                    textColor = R.color.blue_second;
                }
                textView.setTextColor(getActivity().getResources().getColor(
                        textColor));
                textView.setTextSize(16);
                return textView;
            }

        };
        // listView = (ListView)findViewById(R.id.remind_list);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                // TODO Auto-generated method stub
                if (!(type == 0)) {
                    showMessageDialog(infoList.get(position - 1).getFileUrl(),
                            position - 1);
                }
                return true;
            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long itemId) {

                CheckedTextView textView = (CheckedTextView) view;
                for (int i = 1; i < listView.getCount() - 1; i++) {
                    textView = (CheckedTextView) listView.getChildAt(i);
                    if (textView != null) {
                        textView.setTextColor(getResources().getColor(
                                R.color.black));
                        textView.setTextSize(16);
                    }

                }
                listView.invalidate();
                textView = (CheckedTextView) view;
                if (textView != null) {
                    textView.setTextColor(getResources().getColor(
                            R.color.blue_second));
                }
                if (!(type == 0)) {
                    showProgressBar();
                    DownLoad downLoadTask = new DownLoad(infoList.get(
                            position - 1).getFileUrl(), position - 1);
                    downLoadTask.execute();
                }
                // audioRecorderAndPlaybackInterface.startPlayback(audioDirector+contentString.get(position-1));

            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(0, true);
        listView.setAdapter(adapter);
    }

    public void showRecordOptionsDialog() {

        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setTitle("提示");
        String content = "请您选择您需要录制提醒的方式";
        builder.setMessage(content);
        builder.setPositiveButton("语音", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // startActivity(RecordAudioActivity.class);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("视频", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // startActivity(VideoRecordActivity.class);
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private String fileName1 = "";

    public void showMessageDialog(String fileurl, final int index1) {
        String[] a = fileurl.split("\\/");
        fileName1 = a[a.length - 1];

        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setTitle("提示");
        String content = "您确认删除  " + " " + fileName1 + " " + "？";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(Color.RED), 5,
                fileName1.length() + 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setMessage(style);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AscynDeletRemind adrTask = new AscynDeletRemind(getActivity(),
                        infoList.get(index1).getRemindID()) {

                    @Override
                    protected void onPostExecute(BeanDeletRemind result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        if (result.getResult() == 1) {
                            File file = new File(audioDirector + fileName1);
                            file.delete();
//							contentString.remove(index1);
                            adapter.notifyDataSetChanged();
//							setAdapter();
//							listView.invalidate();
                        } else {
                            ToastUtil.showMessage(result.getDetail());
                        }

                    }

                };
                adrTask.execute();

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

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // setAdapter();
        // QueryMyRemindTask task = new QueryMyRemindTask();
        // task.execute(type);
    }

    /*
     * 13.2查询我的提醒列表
     */
    private class QueryMyRemindTask extends AsyncTask<Integer, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            name.clear();
            HashMap<String, String> param = new HashMap<String, String>();
            UtilsSharedData.initDataShare(getActivity());
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("patientID", userId + "");
            param.put("type", "" + params[0]);//
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                String url = AbsParam.getBaseUrl() + "/family/remind/list";
                Log.i("result", url + param.toString());
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                jsonToArray(result);
                if (tempInfoList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
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
            for (BeanRemindInfo tmp : tempInfoList) {
                infoList.add(tmp);
                if (type == 0) {
                    name.add(tmp.getRemindPersonName() + "提醒：" + tmp.getText());
                }
                if (type == 1) {
                    name.add(tmp.getRemindPersonName() + "语音提醒");
                }
                if (type == 2) {
                    name.add(tmp.getRemindPersonName() + "视频提醒");
                }
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }
            adapter.notifyDataSetChanged();

            // orderListViewAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
    }

    private void jsonToArray(String json) {
        Gson gson = new Gson();
        tempInfoList = gson.fromJson(json,
                new TypeToken<List<BeanRemindInfo>>() {
                }.getType());
    }

    private class DownLoad extends AsyncTask<Integer, Integer, String> {
        private String Url = "";
        private int position;
        private String FileName = "";
        /**
         * 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
         */
        private int resultInt = 0;

        public DownLoad(String url, int position) {
            super();
            this.Url = url;
            this.position = position;
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                String[] a = Url.split("\\/");
                FileName = a[a.length - 1];
                String url = AbsParam.getBaseUrl() + Url;
                Log.i("result", url);
                resultInt = NetTool.downloadFile1(url, audioDirector, FileName);
                Log.i("result", resultInt + "");
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!(resultInt == -1)) {
                if (type == 1) {
                    audioRecorderAndPlaybackInterface
                            .startPlayback(audioDirector + FileName);
                }

            }
            hideProgressBar();
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        showProgressBar();
        QueryMyRemindTask task = new QueryMyRemindTask();
        task.execute(type);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            showProgressBar();
            QueryMyRemindTask task = new QueryMyRemindTask();
            task.execute(type);
        }
    }

}