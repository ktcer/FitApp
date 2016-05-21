package com.cn.fit.ui.patient.main.healthdiary.test.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cn.fit.model.healthdiary.BeanResultOfEvaluationItem;

public class AdapterTestResultChildren extends BaseExpandableListAdapter {

    private Context mContext;
    private List<BeanResultOfEvaluationItem> report;

    public AdapterTestResultChildren(Context context,
                                     List<BeanResultOfEvaluationItem> report) {

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
        if (report.get(groupPosition).getChildren() == null) {
            return 0;
        } else {
            if (report.get(groupPosition).getChildren().size() == 0) {
                return 0;
            } else {
                return report.get(groupPosition).getChildren().size();
            }
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (report == null) {
            return null;
        } else {
            return report.get(groupPosition);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (report.get(groupPosition).getChildren() == null) {
            return null;
        } else {
            if (report.get(groupPosition).getChildren().size() == 0) {
                return null;
            } else {
                return report.get(groupPosition).getChildren().get(childPosition);
            }
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

        if (view == null) {

            view = new TextView(mContext);
            view.setText(report.get(groupPosition).getItem());
            view.setPadding(100, 15, 15, 15);

        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition,
                             int childPosition,
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent) {


//		String studio = (String) getChild(groupPosition, childPosition);
//		ArrayList<String> movies = mMoviesMap.get(studio);
//		
//		CustomExpandableListView view = (CustomExpandableListView) convertView;
//		
//		if(view==null){
//			
//			view = new CustomExpandableListView(mContext);
//			MoviesExpandableListViewAdapter adapter = new MoviesExpandableListViewAdapter(mContext,studio,movies);
//			view.setAdapter(adapter);
//			
//		}
//		
//		return view;
        BeanResultOfEvaluationItem bean = (BeanResultOfEvaluationItem) getChild(groupPosition, childPosition);

        TextView view = (TextView) convertView;

        if (view == null) {

            view = new TextView(mContext);
            view.setText(bean.getItem());
            view.setPadding(160, 15, 15, 15);

        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return false;
    }
}
