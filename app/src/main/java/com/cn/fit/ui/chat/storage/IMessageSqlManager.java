/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;

import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.FileAccessor;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.ui.chatting.model.ImgInfo;
import com.cn.fit.ui.chat.ui.contact.ContactLogic;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;

/**
 * 消息数据库管理
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-11
 */
public class IMessageSqlManager extends AbstractSQLManager {

    /**
     * 消息未读状态--未读
     */
    static final public int IMESSENGER_TYPE_UNREAD = 0;
    /**
     * 消息未读状态--已读
     */
    static final public int IMESSENGER_TYPE_READ = 1;

    static final public int IMESSENGER_BOX_TYPE_ALL = 0;
    /**
     * 信箱类型--收件箱
     */
    static final public int IMESSENGER_BOX_TYPE_INBOX = 1;
    static final public int IMESSENGER_BOX_TYPE_SENT = 2;
    /**
     * 信箱类型--草稿箱
     */
    static final public int IMESSENGER_BOX_TYPE_DRAFT = 3;
    /**
     * 信箱类型--发件箱
     */
    static final public int IMESSENGER_BOX_TYPE_OUTBOX = 4;
    static final public int IMESSENGER_BOX_TYPE_FAILED = 5;
    static final public int IMESSENGER_BOX_TYPE_QUEUED = 6;

    public static final String ACTION_SESSION_DEL = "com.yuntonxun.ecdemo.ACTION_SESSION_DEL";
    public static final String ACTION_GROUP_DEL = "com.yuntonxun.ecdemo.ACTION_GROUP_DEL";
    private static IMessageSqlManager instance;

    private IMessageSqlManager() {
        super();
    }

    private static IMessageSqlManager getInstance() {
        if (instance == null) {
            instance = new IMessageSqlManager();
        }
        return instance;
    }

    public static void checkContact(String contactid) {
        checkContact(contactid, null);
    }

    public static void checkContact(String contactid, String username) {

        if (!ContactSqlManager.hasContact(contactid)) {
            ECContacts c = ContactSqlManager.getCacheContact(contactid);
            if (c == null) {
                c = new ECContacts(contactid);
                c.setNickname(contactid);
            }
            c.setContactid(contactid);
            if (TextUtils.isEmpty(username)) {
                int index = ContactSqlManager.getIntRandom(3, 0);
                String remark = ContactLogic.CONVER_PHONTO[index];
                c.setRemark(remark);
            }
            ContactSqlManager.insertContact(c);
        }
    }


