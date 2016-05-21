package com.cn.fit.model.healthpost;

public class BeanItemOfSummary {
    private String items;
    private String content;

    public BeanItemOfSummary() {

    }

    public BeanItemOfSummary(String items, String content) {
        this.items = items;
        this.content = content;
    }


    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
