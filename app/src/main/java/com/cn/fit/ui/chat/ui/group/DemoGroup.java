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
package com.cn.fit.ui.chat.ui.group;

import java.util.ArrayList;

import android.database.Cursor;

import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.storage.GroupMemberSqlManager;
import com.cn.fit.ui.chat.ui.chatting.IMChattingHelper;
import com.yuntongxun.ecsdk.im.ECGroup;


/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-18
 */
public class DemoGroup extends ECGroup {

    /**
     * 是否加入群组
     */
    private boolean isJoin;
    private String ownerName;

    /**
     * @return the isJoin
     */
    public boolean isJoin() {
        return isJoin;
    }

    /**
     * @param isJoin the isJoin to set
     */
    public void setJoin(boolean isJoin) {
        this.isJoin = isJoin;
    }

    /**
     * @return the ownerName
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * @param ownerName the ownerName to set
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setCursor(Cursor cursor) {
        setGroupId(cursor.getString(0));
        setName(cursor.getString(1));
        setGroupType(cursor.getInt(2));
        setCount(cursor.getInt(3));
        setPermission(ECGroup.Permission.values()[cursor.getInt(4)]);
        this.isJoin = (cursor.getInt(5) == 1);

        if (getPermission() == ECGroup.Permission.NEED_AUTH && getName() != null && getName().endsWith(IMChattingHelper.GROUP_PRIVATE_TAG)) {
            ArrayList<String> member = GroupMemberSqlManager.getGroupMemberID(getGroupId());
            if (member != null) {
                ArrayList<String> contactName = ContactSqlManager.getContactName(member.toArray(new String[]{}));
                if (contactName == null || contactName.isEmpty()) {
                    return;
                }
                String chatroomName = DemoUtils.listToString(contactName, ",");
                setName(chatroomName);
            }
        }
    }

    public DemoGroup() {

    }

    public DemoGroup(ECGroup group) {
        setGroupId(group.getGroupId());
        setName(group.getName());
        setDateCreated(group.getDateCreated());
        setDeclare(group.getDeclare());
        setGroupType(group.getGroupType());
        setPermission(group.getPermission());
        setCount(group.getCount());
        setOwner(group.getOwner());
    }

}
