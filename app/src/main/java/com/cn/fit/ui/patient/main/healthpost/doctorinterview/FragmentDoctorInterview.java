package com.cn.fit.ui.patient.main.healthpost.doctorinterview;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthpost.BeanInerView;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.ui.TabFragmentListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentDoctorInterview extends TabFragmentListView {

    private static final String ARG_POSITION = "position";
    private List<BeanInerView> infoList, tempInfoList;
    private int type;
    private VidoInterViewAdapter vidoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

        vidoAdapter = new VidoInterViewAdapter(getActivity());
        listView = new XListView(getActivity());
        params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);
        listView.setCacheColorHint(getResources().getColor(R.color.black));
        listView.setBackgroundResource(R.color.white);
        listView.setDividerHeight(1);
        listView.setDivider(getResources().getDrawable(R.color.lightgray));
        listView.setSelector(R.color.transparent);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                String url = infoList.get(position - 1).getVedioUrl();
                Intent intent = new Intent(getActivity(), VideoPlay.class);
                Bundle mBundle = new Bundle();
                mBundle.putString(Constant.VEDIO_URL, url);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        infoList = new ArrayList<BeanInerView>();
        tempInfoList = new ArrayList<BeanInerView>();
        listView.setAdapter(vidoAdapter);
        fl.addView(listView);
        return fl;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initial(view);
    }

    private void initial(View view) {
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        QueryMyVidosTask task = new QueryMyVidosTask();
        task.execute();
    }

    @Override
    public void onTabFragmentClick() {
        // TODO Auto-generated method stub

    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.doctorinterview;
    }

    @Override
    protected void onReleaseTabUI() {
        // TODO Auto-generated method stub

    }

    private class QueryMyVidosTask extends AsyncTask<Integer, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                String url = AbsParam.getBaseUrl()
                        + "/videoinfo/cdn/getvideolist";
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
            for (BeanInerView tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }

            vidoAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
    }

    private void jsonToArray(String json) {
        Gson gson = new Gson();
        tempInfoList = gson.fromJson(json, new TypeToken<List<BeanInerView>>() {
        }.getType());
    }

    /*
     * 订单列表adapter
     */
    private class VidoInterViewAdapter extends BaseAdapter {
        private Context context;
        public int count = 10;

        public VidoInterViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
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
                        R.layout.fragment_interview, null);
                holder.vidoImage = (ImageView) convertView
                        .findViewById(R.id.vido_circle_img);
                holder.vidoName = (TextView) convertView
                        .findViewById(R.id.vido_name);
                holder.vidoDescription = (TextView) convertView
                        .findViewById(R.id.vido_description);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageLoader.getInstance().displayImage(
                    AbsParam.getBaseUrl() + infoList.get(position).getImgUrl(),
                    holder.vidoImage,
                    AppMain.initImageOptions(R.drawable.default_life_icon,
                            false));
            holder.vidoName.setText(infoList.get(position).getVideoName());
            holder.vidoDescription.setText(infoList.get(position)
                    .getDescription());
            return convertView;
        }
    }

    private class ViewHolder {
        ImageView vidoImage;
        TextView vidoName;
        TextView vidoDescription;

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        QueryMyVidosTask task = new QueryMyVidosTask();
        task.execute();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            QueryMyVidosTask task = new QueryMyVidosTask();
            task.execute();
        }
    }

}