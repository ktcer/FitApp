package com.cn.fit.model.healthdiary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/27 上午10:24:25
 * @parameter
 * @return
 */
public class BeanHealthDiaryLocal implements Parcelable {
    public Integer id;
    public Integer userid;
    public String dateday;
    public String daytime;
    public String content;
    public String path;
    public Integer valid;
    public Integer diaryid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDiaryid() {
        return diaryid;
    }

    public void setDiaryid(Integer diaryid) {
        this.diaryid = diaryid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "BeanHealthDiaryLocal [id=" + id + ", userid=" + userid
                + ", dateday=" + dateday + ", daytime=" + daytime
                + ", content=" + content + ", path=" + path + ", valid="
                + valid + ", diaryid=" + diaryid + "]";
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(id);
        dest.writeInt(userid);
        dest.writeString(dateday);
        dest.writeString(daytime);
        dest.writeString(content);
        dest.writeString(path);
        dest.writeInt(valid);
        dest.writeInt(diaryid);
    }

    public static final Parcelable.Creator<BeanHealthDiaryLocal> CREATOR = new Creator<BeanHealthDiaryLocal>() {
        @Override
        public BeanHealthDiaryLocal[] newArray(int size) {
            return new BeanHealthDiaryLocal[size];
        }

        @Override
        public BeanHealthDiaryLocal createFromParcel(Parcel in) {
            BeanHealthDiaryLocal deleteMsa = new BeanHealthDiaryLocal();
            deleteMsa.id = in.readInt();
            deleteMsa.userid = in.readInt();
            deleteMsa.dateday = in.readString();
            deleteMsa.daytime = in.readString();
            deleteMsa.content = in.readString();
            deleteMsa.path = in.readString();
            deleteMsa.valid = in.readInt();
            deleteMsa.diaryid = in.readInt();
            return deleteMsa;
        }
    };
}
