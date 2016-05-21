package com.cn.fit.util.dropdownmenu;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cn.fit.R;

import java.util.List;

/**
 * Created by chenkeliang on 2015/11/24.
 * 下拉列表控制器
 */
public class DropdownButtonsController implements DropdownListView.Container {
    private Animation dropdown_in, dropdown_out, dropdown_mask_out;
    private Context contex;
    private View mask;
    private DropdownButton dropdownButton;
    private DropdownListView dropdownListViews;
    private DropdownListView currentDropdownList;
    private List<DropdownItemObject> datasetType;//下拉列表中项的列表
    private DropDownSelectListener dropDownSelectListener;

    public DropdownButtonsController(Context context, List<DropdownItemObject> datasetType, View mask, DropdownButton dropdownButton, DropdownListView dropdownListViews) {
        this.contex = context;
        this.datasetType = datasetType;
        this.mask = mask;
        this.dropdownButton = dropdownButton;
        this.dropdownListViews = dropdownListViews;
    }

    @Override
    public void show(DropdownListView view) {

        if (currentDropdownList != null) {
            currentDropdownList.clearAnimation();
            currentDropdownList.startAnimation(dropdown_out);
            currentDropdownList.setVisibility(View.GONE);
            currentDropdownList.button.setChecked(false);
        }
        currentDropdownList = view;
        mask.clearAnimation();
        mask.setVisibility(View.VISIBLE);
        currentDropdownList.clearAnimation();
        currentDropdownList.startAnimation(dropdown_in);
        currentDropdownList.setVisibility(View.VISIBLE);
        currentDropdownList.button.setChecked(true);
        dropDownSelectListener.onListViewsShow();
    }

    @Override
    public void hide() {
        if (currentDropdownList != null) {
            currentDropdownList.clearAnimation();
            currentDropdownList.startAnimation(dropdown_out);
            currentDropdownList.button.setChecked(false);
            mask.clearAnimation();
            mask.startAnimation(dropdown_mask_out);
        }
        currentDropdownList = null;
    }

    @Override
    public void onSelectionChanged(DropdownListView view) {
        dropDownSelectListener.onPageSelected(view);
    }

    void reset() {
        dropdownButton.setChecked(false);
        dropdownListViews.setVisibility(View.GONE);
        mask.setVisibility(View.GONE);

        dropdownListViews.clearAnimation();
        mask.clearAnimation();
    }

    public void init(long selectedId) {
        reset();
        dropdown_in = AnimationUtils.loadAnimation(contex, R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(contex, R.anim.dropdown_out);
        dropdown_mask_out = AnimationUtils.loadAnimation(contex, R.anim.dropdown_mask_out);
        dropdownListViews.bind(datasetType, dropdownButton, this, selectedId);
        dropdown_mask_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (currentDropdownList == null) {
                    reset();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setOnPageSelectedListener(DropDownSelectListener dropDownSelectListener) {
        this.dropDownSelectListener = dropDownSelectListener;
    }

    public interface DropDownSelectListener {
        public void onPageSelected(DropdownListView view);

        public void onListViewsShow();
    }

}