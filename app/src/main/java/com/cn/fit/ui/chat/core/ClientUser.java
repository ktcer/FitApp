package com.cn.fit.ui.chat.core;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.yuntongxun.ecsdk.ECInitParams;

/**
 * Created by Jorstin on 2015/3/18.
 */
public class ClientUser implements Parcelable {

    public static final Parcelable.Creator<ClientUser> CREATOR = new Parcelable.Creator<ClientUser>() {
        public ClientUser createFromParcel(Parcel in) {
            return new ClientUser(in);
        }

        public ClientUser[] newArray(int size) {
            return new ClientUser[size];
        }
    };

    /**
     * 用户注册V账号
     */
    private String userId;
    /**
     * 用户昵称
     */
    private String userName = "";
    /**
     * 用户注册Appkey
     */
    private String appKey;
    /**
     * 用户注册Token
     */
    private String appToken;
    private String password;
    // 1男
    private int sex;
    private long birth;
    private int pVersion;
    private ECInitParams.LoginAuthType loginAuthType;

    public int getpVersion() {
        return pVersion;
    }

    public void setpVersion(int pVersion) {
        this.pVersion = pVersion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ECInitParams.LoginAuthType getLoginAuthType() {
        return loginAuthType;
    }

    public void setLoginAuthType(ECInitParams.LoginAuthType loginAuthType) {
        this.loginAuthType = loginAuthType;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public ClientUser(String userId) {
        this.userId = userId;
    }

    private ClientUser(Parcel in) {
        this.userId = in.readString();
        this.userName = in.readString();
        this.appKey = in.readString();
        this.appToken = in.readString();
        this.password = in.readString();
        this.sex = in.readInt();
        this.birth = in.readLong();
        this.pVersion = in.readInt();
        this.loginAuthType = ECInitParams.LoginAuthType.fromId(in.readInt());
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userName", userName);
            jsonObject.put("appKey", appKey);
            jsonObject.put("appToken", appToken);
            jsonObject.put("inviteCode", password);
            jsonObject.put("sex", sex);
            jsonObject.put("birth", birth);
            jsonObject.put("pVersion", pVersion);
            jsonObject.put("loginAuthType", loginAuthType.getAuthTypeValue());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "ClientUser{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", appKey='" + appKey + '\'' +
                ", appToken='" + appToken + '\'' +
                ", inviteCode='" + password + '\'' +
                '}';
    }

    public ClientUser from(String input) {
        JSONObject object = null;
        try {
            object = new JSONObject(input);
            if (object.has("userId")) {
                this.userId = object.getString("userId");
            }
            if (object.has("userName")) {
                this.userName = object.getString("userName");
            }
            if (object.has("appKey")) {
                this.appKey = object.getString("appKey");
            }
            if (object.has("appToken")) {
                this.appToken = object.getString("appToken");
            }
            if (object.has("inviteCode")) {
                this.password = object.getString("inviteCode");
            }
            if (object.has("sex")) {
                this.sex = object.getInt("sex");
            }
            if (object.has("birth")) {
                this.birth = object.getLong("birth");
            }
            if (object.has("pVersion")) {
                this.pVersion = object.getInt("pVersion");
            }
            if (object.has("loginAuthType")) {
                this.loginAuthType = ECInitParams.LoginAuthType.fromId(object.getInt("loginAuthType"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.appKey);
        dest.writeString(this.appToken);
        dest.writeString(this.password);
        dest.writeInt(this.sex);
        dest.writeLong(this.birth);
        dest.writeInt(this.pVersion);
        dest.writeInt(this.loginAuthType.getAuthTypeValue());
    }
}
