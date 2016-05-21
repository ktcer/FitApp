package com.cn.fit.model.healthdiary;

import java.io.Serializable;

public class BeanContentNode implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5384791249190980971L;
    private int type; // 1表示文字，2表示图片，3表示视频(视频会多一张缩略图地址)，4表示网页链接
    private String content; // 图片、视频、网页链接都会是一个url地址
    private String url;
    private String thumbnail; // 缩略图。如果该字段不为空则显示该图片
    private int sort; //同一个sort表示是同一个节点的不同资源
    private int index; // 同一个sort下的第几个资源
    private String title;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
