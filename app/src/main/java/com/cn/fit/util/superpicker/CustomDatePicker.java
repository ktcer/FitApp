package com.cn.fit.util.superpicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.cn.fit.R;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

public class CustomDatePicker extends EditText implements DatePickerDialog.OnDateSetListener {
    public interface OnDateSetListener {
        public void onDateSet(CustomDatePicker view, int year, int month, int day);
    }

    protected int year;
    protected int month;
    protected int day;
    protected long maxDate = -1;
    protected long minDate = -1;
    protected OnDateSetListener onDateSetListener = null;
    //	protected java.text.DateFormat dateFormat;
    protected SimpleDateFormat dateFormat;


    public OnDateSetListener getOnDateSetListener() {
        return onDateSetListener;
    }

    public void setOnDateSetListener(OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");//

//		dateFormat = DateFormat.getMediumDateFormat(getContext());

        setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        setFocusable(false);
        setHintTextColor(getResources().getColor(R.color.font_gray));
        setTextColor(getResources().getColor(R.color.black));
        setToday();
        UtilsSharedData.initDataShare(context);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        updateText();
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
        updateText();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
        updateText();
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        updateText();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
            showDatePicker();

        return super.onTouchEvent(event);
    }


    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
        updateText();
    }

    public void setMaxDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 23, 59, 59);
        this.maxDate = c.getTimeInMillis();
    }

    public void setMinDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        this.minDate = c.getTimeInMillis();
    }

    public void setToday() {
        String userBirth = UtilsSharedData.getValueByKey(Constant.USER_BIRTHDAY);
        if (userBirth.length() < 6) {
            Calendar c = Calendar.getInstance();
            this.year = c.get(Calendar.YEAR);
            this.month = c.get(Calendar.MONTH);
            this.day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String[] birth = userBirth.split("-");
            this.year = Integer.parseInt(birth[0]);
            this.month = Integer.parseInt(birth[1]) - 1;
            this.day = Integer.parseInt(birth[2]);
        }

    }

    protected void showDatePicker() {
        DatePickerDialog datePickerDialog;

        datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                getYear(),
                getMonth(),
                getDay());

        if (this.maxDate != -1) {
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
        }
        if (this.minDate != -1) {
            datePickerDialog.getDatePicker().setMinDate(minDate);
        }

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year,
                          int month, int day) {
        setDate(year, month, day);
        clearFocus();

        if (onDateSetListener != null)
            onDateSetListener.onDateSet(this, year, month, day);
    }

    public void updateText() {
        Calendar cal = new GregorianCalendar(getYear(), getMonth(), getDay());
        setText(dateFormat.format(cal.getTime()));
        setTextColor(getResources().getColor(R.color.black));
    }
}
