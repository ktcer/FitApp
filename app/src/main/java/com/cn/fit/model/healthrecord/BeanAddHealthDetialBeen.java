package com.cn.fit.model.healthrecord;

import java.util.List;

public class BeanAddHealthDetialBeen {

    private long keyid;
    private String key;
    private int valueType;
    private List<BeanValueBeen> values;

    public long getKeyid() {
        return keyid;
    }

    public void setKeyid(long keyid) {
        this.keyid = keyid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }

    public List<BeanValueBeen> getValues() {
        return values;
    }

    public void setValues(List<BeanValueBeen> values) {
        this.values = values;
    }


}
