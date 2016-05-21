package com.cn.fit.util.superpicker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.PickerView;
import com.cn.fit.util.PickerView.onSelectListener;

public class CustomNumberPicker extends EditText {

    protected String selectedValue;
    protected String tips;
    protected int minValue, maxValue;
    private List<String> list;


    public void setList(List<String> list) {
        this.list = list;
    }


    public void setTips(String tips) {
        this.tips = tips;
    }


    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        setHintTextColor(getResources().getColor(R.color.font_gray));
        setTextColor(getResources().getColor(R.color.black));
    }


    public void setMaxAndMinValue(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if ((list == null) && (minValue == 0) && (maxValue == 0)) {
                    ToastUtil.showMessage(R.string.loaduncomplete);
                } else {
                    if (list != null) {
                        if (list.size() == 0) {
                            ToastUtil.showMessage(R.string.loaduncomplete);
                            return super.onTouchEvent(event);
                        }
                    }
                    showChooseDialog();
                }
            }

        return super.onTouchEvent(event);
    }

    // 弹出选择弹窗
    private void showChooseDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
        builder.setTitle("提示");
        builder.setContentView(formChooseDialog());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                updateText();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    // 构建选择弹窗
    private LinearLayout formChooseDialog() {

        LayoutInflater inflaterDl = LayoutInflater.from(getContext());
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                R.layout.choosepicker_single_layout, null);
        TextView title = (TextView) layout
                .findViewById(R.id.indicate_Info);
        title.setText(tips);
        PickerView pickerView = (PickerView) layout
                .findViewById(R.id.minute_pv);
        pickerView.setVisibility(View.VISIBLE);
        if (list == null) {
            List<String> data3 = new ArrayList<String>();
            for (int a = minValue; a < maxValue; a++) {
                data3.add(a + "");
            }
            pickerView.setData(data3);
        } else {
            pickerView.setData(list);
        }

        pickerView.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text) {
                // TODO Auto-generated method stub
                selectedValue = text;
            }
        });
        selectedValue = pickerView.getText();
        return layout;
    }


    public void updateText() {
        setText(selectedValue);
        setTextColor(getResources().getColor(R.color.black));
    }
}
