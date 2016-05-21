package com.cn.fit.ui.patient.main.healthdiary.datepicker;

/**
 * Very simple helper class that defines a time unit with a label (text) its start-
 * and end date
 */
public class TimeObject {
    public final CharSequence text;
    public final long startTime, endTime;

    public TimeObject(final CharSequence text, final long startTime, final long endTime) {
        this.text = text;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}