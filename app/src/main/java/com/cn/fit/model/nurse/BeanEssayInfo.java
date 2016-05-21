package com.cn.fit.model.nurse;

/**
 * 文章信息
 *
 * @author kuangtiecheng
 */
public class BeanEssayInfo {
    private long essayID;
    private String essayTitle;
    private String dateString;

    public long getEssayID() {
        return essayID;
    }

    public void setEssayID(long essayID) {
        this.essayID = essayID;
    }

    public String getEssayTitle() {
        return essayTitle;
    }

    public void setEssayTitle(String essayTitle) {
        this.essayTitle = essayTitle;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

}
