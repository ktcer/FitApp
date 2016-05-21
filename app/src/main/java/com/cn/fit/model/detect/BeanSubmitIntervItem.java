package com.cn.fit.model.detect;

/**
 * 提交监测数据存储的实体bean
 *
 * @author kuangtiecheng
 */

public class BeanSubmitIntervItem {
    private String value;
    private long nodeItemId;
    private long valueId;

    public BeanSubmitIntervItem(String value, long nodeItemId, long valueId) {
        this.value = value;
        this.nodeItemId = nodeItemId;
        this.valueId = valueId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getNodeItemId() {
        return nodeItemId;
    }

    public void setNodeItemId(long nodeItemId) {
        this.nodeItemId = nodeItemId;
    }

    public long getValueId() {
        return valueId;
    }

    public void setValueId(long valueId) {
        this.valueId = valueId;
    }


}
