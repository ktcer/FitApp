package com.cn.fit.ui.patient.main.healthdiary.test.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MoviesExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private String mStudio = "";
    private ArrayList<String> mMovies = new ArrayList<String>();

    public MoviesExpandableListViewAdapter(Context context, String studio, ArrayList<String> movies) {
        mContext = context;
        mStudio = studio;
        mMovies = movies;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mMovies.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mStudio;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mMovies.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
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
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        TextView view = (TextView) convertView;

        if (view == null) {

            view = new TextView(mContext);
            view.setText(mStudio);
            view.setPadding(130, 15, 15, 15);

        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String movie = (String) getChild(groupPosition, childPosition);

        TextView view = (TextView) convertView;

        if (view == null) {

            view = new TextView(mContext);
            view.setText(movie);
            view.setPadding(160, 15, 15, 15);

        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
