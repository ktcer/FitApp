package com.cn.fit.customer.discovery;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cn.fit.R;
import com.cn.fit.customer.baidulocation.BaiduLacationUtil;
import com.cn.fit.customer.expandtab.view.ExpandTabView;
import com.cn.fit.customer.expandtab.view.ViewLeft;
import com.cn.fit.customer.expandtab.view.ViewMiddle;
import com.cn.fit.customer.expandtab.view.ViewRight;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanDiscovery;
import com.cn.fit.model.nurse.BeanHospitaList;
import com.cn.fit.model.nurse.BeanRegion;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.cn.fit.util.searchview.MaterialSearchView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 发现
 *
 * @author kuangtiecheng
 */
public class ActivityCoachDiscoveryList extends ActivityBasicListView {

    //顶栏 三个
    private ExpandTabView expandTabView;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ViewLeft viewLeft;
    private ViewMiddle viewMiddle;
    private ViewRight viewRight;
    //
    private MaterialSearchView searchView;
    //    private ImageView searchBtn;
    private List<BeanDiscovery> infoList, tempInfoList;
    private DiscoveryAdapter discoveryAdapter;
    private long currentSection = 0;
    private long currentHospital = 0;
    protected int pageNum = 1;
    private double latitude = 39.959013; //纬度 number double，6位小数
    private double longitude = 116.351791;
    private BaiduLacationUtil location;

    private LocationClient mLocationClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private String LocationResult = "";


    private View mask;//下拉黑色背景

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableBanner = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocach_finndlist);
        UtilsSharedData.initDataShare(this);
        loca = new String();
        location = new BaiduLacationUtil(this);
        loca = location.start();
        System.out.print("locationoutput11111111" + loca + "+++++++\n");
        if (loca == null) {
            loca = location.updateListener();
            System.out.print("locationoutput11111111" + loca + "+++++++\n");
        }
        if (loca != null) {
            UtilsSharedData.saveKeyMustValue(Constant.LOCATION, loca);
        }
        String loc = UtilsSharedData.getValueByKey(Constant.LOCATION);
        if (loc != null) {
            l = loc.split(",");
            longitude = Double.parseDouble(l[1]);
            latitude = Double.parseDouble(l[0]);
            for (int i = 0; i < l.length; i++) {
                System.out.print("locationoutput11111111" + l[i] + "+++++++\n");

            }
            if (l.length == 2) {
                showProgressBar();
                QueryDiscoveryList taskQNL = new QueryDiscoveryList(l[0], l[1]);
                taskQNL.execute();
            } else {
                showProgressBar();
                QueryDiscoveryList taskQNL = new QueryDiscoveryList(latitude + "", longitude + "");
                taskQNL.execute();
            }

        }


        initial();
        initVaule();//顶栏选择
        initListener();//顶栏选择
//        Queryregionlist task = new Queryregionlist();
//        task.execute();
//        Queryhospitallist taskqhl = new Queryhospitallist(0);
//        taskqhl.execute();
//        showProgressBar();
//        QueryDiscoveryList taskQNL = new QueryDiscoveryList(0, 0);
//        taskQNL.execute();

    }


    /**
     * 基本组件初始化
     */
    private void initial() {
        infoList = new ArrayList<BeanDiscovery>();
        tempInfoList = new ArrayList<BeanDiscovery>();


        discoveryAdapter = new DiscoveryAdapter(this, infoList);
        listView = (XListView) this.findViewById(R.id.lv_scretray);
        listView.setAdapter(discoveryAdapter);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Intent intent = new Intent(ActivityCoachDiscoveryList.this,
                        ActivityCoachApply.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.BEAN_DISCOVERY, infoList.get(position - 1));
                intent.putExtras(bundle);
                intent.putExtra(Constant.LATITUDE, latitude);
                intent.putExtra(Constant.LONGITUDE, longitude);
                intent.putExtra("buyflag", false);

                startActivity(intent);
            }
        });

