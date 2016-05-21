package com.cn.fit.ui.patient.main.healthpost.healthwebview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.ui.TabFragmentListView;
import com.cn.fit.ui.patient.main.healthpost.healthpost.ActivityDetailWebService;
import com.cn.fit.util.refreshlistview.XListView;

public class FragmentHealthKnowledge extends TabFragmentListView {

    private static final String ARG_POSITION = "position";
    //	private List<BeanHeathKnowledge> infoList, tempInfoList;
    private int type;
    // private KnowledgeViewAdapter vidoAdapter;
    private String[] array = new String[]{"如何避免二甲双胍的胃肠道反应", "六大因素让糖尿病风险加倍", "高血压自我管理", "高血脂饮食运动", "肥胖主动管理",
            "痛风饮食管理", "脑卒中患者家庭护理", "骨折术后康复"};
    private String[] desc = new String[]{"主要讲解避免常用降糖药物二甲双弧的胃肠道反应的方法", "主要讲解增大患糖尿病风险的六大因素及其避免方式", "主要讲解高血压病的诊断标准、重点筛查人群以及相应的治疗方案", "主要讲解高血脂的病因、危险因素及其预防与监测管理", "主要讲解肥胖病的定义、病因和发病机制、分类与危害以及治疗方案",
            "主要讲解痛风的饮食控制管理方案", "主要讲解脑卒中患者家庭护理措施", "主要讲解骨折术后康复期的心理干预、饮食干预、并发症及合并症的干预等干预护理方案"};
    private int[] picture = new int[]{R.drawable.health_erjiashuanghua, R.drawable.health_tangniaobingri, R.drawable.health3, R.drawable.health5,
            R.drawable.health1, R.drawable.health2, R.drawable.health6,
            R.drawable.health4};
//	private SimpleAdapter adapter;

