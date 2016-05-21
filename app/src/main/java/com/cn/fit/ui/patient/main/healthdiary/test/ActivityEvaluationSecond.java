package com.cn.fit.ui.patient.main.healthdiary.test;

/**
 * @author kuangtiecheng
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthdiary.BeanResultOfEvaluation;
import com.cn.fit.model.healthdiarytest.BeanWenjuan;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author kuangtiecheng
 */
public class ActivityEvaluationSecond extends ActivityBasic {
    private BeanWenjuan beanWenjuan;
    private TextView btnNext;
    private int nowPage = 0;//设置当前需要加载的fragment
    public boolean firstIn = true;
    public HashMap<String, String> answerList = new HashMap<String, String>();

    //	public List<BeanAnswer> answerList = new ArrayList<BeanAnswer>();
    public BeanWenjuan getBeanWenjuan() {
        return beanWenjuan;
    }


    public void setBeanWenjuan(BeanWenjuan beanWenjuan) {
        this.beanWenjuan = beanWenjuan;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_evaluationsecond);
        UtilsSharedData.initDataShare(this);
        btnNext = (TextView) findViewById(R.id.right_tv);
        btnNext.setText("下一项");
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setOnClickListener(this);
        ((TextView) findViewById(R.id.middle_tv)).setText("健康评估");
        if (findViewById(R.id.fragment_evaluationSecond) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentSecondTest firstFragment = FragmentSecondTest.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_evaluationSecond, firstFragment).commit();
        }

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.right_tv:
                if (nowPage == 3) {
                    //提交
                    submitData();
                } else {
                    replacePage();
                }
                break;

            default:
                break;
        }
    }

    private void submitData() {
        if (beanWenjuan != null) {
            showProgressBar();
            AnscyncSumbitEvaluation2 mTask = new AnscyncSumbitEvaluation2();
            mTask.execute();
        } else {
            ToastUtil.showMessage(R.string.errormsg_server);
        }
    }

    /*
     * 将题目转换为字符串
     */
    private String getAnswerString() {
        String answerString = "";
        Iterator iter = answerList.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            answerString += "{" + "id:" + key.toString() + ",value:" + val.toString() + "},";
        }
        if (answerString.length() != 0) {
            answerString = answerString.substring(0, answerString.length() - 1);
        }
        answerString = "{" + answerString + "}";
        return answerString;
    }


    /**
     * @author kuangtiecheng
     *         提交问卷；
     */
    private class AnscyncSumbitEvaluation2 extends AsyncTask<Integer, Integer, BeanResultOfEvaluation> {
        private String allAnswer;
        private BeanResultOfEvaluation broe;

        @Override
        protected BeanResultOfEvaluation doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            //将答案转化为json
            allAnswer = getAnswerString();
            HashMap<String, String> param = new HashMap<String, String>();
            String patientID = UtilsSharedData.getLong(Constant.USER_ID, 0) + "";
            param.put("patientID", patientID);
            param.put("wenjuanID", beanWenjuan.getWenuanID() + "");
            param.put("answer", allAnswer);
            try {
                String url = AbsParam.getBaseUrl() + "/webapp/questionnaire/submitquestionnaire2";
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
                ToastUtil.showMessage("提交成功");
                UtilsSharedData.initDataShare(ActivityEvaluationSecond.this);
                UtilsSharedData.saveKeyMustValue("BodyTestState", 1);
                disPlayResult(result);
            } else {
                ToastUtil.showMessage("您的评估好像没有正确提交哦，再试一次吧");
            }

        }

        private void disPlayResult(BeanResultOfEvaluation result) {
            //			title.setText("测评结果");
            //			submit.setVisibility(View.INVISIBLE);
            //			setAnimation(0,0,1,0);
            //			layoutTestResult.startAnimation(animation);
            //			layoutTestResult.setVisibility(View.VISIBLE);
            //			tvTestResult.setText(result.getDetail());
            //			if(result.getDetail().equals("高危")){
            //				tvTestResult.setTextColor(getResources().getColor(R.color.orangered));
            //			}else if(result.getDetail().equals("极高危")){
            //				tvTestResult.setTextColor(getResources().getColor(R.color.red));
            //			}else if(result.getDetail().equals("中危")){
            //				tvTestResult.setTextColor(getResources().getColor(R.color.orange));
            //			}else if(result.getDetail().equals("低危")){
            //				tvTestResult.setTextColor(getResources().getColor(R.color.yellow));
            //			}else if(result.getDetail().equals("健康")){
            //				tvTestResult.setTextColor(getResources().getColor(R.color.lightgreen));
            //			}
            //			tvTestWarming.setText(result.getWarming());
            Intent intent = new Intent(ActivityEvaluationSecond.this,
                    ActivityTestReportDetail.class);
            intent.putExtra(Constant.TESTREPORTDETAIL, result);
            startActivity(intent);
            finish();
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

    private void replacePage() {
        nowPage++;

        FragmentSecondTest firstFragment = FragmentSecondTest.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt(FragmentSecondTest.PINGGU_TYPE, nowPage);
        firstFragment.setArguments(bundle);
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.common_slide_right_in, R.anim.common_slide_left_out, R.anim.common_slide_left_in, R.anim.common_slide_right_out);
        mFragmentTransaction.addToBackStack("nowPage");
        mFragmentTransaction.replace(R.id.fragment_evaluationSecond, firstFragment).commit();
        if (nowPage == 3) {
            btnNext.setText("提交");
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        nowPage--;
        btnNext.setText("下一项");
    }


}