//        ((TextView) this.findViewById(R.id.middle_tv)).setText("健康秘书列表");
//        searchBtn = (ImageView) this.findViewById(R.id.right_Btn);
//        searchBtn.setVisibility(View.VISIBLE);
//        searchBtn.setImageResource(R.drawable.ic_action_action_search);
//        searchBtn.setOnClickListener(this);


//        initSearchView();
        //顶栏选择
        expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
        viewLeft = new ViewLeft(this);
        viewMiddle = new ViewMiddle(this);
        viewRight = new ViewRight(this);
        discoveryAdapter.notifyDataSetChanged();
    }

    /**
     * 顶栏数据初始化
     */
    private void initVaule() {

        mViewArray.add(viewLeft);
        mViewArray.add(viewRight);
        mViewArray.add(viewMiddle);
        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("距离");
        mTextArray.add("距离");
        mTextArray.add("区域");
        expandTabView.setValue(mTextArray, mViewArray);
//		expandTabView.setTitle(viewLeft.getShowText(), 0);
//		expandTabView.setTitle(viewMiddle.getShowText(), 1);
//		expandTabView.setTitle(viewRight.getShowText(), 2);

    }

    /**
     * 有关顶栏的
     */
    private void initListener() {

        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(viewLeft, showText);
            }
        });

        viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {

            @Override
            public void getValue(String showText) {

                onRefresh(viewMiddle, showText);

            }
        });

        viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(viewRight, showText);
            }
        });

    }

    /**
     * 有关顶栏的
     *
     * @param view
     * @param showText
     */
    private void onRefresh(View view, String showText) {

        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
        }
        Toast.makeText(getApplicationContext(), showText, Toast.LENGTH_SHORT).show();

    }

    /**
     * 有关顶栏的
     *
     * @param tView
     * @return
     */
    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }


    private void initSearchView() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), "Query: " + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        location.stop();
    }

    String[] l = {};
    String loca = "";

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        UtilsSharedData.initDataShare(this);
        loca = new String();
        location = new BaiduLacationUtil(this);
        loca = location.start();
        System.out.print("locationoutput11111111" + loca + "+++++++\n");
        if (loca == null) {
            loca = location.updateListener();
            System.out.print("locationoutput11111111" + loca + "+++++++\n");
        }
        if (loca != null) {
            UtilsSharedData.saveKeyMustValue(Constant.LOCATION, loca);
        }
        String loc = UtilsSharedData.getValueByKey(Constant.LOCATION);
        if (loc != null) {
            l = loc.split(",");
            longitude = Double.parseDouble(l[1]);
            latitude = Double.parseDouble(l[0]);
            for (int i = 0; i < l.length; i++) {
                System.out.print("locationoutput11111111" + l[i] + "+++++++\n");

            }
            if (l.length == 2) {
                showProgressBar();
                QueryDiscoveryList taskQNL = new QueryDiscoveryList(l[0], l[1]);
                taskQNL.execute();
            } else {
                showProgressBar();
                QueryDiscoveryList taskQNL = new QueryDiscoveryList(latitude + "", longitude + "");
                taskQNL.execute();
            }

        }

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 1000;
//        try {
//            span = Integer.valueOf(frequence.getText().toString());
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(false);//反地理编码可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_Btn:
                v.startAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.icon_scale));
                searchView.showSearch();
                break;

        }
    }

    /**
     * 查询地区列表
     *
     * @author kuangtiecheng
     */
    private class Queryregionlist extends AsyncTask<Integer, Integer, String> {
        String result = null;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            try {
                Log.i("input", AbsParam.getBaseUrl()
                        + "/base/app/queryregionlist" + param.toString());
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/base/app/queryregionlist", param, "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                e.printStackTrace();
                hideProgressBar();

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
//            listSectionsItems.clear();
//            listSectionsItems.add(new DropdownItemObject("全部地区", 0, "全部地区"));
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    List<BeanRegion> listRegion = gson
                            .fromJson(jsonString,
                                    new TypeToken<List<BeanRegion>>() {
                                    }.getType());
                    if (listRegion != null) {
                        for (BeanRegion region : listRegion) {
//                            listSectionsItems.add(new DropdownItemObject(region.getRegionName(), region.getRegionID(), region.getRegionName()));
                        }
                    }
//					dropdownButtonsControllerSection.init(0);
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // showProgressBar();
//            dropdownButtonsControllerSection.init(0);
            discoveryAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 查询医院列表
     *
     * @author kuangtiecheng
     */
    private class Queryhospitallist extends AsyncTask<Integer, Integer, String> {

        public Queryhospitallist() {
            super();
        }

        String result = null;
        private long regionID;

        public Queryhospitallist(long regionID) {
            super();
            this.regionID = regionID;
        }

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("regionID", regionID + "");
            try {
                Log.i("input", AbsParam.getBaseUrl()
                        + "/base/app/queryhospitallist" + param.toString());
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/base/app/queryhospitallist", param, "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                e.printStackTrace();
                hideProgressBar();

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
//            listHospitalItems.clear();
//            listHospitalItems.add(new DropdownItemObject("全部医院", 0, "全部医院"));
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    List<BeanHospitaList> listHospital = gson
                            .fromJson(jsonString,
                                    new TypeToken<List<BeanHospitaList>>() {
                                    }.getType());
                    if (listHospital != null) {
                        for (BeanHospitaList hospital : listHospital) {
//                            listHospitalItems.add(new DropdownItemObject(hospital.getHospitalName(), hospital.getHospitalID(), hospital.getHospitalName()));
                        }
                    }

                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
        }
    }

    /**
     * 查询活动列表
     *
     * @author kuangtiecheng
     */
    private class QueryDiscoveryList extends AsyncTask<Integer, Integer, String> {

        public QueryDiscoveryList() {
            super();
        }

        String result = null;
        private String latitude1;
        private String longitude1;

        public QueryDiscoveryList(String latitude, String longitude) {
            super();
            this.latitude1 = latitude;
            this.longitude1 = longitude;
        }

        @Override
        protected String doInBackground(Integer... params) {
            UtilsSharedData.initDataShare(ActivityCoachDiscoveryList.this);
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("inDistance", 20000000 + "");
            param.put("latitude", latitude1 + "");
            param.put("longitude", longitude1 + "");
            param.put("pageSize", 10 + "");
            param.put("pageNumer", pageNum + "");
            param.put("programType", -1 + "");
            param.put("state", 1 + "");
            param.put("time", -1 + "");
            try {
                Log.i("input", AbsParam.getBaseUrl()
                        + "/ad/app/getlist" + param.toString());
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/ad/app/getlist", param, "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                JsonArrayToList(result);
                if (tempInfoList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            } catch (Exception e) {
                canLoadMore = false;
                e.printStackTrace();
                hideProgressBar();

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
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    tempInfoList = gson.fromJson(jsonString,
                            new TypeToken<List<BeanDiscovery>>() {
                            }.getType());

                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            infoList.add(new BeanDiscovery());//去除显示少一个bug
            for (BeanDiscovery tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }

            discoveryAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();

        }
    }


    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        pageNum = 1;
        // showProgressBar();
        QueryDiscoveryList taskQNL = new QueryDiscoveryList(l[0], l[1]);
        taskQNL.execute();
    }

    QueryDiscoveryList taskQNL;

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            // showProgressBar();
            if (taskQNL != null
                    && taskQNL.getStatus() == AsyncTask.Status.RUNNING) {
                taskQNL.cancel(true); // 如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }

            taskQNL = new QueryDiscoveryList(l[0], l[1]);
            taskQNL.execute();
        }
    }

    @Override
    public void onBackPressed() {
        if (!expandTabView.onPressBack()) {
            finish();
        } else {
            super.onBackPressed();
        }
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
