package com.cn.fit.ui.patient.main.detectioncenter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.mycare.historicDetectionList;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsDate;
import com.cn.fit.util.UtilsSharedData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ActivityHistrionicDataCenter extends ActivityBasic {
    private TextView tv;
    private ListView lv;
    private String unit;
    private Long monitorDataId;
    private List<historicDetectionList> HBDList = new ArrayList<historicDetectionList>();
    private AdapterHistoryData HDAdapter;
    private UtilsSharedData sharedData;
    private String dataCode, dataName;
    private Long refValueMax_TWorSSY, refValueMin_TWorSSY, refValueMax_SZY, refValueMin_SZY;
    private String temp = "", tempDate = "";
    private int num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historycenter);
        sharedData.initDataShare(getApplicationContext());
        initial();
        getData();
    }


    private void initial() {
        tv = (TextView) findViewById(R.id.middle_tv);
        tv.setText("测量记录");
        choseTitle();
        lv = (ListView) findViewById(R.id.lv_historyCenter);
        HDAdapter = new AdapterHistoryData(HBDList, this);
        lv.setAdapter(HDAdapter);
    }

    private void choseTitle() {
        dataCode = getIntent().getStringExtra("dataCode");
        if (dataCode.equals("XT")) {
            num = 56;
        } else {
            num = 10;
        }
        if (dataCode.equals("BP")) {
            tv.setText("血压心率测量记录");
        } else if (dataCode.equals("TW")) {
            tv.setText("体温测量记录");
        } else if (dataCode.equals("XT")) {
            tv.setText("血糖测量记录");
        } else if (dataCode.equals("XY")) {
            tv.setText("血氧测量记录");
        } else if (dataCode.equals("TIZHI")) {
            tv.setText("体脂率测量记录");
        } else if (dataCode.equals("TIZHONG")) {
            tv.setText("体重测量记录");
        }
    }

    private void getData() {

        AscyncGetHistoryData GHD = new AscyncGetHistoryData(dataCode);
        GHD.execute();
        showProgressBar();
    }


    public class AdapterHistoryData extends BaseAdapter {
        private List<historicDetectionList> plist;
        private Context mContext;

        public AdapterHistoryData() {
            super();
            // TODO Auto-generated constructor stub
        }

        public AdapterHistoryData(List<historicDetectionList> plist,
                                  Context mContext) {
            this.plist = plist;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return plist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return plist.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.historyitem, parent, false);
                holder.date = (TextView) convertView.findViewById(R.id.date_history);
                holder.iv1 = (ImageView) convertView.findViewById(R.id.image1_historyitem);
                holder.iv2 = (ImageView) convertView.findViewById(R.id.image2_historyitem);
                holder.data1 = (TextView) convertView.findViewById(R.id.data1_historyitem);
                holder.data2 = (TextView) convertView.findViewById(R.id.data2_historyitem);
                holder.time = (TextView) convertView.findViewById(R.id.time_historyitem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//			if(position==0){
//				temp=plist.get(0).getDate().substring(5);
//				holder.date.setText(plist.get(0).getDate().substring(5,7)+"月"+plist.get(0).getDate().substring(8)+"日");
//				holder.date.setVisibility(View.VISIBLE);
//				Log.i("position="+position+"", "GONE");
//			}
//			else{
//				if(plist.get(position).getDate().substring(5).equals(temp)){
//					holder.date.setVisibility(View.GONE);
//					Log.i("position="+position+"", "GONE");
//				}else{
//					Log.i("position="+position+"", "Visible");
//					temp=plist.get(position).getDate().substring(5);
//					holder.date.setText(plist.get(position).getDate().substring(5,7)+"月"+plist.get(position).getDate().substring(8)+"日");
//					holder.date.setVisibility(View.VISIBLE);
//				}
//			}		
            if (plist.get(position).isWhetherNeedTitle()) {
                holder.date.setText(plist.get(position).getDate().substring(5, 7) + "月" + plist.get(position).getDate().substring(8) + "日");
                holder.date.setVisibility(View.VISIBLE);
            } else {
                holder.date.setVisibility(View.GONE);
            }
            holder.time.setText(plist.get(position).getTime().substring(0, 5));
            holder.iv1.setBackgroundResource(R.drawable.point1);
            double data1 = Double.parseDouble(plist.get(position).getValueTWorSSY());
            String newColorData1 = data1 + "";
            /**
             *如果数据超标，字体变红
             */
            if (data1 > refValueMax_TWorSSY || data1 < refValueMin_TWorSSY) {
                newColorData1 = "<font color=#ff0000>" + plist.get(position).getValueTWorSSY() + "</font>";
            } else {
                newColorData1 = "<font color=#000000>" + plist.get(position).getValueTWorSSY() + "</font>";
            }
            if (plist.get(position).getValueSZY() != null) {
                int data2 = Integer.parseInt(plist.get(position).getValueSZY());
                String newColorData2 = data2 + "";
                if (data2 > refValueMax_SZY || data2 < refValueMin_SZY) {
                    newColorData2 = "<font color=#ff0000>" + plist.get(position).getValueSZY() + "</font>";
                } else {
                    newColorData2 = "<font color=#000000>" + plist.get(position).getValueSZY() + "</font>";
                }
                holder.data1.setText(Html.fromHtml(newColorData1 + "<font color=#000000>/</font>" + newColorData2 + "<font color=#000000>" + unit + "</font>"));
            } else {
                holder.data1.setText(Html.fromHtml(newColorData1 + "<font color=#000000>" + unit + "</font>"));
            }
            if (dataCode.equals("XT")) {
                if (plist.get(position).getDataName().length() == 4) {
                    holder.data2.setText(plist.get(position).getDataName().substring(0, 2));
                } else {
                    holder.data2.setText(plist.get(position).getDataName().substring(0, 3));
                }
            }
            if (plist.get(position).getValueHR() != null) {
                holder.data2.setText(plist.get(position).getValueHR() + "次");
                holder.iv2.setBackgroundResource(R.drawable.xin);
            }


            return convertView;
        }
    }

    class ViewHolder {
        TextView date;
        ImageView iv1;
        ImageView iv2;
        TextView data1;
        TextView data2;
        TextView time;
    }


    /**
     * 获得历史数据的类
     */
    private class AscyncGetHistoryData extends AsyncTask<Integer, Integer, String> {
        String result = "";
        private String dataCode;

        public AscyncGetHistoryData(String dataCode) {
            // TODO Auto-generated constructor stub
            this.dataCode = dataCode;
        }

        @Override
        protected String doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
//			UtilsSharedData.initDataShare(getActivity());// ////////
//			long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            //获取选中人员的历史数据
            Long patientId = sharedData.getLong(Constant.USER_ID, 0);
            param.put("patientId", patientId + "");
            param.put("dataCode", dataCode);
            param.put("pageNum", "1");
            param.put("num", 8 + "");
            try {
                String url = AbsParam.getBaseUrl() + "/monitordata/record/history";
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                JSONObject data = new JSONObject(result);
                JSONObject historicDection = new JSONObject(data.getString("data"));
                if (dataCode.equals("BP")) {
                    JSONObject historicDection_SSY = new JSONObject(historicDection.getString("SSY"));
                    JSONObject historicDection_SZY = new JSONObject(historicDection.getString("SZY"));
                    JSONObject historicDection_HR = new JSONObject(historicDection.getString("XL"));
                    monitorDataId = historicDection_SSY.getLong("monitorDataId");
                    unit = historicDection_SSY.getString("unit");
                    dataCode = historicDection_SSY.getString("dataCode");
                    dataName = historicDection_SSY.getString("dataName");
                    refValueMax_TWorSSY = historicDection_SSY.getLong("refValueMax");
                    refValueMin_TWorSSY = historicDection_SSY.getLong("refValueMin");
                    refValueMax_SZY = historicDection_SZY.getLong("refValueMax");
                    refValueMin_SZY = historicDection_SZY.getLong("refValueMin");
                    JSONArray array_SSY = new JSONArray(historicDection_SSY.getString("value"));
                    JSONArray array_SZY = new JSONArray(historicDection_SZY.getString("value"));
                    JSONArray array_HR = new JSONArray(historicDection_HR.getString("value"));
                    HBDList.clear();
                    for (int i = 0; i < array_SSY.length(); i++) {
                        JSONObject object_SSY = array_SSY.getJSONObject(i);
                        JSONObject object_SZY = array_SZY.getJSONObject(i);
                        JSONObject object_HR = array_HR.getJSONObject(i);
                        historicDetectionList hDList = new historicDetectionList();
                        hDList.setDate(object_SSY.getString("date"));
                        hDList.setTime(object_SSY.getString("time"));
                        hDList.setValueTWorSSY(object_SSY.getString("value"));
                        hDList.setValueSZY(object_SZY.getString("value"));
                        hDList.setValueHR(object_HR.getString("value"));
                        HBDList.add(hDList);
                    }
                } else if (dataCode.equals("XT")) {
                    String[] code = new String[]{"SQXT", "ZCHXT", "WUCQXT", "WUCHXT", "WANCQXT", "KFXT", "ZCQXT", "WANCHXT"};
                    HBDList.clear();
                    for (int index = 0; index < 8; index++) {
                        JSONObject historicDection_SQXT = new JSONObject(historicDection.getString(code[index]));
                        if (historicDection_SQXT == null) {
                            continue;
                        }
                        monitorDataId = historicDection_SQXT.getLong("monitorDataId");
                        unit = historicDection_SQXT.getString("unit");
                        refValueMax_TWorSSY = historicDection_SQXT.getLong("refValueMax");
                        refValueMin_TWorSSY = historicDection_SQXT.getLong("refValueMin");
                        dataCode = historicDection_SQXT.getString("dataCode");
                        dataName = historicDection_SQXT.getString("dataName");
                        JSONArray array_SQXT = new JSONArray(historicDection_SQXT.getString("value"));
                        for (int i = 0; i < array_SQXT.length(); i++) {
                            JSONObject object_SSY = array_SQXT.getJSONObject(i);
                            historicDetectionList hDList = new historicDetectionList();
//							if(i==0){
//								tempDate=object_SSY.getString("date");
//								hDList.setWhetherNeedTitle(true);
//							}else{
//								if(object_SSY.getString("date").equals(tempDate)){
//									hDList.setWhetherNeedTitle(false);
//								}else{
//									tempDate=object_SSY.getString("date");
//									hDList.setWhetherNeedTitle(true);
//								}
//								
//							}
                            hDList.setDate(object_SSY.getString("date"));
                            hDList.setTime(object_SSY.getString("time"));
                            hDList.setValueTWorSSY(object_SSY.getString("value"));
                            hDList.setDataName(dataName);
                            HBDList.add(hDList);
                        }
                    }

                } else {
                    //其他的值获取历史的方式相同
                    JSONObject historicDection_TW = new JSONObject(historicDection.getString(dataCode));
                    JSONArray array_tw = new JSONArray(historicDection_TW.getString("value"));
                    monitorDataId = historicDection_TW.getLong("monitorDataId");
                    unit = historicDection_TW.getString("unit");
//					dataCode=historicDection_TW.getString("dataCode");
//					dataName=historicDection_TW.getString("dataName");
                    refValueMax_TWorSSY = historicDection_TW.getLong("refValueMax");
                    refValueMin_TWorSSY = historicDection_TW.getLong("refValueMin");
                    HBDList.clear();
                    for (int i = 0; i < array_tw.length(); i++) {
                        JSONObject object_tw = array_tw.getJSONObject(i);
                        historicDetectionList hDList_tw = new historicDetectionList();
                        hDList_tw.setDate(object_tw.getString("date"));
                        hDList_tw.setTime(object_tw.getString("time"));
                        hDList_tw.setValueTWorSSY(object_tw.getString("value"));
                        HBDList.add(hDList_tw);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                hideProgressBar();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hideProgressBar();
            Collections.sort(HBDList, new Comparator<historicDetectionList>() {
                /**
                 *
                 * @param lhs
                 * @param rhs
                 * @return an integer < 0 if lhs is less than rhs, 0 if they are
                 *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                 */
                @Override
                public int compare(historicDetectionList lhs, historicDetectionList rhs) {
                    Date date1 = UtilsDate.stringToDate(lhs.getDate() + " " + lhs.getTime().substring(0, 5));
                    Date date2 = UtilsDate.stringToDate(rhs.getDate() + " " + rhs.getTime().substring(0, 5));
                    Log.i("HBDList", "正在排序！");
                    // 对日期字段进行升序，如果欲降序可采用after方法
                    if (date1.before(date2)) {
                        return 1;
                    }
                    return -1;
                }
            });
            for (int i = 0; i < HBDList.size(); i++) {
                if (i == 0) {
                    tempDate = HBDList.get(i).getDate();
                    HBDList.get(i).setWhetherNeedTitle(true);
                } else {
                    if (HBDList.get(i).getDate().equals(tempDate)) {
                        HBDList.get(i).setWhetherNeedTitle(false);
                    } else {
                        tempDate = HBDList.get(i).getDate();
                        HBDList.get(i).setWhetherNeedTitle(true);
                    }

                }
            }

            HDAdapter.notifyDataSetChanged();
        }

    }


//
//	private class AscyncGetHistoryData extends AsyncTask<Integer, Integer, BeanAllHistoryData>{
//		private BeanAllHistoryData listTemp;
//		@Override
//		protected BeanAllHistoryData doInBackground(Integer... params) {
//			// TODO Auto-generated method stub
//			HashMap<String,String> param=new HashMap<String,String>();
//			Long patientId=sharedData.getLong(Constant.USER_ID, 0);
//			param.put("patientId", patientId+"");
//			param.put("dataCode", "BP");
//			param.put("pageNum", "1");
//			param.put("num", "10");
//			try {
//				String url=AbsParam.getBaseUrl()+"/monitordata/record/historys";
//				String retString=NetTool.sendHttpClientPost(url, param, "utf-8");
////				JSONObject json=new JSONObject(retString);
////				JSONArray jsonString=json.getJSONArray("data");
//				JSONObject data=new JSONObject(retString);
//				JSONObject historicDection=new JSONObject(data.getString("data"));
//				
//				listTemp=jsonToList(retString);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			
//			return listTemp;
//		}
//		
//		@Override
//		protected void onPostExecute(BeanAllHistoryData result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
////			HBDList=result;
//			HBDList= result.getData();
//			HDAdapter=new AdapterHistoryData(HBDList,getApplicationContext());
//			lv.setAdapter(HDAdapter);
//		}
//
//
//
//
//		private BeanAllHistoryData jsonToList(String str){
//			BeanAllHistoryData list=new BeanAllHistoryData();
//			Gson gson=new Gson();
//			if(str!=null){
//				list=gson.fromJson(str, new TypeToken<BeanAllHistoryData>(){}.getType());
//			}
//			return list;
//		};
//	}
}
