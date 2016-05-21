package com.cn.fit.customer.coach;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.customer.baidulocation.BaiduLacationUtil;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanCoachsList;
import com.cn.fit.model.nurse.BeanHospitaList;
import com.cn.fit.model.nurse.BeanRegion;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.AppPool;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.dropdownmenu.DropdownButton;
import com.cn.fit.util.dropdownmenu.DropdownButtonsController;
import com.cn.fit.util.dropdownmenu.DropdownItemObject;
import com.cn.fit.util.dropdownmenu.DropdownListView;
import com.cn.fit.util.refreshlistview.XListView;
import com.cn.fit.util.searchview.MaterialSearchView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityCoachsList extends ActivityBasicListView {

    private MaterialSearchView searchView;
    //    private ImageView searchBtn;
    private List<BeanCoachsList> infoList, tempInfoList;
    private SecretaryListAdapter secretaryListAdapter;
    private String latitude = "39.959013";
    private String longitude = "116.351791";
    private int type = 0;
    protected int pageNum = 1;

    private DropdownButton chooseSection, chooseHospital;// 选择方案下拉按钮
    private View mask;//下拉黑色背景
    private DropdownListView dropdownSectionList, dropdownHospitalList;
    private DropdownButtonsController dropdownButtonsControllerSection, dropdownButtonsControllerHospital;
    private List<DropdownItemObject> listSectionsItems, listHospitalItems;
    private BaiduLacationUtil location;
    private String[] l;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expertlist);
        AppPool.createActivity(this);
        initial();
        initDropDownMenu();
//        Queryregionlist task = new Queryregionlist();
//        task.execute();
//        Queryhospitallist taskqhl = new Queryhospitallist(0);
//        taskqhl.execute();
        UtilsSharedData.initDataShare(this);
        String loca = new String();
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
            for (int i = 0; i < l.length; i++) {
                System.out.print("locationoutput11111111" + l[i] + "+++++++\n");
            }
            if (l.length == 2) {
                latitude = l[0];
                longitude = l[1];
            }
            showProgressBar();
            QueryAllCoachslist taskQNL = new QueryAllCoachslist(latitude, longitude, type);
            taskQNL.execute();

        }

    }

    /**
     * 初始化下拉栏
     */
    private void initDropDownMenu() {
        chooseSection = (DropdownButton) findViewById(R.id.chooseSection);
        chooseHospital = (DropdownButton) findViewById(R.id.chooseHospital);
        mask = findViewById(R.id.mask);
        dropdownSectionList = (DropdownListView) findViewById(R.id.dropdownsectionList);
        dropdownHospitalList = (DropdownListView) findViewById(R.id.dropdownhospitalList);
        listSectionsItems = new ArrayList<DropdownItemObject>();
        listHospitalItems = new ArrayList<DropdownItemObject>();
        dropdownButtonsControllerSection = new DropdownButtonsController(this, listSectionsItems, mask, chooseSection, dropdownSectionList);
        dropdownButtonsControllerHospital = new DropdownButtonsController(this, listHospitalItems, mask, chooseHospital, dropdownHospitalList);
        listSectionsItems.add(new DropdownItemObject("全部地区", 0, "全部地区"));
        listHospitalItems.add(new DropdownItemObject("全部医院", 0, "全部医院"));
        dropdownButtonsControllerSection.init(0);
        dropdownButtonsControllerHospital.init(0);
        dropdownButtonsControllerSection.setOnPageSelectedListener(new DropdownButtonsController.DropDownSelectListener() {
            @Override
            public void onPageSelected(DropdownListView view) {
//                //这儿处理选中的操作
////                dropdownButtonsControllerHospital.hide();
//                latitude = view.getCurrent().id;
//                listHospitalItems.clear();
//                pageNum = 1;
//                Queryhospitallist taskqhl = new Queryhospitallist(view.getCurrent().id);
//                taskqhl.execute();
//                showProgressBar();
//                QueryAllCoachslist taskQNL = new QueryAllCoachslist(latitude, 0);
//                taskQNL.execute();
            }

            @Override
            public void onListViewsShow() {
                //当前列表被显示的时候隐藏别的下拉列表
                dropdownButtonsControllerHospital.hide();
            }
        });
        dropdownButtonsControllerHospital.setOnPageSelectedListener(new DropdownButtonsController.DropDownSelectListener() {
            @Override
            public void onPageSelected(DropdownListView view) {
//                //这儿处理选中的操作
//                dropdownButtonsControllerSection.hide();
//             long    longitude1 = view.getCurrent().id;
//                infoList.clear();
//                pageNum = 1;
//                showProgressBar();
//                QueryAllCoachslist taskQNL = new QueryAllCoachslist(
//                        latitude, longitude,type);
//                taskQNL.execute();
            }

            @Override
            public void onListViewsShow() {
                //当前列表被显示的时候隐藏别的下拉列表
                dropdownButtonsControllerSection.hide();
            }
        });
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownButtonsControllerHospital.hide();
                dropdownButtonsControllerSection.hide();
            }
        });
    }

    /**
     * 基本组件初始化
     */
    private void initial() {
        secretaryListAdapter = new SecretaryListAdapter(this);
        listView = (XListView) this.findViewById(R.id.lv_scretray);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Intent intent = new Intent(ActivityCoachsList.this,
                        ActivityCoachPage.class);
                intent.putExtra("ExpertId", infoList.get(position - 1).getUserID());
                intent.putExtra("image", infoList.get(position - 1).getPicUrl());
                intent.putExtra("pageType", "1");
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });

        ((TextView) this.findViewById(R.id.middle_tv)).setText("附近教练列表");
