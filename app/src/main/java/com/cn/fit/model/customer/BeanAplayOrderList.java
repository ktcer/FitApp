package com.cn.fit.model.customer;


public class BeanAplayOrderList {
    //	private static final long serialVersionUID = -7060210544600464481L;
    private String cover;//封面url string
    private float money;//支付钱数 number
    private String ddh;//订单号 number
    private String serveType;// 服务类型 string
    private String startTime;//活动时间 string
    private byte state;//状态 number
    private String title;//
    private int orderID;
    private int ifComments;
    private long coachID;
    private long classID;

    public long getClassID() {
        return classID;
    }

    public void setClassID(long classID) {
        this.classID = classID;
    }

    public long getCoachID() {
        return coachID;
    }

    public void setCoachID(long coachID) {
        this.coachID = coachID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getIfComments() {
        return ifComments;
    }

    public void setIfComments(int ifComments) {
        this.ifComments = ifComments;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getServeType() {
        return serveType;
    }

    public void setServeType(String serveType) {
        this.serveType = serveType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
