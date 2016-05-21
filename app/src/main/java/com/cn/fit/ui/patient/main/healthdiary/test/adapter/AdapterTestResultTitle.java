package com.cn.fit.ui.patient.main.healthdiary.test.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cn.fit.model.healthdiary.BeanResultOfEvaluationItem;

public class AdapterTestResultTitle extends BaseExpandableListAdapter {

    private Context mContext;
    private List<BeanResultOfEvaluationItem> report;

    public AdapterTestResultTitle(Context context, List<BeanResultOfEvaluationItem> report
    ) {
        mContext = context;
        this.report = report;
    }

    @Override
    public int getGroupCount() {
        if (report == null) {
            return 0;
        } else {
            return report.size();
        }

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (getGroupCount() == 0) {
            return 0;
        }
        if (groupPosition == getGroupCount()) {
            groupPosition -= 1;
        }
        if ((report.get(groupPosition).getChildren() == null) || report.get(groupPosition).getChildren().size() == 0) {
            return 0;
        } else {
            return report.get(groupPosition).getChildren().size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (getGroupCount() == 0) {
            return null;
        }
        return report.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (getGroupCount() == 0) {
            return null;
        }
        if (groupPosition == getGroupCount()) {
            groupPosition -= 1;
        }
        if ((report.get(groupPosition).getChildren() == null) || report.get(groupPosition).getChildren().size() == 0) {
            return null;
        } else {
            return report.get(groupPosition).getChildren().get(childPosition);
        }

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition,
                             boolean isExpanded,
                             View convertView,
                             ViewGroup parent) {

        TextView view = (TextView) convertView;
        BeanResultOfEvaluationItem bean = (BeanResultOfEvaluationItem) getGroup(groupPosition);

        if (view == null) {

            view = new TextView(mContext);
            view.setText(bean.getItem());
            view.setPadding(20, 20, 20, 20);

        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition,
                             int childPosition,
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent) {

        CustomExpandableListView view = (CustomExpandableListView) convertView;
//		BeanResultOfEvaluationItem bean = (BeanResultOfEvaluationItem) getChild(groupPosition, childPosition);

        if (view == null) {

            view = new CustomExpandableListView(mContext);
            AdapterTestResultChildren gelva = new AdapterTestResultChildren(mContext, report.get(groupPosition).getChildren());
            view.setAdapter(gelva);

        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