//        searchBtn = (ImageView) this.findViewById(R.id.right_Btn);
//        searchBtn.setVisibility(View.INVISIBLE);
//        searchBtn.setImageResource(R.drawable.ic_action_action_search);
//        searchBtn.setOnClickListener(this);
        infoList = new ArrayList<BeanCoachsList>();
        tempInfoList = new ArrayList<BeanCoachsList>();
        listView.setAdapter(secretaryListAdapter);
        initSearchView();
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
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
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
            listSectionsItems.clear();
            listSectionsItems.add(new DropdownItemObject("全部地区", 0, "全部地区"));
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    List<BeanRegion> listRegion = gson
                            .fromJson(jsonString,
                                    new TypeToken<List<BeanRegion>>() {
                                    }.getType());
                    if (listRegion != null) {
                        for (BeanRegion region : listRegion) {
                            listSectionsItems.add(new DropdownItemObject(region.getRegionName(), region.getRegionID(), region.getRegionName()));
                        }
                    }
//					dropdownButtonsControllerSection.init(0);
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // showProgressBar();
            dropdownButtonsControllerSection.init(0);
            secretaryListAdapter.notifyDataSetChanged();
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
            listHospitalItems.clear();
            listHospitalItems.add(new DropdownItemObject("全部医院", 0, "全部医院"));
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    List<BeanHospitaList> listHospital = gson
                            .fromJson(jsonString,
                                    new TypeToken<List<BeanHospitaList>>() {
                                    }.getType());
                    if (listHospital != null) {
                        for (BeanHospitaList hospital : listHospital) {
                            listHospitalItems.add(new DropdownItemObject(hospital.getHospitalName(), hospital.getHospitalID(), hospital.getHospitalName()));
                        }
                    }

                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dropdownButtonsControllerHospital.init(0);
            hideProgressBar();
        }
    }

    /**
     * 查询教练列表
     *
     * @author kuangtiecheng
     */
    private class QueryAllCoachslist extends AsyncTask<Integer, Integer, String> {

        public QueryAllCoachslist() {
            super();
        }

        String result = null;
        private String latitude;
        private String longitude;
        private int type;

        public QueryAllCoachslist(String latitude, String longitude, int type) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.type = type;
        }

        @Override
        protected String doInBackground(Integer... params) {
            UtilsSharedData.initDataShare(ActivityCoachsList.this);
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("latitude", latitude);
            param.put("longitude", longitude);
            param.put("type", type + "");
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                Log.i("input", AbsParam.getBaseUrl()
                        + "/coach/app/getcoachlist" + param.toString());
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/coach/app/getcoachlist", param, "utf-8");
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
                            new TypeToken<List<BeanCoachsList>>() {
                            }.getType());

                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            for (BeanCoachsList tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }

            secretaryListAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();

        }
    }

    private class SecretaryListAdapter extends BaseAdapter {
        private Context context;
        public int count = 10;

        public SecretaryListAdapter(Context context) {
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
                        R.layout.list_item_hearth_secretary, null);
                holder.secretaryName = (TextView) convertView
                        .findViewById(R.id.secretary_name);
                holder.secretaryGoodAt = (TextView) convertView
                        .findViewById(R.id.secretary_good_at);
                holder.secretaryStaff = (TextView) convertView
                        .findViewById(R.id.secretary_staff);
                holder.secretaeyHeadImg = (CircleImageView) convertView
                        .findViewById(R.id.secretary_head_image);
                holder.hosptial = (TextView) convertView
                        .findViewById(R.id.secretary_hospitalname);
                holder.mount = (TextView) convertView
                        .findViewById(R.id.mountOfFans);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.secretaryName.setText(infoList.get(position).getName());// infoList.get(position)
            holder.secretaryStaff.setText("" + "");
            holder.mount.setText("" + infoList.get(position).getMembers());
            holder.hosptial.setText(infoList.get(position).getSex());
            String ditans = "";
            if (Double.parseDouble(infoList.get(position).getDistance()) > 1000) {
                ditans = Double.parseDouble(infoList.get(position).getDistance()) / 1000 + "km";
            } else {
                ditans = infoList.get(position).getDistance() + "m";
            }
            holder.secretaryGoodAt.setText("距离" + ditans);// infoList.get(position)


            ImageLoader.getInstance().displayImage(
                    AbsParam.getBaseUrl() + infoList.get(position).getPicUrl(),
                    holder.secretaeyHeadImg,
                    AppMain.initImageOptions(R.drawable.default_user_icon,
                            false));
            return convertView;
        }
    }

    private class ViewHolder {
        TextView secretaryName;
        TextView hosptial;
        /**
         * 粉丝数量
         */
        TextView mount;
        TextView secretaryGoodAt;
        TextView secretaryStaff;
        CircleImageView secretaeyHeadImg;

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        pageNum = 1;
        // showProgressBar();
        QueryAllCoachslist taskQNL = new QueryAllCoachslist(latitude, longitude, type);
        taskQNL.execute();
    }

    QueryAllCoachslist taskQNL;

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

            taskQNL = new QueryAllCoachslist(latitude, longitude, type);
            taskQNL.execute();
        }
    }

    @Override
    public void onBackPressed() {
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
