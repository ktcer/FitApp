package com.cn.fit.ui.patient.main.healthpost.doctorinterview;

import android.graphics.Bitmap;

public class VideoContent {
    private Bitmap videoPicture;
    private String videoTitle;
    private String date;
    private String lecturer;
    private String duration;

    public Bitmap getVideoPicture() {
        return videoPicture;
    }

    public void setVideoPicture(Bitmap videoPicture) {
        this.videoPicture = videoPicture;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
