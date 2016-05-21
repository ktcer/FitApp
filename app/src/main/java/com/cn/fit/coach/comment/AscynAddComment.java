package com.cn.fit.coach.comment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanAddComment;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Created by ktcer on 2016/1/3.
 */
public class AscynAddComment extends AsyncTask<Integer, Integer, BeanAddComment> {

    public AscynAddComment() {
        super();
    }

    String result = null;
    private Context context;

    private String classID;//课程id number
    private String coachID;//教练id number
    private String comments;//评论 string
    private String starLevelClass;//课程星级 number 1-5（int）
    private String starLevelCoach;//教练星级 number 1-5（int）
    private String state;//0差评，1中评，2好评 number Byte
    private String ddh;
    private long userID;

    private BeanAddComment bean;

    public AscynAddComment(String state, Context context, String classID, String coachID, String comments, String starLevelClass, String starLevelCoach, String ddh) {
        this.state = state;
        this.context = context;
        this.classID = classID;
        this.coachID = coachID;
        this.comments = comments;
        this.starLevelClass = starLevelClass;
        this.starLevelCoach = starLevelCoach;
        this.ddh = ddh;
        UtilsSharedData.initDataShare(context);// ////////
        userID = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }


    @Override
    protected BeanAddComment doInBackground(Integer... params) {
//        UtilsSharedData.initDataShare(context.this);
//        long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("classID", classID);
        param.put("coachID", coachID);
        param.put("comments", comments);
        param.put("starLevelClass", starLevelClass);
        param.put("starLevelCoach", starLevelCoach);
        param.put("userID", userID + "");
        param.put("state", state);
        param.put("ddh", ddh);
        try {
            result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                    + "/home/app/subcomments", param, "utf-8");
            Log.i("result", result);
            bean = JsonArrayToList(result);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return bean;
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanAddComment JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanAddComment beanAddComment = new BeanAddComment();
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beanAddComment = gson.fromJson(jsonString,
                        new TypeToken<BeanAddComment>() {
                        }.getType());

            }
        }
        return beanAddComment;
    }

    @Override
    protected void onPostExecute(BeanAddComment beanAddComment) {

    }
}
