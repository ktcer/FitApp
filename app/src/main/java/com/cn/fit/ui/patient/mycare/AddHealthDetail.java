package com.cn.fit.ui.patient.mycare;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthrecord.BeanAddHealthDetialBeen;
import com.cn.fit.model.healthrecord.BeanValueBeen;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.DateTimePickerDialog;
import com.cn.fit.util.PickerView;
import com.cn.fit.util.PickerView.onSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddHealthDetail extends ActivityBasic {
    private LinearLayout addMyText;
    private PickerView minute_pv;
    private LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    private TextView myTextView, selectTextTime, myTextView1, rightTV;
    private EditText selectTextData;
    private EditText myEditText;
    private String healthStateID;
    private List<BeanAddHealthDetialBeen> tempHealthList;
    private List<BeanValueBeen> listValuBeen;
    public static BeanAddHealthDetialBeen addHealthDetialBeen;
    private String[] id;// = new String[] { "我的设备", "驿站设备" };
    private List<String> addValue;
    private final static int DATE_DIALOG = 0;
    private final static int TIME_DIALOG = 1;

    private long keyid, keyidBt;
    private String editTextConteng, btonConteng;
    private String data;
    private String[] con;
    private long[] keyId;
    private long[] valuid;
    private String item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_health_detail);
        healthStateID = getIntent().getStringExtra("healthStateID");
        addMyText = (LinearLayout) findViewById(R.id.add_detail_health_info);
        // 实例化线性布局并设置垂直方向
        addHealthDetialBeen = new BeanAddHealthDetialBeen();
        tempHealthList = new ArrayList<BeanAddHealthDetialBeen>();
        listValuBeen = new ArrayList<BeanValueBeen>();
        addValue = new ArrayList<String>();
        con = new String[10];// 存储选着后的类容（类型2的）
        keyId = new long[20];

        rightTV = (TextView) findViewById(R.id.right_tv);
        rightTV.setVisibility(View.VISIBLE);
        rightTV.setOnClickListener(this);
        rightTV.setText("完成");
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("新增");
    }

    class EditTextOnClickListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //
            // Intent intent2 = new Intent();
            // intent2.setClass(PointConvertActivity.this,
            // MyPointsActivity.class);
            // PointConvertActivity.this.startActivity(intent2);
            int tag = (Integer) arg0.getTag();
            // keyid = tempHealthList.get(tag).getKeyid();

        }
    }

    class ButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            int tag = (Integer) arg0.getTag();
            keyidBt = tempHealthList.get(tag).getKeyid();
            addValue.clear();
            for (int j = 0; j < tempHealthList.get(tag).getValues().size(); j++) {
                addValue.add(tempHealthList.get(tag).getValues().get(j)
                        .getValue());// id[j]= listValuBeen.get(j).getValue();
            }
            listValuBeen = tempHealthList.get(tag).getValues();
            pop(true, (TextView) arg0);
        }
    }


    private void disPlay(int len) {
        for (int i = 0; i < len; i++) {
            LinearLayout lin1 = new LinearLayout(this);
            lin1.setOrientation(LinearLayout.HORIZONTAL);

            myTextView = new TextView(this);
            myTextView.setText(tempHealthList.get(i).getKey() + ":");
            myTextView.setTextSize(18);
            myTextView.setTextColor(Color.BLACK);
            lin1.addView(myTextView);
            keyId[i] = tempHealthList.get(i).getKeyid();
            int type = tempHealthList.get(i).getValueType();
            switch (type) {
                case 0:
                    myEditText = new EditText(this);
                    myEditText.setWidth(getWallpaperDesiredMinimumWidth() - 40);
                    myEditText.setTextColor(Color.BLACK);
                    myEditText.setTextSize(18);
                    myEditText.setHint("请输入");
                    myEditText.setTag(i);
                    myEditText.setOnClickListener(new EditTextOnClickListener());
                    lin1.addView(myEditText);

                    break;
                case 1:
                    selectTextData = new EditText(this);
                    selectTextData.setHint("请选择年");
                    selectTextData
                            .setWidth((getWallpaperDesiredMinimumWidth() - 40) / 2);
                    selectTextData.setTextColor(Color.BLACK);
                    selectTextData.setTextSize(18);
                    selectTextData.setInputType(DATE_DIALOG);
                    selectTextData.setOnClickListener(new DateOnClick());
                    lin1.addView(selectTextData);

                    // selectTextTime= new TextView(this);
                    // selectTextTime.setHint("请选择日");
                    // //
                    // selectTextTime.setWidth((getWallpaperDesiredMinimumWidth()-40)/2);
                    // selectTextTime.setBackgroundColor(Color.GRAY);
                    // selectTextTime.setTextColor(Color.BLACK);
                    // selectTextTime.setTextSize(18);
                    // selectTextTime.setLeft(10);
                    // selectTextTime.setRight(10);
                    // selectTextTime.setOnClickListener(new View.OnClickListener(){
                    // public void onClick(View v) {
                    // showDialog(TIME_DIALOG);
                    // }
                    // });
                    //
                    // lin1.addView(selectTextTime);
                    break;
                case 2:
                    TextView selectText2 = new TextView(this);
                    selectText2.setText("请选择选项");
                    selectText2.setTextSize(18);
                    selectText2.setBackgroundColor(Color.GRAY);
                    selectText2.setTextColor(Color.BLACK);
                    selectText2.setWidth(getWallpaperDesiredMinimumWidth() - 40);
                    selectText2.setTag(i);
                    selectText2.setOnClickListener(new ButtonOnClickListener());
                    lin1.addView(selectText2);

                    // id=new String[listValuBeen.size()];

                    // addValue.add(",");
                    //

                    break;

                default:
                    break;
            }
            myTextView1 = new TextView(this);
            myTextView1.setText("");
            myTextView1.setTextSize(18);
            myTextView.setTextColor(Color.BLACK);
            addMyText.addView(lin1);
            addMyText.addView(myTextView1);
        }

    }

    private final class DateOnClick implements OnClickListener {
        public void onClick(View v) {
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
                    AddHealthDetail.this);
            dateTimePicKDialog.dateTimePicKDialog(selectTextData, 1);

        }
    }

    private void pop(boolean i, final TextView view) {
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                R.layout.choosepicker_single_layout, null);
        TextView title = (TextView) layout.findViewById(R.id.indicate_Info);
        minute_pv = (PickerView) layout.findViewById(R.id.minute_pv);
        // List<String> data = new ArrayList<String>();
        title.setText("请选择");
        // for (int a = 0; a < id.length; a++) {
        // if (i) {
        // title.setText("请选择您的接听设备");
        // data.add(id[a]);
        // }
        // }
        minute_pv.setData(addValue);
        // Id_Message = minute_pv.getText();
        minute_pv.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text) {
                // Id_Message = text;
            }
        });
        // Id_Message = minute_pv.getText();
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setContentView(layout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                btonConteng = minute_pv.getText();
                view.setText(btonConteng);
                int tag = (Integer) view.getTag();

                con[tag] = btonConteng;
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

    private class QueryHealthTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", "1");
            param.put("healthStateID", healthStateID);
            try {
                String a = AbsParam.getBaseUrl() + "/app/healthrecord/"
                        + "healthstate";
                Log.i("result", a);
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                                + "/app/healthrecord/" + "healthstateitems", param,
                        "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 解析返回来的Json数组
         *
         * @param jsonString
         * @return
         * @throws Exception
         */
        private void JsonArrayToList(String jsonString) throws Exception {
            Gson gson = new Gson();
            // addHealthDetialBeen =
            // gson.fromJson(jsonString,BeanAddHealthDetialBeen.class);
            tempHealthList = gson.fromJson(jsonString,
                    new TypeToken<List<BeanAddHealthDetialBeen>>() {
                    }.getType());
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            int len = tempHealthList.size();
            disPlay(len);

        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        showProgressBar();
        QueryHealthTask baseInfoTask = new QueryHealthTask();
        baseInfoTask.execute();

    }

    private int resultID;
    private String detail;

    private class UpHealthTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("items", item);
            param.put("healthStateID", healthStateID);

            try {
                String a = AbsParam.getBaseUrl() + "/app/healthrecord/"
                        + "addhealthstateitems";
                Log.i("result", a);
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                                + "/app/healthrecord/" + "addhealthstateitems", param,
                        "utf-8");
                Log.i("result", result);
                jsonResilt(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 解析返回来的Json数组
         *
         * @param jsonString
         * @return
         * @throws Exception
         */
        private void jsonResilt(String jsonString) throws Exception {
            // addHealthDetialBeen =
            // gson.fromJson(jsonString,BeanAddHealthDetialBeen.class);
            JSONObject jsonObject = new JSONObject(jsonString);
            detail = jsonObject.getString("detail");
            resultID = jsonObject.getInt("resultID");
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            if (resultID == 1) {
                Toast.makeText(getApplicationContext(), "新增完成",
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "新增失败,请重新填写",
                        Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
                long[] a = keyId;
                long[] valuid1 = new long[10];
                editTextConteng = myEditText.getText().toString();
                data = selectTextData.getText().toString();
                valuid = new long[10];
                for (int i = 0; i < con.length; i++) {
                    if (con[i] == null) {
                        continue;
                    } else {
                        for (int j = 0; j < tempHealthList.size(); j++) {
                            for (int j2 = 0; j2 < tempHealthList.get(j).getValues()
                                    .size(); j2++) {
                                if (con[i].equals(tempHealthList.get(j).getValues()
                                        .get(j2).getValue())) {
                                    valuid[i] = tempHealthList.get(j).getValues()
                                            .get(j2).getValueid();
                                }
                            }

                        }
                    }
                }
                item = "";
                for (int i = 0; i < tempHealthList.size(); i++) {
                    if (tempHealthList.get(i).getValueType() == 0) {
                        item += "keyid:" + keyId[i] + ";" + "value:"
                                + editTextConteng + ",";
                    } else if (tempHealthList.get(i).getValueType() == 1) {
                        item += "keyid:" + keyId[i] + ";" + "value:" + data + ",";
                    } else if (tempHealthList.get(i).getValueType() == 2) {
                        // for (int j = 0; j < valuid.length; j++) {
                        // if (valuid[j] != 0) {
                        // int k=0;
                        // valuid1[k++] = valuid[j];
                        //
                        // }
                        //
                        // }
                        item += "keyid:" + keyId[i] + ";" + "value:" + valuid[i]
                                + ",";
                    }
                }
                UpHealthTask upHealthTask = new UpHealthTask();
                upHealthTask.execute();
                break;

            default:
                break;
        }
    }

}
