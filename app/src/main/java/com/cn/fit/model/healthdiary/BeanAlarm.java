package com.cn.fit.model.healthdiary;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/11/6 上午9:20:18
 * @parameter
 * @return
 */
public class BeanAlarm {
    public Integer alarmid;// 主键
    public Integer id;// 外键
    public String dateday;
    public String daytime;
    public Integer valid;

    public Integer getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(Integer alarmid) {
        this.alarmid = alarmid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateday() {
        return dateday;
    }

    public void setDateday(String dateday) {
        this.dateday = dateday;
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "BeanAlarm [alarmid=" + alarmid + ", id=" + id + ", dateday="
                + dateday + ", daytime=" + daytime + ", valid=" + valid + "]";
    }

}
