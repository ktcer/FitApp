package com.cn.fit.model.healthdiary;

import java.io.Serializable;

public class BeanIntervItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -786832779507402960L;
    // T_INTERVENTION_ITEM_VALUE 对应的ID，-1表示该点没有值，编辑后会新建一个节点
    private long _id = -1;
    // T_INTERVENTION_NODE_ITEMS 表的ID
    private long nodeItemId;
    private String code;
    private String name;
    private double value;
    private String unit;

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public long getNodeItemId() {
        return nodeItemId;
    }

    public void setNodeItemId(long nodeItemId) {
        this.nodeItemId = nodeItemId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}