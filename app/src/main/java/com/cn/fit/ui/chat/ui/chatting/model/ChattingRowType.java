/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.ui.chatting.model;


/**
 * <p>Title: ChatRowType.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-4-16
 */
public enum ChattingRowType {

    /**
     * display a image of message received
     */
    IMAGE_ROW_RECEIVED("C200R", Integer.valueOf(1)),

    /**
     * display a image of message transmitted
     */
    IMAGE_ROW_TRANSMIT("C200T", Integer.valueOf(2)),


    /**
     * display a file of message received
     */
    FILE_ROW_RECEIVED("C1024R", Integer.valueOf(3)),

    /**
     * display a file of message transmitted
     */
    FILE_ROW_TRANSMIT("C1024T", Integer.valueOf(4)),

    /**
     * display a voice of message received
     */
    VOICE_ROW_RECEIVED("C60R", Integer.valueOf(5)),

    /**
     * display a voice of message transmitted
     */
    VOICE_ROW_TRANSMIT("C60T", Integer.valueOf(6)),

    /**
     * Display text of message received
     */
    DESCRIPTION_ROW_RECEIVED("C2000R", Integer.valueOf(7)),

    /**
     * Display text of message transmitted
     */
    DESCRIPTION_ROW_TRANSMIT("C2000T", Integer.valueOf(8)),

    /**
     * chatting item for system .such as time
     */
    CHATTING_SYSTEM("C18600668603R", Integer.valueOf(9));


    private final Integer mId;
    private final Object mDefaultValue;

    /**
     * Constructor of <code>ChattingRowType</code>.
     *
     * @param id           The unique identifier of the setting
     * @param defaultValue The default value of the setting
     */
    private ChattingRowType(Object defaultValue, Integer id) {
        this.mId = id;
        this.mDefaultValue = defaultValue;
    }

    /**
     * Method that returns the unique identifier of the setting.
     *
     * @return the mId
     */
    public Integer getId() {
        return this.mId;
    }

    /**
     * Method that returns the default value of the setting.
     *
     * @return Object The default value of the setting
     */
    public Object getDefaultValue() {
        return this.mDefaultValue;
    }


    /**
     * Method that returns an instance of {@link com.cn.fit.ui.chat.common.utils.ECPreferenceSettings} from its.
     * unique identifier
     *
     * @param value The unique identifier
     * @return CCPPreferenceSettings The navigation sort mode
     */
    public static ChattingRowType fromValue(String value) {
        ChattingRowType[] values = values();
        int cc = values.length;
        for (int i = 0; i < cc; i++) {
            if (values[i].mDefaultValue.equals(value)) {
                return values[i];
            }
        }
        return null;
    }

}
