package com.cn.fit.ui.patient.others.mement;

import android.graphics.Bitmap;

public class PostContent {
    private String headLine;
    private String text;
    private Bitmap postImage;

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getPostImage() {
        return postImage;
    }

    public void setPostImage(Bitmap postImage) {
        this.postImage = postImage;
    }

}
