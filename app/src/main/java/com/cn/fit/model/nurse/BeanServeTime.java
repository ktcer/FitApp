package com.cn.fit.model.nurse;

public class BeanServeTime {
    /**
     * 时长
     */
    private String time;
    /**
     * 价格
     */
    private String price;
    /**
     * 服务id
     */
    private long servetimeid;
    /**
     * 服务时间
     */
    private int days;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getServetimeid() {
        return servetimeid;
    }

    public void setServetimeid(long servetimeid) {
        this.servetimeid = servetimeid;
    }


}
