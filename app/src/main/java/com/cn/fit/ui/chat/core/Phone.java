package com.cn.fit.ui.chat.core;

import com.cn.fit.ui.chat.common.utils.DemoUtils;

/**
 * com.cn.aihu.ui.chat.core in ECDemo_Android
 * Created by Jorstin on 2015/3/21.
 */
public class Phone {

    /**
     *
     */
    private static final long serialVersionUID = -1086069361640884784L;
    public static final int CUSTOM_TYPE = 0;
    public long rawContactId;// 联系人ID
    public long id;// 数据表ID
    public int type;// 类型 0为自定义类型
    public String customLabel;// 自定义
    private String phoneNum;

    public Phone(int type, String phoneNum) {
        super();
        phoneNum = DemoUtils.filterUnNumber(phoneNum);
        this.type = type;
        this.phoneNum = phoneNum;

    }

    public Phone(String phoneNum, String customLabel) {
        super();
        phoneNum = DemoUtils.filterUnNumber(phoneNum);
        this.type = CUSTOM_TYPE;
        this.phoneNum = phoneNum;
        this.customLabel = customLabel;
    }

    public Phone(long rawContactId, long id, int type, String phoneNum) {
        super();
        phoneNum = DemoUtils.filterUnNumber(phoneNum);
        this.rawContactId = rawContactId;
        this.id = id;
        this.type = type;
        this.phoneNum = phoneNum;
    }

    public Phone(long rawContactId, long id, String phoneNum, String customLabel) {
        super();
        phoneNum = DemoUtils.filterUnNumber(phoneNum);
        this.rawContactId = rawContactId;
        this.id = id;
        this.type = CUSTOM_TYPE;
        this.customLabel = customLabel;
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        phoneNum = DemoUtils.filterUnNumber(phoneNum);
        this.phoneNum = phoneNum;
    }

    public void releaseResources() {
        phoneNum = null;
        customLabel = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phoneNum == null) ? 0 : phoneNum.hashCode());
        result = prime * result + type;
/*		if (type == 0) {
            result = prime * result + ((customLabel == null) ? 0 : customLabel.hashCode());
		}*/
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Phone other = (Phone) obj;
        if (phoneNum == null) {
            if (other.phoneNum != null)
                return false;
        } else if (!phoneNum.equals(other.phoneNum))
            return false;
/*		if (type != other.type)
			return false;
		if (type == 0) {
			if (customLabel == null) {
				if (other.customLabel != null)
					return false;
			} else if (!customLabel.equals(other.customLabel))
				return false;
		}*/
        return true;
    }

}

