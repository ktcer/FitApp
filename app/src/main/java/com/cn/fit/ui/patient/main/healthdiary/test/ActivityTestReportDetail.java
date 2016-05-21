package com.cn.fit.ui.patient.main.healthdiary.test;

import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.healthdiary.BeanResultOfEvaluation;
import com.cn.fit.model.healthdiary.BeanResultOfEvaluationItem;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kuangtiecheng
 */
public class ActivityTestReportDetail extends ActivityBasic {

    private ListView listView;
    private BeanResultOfEvaluation mBeanResultOfEvaluation;
    private List<NLevelItem> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlyalistview);
        mBeanResultOfEvaluation = (BeanResultOfEvaluation) getIntent()
                .getSerializableExtra(Constant.TESTREPORTDETAIL);
        ((TextView) findViewById(R.id.middle_tv)).setText("评估详情");
        listView = (ListView) findViewById(R.id.list);
        listView.setDivider(null);
        // list.setGroupIndicator(null);
        initializeHeaderAndFooter();
        initializeAdapter();
    }

    // private void initializePadding() {
    // float density = getResources().getDisplayMetrics().density;
    // int padding = addPadding ? (int) (16 * density) : 0;
    // list.setPadding(padding, padding, padding, padding);
    // }
    //
    private void initializeHeaderAndFooter() {

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout header1 = (LinearLayout) inflater.inflate(
                R.layout.item_testresult_head, listView, false);
        listView.addHeaderView(header1, null, false);
        //
        UtilsSharedData.initDataShare(this);
        TextView txt_desc = (TextView) header1.findViewById(R.id.txt_title);
        txt_desc.setText("亲爱的 "
                + (((UtilsSharedData.getValueByKey(Constant.USER_NAME) == null) || (UtilsSharedData
                .getValueByKey(Constant.USER_ACCOUNT).equals(""))) ? UtilsSharedData
                .getValueByKey(Constant.USER_NAME) : UtilsSharedData
                .getValueByKey(Constant.USER_NAME)) + "：");
        if (mBeanResultOfEvaluation.getWenjuanName().equals("身体测评")) {
            header1.findViewById(R.id.test_result).setVisibility(View.GONE);
        } else {
            if (mBeanResultOfEvaluation.getDetail() != null) {
                if (mBeanResultOfEvaluation.getDetail().equals("高危")) {
                    header1.findViewById(R.id.img4).setVisibility(View.VISIBLE);
                } else if (mBeanResultOfEvaluation.getDetail().equals("极高危")) {
                    header1.findViewById(R.id.img5).setVisibility(View.VISIBLE);
                } else if (mBeanResultOfEvaluation.getDetail().equals("中危")) {
                    header1.findViewById(R.id.img3).setVisibility(View.VISIBLE);
                } else if (mBeanResultOfEvaluation.getDetail().equals("低危")) {
                    header1.findViewById(R.id.img2).setVisibility(View.VISIBLE);
                } else if (mBeanResultOfEvaluation.getDetail().equals("健康")) {
                    header1.findViewById(R.id.img1).setVisibility(View.VISIBLE);
                }
            } else {
                header1.findViewById(R.id.img1).setVisibility(View.VISIBLE);

            }
        }

        //
        // TextView footer = (TextView) inflater.inflate(
        // android.R.layout.simple_list_item_1, list, false);
        // footer.setText("Single footer");
        // list.addFooterView(footer);
        // }
        // initializeAdapter();
    }

    private void setDescColor(TextView tv, String desc) {
        if (desc == null) {
            return;
        }
        if (desc.equals("危险")) {
            tv.setTextColor(getResources().getColor(R.color.red));
        } else if (desc.equals("良好")) {
            tv.setTextColor(getResources().getColor(R.color.blue_second));
        } else if (desc.equals("理想")) {
            tv.setTextColor(getResources().getColor(R.color.lightgreen));
        }
    }

    private void initializeAdapter() {
        // list.setAdapter(new AdapterNLevel(this, mBeanResultOfEvaluation
        // .getReport()));
        // int groupCount = list.getCount();
        //
        // for (int i = 0; i < groupCount; i++) {
        // list.expandGroup(i);
        // }
        // ;

        // AdapterTestResultTitle adapter = new
        // AdapterTestResultTitle(this,mBeanResultOfEvaluation.getReport());
        // list.setAdapter(adapter);
        // int groupCount = list.getCount();
        // for (int i = 0; i < groupCount; i++) {
        // list.expandGroup(i);
        // }

        list = new ArrayList<NLevelItem>();

        // here we create 5 grandparent (top level) NLevelItems
        // then foreach grandparent create a random number of parent (second
        // level) NLevelItems
        // then foreach parent create a random number of children (third level)
        // NLevelItems

        // we pass in an anonymous instance of NLevelView to the NLevelItem,
        // this NLevelView is
        // what supplies the NLevelAdapter with a View for this NLevelItem
        // Random rng = new Random();
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < mBeanResultOfEvaluation.getReport().size(); i++) {

            final NLevelItem grandParent = new NLevelItem(
                    mBeanResultOfEvaluation.getReport().get(i), null,
                    new NLevelView() {

                        @Override
                        public View getView(NLevelItem item) {
                            View view = inflater.inflate(
                                    R.layout.item_testresult_group2, null);
                            TextView tv_title = (TextView) view
                                    .findViewById(R.id.txt_item_title);
                            // tv_title.setBackgroundColor(Color.GREEN);
                            TextPaint paint = tv_title.getPaint();
                            paint.setFakeBoldText(true);
                            String name = (String) ((BeanResultOfEvaluationItem) item
                                    .getWrappedObject()).getItem();
                            name = name.replaceAll("null", "");
                            tv_title.setText(name);

                            if (((BeanResultOfEvaluationItem) item
                                    .getWrappedObject()).getHaschild() == 0) {

                                TextView tv_value = (TextView) view
                                        .findViewById(R.id.txt_item_value);
                                String value = (String) (((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getValue() + " " + ((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getUtil());
                                value = value.replaceAll("null", "");
                                tv_value.setText(value);

//								TextView tv_desc = (TextView) view
//										.findViewById(R.id.txt_item_desc);
//								String desc = (String) ((BeanResultOfEvaluationItem) item
//										.getWrappedObject()).getState();
//								tv_desc.setText(desc);
//								setDescColor(tv_desc, desc);
                            }
                            return view;
                        }
                    });
            list.add(grandParent);
            if (mBeanResultOfEvaluation.getReport().get(i).getHaschild() == 0) {
                continue;
            }
            int numChildren = mBeanResultOfEvaluation.getReport().get(i)
                    .getChildren().size();
            for (int j = 0; j < numChildren; j++) {
                NLevelItem parent = new NLevelItem(mBeanResultOfEvaluation
                        .getReport().get(i).getChildren().get(j), grandParent,
                        new NLevelView() {

                            @Override
                            public View getView(NLevelItem item) {
                                // View view =
                                // inflater.inflate(R.layout.list_item, null);
                                // TextView tv = (TextView)
                                // view.findViewById(R.id.textView);
                                // tv.setBackgroundColor(Color.YELLOW);
                                // String name = (String) ((SomeObject)
                                // item.getWrappedObject()).getName();
                                // tv.setText(name);
                                // return view;
                                //
                                View view = inflater.inflate(
                                        R.layout.item_testresult_group2, null);
                                TextView tv_title = (TextView) view
                                        .findViewById(R.id.txt_item_title);
                                // tv_title.setBackgroundColor(Color.YELLOW);
                                String name = (String) ((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getItem();
                                name = name.replaceAll("null", "");
                                tv_title.setText("  " + name);

                                if (((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getHaschild() == 0) {

                                    TextView tv_value = (TextView) view
                                            .findViewById(R.id.txt_item_value);
                                    String value = (String) (((BeanResultOfEvaluationItem) item
                                            .getWrappedObject()).getValue()
                                            + " " + ((BeanResultOfEvaluationItem) item
                                            .getWrappedObject()).getUtil());
                                    value = value.replaceAll("null", "");
                                    tv_value.setText(value);

                                    TextView tv_desc = (TextView) view
                                            .findViewById(R.id.txt_item_desc);
                                    String desc = (String) ((BeanResultOfEvaluationItem) item
                                            .getWrappedObject()).getState();
//									desc = desc.replaceAll("null", "");
                                    tv_desc.setText(desc);
                                    setDescColor(tv_desc, desc);
                                }
                                return view;
                            }
                        });

                list.add(parent);
                if (mBeanResultOfEvaluation.getReport().get(i).getChildren()
                        .get(j).getHaschild() == 0) {
                    continue;
                }
                int grandChildren = mBeanResultOfEvaluation.getReport().get(i)
                        .getChildren().get(j).getChildren().size();
                for (int k = 0; k < grandChildren; k++) {
                    NLevelItem child = new NLevelItem(mBeanResultOfEvaluation
                            .getReport().get(i).getChildren().get(j)
                            .getChildren().get(k), parent, new NLevelView() {

                        @Override
                        public View getView(NLevelItem item) {
                            // View view = inflater.inflate(R.layout.list_item,
                            // null);
                            // TextView tv = (TextView)
                            // view.findViewById(R.id.textView);
                            // tv.setBackgroundColor(Color.GRAY);
                            // String name = (String) ((SomeObject)
                            // item.getWrappedObject()).getName();
                            // tv.setText(name);
                            // return view;

                            View view = inflater.inflate(
                                    R.layout.item_testresult_group2, null);
                            TextView tv_title = (TextView) view
                                    .findViewById(R.id.txt_item_title);
                            // tv_title.setBackgroundColor(Color.GRAY);
                            String name = (String) ((BeanResultOfEvaluationItem) item
                                    .getWrappedObject()).getItem();
                            name = name.replaceAll("null", "");
                            tv_title.setText("    " + name);

                            if (((BeanResultOfEvaluationItem) item
                                    .getWrappedObject()).getHaschild() == 0) {

                                TextView tv_value = (TextView) view
                                        .findViewById(R.id.txt_item_value);
                                String value = (String) (((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getValue() + " " + ((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getUtil());
                                value = value.replaceAll("null", "");
                                tv_value.setText(value);

                                TextView tv_desc = (TextView) view
                                        .findViewById(R.id.txt_item_desc);
                                String desc = (String) ((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getState();
//								desc = desc.replaceAll("null", "");
                                tv_desc.setText(desc);
                                setDescColor(tv_desc, desc);
                            }
                            return view;
                        }
                    });

                    list.add(child);
                }
            }
        }

        NLevelAdapter adapter = new NLevelAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) listView
                        .getAdapter();
                NLevelAdapter adapter = (NLevelAdapter) listAdapter
                        .getWrappedAdapter();
                adapter.toggle(arg2 - 1);
                adapter.getFilter().filter();

            }
        });
    }

}
