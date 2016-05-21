package com.cn.fit.model.healthdiary;

/**
 * @author kuangtiecheng
 */
public class BeanHealthProgram {
    private String planName;
    private String imgUrl;
    private long id;
    /**
     * 表示健康方案是否可用（0未交费不可用、1免费可用、2已缴费可用）
     */
    private long templateId;
    /**
     * 康复日记简介
     */
    private String profile;
    /**
     * 是否绑定（0没绑定、1已绑定）
     */
    private int is_tie;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * 表示健康方案是否可用，（0未交费不可用、1免费可用、2已缴费可用）
     */
    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * 是否绑定（0没绑定、1已绑定）
     */
    public int getIs_tie() {
        return is_tie;
    }

    public void setIs_tie(int is_tie) {
        this.is_tie = is_tie;
    }
}
