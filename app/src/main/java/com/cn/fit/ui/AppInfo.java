package com.cn.fit.ui;

/**
 * 应用软件相关信息
 *
 * @author kuangtiecheng
 */
public class AppInfo {

    //包名称
    private String packageName = new String();

    //版本编号
    private int versionCode = 0;

    //版本名称（如：1.2.3）
    private String versionName = new String();

    /**
     * 设备信息，手机号
     */
    private String tel = "unknown";

    /**
     * 设备信息，唯一识别号
     */
    private String simSerial = "unknown";

    /**
     * 设备信息，SIM卡唯一串号
     */
    private String simNum = "unknown";

    static AppInfo instance = new AppInfo();

    public static AppInfo getInstance() {
        return instance;
    }

    private AppInfo() {
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[PackageName]").append(packageName);
        buf.append(" [VersionCode]").append(versionCode);
        buf.append(" [VersionName]").append(versionName);
        buf.append(" [Tel]").append(tel);
        buf.append(" [SimSerial]").append(simSerial);
        buf.append(" [SimNum]").append(simNum);
        return buf.toString();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        //异常手机号，如： 13804320002#0#218027148090#8889
        if (tel.length() > 11) {
            return;
        }
        this.tel = tel;
    }

    public String getSimSerial() {
        return simSerial;
    }

    public void setSimSerial(String simSerial) {
        this.simSerial = simSerial;
    }

    public String getSimNum() {
        return simNum;
    }

    public void setSimNum(String simNum) {
        this.simNum = simNum;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
