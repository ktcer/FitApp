/**
 * @Title: MessageVo.java
 * @Package com.test.testim
 * @Description: TODO(鐢ㄤ竴鍙ヨ瘽鎻忚堪璇ユ枃浠跺仛浠?箞)
 * @author kuangtiecheng.Z
 * @date 2013-2-27 涓婂崍11:23:17
 * @version V1.0
 */
package com.cn.fit.ui.patient.myres.mycustomer;

public class MessageVo {
    public static final int MESSAGE_FROM = 0;
    public static final int MESSAGE_TO = 1;

    private int direction;    //璇ュ彉閲忎负娑堟伅鏄敹鍒?MESSAGE_FROM)鐨勮繕鏄彂閫?MESSAGE_TO)鐨?
    private String content;   //鍐呭
    private String time;      //鏃堕棿

    public MessageVo(int direction, String content, String time) {
        super();
        this.direction = direction;
        this.content = content;
        this.time = time;
    }

    public int getDirection() {
        return direction;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
