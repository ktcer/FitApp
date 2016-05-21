package com.cn.fit.ui.patient.main.healthdiary.test;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthdiary.BeanResultOfEvaluation;
import com.cn.fit.model.healthdiarytest.BeanAnswer;
import com.cn.fit.model.healthdiarytest.BeanEvaluation;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.customer.coach.ActivityCoachsList;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.FButton;
import com.cn.fit.util.FlowLayout;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityEvaluationCenter extends ActivityBasic {
    private EvaluationAdapter evaluationAdapter;
    private List<BeanEvaluation> BEList = new ArrayList<BeanEvaluation>();
    private TextView title, submit;
    private List<BeanAnswer> answerList = new ArrayList<BeanAnswer>();
    /**
     * 所有题目答案拼接以后得到的字符串
     */
    private LinearLayout layoutTestResult;
    private FButton btnChooseAssist;
    private TextView tvTestResult, tvTestWarming;
    private ListView listView;

    /*回答问题的次数*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluation_center);
        initial();
        setHasUsed();
    }

    private void setHasUsed() {
        UtilsSharedData.initDataShare(this);
        UtilsSharedData.saveKeyMustValue(Constant.HASUSED, true);
    }

    private void initial() {
        title = (TextView) findViewById(R.id.middle_tv);
        title.setText("身体状况测评");
        submit = (TextView) findViewById(R.id.right_tv);
        submit.setText("提交");
        submit.setOnClickListener(this);
        submit.setVisibility(View.VISIBLE);
        layoutTestResult = (LinearLayout) findViewById(R.id.layout_test_result);
        btnChooseAssist = (FButton) findViewById(R.id.btn_choose_assist);
        btnChooseAssist.setCornerRadius(3);
        btnChooseAssist.setOnClickListener(this);
        tvTestResult = (TextView) findViewById(R.id.tv_test_result);
        tvTestWarming = (TextView) findViewById(R.id.tv_test_warming);
        listView = (ListView) findViewById(R.id.evaluationList_evaluationCenter);
        evaluationAdapter = new EvaluationAdapter(BEList, this, false);
        listView.setAdapter(evaluationAdapter);

        showProgressBar();
        AscyncGetEvaluationList getEL_Task = new AscyncGetEvaluationList();
        getEL_Task.execute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
                if (!judgeWhetherFinishedEvaluation()) {
                    showToastDialog("完成测评奖励30金币，还有一点，加油哦！");
                    return;
                }
                showProgressBar();
                AnscyncSumbitEvaluation SE_task = new AnscyncSumbitEvaluation();
                SE_task.execute();
                break;
            case R.id.btn_choose_assist:
                startActivity(ActivityCoachsList.class);
                break;
            default:
                break;
        }
    }

    /**
     * @return int[];
     * 判断所有题目是否已经完成,返回的是没有完成的题号的数据
     * @author kuangtiecheng
     */
    private boolean judgeWhetherFinishedEvaluation() {
        for (int i = 0; i < BEList.size(); i++) {
            if (BEList.get(i).getType() == 1) {
                //单选必须选择才可以
                if (answerList.get(i).getValue().equals("")) {
                    //有未填写的返回false
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 在完成所有题目以后将对答案数据进行整理
     *
     * @return 所有答案连接起来的字符串
     * @author kuangtiecheng
     */
    private String getData(List<BeanAnswer> listAnswer) {
        String tempAnswer = "";
        for (int i = 0; i < listAnswer.size(); i++) {
            if (i == 0) {
                //第一个不用加前面逗号
                tempAnswer += "{" + "id:" + listAnswer.get(i).getId() + "," + "value" + ":" + listAnswer.get(i).getValue() + "}";
            } else {
                tempAnswer += ",{" + "id:" + listAnswer.get(i).getId() + "," + "value" + ":" + listAnswer.get(i).getValue() + "}";
            }
        }

        tempAnswer = "{" + tempAnswer + "}";

        return tempAnswer;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private class EvaluationAdapter extends BaseAdapter {
        private List<BeanEvaluation> mlist;
        private Context mContext;
        private Integer touchedPosition = -1;

        public EvaluationAdapter() {
            super();
            // TODO Auto-generated constructor stub
        }

        public EvaluationAdapter(List<BeanEvaluation> mlist, Context mContext, boolean isInitial) {
            this();
            this.mlist = mlist;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            BeanEvaluation bEvaluation = mlist.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                /**初始化控件*/
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.evaluation_chooseitem, parent, false);
                holder.content = (TextView) convertView.findViewById(R.id.content_evaluationItem);
                holder.answer = (EditText) convertView.findViewById(R.id.answer_evaluationItem);
                holder.unit = (TextView) convertView.findViewById(R.id.unit_evalutionItem);
                holder.radioButtonList[0] = (RadioButton) convertView.findViewById(R.id.result_a);
                holder.radioButtonList[1] = (RadioButton) convertView.findViewById(R.id.result_b);
                holder.radioButtonList[2] = (RadioButton) convertView.findViewById(R.id.result_c);
                holder.radioButtonList[3] = (RadioButton) convertView.findViewById(R.id.result_d);

                holder.chechBoxList[0] = (CheckBox) convertView.findViewById(R.id.check_a);
                holder.chechBoxList[1] = (CheckBox) convertView.findViewById(R.id.check_b);
                holder.chechBoxList[2] = (CheckBox) convertView.findViewById(R.id.check_c);
                holder.chechBoxList[3] = (CheckBox) convertView.findViewById(R.id.check_d);
                holder.radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGroup);
                holder.checkGroup = (FlowLayout) convertView.findViewById(R.id.checkGroup);
                holder.answer.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            touchedPosition = (Integer) v.getTag();
                        }
                        return false;
                    }

                });
                // 这个地方可以添加将保存的文本内容设置到EditText上的代码，会有用的～～
                holder.answer.clearFocus();
                if (touchedPosition != -1 && touchedPosition == position) {
                    // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                    holder.answer.requestFocus();
                }
                class MyTextWatcher implements TextWatcher {
                    public MyTextWatcher(ViewHolder holder) {
                        mHolder = holder;
                    }

                    private ViewHolder mHolder;

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null && !"".equals(s.toString())) {
                            int position = (Integer) mHolder.answer.getTag();
                            answerList.get(position).setValue(s.toString());
                        }
                    }
                }
                holder.answer.addTextChangedListener(new MyTextWatcher(holder));
                holder.answer.setTag(position);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.answer.setTag(position);
            }
            /**显示处理*/
            switch (bEvaluation.getType()) {
            /*测评题目类型为填空题*/
                case 0:
                    holder.checkGroup.setVisibility(View.GONE);
                    holder.radioGroup.setVisibility(View.GONE);
                    holder.answer.setVisibility(View.VISIBLE);
                    holder.unit.setVisibility(View.VISIBLE);
                    for (int i = 0; i < 4; i++) {
                        holder.radioButtonList[i].setVisibility(View.GONE);
                    }
                    holder.content.setText(position + 1 + "." + bEvaluation.getContext());
                    holder.unit.setText(bEvaluation.getUnite());
                    break;
			/*测评题目类型为是否题*/
                case 1:
                    holder.answer.setVisibility(View.GONE);
                    holder.unit.setVisibility(View.GONE);
                    holder.checkGroup.setVisibility(View.GONE);
                    holder.radioGroup.setVisibility(View.VISIBLE);
                    for (int i = 0; i < 4; i++) {
                        if (i < bEvaluation.getOptions().size()) {
                            String contextStr = bEvaluation.getOptions().get(i).getContext();
                            holder.radioButtonList[i].setVisibility(View.VISIBLE);
                            holder.radioButtonList[i].setText(contextStr);
                            holder.radioButtonList[i].setOnCheckedChangeListener(new Choose(position, i));
                        } else {
                            holder.radioButtonList[i].setVisibility(View.GONE);
                        }
                    }
                    holder.content.setText(position + 1 + "." + bEvaluation.getContext().replace("\r\n", ""));
                    holder.radioGroup.setTag(position);
                    break;
				/*测评题目类型为选择题*/
                case 2:
                    holder.checkGroup.setVisibility(View.VISIBLE);
                    holder.radioGroup.setVisibility(View.GONE);
                    holder.answer.setVisibility(View.GONE);
                    holder.unit.setVisibility(View.GONE);
                    for (int i = 0; i < 4; i++) {
                        if (i < bEvaluation.getOptions().size()) {
                            holder.chechBoxList[i].setVisibility(View.VISIBLE);
                            holder.chechBoxList[i].setText("" + bEvaluation.getOptions().get(i).getContext());
                            holder.chechBoxList[i].setOnCheckedChangeListener(new Check(position, i));
                        } else {
                            holder.chechBoxList[i].setVisibility(View.GONE);
                        }
                    }
                    holder.content.setText(position + 1 + "." + bEvaluation.getContext().replace("\r\n", ""));
                    break;
                default:
                    break;
            }
            return convertView;

        }
    }

    private class ViewHolder {

        EditText answer;
        TextView unit;
        TextView content;
        RadioButton[] radioButtonList = new RadioButton[4];
        RadioGroup radioGroup;
        FlowLayout checkGroup;
        CheckBox[] chechBoxList = new CheckBox[4];
    }

    private class Choose implements android.widget.CompoundButton.OnCheckedChangeListener {
        private int position;
        private int selection;

        public Choose(int position, int selection) {
            this.position = position;
            this.selection = selection;
        }

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            // TODO Auto-generated method stub
            switch (selection) {
                case 0:
                    answerList.get(position).setValue("A");
                    break;
                case 1:
                    answerList.get(position).setValue("B");
                    break;
                case 2:
                    answerList.get(position).setValue("C");
                    break;
                case 3:
                    answerList.get(position).setValue("D");
                    break;
                default:
                    break;
            }
        }

    }


    private class Check implements android.widget.CompoundButton.OnCheckedChangeListener {

        private int position;
        private int selection;

        public Check(int position, int selection) {
            this.position = position;
            this.selection = selection;
        }

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            // TODO Auto-generated method stub
            String tempAnswer = answerList.get(position).getValue();
            String thisAnswer = "";
            switch (selection) {
                case 0:
                    thisAnswer = "A";
                    break;
                case 1:
                    thisAnswer = "B";
                    break;
                case 2:
                    thisAnswer = "C";
                    break;
                case 3:
                    thisAnswer = "D";
                    break;
                default:
                    break;
            }
            if (arg1) {
                if (!tempAnswer.contains(thisAnswer)) {
                    //没有当前选项再进行添加
                    if (tempAnswer.equals("")) {
                        tempAnswer += thisAnswer;
                    } else {
                        tempAnswer += "," + thisAnswer;
                    }
                }
            } else {
                //取消当前选项
                if (tempAnswer.length() == 1) {
                    tempAnswer = "";
                } else {
                    StringBuffer stb = new StringBuffer(tempAnswer);
                    if (stb.indexOf(thisAnswer) == 0) {
                        stb.delete(0, 2);
                    } else if (stb.indexOf(thisAnswer) == thisAnswer.length() - 1) {
                        stb.delete(thisAnswer.length() - 3, thisAnswer.length() - 1);
                    } else {
                        stb.delete(stb.indexOf(thisAnswer), stb.indexOf(thisAnswer) + 2);
                    }
                    tempAnswer = stb.toString();
                }
            }
            answerList.get(position).setValue(tempAnswer);

        }


    }

    /**
     * @author kuangtiecheng
     *         从后台请求得到问卷的具体内容；
     */
    private class AscyncGetEvaluationList extends AsyncTask<Integer, Integer, List<BeanEvaluation>> {
        private List<BeanEvaluation> plist = new ArrayList<BeanEvaluation>();

        public AscyncGetEvaluationList() {
            super();
            // TODO Auto-generated constructor stub
        }

        @Override
        protected List<BeanEvaluation> doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            long patientID = UtilsSharedData.getLong(Constant.USER_ID, 0);
            param.put("patientID", patientID + "");
            try {
                String url = AbsParam.getBaseUrl() + "/webapp/questionnaire/getquestionnaire";
                String retString = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", retString);
                Log.i("input", url);
                plist = jsonToList(retString);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return plist;
        }

        @Override
        protected void onPostExecute(List<BeanEvaluation> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            BEList.clear();
            for (int i = 0; i < result.size(); i++) {
                BEList.add(result.get(i));
                answerList.add(new BeanAnswer(result.get(i).getQuestionID(), ""));
            }
            evaluationAdapter.notifyDataSetChanged();
            hideProgressBar();
        }

        /**
         * @param retString
         * @return List<BeanEvaluation>
         * @author kuangtiecheng
         */
        private List<BeanEvaluation> jsonToList(String retString) {
            List<BeanEvaluation> list = new ArrayList<BeanEvaluation>();
            Gson gson = new Gson();
            if (retString != null && !retString.equals("-1")) {
                list = gson.fromJson(retString, new TypeToken<List<BeanEvaluation>>() {
                }.getType());
            }
            return list;
        }

    }

    /**
     * @author kuangtiecheng
     *         提交问卷；
     */
    private class AnscyncSumbitEvaluation extends AsyncTask<Integer, Integer, BeanResultOfEvaluation> {
        //		private String planID;
        private String allAnswer;
        private BeanResultOfEvaluation broe;

        @Override
        protected BeanResultOfEvaluation doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            //将答案转化为json
            allAnswer = getData(answerList);
            HashMap<String, String> param = new HashMap<String, String>();
            String patientID = UtilsSharedData.getLong(Constant.USER_ID, 0) + "";
            param.put("patientID", patientID);
            param.put("planID", "0");
            param.put("answer", allAnswer);
            try {
                String url = AbsParam.getBaseUrl() + "/webapp/questionnaire/submitquestionnaire";
                String retString = NetTool.sendPostRequest(url, param, "utf-8");
                broe = jsonToBean(retString);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return broe;
        }

        @Override
        protected void onPostExecute(BeanResultOfEvaluation result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hideProgressBar();
            if (result == null) {
                return;
            }
            if (result.getStatus().equals("success")) {
                UtilsSharedData.initDataShare(ActivityEvaluationCenter.this);
                UtilsSharedData.saveKeyMustValue("HabbitTestState", 1);
                disPlayResult(result);
            } else {
                ToastUtil.showMessage("您的测评好像没有正确提交哦，再试一次吧");
            }

        }

        private void disPlayResult(BeanResultOfEvaluation result) {
            title.setText("测评结果");
            submit.setVisibility(View.INVISIBLE);
            setAnimation(0, 0, 1, 0);
            layoutTestResult.startAnimation(animation);
            layoutTestResult.setVisibility(View.VISIBLE);
            tvTestResult.setText(result.getDetail());
            if (result.getDetail().equals("高危")) {
                tvTestResult.setTextColor(getResources().getColor(R.color.orangered));
            } else if (result.getDetail().equals("极高危")) {
                tvTestResult.setTextColor(getResources().getColor(R.color.red));
            } else if (result.getDetail().equals("中危")) {
                tvTestResult.setTextColor(getResources().getColor(R.color.orange));
            } else if (result.getDetail().equals("低危")) {
                tvTestResult.setTextColor(getResources().getColor(R.color.yellow));
            } else if (result.getDetail().equals("健康")) {
                tvTestResult.setTextColor(getResources().getColor(R.color.lightgreen));
            }
            tvTestWarming.setText(result.getWarming());
        }

        private BeanResultOfEvaluation jsonToBean(String str) {
            BeanResultOfEvaluation beanROE = new BeanResultOfEvaluation();
            Gson gson = new Gson();
            if (str != null && !str.equals("-1")) {
                beanROE = gson.fromJson(str, new TypeToken<BeanResultOfEvaluation>() {
                }.getType());
            }
            return beanROE;
        }
    }

}
