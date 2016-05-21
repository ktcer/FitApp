package com.cn.fit.model.family;

public class BeanFamilyResult {

    //	public static final int Integer = 0;
//	public static final int String = 1;
    public static final int JsonObject = 2;
//	public static final int JsonArray = 3;

    private int statusCode = -1;
    private int valid = 0;
    private int dataType = JsonObject;
    private Object data;
    private String detail;


	/* private T dataObj; */

    public int getStatusCode() {
        return statusCode;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
	/*
	 * public T getDataObj() { return dataObj; } public void setDataObj(T
	 * dataObj) { this.dataObj = dataObj; }
	 */

    @Override
    public String toString() {
        return "Result [statusCode=" + statusCode + ", valid=" + valid
                + ", dataType=" + dataType + ", data=" + data + "]";
    }

}
