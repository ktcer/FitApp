package com.cn.fit.ui.patient.main.healthpost.healthpost;

import android.os.AsyncTask;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthpost.BeanHealthPost;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthPostDataLoader {
    private ActivityBasic context;
    private OnCompletedListener l;

    //	//TODO
    public HealthPostDataLoader(ActivityBasic mContext) {
        this.context = mContext;

    }

    public void setOnCompletedListerner(OnCompletedListener mL) {
        l = mL;
    }

    public void startLoading(HashMap<String, String> mParams) {
        if (mParams != null) {
            ((ActivityBasic) context).showProgressBar();
            LoadTask task = new LoadTask();
            task.execute(mParams);
        }
    }


    private class LoadTask extends
            AsyncTask<HashMap<String, String>, Void, List<BeanHealthPost>> {

        @Override
        protected List<BeanHealthPost> doInBackground(HashMap<String, String>... params) {
            List<BeanHealthPost> ret = new ArrayList<BeanHealthPost>();
            Gson gson = new Gson();
            try {
                // 模拟数据加载
                int page = Integer.parseInt(params[0].get("page"));
                int page_size = Integer.parseInt(params[0].get("page_size"));

                HashMap<String, String> param = new HashMap<String, String>();
                param.put("pageSize", page_size + "");
                param.put("pageNum", page + "");
                try {
                    String retString = NetTool.sendPostRequest(AbsParam.getBaseUrl() + "/travel/getlist", param, "utf-8");
//					JSONObject json=new JSONObject(retString);
                    ret = gson.fromJson(retString, new TypeToken<List<BeanHealthPost>>() {
                    }.getType());

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    ((ActivityBasic) context).hideProgressBar();
                }
                //从网络获取旅游数据
            } catch (Exception e) {
                ((ActivityBasic) context).hideProgressBar();
            }

            return ret;
        }

        @Override
        protected void onPostExecute(List<BeanHealthPost> ret) {
            ((ActivityBasic) context).hideProgressBar();
            if (ret == null) {
                l.onCompletedFailed("------------------faild");
            } else {
                l.onCompletedSucceed(ret);
            }

        }
    }

    public interface OnCompletedListener {
        /**
         * @param list 列表
         */
        public void onCompletedSucceed(List<BeanHealthPost> list);

        /**
         * 失败
         *
         * @param str
         */
        public void onCompletedFailed(String str);

        /**
         * 总数
         *
         * @param count 数量
         */
        public void getCount(int count);
    }
}