    /**
     * 更新消息到本地数据库
     *
     * @param message 消息
     * @param boxType 消息保存的信箱类型
     * @return 更新的消息ID
     */
    public static long insertIMessage(ECMessage message, int boxType) {
        long ownThreadId = 0;
        long row = 0L;
        try {
            if (!TextUtils.isEmpty(message.getSessionId())) {
                String contactIds = message.getSessionId();
                if (contactIds.toUpperCase().startsWith("G")) {
                    GroupSqlManager.checkGroup(contactIds);
                }
                checkContact(message.getForm());
                ownThreadId = ConversationSqlManager.querySessionIdForBySessionId(contactIds);
                if (ownThreadId == 0) {
                    try {
                        ownThreadId = ConversationSqlManager.insertSessionRecord(message);
                    } catch (Exception e) {
                        LogUtil.e(TAG + " " + e.toString());
                    }
                }
                if (ownThreadId > 0) {
                    int isread = IMESSENGER_TYPE_UNREAD;
                    if (boxType == IMESSENGER_BOX_TYPE_OUTBOX
                            || boxType == IMESSENGER_BOX_TYPE_DRAFT) {
                        isread = IMESSENGER_TYPE_READ;
                    }
                    ContentValues values = new ContentValues();
                    if (boxType == IMESSENGER_BOX_TYPE_DRAFT) {
                        try { // 草稿箱只保存文本
                            values.put(IMessageColumn.OWN_THREAD_ID, ownThreadId);
                            values.put(IMessageColumn.sender, message.getForm());
                            values.put(IMessageColumn.MESSAGE_ID, message.getMsgId());
                            values.put(IMessageColumn.MESSAGE_TYPE, message.getType().ordinal());
                            values.put(IMessageColumn.SEND_STATUS, message.getMsgStatus().ordinal());
                            values.put(IMessageColumn.READ_STATUS, isread);
                            values.put(IMessageColumn.BOX_TYPE, boxType);
                            values.put(IMessageColumn.BODY, ((ECTextMessageBody) message.getBody()).getMessage());
                            values.put(IMessageColumn.USER_DATA, message.getUserData());
                            values.put(IMessageColumn.RECEIVE_DATE, System.currentTimeMillis());
                            values.put(IMessageColumn.CREATE_DATE, message.getMsgTime());

                            row = getInstance().sqliteDB().insertOrThrow(DatabaseHelper.TABLES_NAME_IM_MESSAGE, null, values);
                        } catch (SQLException e) {
                            LogUtil.e(TAG + " " + e.toString());
                        } finally {
                            values.clear();
                        }
                    } else {
                        try {
                            values.put(IMessageColumn.OWN_THREAD_ID, ownThreadId);
                            values.put(IMessageColumn.MESSAGE_ID, message.getMsgId());
                            values.put(IMessageColumn.SEND_STATUS, message.getMsgStatus().ordinal());
                            values.put(IMessageColumn.READ_STATUS, isread);
                            values.put(IMessageColumn.BOX_TYPE, boxType);
                            // values.put(IMessageColumn.VERSION, message.getVersion());
                            values.put(IMessageColumn.USER_DATA, message.getUserData());
                            values.put(IMessageColumn.RECEIVE_DATE, System.currentTimeMillis());
                            values.put(IMessageColumn.CREATE_DATE, message.getMsgTime());
                            values.put(IMessageColumn.sender, message.getForm());
                            values.put(IMessageColumn.MESSAGE_TYPE, message.getType().ordinal());
                            putValues(message, values);
                            LogUtil.d(TAG, "[insertIMessage] " + values.toString());
                            row = getInstance().sqliteDB().insertOrThrow(
                                    DatabaseHelper.TABLES_NAME_IM_MESSAGE,
                                    null, values);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            LogUtil.e(TAG + " " + e.toString());
                        } finally {
                            values.clear();
                        }
                    }
                    getInstance().notifyChanged(contactIds);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return row;
    }

    /**
     * @return
     */
    public static int getMaxVersion() {
        String sql = "select max(version) as maxVersion from " + DatabaseHelper.TABLES_NAME_IM_MESSAGE;
        Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int maxVersion = cursor.getInt(cursor.getColumnIndex("maxVersion"));
                cursor.close();
                ;
                return maxVersion;
            }
        }
        return 0;
    }

    public static ECMessage getLastECMessage() {
        int maxVersion = getMaxVersion();
        String sql = "select im_message.* ,im_thread.sessionId from im_message ,im_thread where version = " + maxVersion + " and im_message.sid=im_thread.id";
        Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                ECMessage ecMessage = packageMessage(cursor);
                String sessionid = cursor.getString(cursor.getColumnIndexOrThrow(IThreadColumn.THREAD_ID));
                ecMessage.setSessionId(sessionid);
                cursor.close();
                if (ecMessage != null) {
                    return ecMessage;
                }
            }
        }
        return null;
    }

    /**
     * 更新消息的状态
     *
     * @param msgId
     * @param sendStatu
     * @return
     */
    public static int setIMessageSendStatus(String msgId, int sendStatu) {
        return setIMessageSendStatus(msgId, sendStatu, 0);
    }

    /**
     * 更新文件的下载状态
     *
     * @param msg
     * @return
     */
    public static int updateIMessageDownload(ECMessage msg) {
        if (msg == null || TextUtils.isEmpty(msg.getMsgId())) {
            return -1;
        }
        int row = -1;
        ContentValues values = new ContentValues();
        try {
            String where = IMessageColumn.MESSAGE_ID + " = '" + msg.getMsgId() + "'";
            ECFileMessageBody msgBody = (ECFileMessageBody) msg.getBody();
            values.put(IMessageColumn.FILE_PATH, msgBody.getLocalUrl());
            values.put(IMessageColumn.USER_DATA, msg.getUserData());
            if (msg.getType() == ECMessage.Type.VOICE) {
                int voiceTime = DemoUtils.calculateVoiceTime(msgBody.getLocalUrl());
                values.put(IMessageColumn.DURATION, voiceTime);
            }
            row = getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_IM_MESSAGE,
                    values, where, null);
            //notifyChanged(msgId);
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
            e.getStackTrace();
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }
        }
        return row;
    }

    /**
     * 设置Im消息发送状态
     *
     * @param msgId     消息ID
     * @param sendStatu 发送状态
     * @return
     */
    public static int setIMessageSendStatus(String msgId, int sendStatu, int duration) {
        int row = 0;
        ContentValues values = new ContentValues();
        try {
            String where = IMessageColumn.MESSAGE_ID + " = '" + msgId
                    + "' and " + IMessageColumn.SEND_STATUS + "!=" + sendStatu;
            values.put(IMessageColumn.SEND_STATUS, sendStatu);
            if (duration > 0) {
                values.put(IMessageColumn.DURATION, duration);
            }
            row = getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_IM_MESSAGE,
                    values, where, null);
            //notifyChanged(msgId);
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
            e.getStackTrace();
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }
        }
        return row;
    }

    /**
     * 根据不同的消息类型将数据保存到数据库
     *
     * @param message
     * @param values
     */
    private static void putValues(ECMessage message, ContentValues values) {
        if (message.getType() == ECMessage.Type.TXT) {
            values.put(IMessageColumn.BODY, ((ECTextMessageBody) message.getBody()).getMessage());
        } else {
            ECFileMessageBody body = (ECFileMessageBody) message.getBody();
            values.put(IMessageColumn.FILE_PATH, body.getLocalUrl());
            values.put(IMessageColumn.FILE_URL, body.getRemoteUrl());
            if (message.getType() == ECMessage.Type.VOICE) {
                ECVoiceMessageBody Voicebody = (ECVoiceMessageBody) message.getBody();
                values.put(IMessageColumn.DURATION, Voicebody.getDuration());
            }

        }
    }

    /**
     * @param threadId
     * @param lastTime
     * @return
     */
    public static ArrayList<ECMessage> queryIMessageListAfter(long threadId,
                                                              String lastTime) {
        ArrayList<ECMessage> al = null;
        Cursor cursor = null;
        StringBuffer sb = new StringBuffer();
        if (lastTime != null && !lastTime.equals("") && !lastTime.equals("0")) {
            sb.append(IMessageColumn.CREATE_DATE + " > ").append(lastTime);
        } else {
            sb.append("1=1");
        }
        sb.append(" and " + IMessageColumn.OWN_THREAD_ID + " = ").append(
                threadId);
        sb.append(" and  " + IMessageColumn.BOX_TYPE + " != 3");
        try {
            cursor = getInstance().sqliteDB().query(false,
                    DatabaseHelper.TABLES_NAME_IM_MESSAGE, null, sb.toString(),
                    null, null, null, IMessageColumn.RECEIVE_DATE + " asc",
                    null);
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    return null;
                }
                al = new ArrayList<ECMessage>();
                while (cursor.moveToNext()) {

                    long id = cursor.getLong(cursor.getColumnIndex(IMessageColumn.ID));
                    String sender = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.sender));
                    String msgId = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.MESSAGE_ID));
                    long ownThreadId = cursor.getLong(cursor.getColumnIndexOrThrow(IMessageColumn.OWN_THREAD_ID));
                    long createDate = cursor.getLong(cursor.getColumnIndexOrThrow(IMessageColumn.CREATE_DATE));
                    long receiveDate = cursor.getLong(cursor.getColumnIndexOrThrow(IMessageColumn.RECEIVE_DATE));
                    String userData = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.USER_DATA));
                    int read = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.READ_STATUS));
                    int boxType = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.BOX_TYPE));
                    int msgType = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.MESSAGE_TYPE));
                    int sendStatus = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.SEND_STATUS));

                    ECMessage ecMessage = null;
                    if (msgType == ECMessage.Type.TXT.ordinal()) {
                        String content = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.BODY));
                        ecMessage = ECMessage.createECMessage(ECMessage.Type.TXT);
                        ECTextMessageBody textBody = new ECTextMessageBody(content);
                        ecMessage.setBody(textBody);
                    } else {
                        /*String fileUrl = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.FILE_URL));
						String fileLocalPath = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.FILE_PATH));
						
						if (msgType == ECMessage.Type.VOICE.ordinal()) {
							int duration = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.DURATION));
							ECVoiceMessageBody voiceBody = new ECVoiceMessageBody(new File(fileLocalPath), 0);
							voiceBody.setRemoteUrl(fileUrl);
							ecMessage.setBody(voiceBody);
							ecMessage = ECMessage.createECMessage(ECMessage.Type.VOICE);
						} else if (msgType == ECMessage.Type.IMAGE.ordinal() || msgType == ECMessage.Type.FILE.ordinal()) {
							ECFileMessageBody fileBody = new 
						} else {
							continue;
						}*/
                    }
                    ecMessage.setId(id);
                    ecMessage.setForm(sender);
                    ecMessage.setMsgId(msgId);
                    ecMessage.setMsgTime(createDate);
                    ecMessage.setUserData(userData);
                    ecMessage.setDirection(getMessageDirect(boxType));
                    al.add(0, ecMessage);
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return al;
    }

    public static void deleteChattingMessage(long sessionId) {
        while (true) {
            ArrayList<ECMessage> iMessageList = IMessageSqlManager.queryIMessageList(sessionId, 50, null);
            if (iMessageList != null && !iMessageList.isEmpty()) {
                for (ECMessage detail : iMessageList) {
                    delSingleMsg(detail.getMsgId());
                }
                continue;
            }
            return;
        }
    }

    public static void _deleteChattingMessage(long sessionId) {
        ArrayList<ECMessage> iMessageList = IMessageSqlManager.queryIMessageList(sessionId, 0, null);
        if (iMessageList != null && !iMessageList.isEmpty()) {
            ArrayList<String> fileList = new ArrayList<String>();
            for (ECMessage detail : iMessageList) {
                if (detail.getType() != ECMessage.Type.TXT) {
                    ECFileMessageBody body = (ECFileMessageBody) detail.getBody();
                    if (!TextUtils.isEmpty(body.getLocalUrl())) {
                        if (body.getLocalUrl().startsWith("THUMBNAIL://")) {
                            ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(detail.getMsgId());
                            if (imgInfo != null) {
                                ImgInfoSqlManager.getInstance().delImgInfo(imgInfo.getMsglocalid());
                                if (TextUtils.isEmpty(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath())) {
                                    continue;
                                }
                                if (!new File(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath()).exists()) {
                                    continue;
                                }
                                fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getBigImgPath());
                                fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath());
                            }
                        } else if (detail.getUserData() != null && detail.getUserData().indexOf("THUMBNAIL://") != -1) {
                            String userData = detail.getUserData();
                            int start = userData.indexOf("THUMBNAIL://");
                            if (start != -1) {
                                String thumbnail = userData.substring(start);
                                fileList.add(ImgInfoSqlManager.getInstance().getThumbUrlAndDel(thumbnail));
                            }
                        } else {
                            fileList.add(body.getLocalUrl());
                        }
                    }
                }
            }
            int rows = IMessageSqlManager.deleteMulitMsgs(iMessageList);
            if (rows > 0 && !fileList.isEmpty()) {
                FileAccessor.delFiles(fileList);
            }
        }
    }

    public static void delSingleMsg(String id) {
        ECMessage msg = getMsg(id);
        if (msg == null) {
            return;
        }
        delMessage(id);
        if (msg.getType() != ECMessage.Type.TXT) {
            ArrayList<String> fileList = new ArrayList<String>();
            ECFileMessageBody body = (ECFileMessageBody) msg.getBody();
            if (!TextUtils.isEmpty(body.getLocalUrl())) {
                if (body.getLocalUrl().startsWith("THUMBNAIL://")) {
                    ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(msg.getMsgId());
                    if (imgInfo != null) {
                        ImgInfoSqlManager.getInstance().delImgInfo(imgInfo.getMsglocalid());
                        if (TextUtils.isEmpty(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath())) {
                            return;
                        }
                        if (!new File(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath()).exists()) {
                            return;
                        }
                        fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getBigImgPath());
                        fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath());

                    }

                } else if (msg.getUserData() != null && msg.getUserData().indexOf("THUMBNAIL://") != -1) {
                    String userData = msg.getUserData();
                    int start = userData.indexOf("THUMBNAIL://");
                    if (start != -1) {
                        String thumbnail = userData.substring(start + "THUMBNAIL://".length());
                        ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(thumbnail);
                        if (imgInfo != null) {
                            ImgInfoSqlManager.getInstance().delImgInfo(imgInfo.getMsglocalid());
                            if (TextUtils.isEmpty(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath())) {
                                return;
                            }
                            if (!new File(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath()).exists()) {
                                return;
                            }
                            fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getBigImgPath());
                            fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath());

                        }
                    }
                } else {
                    fileList.add(body.getLocalUrl());
                }
            }
            FileAccessor.delFiles(fileList);
        }
    }

    public static void delMessage(String id) {
        LogUtil.d(TAG, "[delMessage] msgid = " + id);
        getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_IM_MESSAGE, "msgid='" + id + "'", null);
    }

    /**
     * 删除会话
     *
     * @param contactId
     */
    public static void deleteChattingMessage(String contactId) {
        long sessionId = ConversationSqlManager.querySessionIdForBySessionId(contactId);
        deleteChattingMessage(sessionId);
        ConversationSqlManager.getInstance().delSession(contactId);
    }

    /**
     * IM分页查询
     *
     * @param num
     * @param lastTime
     * @return
     */
    public static ArrayList<ECMessage> queryIMessageList(long threadId, int num,
                                                         String lastTime) {
        ArrayList<ECMessage> al = null;
        Cursor cursor = null;
        StringBuffer sb = new StringBuffer();
        if (lastTime != null && !lastTime.equals("") && !lastTime.equals("0")) {
            sb.append(IMessageColumn.CREATE_DATE + " < ").append(lastTime);
        } else {
            sb.append("1=1");
        }
        // if (threadId != 0) {
        sb.append(" and " + IMessageColumn.OWN_THREAD_ID + " = ").append(
                threadId);
        // }
        sb.append(" and  " + IMessageColumn.BOX_TYPE + " != " + ECMessage.Direction.DRAFT.ordinal());
        try {
            cursor = getInstance().sqliteDB().query(false,
                    DatabaseHelper.TABLES_NAME_IM_MESSAGE, null, sb.toString(),
                    null, null, null, IMessageColumn.RECEIVE_DATE + " desc",
                    num == 0 ? null : String.valueOf(num));
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    return null;
                }
                al = new ArrayList<ECMessage>();
                while (cursor.moveToNext()) {

                    ECMessage ecMessage = packageMessage(cursor);
                    if (ecMessage == null) {
                        continue;
                    }
                    al.add(0, ecMessage);
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return al;

    }

    /**
     * IM分页查询
     *
     * @param num
     * @param version
     * @return
     */
    public static ArrayList<ECMessage> queryIMessageVersionList(long threadId, int num, String version) {
        ArrayList<ECMessage> al = null;
        Cursor cursor = null;
        StringBuffer sb = new StringBuffer();
        if (version != null && !version.equals("") && !version.equals("0")) {
            sb.append(IMessageColumn.ID + " < ").append(version);
        } else {
            sb.append("1=1");
        }
        // if (threadId != 0) {
        sb.append(" and " + IMessageColumn.OWN_THREAD_ID + " = ").append(
                threadId);
        // }
        sb.append(" and  " + IMessageColumn.BOX_TYPE + " != " + ECMessage.Direction.DRAFT.ordinal());
        try {
            cursor = getInstance().sqliteDB().query(false,
                    DatabaseHelper.TABLES_NAME_IM_MESSAGE, null, sb.toString(),
                    null, null, null, IMessageColumn.RECEIVE_DATE + " desc",
                    num == 0 ? null : String.valueOf(num));
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    return null;
                }
                al = new ArrayList<ECMessage>();
                while (cursor.moveToNext()) {

                    ECMessage ecMessage = packageMessage(cursor);
                    if (ecMessage == null) {
                        continue;
                    }
                    al.add(0, ecMessage);
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return al;

    }

    /**
     * 组装消息
     *
     * @param cursor
     * @return
     */
    private static ECMessage packageMessage(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(IMessageColumn.ID));
        String sender = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.sender));
        String msgId = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.MESSAGE_ID));
        //long ownThreadId = cursor.getLong(cursor.getColumnIndexOrThrow(IMessageColumn.OWN_THREAD_ID));
        long createDate = cursor.getLong(cursor.getColumnIndexOrThrow(IMessageColumn.CREATE_DATE));
        int version = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.VERSION));
        String userData = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.USER_DATA));
        int read = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.READ_STATUS));
        int boxType = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.BOX_TYPE));
        int msgType = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.MESSAGE_TYPE));
        int sendStatus = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.SEND_STATUS));

        ECMessage ecMessage = ECMessage.createECMessage(ECMessage.Type.NONE);
        if (msgType == ECMessage.Type.TXT.ordinal()) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.BODY));
            ecMessage.setType(ECMessage.Type.TXT);
            ECTextMessageBody textBody = new ECTextMessageBody(content);
            ecMessage.setBody(textBody);
        } else {
            String fileUrl = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.FILE_URL));
            String fileLocalPath = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.FILE_PATH));

            if (msgType == ECMessage.Type.VOICE.ordinal()) {
                ecMessage.setType(ECMessage.Type.VOICE);
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.DURATION));
                ECVoiceMessageBody voiceBody = new ECVoiceMessageBody(new File(fileLocalPath), 0);
                voiceBody.setRemoteUrl(fileUrl);
                ecMessage.setBody(voiceBody);
                voiceBody.setDuration(duration);
            } else if (msgType == ECMessage.Type.IMAGE.ordinal() || msgType == ECMessage.Type.FILE.ordinal()) {
                ECFileMessageBody fileBody = new ECFileMessageBody();
                if (msgType == ECMessage.Type.FILE.ordinal()) {
                    ecMessage.setType(ECMessage.Type.FILE);
                } else {
                    fileBody = new ECImageMessageBody();
                    ecMessage.setType(ECMessage.Type.IMAGE);
                }
                fileBody.setLocalUrl(fileLocalPath);
                fileBody.setRemoteUrl(fileUrl);
                fileBody.setFileName(DemoUtils.getFileNameFormUserdata(userData));
                ecMessage.setBody(fileBody);
            } else {
                return null;
            }
        }
        ecMessage.setId(id);
        ecMessage.setForm(sender);
        ecMessage.setMsgId(msgId);
        ecMessage.setMsgTime(createDate);
        ecMessage.setUserData(userData);
        ecMessage.setVersion(version);
        if (sendStatus == ECMessage.MessageStatus.SENDING.ordinal()) {
            ecMessage.setMsgStatus(ECMessage.MessageStatus.SENDING);
        } else if (sendStatus == ECMessage.MessageStatus.RECEIVE.ordinal() || sendStatus == 4) {
            // sendStatus == 4 兼容以前版本
            ecMessage.setMsgStatus(ECMessage.MessageStatus.RECEIVE);
        } else if (sendStatus == ECMessage.MessageStatus.SUCCESS.ordinal()) {
            ecMessage.setMsgStatus(ECMessage.MessageStatus.SUCCESS);
        } else if (sendStatus == ECMessage.MessageStatus.FAILED.ordinal()) {
            ecMessage.setMsgStatus(ECMessage.MessageStatus.FAILED);
        }
        ecMessage.setDirection(getMessageDirect(boxType));
        return ecMessage;
    }

    public static long deleteAllBySession(String sessionId) {
        long l = ConversationSqlManager.querySessionIdForBySessionId(sessionId);
        if (l > 0) {
            CCPAppManager.getContext().sendBroadcast(new Intent(ACTION_SESSION_DEL));
            return getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_IM_MESSAGE, IMessageColumn.OWN_THREAD_ID + " = " + l, null);
        }
        return -1;
    }

    public static void getAllFileMessageBySession(String sessionId) {

    }

    /**
     * 返回消息的类型，发送、接收、草稿
     *
     * @param type 消息类型
     * @return
     */
    public static ECMessage.Direction getMessageDirect(int type) {
        if (type == ECMessage.Direction.SEND.ordinal()) {
            return ECMessage.Direction.SEND;
        } else if (type == ECMessage.Direction.RECEIVE.ordinal()) {
            return ECMessage.Direction.RECEIVE;
        } else {
            return ECMessage.Direction.DRAFT;
        }
    }

    /**
     * 根据会话ID查询会话消息的数据量
     *
     * @param threadId 当前会话ID
     * @return 会话总数
     */
    public static int getTotalCount(long threadId) {
        String sql = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLES_NAME_IM_MESSAGE + " WHERE " + "sid" + "=" + threadId;
        Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToLast()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public static Cursor getNullCursor() {
        return getInstance().sqliteDB().query(DatabaseHelper.TABLES_NAME_IM_MESSAGE, null, "msgid=?", new String[]{"-1"}, null, null, null);
    }

    public static int deleteMulitMsgs(List<ECMessage> msgs) {
        List<Long> rowIds = new ArrayList<Long>();
        for (ECMessage detail : msgs) {
            rowIds.add(detail.getId());
        }

        return deleteMulitMsg(rowIds);
    }

    /**
     * @param rowIds
     * @return
     */
    public static int deleteMulitMsg(List<Long> rowIds) {
        if (rowIds == null || rowIds.isEmpty()) {
            LogUtil.d(TAG, "ignore delete , rowIds empty");
            return 0;
        }

        StringBuilder where = new StringBuilder(IMessageColumn.ID + " IN (");
        int lenght = where.length();
        for (long rowId : rowIds) {

            if (where.length() > lenght) {
                where.append(",");
            }
            where.append(rowId);
        }

        where.append(")");
        LogUtil.d(TAG, "executeSql where " + where);

        int row = 0;
        try {
            row = getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_IM_MESSAGE, where.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * 分页加载查询数据
     *
     * @param threadId
     * @param limit
     * @return
     */
    public static Cursor getCursor(long threadId, int limit) {
        String sql = "SELECT * FROM " + DatabaseHelper.TABLES_NAME_IM_MESSAGE
                + " WHERE " + "sid" + "= " + threadId + " ORDER BY "
                + "serverTime" + " ASC LIMIT " + limit + " offset "
                + "(SELECT count(*) FROM "
                + DatabaseHelper.TABLES_NAME_IM_MESSAGE + " WHERE " + "sid"
                + "= " + threadId + " ) -" + limit;
        LogUtil.d(TAG, "getCursor sid:" + threadId + " limit:" + limit + " [" + sql + "]");
        return getInstance().sqliteDB().rawQuery(sql, null);
    }

    /**
     * 查询会话所有的图片
     *
     * @param session
     * @return
     */
    public static List<String> getImageMessageIdSession(long session) {
        String sql = "select msgid from " + DatabaseHelper.TABLES_NAME_IM_MESSAGE + " where " + IMessageColumn.OWN_THREAD_ID + " = " + session + " and msgType=" + ECMessage.Type.IMAGE.ordinal();
        Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
        List<String> msgids = null;
        if (cursor != null && cursor.getCount() > 0) {
            msgids = new ArrayList<String>();
            while (cursor.moveToNext()) {
                msgids.add(cursor.getString(0));
            }
            cursor.close();
        }
        return msgids;
    }

    /**
     * 查询所有未读数
     *
     * @return
     */
    public static int qureyAllSessionUnreadCount() {
        int count = 0;
        String[] columnsList = {"sum(" + IThreadColumn.UNREAD_COUNT + ")"};
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().query(DatabaseHelper.TABLES_NAME_IM_SESSION,
                    columnsList, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(cursor.getColumnIndex("sum("
                            + IThreadColumn.UNREAD_COUNT + ")"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return count;
    }

    public static int getUnNotifyUnreadCount() {
        String sql = "SELECT sum(" + IThreadColumn.UNREAD_COUNT + ") FROM im_thread  \n" +
                "                         inner JOIN groups2 ON im_thread.sessionId = groups2.groupid and isnotice == 2";
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(cursor.getColumnIndex("sum("
                            + IThreadColumn.UNREAD_COUNT + ")"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    /**
     * 通过联系着查找会话ID
     *
     * @param contactId
     * @return
     */
    public static long querySessionIdForByContactId(String contactId) {
        Cursor cursor = null;
        long threadId = 0;
        if (contactId != null) {
            String where = IThreadColumn.CONTACT_ID + " = '" + contactId + "' ";
            try {
                cursor = getInstance().sqliteDB().query(
                        DatabaseHelper.TABLES_NAME_IM_SESSION, null, where,
                        null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        threadId = cursor.getLong(cursor
                                .getColumnIndexOrThrow(IThreadColumn.ID));
                    }
                }
            } catch (SQLException e) {
                LogUtil.e(TAG + " " + e.toString());
            } finally {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        }
        return threadId;
    }

    /**
     * 消息重发
     *
     * @param rowid
     * @param detail
     * @return
     */
    public static int changeResendMsg(long rowid, ECMessage detail) {

        if (detail == null || TextUtils.isEmpty(detail.getMsgId()) || rowid == -1) {
            return -1;
        }

        String where = IMessageColumn.ID + "=" + rowid + " and " + IMessageColumn.SEND_STATUS + " = " + ECMessage.MessageStatus.FAILED.ordinal();
        ContentValues values = null;
        try {
            values = new ContentValues();
            values.put(IMessageColumn.MESSAGE_ID, detail.getMsgId());
            values.put(IMessageColumn.SEND_STATUS, detail.getMsgStatus().ordinal());
            values.put(IMessageColumn.USER_DATA, detail.getUserData());
            return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_IM_MESSAGE, values, where, null);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }
        }
    }


    /**
     * 查询某一会话所有信息条数
     *
     * @return
     */
    public static int qureyIMCountForSession(long threadId) {
        int count = 0;
        String[] columnsList = {"count(*)"};
        String where = IMessageColumn.OWN_THREAD_ID + " = " + threadId
                + " and " + IMessageColumn.BOX_TYPE + " != 3";
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().query(DatabaseHelper.TABLES_NAME_IM_MESSAGE,
                    columnsList, where, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(cursor.getColumnIndex("count(*)"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return count;
    }

    /**
     * 查询下载失败的图片消息
     *
     * @return
     */
    public static List<ECMessage> getDowndFailMsg() {
        String sql = "select * from " + DatabaseHelper.TABLES_NAME_IM_MESSAGE +
                " where msgType=" + ECMessage.Type.IMAGE.ordinal() + " and box_type=2 and userData is null";
        Cursor cursor = null;

        List<ECMessage> al = null;
        try {
            cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                al = new ArrayList<ECMessage>();
                while (cursor.moveToNext()) {

                    ECMessage ecMessage = packageMessage(cursor);
                    if (ecMessage == null) {
                        continue;
                    }
                    al.add(0, ecMessage);
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return al;
    }

    public static ECMessage getMsg(String id) {
        String sql = "select * from " + DatabaseHelper.TABLES_NAME_IM_MESSAGE + " where msgid = '" + id + "'";
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    return packageMessage(cursor);
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }

    public static void registerMsgObserver(OnMessageChange observer) {
        getInstance().registerObserver(observer);
    }

    public static void unregisterMsgObserver(OnMessageChange observer) {
        getInstance().unregisterObserver(observer);
    }

    public static void notifyMsgChanged(String session) {
        getInstance().notifyChanged(session);
    }

    public static void reset() {
        getInstance().release();
    }

    @Override
    protected void release() {
        super.release();
        instance = null;
    }
}