    @SuppressWarnings("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

        // vidoAdapter = new KnowledgeViewAdapter(getActivity());
        listView = new XListView(getActivity());
        params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);
        listView.setCacheColorHint(R.color.black);
        listView.setBackgroundResource(R.color.white);
        listView.setDividerHeight(1);
        // listView.setDivider(getResources().getDrawable(R.color.lightgray));
        listView.setSelector(R.color.transparent);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                select(position - 1);
                // String url = infoList.get(position-1).getVedioUrl();
                // Intent intent =new Intent(getActivity(),VideoPlay.class);
                // Bundle mBundle = new Bundle();
                // mBundle.putString(Constant.VEDIO_URL, url);
                // intent.putExtras(mBundle);
                // startActivity(intent);
                Intent intent = new Intent(getActivity(),
                        ActivityDetailWebService.class);
                intent.putExtra("url", URL);
                intent.putExtra("title", array[position - 1]);
                startActivity(intent);
            }

        });
        // infoList = new ArrayList<BeanHeathKnowledge>();
        // tempInfoList = new ArrayList<BeanHeathKnowledge>();
        // listView.setAdapter(vidoAdapter);
        fl.addView(listView);
        return fl;
    }

    String URL;

    private void select(int i) {
        switch (i) {
            case 0:
                URL = "http://test.inurse.com.cn/static/mobileshow/weichangdao.html";
                break;
            case 1:
                URL = "http://test.inurse.com.cn/static/mobileshow/tangniaobing.html";
                break;
            case 2:
                URL = "http://test.inurse.com.cn/static/mobileshow/bloodpressure.html";
                break;
            case 3:
                URL = "http://test.inurse.com.cn/static/mobileshow/xuezhi.html";
                break;
            case 4:
                URL = "http://test.inurse.com.cn/static/mobileshow/fat.html";
                break;
            case 5:
                URL = "http://test.inurse.com.cn/static/mobileshow/tongfeng.html";
                break;
            case 6:
                URL = "http://test.inurse.com.cn/static/mobileshow/head.html";
                break;
            case 7:
                URL = "http://test.inurse.com.cn/static/mobileshow/guzhe.html";
                break;


            default:
                break;
        }

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
        load();
        // QueryMyVidosTask task = new QueryMyVidosTask();
        // task.execute();
    }

    @Override
    public void onTabFragmentClick() {
        // TODO Auto-generated method stub

    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected void onReleaseTabUI() {
        // TODO Auto-generated method stub

    }

    private void load() {
//		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < array.length; i++) {
//			Map<String, Object> item = new HashMap<String, Object>();
//			item.put("name", array[i]);
//			item.put("picture", picture[i]);
//			listItem.add(item);
//		}
//		adapter = new SimpleAdapter(getActivity(), listItem,
//				R.layout.list_item, new String[] { "name", "picture" },
//				new int[] { R.id.list_item_title, R.id.list_item_image });
        KnowledgeViewAdapter adapter = new KnowledgeViewAdapter(getActivity());
        listView.setAdapter(adapter);
        // listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        // {
        //
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        // long arg3) {
        // // TODO Auto-generated method stub
        // // if(arg2==0){
        // // ActivityTag="VideoListActivity";
        // // wehtherLogin(ActivityTag, VideoListActivity.class);
        // // }else{
        // // Toast.makeText(getApplicationContext(), "暂无此分类视频", 1000).show();
        // // }
        // // startActivity(VideoListActivity.class);
        // }
        //
        // });
    }


    /*
     * 健康知识列表adapter
     */
    private class KnowledgeViewAdapter extends BaseAdapter {
        private Context context;
        public int count = 10;

        public KnowledgeViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
//			return infoList.size();
            return array.length;
        }

        @Override
        public Object getItem(int position) {
//			return infoList.get(position);
            return array[position];
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
                holder.knowledgeImage = (ImageView) convertView
                        .findViewById(R.id.vido_circle_img);
                holder.knowledgeImage.setScaleType(ScaleType.CENTER);
                holder.knowledgeName = (TextView) convertView
                        .findViewById(R.id.vido_name);
                holder.knowledgeDesc = (TextView) convertView.findViewById(R.id.vido_description);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//			ImageLoader.getInstance().displayImage(
//					AbsParam.getBaseUrl() + infoList.get(position).getImgUrl(),
//					holder.knowledgeImage,
//					AppMain.initImageOptions(R.drawable.default_life_icon,
//							false));
            holder.knowledgeImage.setImageResource(picture[position]);
//			holder.knowledgeName.setText(infoList.get(position).getVideoName());
            holder.knowledgeName.setText(array[position]);
            holder.knowledgeDesc.setText(desc[position]);
            return convertView;
        }
    }

    private class ViewHolder {
        ImageView knowledgeImage;
        TextView knowledgeName;
        TextView knowledgeDesc;
    }

    // private class QueryMyVidosTask extends AsyncTask<Integer, Integer,
    // String> {
    // String result = "";
    //
    // @Override
    // protected String doInBackground(Integer... params) {
    // HashMap<String, String> param = new HashMap<String, String>();
    // param.put("pageSize", 10 + "");
    // param.put("pageNum", pageNum + "");
    // try {
    // String url = AbsParam.getBaseUrl()
    // + "/videoinfo/cdn/getvideolist";
    // Log.i("result", url + param.toString());
    // result = NetTool.sendPostRequest(url, param, "utf-8");
    // Log.i("result", result);
    // tempInfoList.clear();
    // jsonToArray(result);
    // if (tempInfoList.size() < 10) {
    // canLoadMore = false;
    // } else {
    // canLoadMore = true;
    // }
    // } catch (Exception e) {
    // canLoadMore = false;
    // hideProgressBar();
    // e.printStackTrace();
    // }
    // return null;
    // }
    //
    // @Override
    // protected void onPostExecute(String result) {
    // if (pageNum == 1) {
    // infoList.clear();
    // }
    // for (BeanInerView tmp : tempInfoList) {
    // infoList.add(tmp);
    // }
    // if (canLoadMore) {
    // listView.setPullLoadEnable(true);
    // } else {
    // listView.setPullLoadEnable(false);
    // }
    //
    // vidoAdapter.notifyDataSetChanged();
    // hideProgressBar();
    // onLoad();
    // }
    // }
    //
    // private void jsonToArray(String json) {
    // Gson gson = new Gson();
    // tempInfoList = gson.fromJson(json, new TypeToken<List<BeanInerView>>() {
    // }.getType());
    // }


    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        // QueryMyVidosTask task = new QueryMyVidosTask();
        // task.execute();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            // QueryMyVidosTask task = new QueryMyVidosTask();
            // task.execute();
        }
    }

}