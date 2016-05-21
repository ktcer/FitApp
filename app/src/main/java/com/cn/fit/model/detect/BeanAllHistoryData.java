package com.cn.fit.model.detect;

import java.util.List;

public class BeanAllHistoryData {
    private int statusCode;
    private int valid;
    private int dataType;
    private List<BeanHistoryDetection> data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public List<BeanHistoryDetection> getData() {
        return data;
    }

    public void setData(List<BeanHistoryDetection> data) {
        this.data = data;
    }


}
