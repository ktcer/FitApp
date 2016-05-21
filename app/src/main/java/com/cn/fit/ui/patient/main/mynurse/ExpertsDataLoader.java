package com.cn.fit.ui.patient.main.mynurse;

import android.os.AsyncTask;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanNurseInfo;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpertsDataLoader {
    private ActivityBasic context;
    private OnCompletedListenerExperts l;

    //	long deptID;
//	//TODO
    public ExpertsDataLoader(ActivityBasic mContext) {
        this.context = mContext;

    }

    public void setOnCompletedListerner(OnCompletedListenerExperts mL) {
        l = mL;
    }

    public void startLoading(HashMap<String, String> mParams) {
        if (mParams != null) {
            ((ActivityBasic) context).showProgressBar();
            LoadTask task = new LoadTask();
            task.execute(mParams);
        }
//		this.deptID = deptId;
    }


    private class LoadTask extends
            AsyncTask<HashMap<String, String>, Void, List<BeanNurseInfo>> {

        @Override
        protected List<BeanNurseInfo> doInBackground(HashMap<String, String>... params) {
            List<BeanNurseInfo> ret = new ArrayList<BeanNurseInfo>();
            Gson gson = new Gson();
            String result = "";

            try {
                // 模拟数据加载
                int page = Integer.parseInt(params[0].get("page"));
                int page_size = Integer.parseInt(params[0].get("page_size"));
                int deptId = Integer.parseInt(params[0].get("deptId"));
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("pageSize", page_size + "");
                param.put("pageNum", page + "");
                param.put("deptID", deptId + "");
                try {
                    String retString = NetTool.sendHttpClientPost(AbsParam.getBaseUrl() + "/yyzx/app/querynurselist", param, "utf-8");
//					JSONObject json=new JSONObject(retString);
                    ret = gson.fromJson(retString, new TypeToken<List<BeanNurseInfo>>() {
                    }.getType());

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    ((ActivityBasic) context).hideProgressBar();
                }

//				HashMap<String, String> param = new HashMap<String, String>();
//				param.put("deptID", deptID + "");
//				param.put("pageSize", page_size+"");
//				param.put("pageNum", page+"");
//				try {
//					result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
//							+ "/yyzx/app/querynurselist" , param, "utf-8");
//					jsonToExpertList(result);
//				} catch (Exception e) {
//					hideProgressBar();
//					e.printStackTrace();
//				}
//				return null;
                //从网络获取旅游数据
            } catch (Exception e) {
                ((ActivityBasic) context).hideProgressBar();
            }

            return ret;
        }

        @Override
        protected void onPostExecute(List<BeanNurseInfo> ret) {
            ((ActivityBasic) context).hideProgressBar();
            if (ret == null) {
                l.onCompletedFailed("------------------faild");
            } else {
                l.onCompletedSucceed(ret);
            }

        }
    }

    public interface OnCompletedListenerExperts {
        /**
         * @param list 列表
         */
        public void onCompletedSucceed(List<BeanNurseInfo> list);

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
