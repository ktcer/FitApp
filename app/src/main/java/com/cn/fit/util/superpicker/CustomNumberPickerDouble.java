package com.cn.fit.util.superpicker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.PickerView;
import com.cn.fit.util.PickerView.onSelectListener;

public class CustomNumberPickerDouble extends EditText {

    protected String selectedValueLeft, selectedValueRight;
    protected String tips;
    protected String littleTips;
    protected int leftNumMin = 0, leftNumMax = 0, rightNumMin = 0, rightNumMax = 0;
    private List<String> list;


    public void setSelectedValueLeft(String selectedValueLeft) {
        this.selectedValueLeft = selectedValueLeft;
    }


    public void setSelectedValueRight(String selectedValueRight) {
        this.selectedValueRight = selectedValueRight;
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    public void setTips(String tips) {
        this.tips = tips;
    }


    public void setLittleTips(String littleTips) {
        this.littleTips = littleTips;
    }


    public CustomNumberPickerDouble(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        setHintTextColor(getResources().getColor(R.color.font_gray));
        setTextColor(getResources().getColor(R.color.black));
    }


    public void setMaxAndMinValue(int leftNumMin, int leftNumMax, int rightNumMin, int rightNumMax) {
        this.leftNumMin = leftNumMin;
        this.leftNumMax = leftNumMax;
        this.rightNumMin = rightNumMin;
        this.rightNumMax = rightNumMax;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if ((list == null) && (leftNumMin == 0) && (leftNumMax == 0) && (rightNumMin == 0) && (rightNumMax == 0)) {
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
                R.layout.choosepicker_double_layout, null);
        TextView title = (TextView) layout.findViewById(R.id.indicate_Info);
        TextView type = (TextView) layout.findViewById(R.id.info_Type);
        title.setText(tips);
        type.setText(littleTips);

        PickerView pickerView1 = (PickerView) layout
                .findViewById(R.id.choose_Picker1);
        List<String> data1 = new ArrayList<String>();
        for (int a = leftNumMin; a < leftNumMax; a++) {
            data1.add(a + "");
        }
        pickerView1.setData(data1, selectedValueLeft);

        PickerView pickerView2 = (PickerView) layout
                .findViewById(R.id.choose_Picker2);
        List<String> data2 = new ArrayList<String>();
        for (int a = rightNumMin; a < rightNumMax; a++) {
            data2.add(a + "");
        }
        pickerView2.setData(data2, selectedValueRight);
        // 设置选择初始值
//		selectedValueLeft = String.valueOf(((leftNumMin + leftNumMax) / 2));
//		selectedValueRight = String.valueOf(((rightNumMin + rightNumMax) / 2));

        pickerView1.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text) {
                selectedValueLeft = text;
            }
        });

        pickerView2.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text) {
                selectedValueRight = text;
            }
        });
        selectedValueLeft = pickerView1.getText();
        selectedValueRight = pickerView2.getText();
        return layout;
    }


    public void updateText() {
        setText(selectedValueLeft + "." + selectedValueRight);
        setTextColor(getResources().getColor(R.color.black));
    }
}
