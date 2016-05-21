package com.cn.fit.model.personinfo;

public class BeanPersonInfo {
    private int result;
    private String detail;
    private long id;
    private String name;
    private int ifModify;
    private String txlj;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIfModify() {
        return ifModify;
    }

    public void setIfModify(int ifModify) {
        this.ifModify = ifModify;
    }

    public String getTxlj() {
        return txlj;
    }

    public void setTxlj(String txlj) {
        this.txlj = txlj;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
