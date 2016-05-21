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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.yuntongxun.ecsdk.im.ECGroupMember;


/**
 * 群组数据库接口
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-29
 */
public class GroupMemberSqlManager extends AbstractSQLManager {

    private static final String TAG = "ECDemo.GroupMemberSqlManager";
    Object mLock = new Object();
    private static GroupMemberSqlManager sInstance;

    private static GroupMemberSqlManager getInstance() {
        if (sInstance == null) {
            sInstance = new GroupMemberSqlManager();
        }
        return sInstance;
    }

    private GroupMemberSqlManager() {

    }

    /**
     * 查询群组成员
     *
     * @param groupId
     * @return
     */
    public static ArrayList<ECGroupMember> getGroupMembers(String groupId) {
        String sql = "select * from group_members where group_id ='" + groupId + "'";
        ArrayList<ECGroupMember> list = null;
        try {
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                list = new ArrayList<ECGroupMember>();
                while (cursor.moveToNext()) {
                    ECGroupMember groupMember = new ECGroupMember();
                    groupMember.setBelong(cursor.getString(cursor.getColumnIndex(GroupMembersColumn.OWN_GROUP_ID)));
                    groupMember.setEmail(cursor.getString(cursor.getColumnIndex(GroupMembersColumn.MAIL)));
                    groupMember.setRemark(cursor.getString(cursor.getColumnIndex(GroupMembersColumn.REMARK)));
                    groupMember.setTel(cursor.getString(cursor.getColumnIndex(GroupMembersColumn.TEL)));
                    groupMember.setBan(cursor.getInt(cursor.getColumnIndex(GroupMembersColumn.ISBAN)) == 1 ? true : false);
                    groupMember.setVoipAccount(cursor.getString(cursor.getColumnIndex(GroupMembersColumn.VOIPACCOUNT)));
                    //cursor.getString(cursor.getColumnIndex(GroupMembersColumn.BIRTH));
                    //cursor.getString(cursor.getColumnIndex(GroupMembersColumn.SIGN));
                    //cursor.getString(cursor.getColumnIndex(GroupMembersColumn.RULE));
                    //cursor.getString(cursor.getColumnIndex(GroupMembersColumn.SEX));
                    list.add(groupMember);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询群组成员账号
     *
     * @param groupId
     * @return
     */
    public static ArrayList<String> getGroupMemberID(String groupId) {
        String sql = "select voipaccount from group_members where group_id ='" + groupId + "'";
        ArrayList<String> list = null;
        try {
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex(GroupMembersColumn.VOIPACCOUNT)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 查询群组成员用于列表显示
     *
     * @param groupId
     * @return
     */
    public static ArrayList<ECGroupMember> getGroupMemberWithName(String groupId) {
        String sql = "select voipaccount ,contacts.username ,contacts.remark ,role ,isban from group_members ,contacts where group_id ='" + groupId + "' and contacts.contact_id = group_members.voipaccount order by role";
        //String sql = "select voipaccount ,remark,role , isban from group_members where group_id ='" + groupId + "'" ;
        ArrayList<ECGroupMember> list = null;
        try {
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                list = new ArrayList<ECGroupMember>();
                while (cursor.moveToNext()) {
                    ECGroupMember groupMember = new ECGroupMember();
                    groupMember.setBelong(groupId);
                    groupMember.setVoipAccount(cursor.getString(0));
                    groupMember.setDisplayName(cursor.getString(1));
                    groupMember.setRemark(cursor.getString(2));
                    groupMember.setRole(cursor.getInt(3));
                    groupMember.setBan(cursor.getInt(4) == 2 ? true : false);
                    list.add(groupMember);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询所有群组成员帐号
     *
     * @param groupId
     * @return
     */
    public static ArrayList<String> getGroupMemberAccounts(String groupId) {
        String sql = "select * from group_members where group_id ='" + groupId + "'";
        ArrayList<String> list = null;
        try {
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex(GroupMembersColumn.VOIPACCOUNT)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 更新群组成员
     *
     * @param members
     * @return
     */
    public static ArrayList<Long> insertGroupMembers(List<ECGroupMember> members) {

        ArrayList<Long> rows = new ArrayList<Long>();
        if (members == null) {
            return rows;
        }
        try {
            synchronized (getInstance().mLock) {
                // Set the start transaction
                getInstance().sqliteDB().beginTransaction();

                // Batch processing operation
                for (ECGroupMember member : members) {
                    try {
                        long row = insertGroupMember(member);
                        if (row != -1) {
                            rows.add(row);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Set transaction successful, do not set automatically
                // rolls back not submitted.
                getInstance().sqliteDB().setTransactionSuccessful();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getInstance().sqliteDB().endTransaction();
        }
        return rows;
    }

    /**
     * 更新群组到数据库
     *
     * @param member
     * @return
     */
    public static long insertGroupMember(ECGroupMember member) {
        if (member == null || TextUtils.isEmpty(member.getBelong())
                || TextUtils.isEmpty(member.getVoipAccount())) {
            return -1L;
        }
        ContentValues values = null;
        try {
            if (!ContactSqlManager.hasContact(member.getVoipAccount()) || needUpdateSexPhoto(member.getBelong(), member.getVoipAccount(), member.getSex())) {
                updateContact(member);
            } else {
                if (!TextUtils.isEmpty(member.getDisplayName())) {
                    ContactSqlManager.updateContactName(member);
                }
            }
            values = new ContentValues();
            values.put(GroupMembersColumn.OWN_GROUP_ID, member.getBelong());
            values.put(GroupMembersColumn.VOIPACCOUNT, member.getVoipAccount());
            values.put(GroupMembersColumn.TEL, member.getTel());
            values.put(GroupMembersColumn.MAIL, /*member.getEmail()*/"Jorstinchan@gmail.com");
            values.put(GroupMembersColumn.REMARK, member.getDisplayName());
            values.put(GroupMembersColumn.ISBAN, member.isBan() ? 2 : 1);
            values.put(GroupMembersColumn.ROLE, member.getRole());
            values.put(GroupMembersColumn.SEX, member.getSex());
            if (!isExitGroupMember(member.getBelong(), member.getVoipAccount())) {
                return getInstance().sqliteDB().insert(DatabaseHelper.TABLES_NAME_GROUP_MEMBERS, null, values);
            } else {
                return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUP_MEMBERS, values, "group_id ='" + member.getBelong() + "'" + " and voipaccount='" + member.getVoipAccount() + "'", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (values != null) {
                values.clear();
            }

        }
        return -1L;
    }

    private static void updateContact(ECGroupMember member) {
        ECContacts contacts = new ECContacts(member.getVoipAccount());
        contacts.setNickname(member.getDisplayName());
        ContactSqlManager.insertContact(contacts, member.getSex());
    }

    /**
     * 判断性格是否改变
     *
     * @param belong
     * @param userid
     * @param sex
     * @return
     */
    public static boolean needUpdateSexPhoto(String belong, String userid, int sex) {
        String sql = "select voipaccount ,sex from group_members where sex !=" + sex + " and voipaccount = '" + userid + "' and group_id='" + belong + "'";
        Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int anInt = cursor.getInt(1);
            String string = cursor.getString(0);
            cursor.close();
            ;
            return true;
        }
        return false;
    }

    /**
     * 是否存在该联系人
     *
     * @param groupId
     * @param member
     * @return
     */
    public static boolean isExitGroupMember(String groupId, String member) {
        String sql = "select voipaccount from group_members where group_id ='" + groupId + "'" + " and voipaccount='" + member + "'";
        try {
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新群组成员
     *
     * @param groupId
     * @param members
     */
    public static void insertGroupMembers(String groupId, String[] members) {
        if (TextUtils.isEmpty(groupId) || members == null || members.length <= 0) {
            return;
        }
        for (String member : members) {
            ECGroupMember groupMember = new ECGroupMember();
            groupMember.setBelong(groupId);
            groupMember.setVoipAccount(member);
            if (CCPAppManager.getClientUser() != null && CCPAppManager.getClientUser().getUserId().equals(member)) {
                groupMember.setRole(1);
            } else {
                groupMember.setRole(3);
            }
            groupMember.setTel(member);
            insertGroupMember(groupMember);
        }
    }

    /**
     * 删除群组所有成员
     *
     * @param groupId
     */
    public static void delAllMember(String groupId) {
        String sqlWhere = "group_id ='" + groupId + "'";
        try {
            getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_GROUP_MEMBERS, sqlWhere, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除群组成员
     *
     * @param groupId 群组ID
     * @param member  群组成员
     * @return
     */
    public static void delMember(String groupId, String member) {
        String sqlWhere = "group_id ='" + groupId + "'" + " and voipaccount='" + member + "'";
        try {
            getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_GROUP_MEMBERS, sqlWhere, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除群组成员
     *
     * @param groupId
     * @param members
     */
    public static void delMember(String groupId, String[] members) {
        StringBuilder builder = new StringBuilder("in(");
        for (String member : members) {
            builder.append("'").append(member).append("'").append(",");
        }
        if (builder.toString().endsWith(",")) {
            builder.replace(builder.length() - 1, builder.length(), "");
            builder.append(")");
        } else {
            builder.replace(0, builder.length(), "");
        }
        String sqlWhere = " group_id ='" + groupId + "'" + " and voipaccount " + builder.toString();
        try {
            getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_GROUP_MEMBERS, sqlWhere, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新成员禁言状态
     *
     * @param groupid
     * @param member
     * @param enabled
     * @return
     */
    public static long updateMemberSpeakState(String groupid, String member, boolean enabled) {
        try {
            String where = GroupMembersColumn.VOIPACCOUNT + "='" + member + "' and " + GroupMembersColumn.OWN_GROUP_ID + "='" + groupid + "'";
            ContentValues values = new ContentValues();
            values.put(GroupMembersColumn.ISBAN, enabled ? 2 : 1);
            return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUP_MEMBERS, values, where, null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public static void reset() {
        getInstance().release();
    }

    @Override
    protected void release() {
        super.release();
        sInstance = null;
    }
}
