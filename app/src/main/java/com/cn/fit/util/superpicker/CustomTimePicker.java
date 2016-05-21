package com.cn.fit.util.superpicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.cn.fit.R;

public class CustomTimePicker extends EditText implements android.app.TimePickerDialog.OnTimeSetListener {
    public interface OnTimeSetListener {
        public void onTimeSet(CustomTimePicker view, int hour, int minute);
    }

    protected int hour;
    protected int minute;
    protected OnTimeSetListener onTimeSetListener;
    protected java.text.DateFormat timeFormat;

    public int getHour() {
        return hour;
    }

    public OnTimeSetListener getOnTimeSetListener() {
        return onTimeSetListener;
    }

    public void setOnTimeSetListener(OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    public void setHour(int hour) {
        this.hour = hour;
        updateText();
    }

    public int getMinute() {
        return minute;
    }

    public void setNow() {
        Calendar c = Calendar.getInstance();
        setTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    public void setMinute(int minute) {
        this.minute = minute;
        updateText();
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        updateText();
    }

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        timeFormat = DateFormat.getTimeFormat(getContext());

        setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        setFocusable(false);
        setHintTextColor(getResources().getColor(R.color.font_gray));
        setNow();
        setTextColor(getResources().getColor(R.color.black));
    }

    protected void showTimePicker() {
        TimePickerDialog timePickerDialog;

        timePickerDialog = new TimePickerDialog(
                getContext(),
                this,
                getHour(),
                getMinute(),
                true);
        timePickerDialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
            showTimePicker();

        return super.onTouchEvent(event);
    }


    public java.text.DateFormat getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(java.text.DateFormat timeFormat) {
        this.timeFormat = timeFormat;
        updateText();
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hour,
                          int minute) {
        setTime(hour, minute);
        clearFocus();

        if (onTimeSetListener != null)
            onTimeSetListener.onTimeSet(this, hour, minute);

    }

    public void updateText() {
        Calendar cal = new GregorianCalendar(0, 0, 0, getHour(), getMinute());
        setText(timeFormat.format(cal.getTime()));
    }
}
