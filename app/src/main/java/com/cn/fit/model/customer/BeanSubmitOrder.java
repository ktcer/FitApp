package com.cn.fit.model.customer;

/**
 * /ad/app/submitorder
 * Created by ktcer on 2016/1/6.
 */
public class BeanSubmitOrder {
    private String ddh;//订单号 string
    private String detail; //详情 string
    private int result;//

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
