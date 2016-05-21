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
package com.cn.fit.ui.chat.ui.contact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.cn.fit.R;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.utils.BitmapUtil;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.DialNumberMap;
import com.cn.fit.ui.chat.common.utils.ECPropertiesUtil;
import com.cn.fit.ui.chat.common.utils.FileAccessor;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.ResourceHelper;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.storage.GroupMemberSqlManager;
import com.cn.fit.util.RegexChar;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 联系人逻辑处理
 * Created by Jorstin on 2015/3/18.
 */
public class ContactLogic {


    public static final String ALPHA_ACCOUNT = "izhangjy@163.com";
    private static HashMap<String, Bitmap> photoCache = new HashMap<String, Bitmap>(20);
    public static final String[] CONVER_NAME = {"张三", "李四", "王五", "赵六", "钱七"};
    public static final String[] CONVER_PHONTO = {"select_account_photo_one.png"
            , "select_account_photo_two.png"
            , "select_account_photo_three.png"
            , "select_account_photo_four.png"
            , "select_account_photo_five.png"
    };

    private static String[] projection_getSettingList = {
            ContactsContract.Settings.ACCOUNT_TYPE,
            ContactsContract.Settings.ACCOUNT_NAME};

    private static String[] projection_getContractList = {
            ContactsContract.Data.RAW_CONTACT_ID,
            ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1,
            ContactsContract.Data.DATA2, ContactsContract.Data.DATA3,
            ContactsContract.RawContacts.ACCOUNT_TYPE,
            ContactsContract.Data._ID, ContactsContract.Data.TIMES_CONTACTED,
            ContactsContract.Data.DATA5, ContactsContract.Data.DATA6,};

    public static Bitmap mDefaultBitmap = null;


    static {
        try {
            if (mDefaultBitmap == null) {
                mDefaultBitmap = DemoUtils.decodeStream(CCPAppManager.getContext().getAssets().open("avatar/personal_center_default_avatar.png"), ResourceHelper.getDensity(null));
            }
        } catch (IOException e) {
        }
    }

    private static ContactLogic sInstance;

    public static ContactLogic getInstance() {
        if (sInstance == null) {
            sInstance = new ContactLogic();
        }
        return sInstance;
    }

    /**
     * 查找头像
     *
     * @param username
     * @return
     */
    public static Bitmap getPhoto(String username) {

        if (TextUtils.isEmpty(username)) {
            return mDefaultBitmap;
        }
        try {
            if (photoCache.containsKey(username)) {
                return photoCache.get(username);
            }
            Bitmap bitmap;
            if (username.startsWith("mobilePhoto://")) {
                bitmap = BitmapFactory.decodeFile(new File(FileAccessor.getAvatarPathName(), username.substring("mobilePhoto://".length())).getAbsolutePath());
            } else {

                bitmap = DemoUtils.decodeStream(CCPAppManager.getContext()
                                .getAssets().open("avatar/" + username),
                        ResourceHelper.getDensity(null));
            }
            photoCache.put(username, bitmap);
            return bitmap;
        } catch (IOException e) {
        }
        return mDefaultBitmap;
    }

    /**
     * 按照字母顺序对联系人进行排序
     *
     * @param beas
     * @return
     */

    public static ArrayList<ECContacts> sortContacts(ArrayList<ECContacts> beas) {

        if (beas == null || beas.isEmpty()) {
            return null;
        }
        List<ECContacts> tempList = new ArrayList<ECContacts>();
        Collections.sort(beas, new Comparator<ECContacts>() {

            @Override
            public int compare(ECContacts lhs, ECContacts rhs) {

//            	if(lhs.getIsmaster()!=rhs.getIsmaster())
//            		return rhs.getIsmaster()-lhs.getIsmaster();
//            	if(lhs.getJpName()!=null&&rhs.getJpName()!=null){
//            		String n1 = lhs.getJpNumber();
//            		String n2 = rhs.getJpNumber();
//            		int addTimes = Math.abs(n2.length()-n1.length());
//            		if(n1.length()>n2.length()){
//            			for(int i=0;i<addTimes;i++)
//            				n2 = n2 +"0";
//            		}else if(n2.length()>n1.length()){
//            			for(int i=0;i<addTimes;i++)
//            				n1 = n1 +"0";
//            		}
//            		return Integer.parseInt(n1)-Integer.parseInt(n2);
//            	}
                return ((lhs.getJpName() == null) ? "#" : lhs.getJpName()).compareTo((rhs.getJpName() == null) ? "#" : rhs.getJpName());
            }

        });
//        Collections.sort(beas,Collator.getInstance(java.util.Locale.CHINA));
        //将遍历到前面的#重新排到后面
//        Iterator<ECContacts> iterator = beas.iterator();
//        while(iterator.hasNext()){
//            ECContacts bean = iterator.next();
//            if(bean.getJpName().substring(0, 1).equals("#")){
//                tempList.add(bean);
//                iterator.remove();
//            }
//        }
//        //再将#放到末尾
//        for(ECContacts bean:tempList){
//            beas.add(bean);
//        }

        return beas;
    }

    /**
     * 随即设置用户昵称
     *
     * @param beas
     * @return
     */
//    public static ArrayList<ECContacts> converContacts(ArrayList<ECContacts> beas) {
//
//        if(beas == null || beas.isEmpty()) {
//            return null;
//        }
//        Collections.sort(beas, new Comparator<ECContacts>() {
//
//            @Override
//            public int compare(ECContacts lhs, ECContacts rhs) {
//
//                return lhs.getContactid().compareTo(rhs.getContactid());
//            }
//
//        });
//
//        for(int i = 0 ; i < beas.size() ; i ++ ) {
//            ECContacts accountBean = beas.get(i);
//            if (i < 5) {
//                //accountBean.setNickname(CONVER_NAME[i]);
//                accountBean.setRemark(ContactLogic.CONVER_PHONTO[i]);
//            } else {
//                //accountBean.setNickname("云通讯" + i);
//                accountBean.setRemark("personal_center_default_avatar.png");
//            }
//        }
//        return beas;
//    }

//    public static ArrayList<ECContacts> initContacts() {
//        ArrayList<ECContacts> list = new ArrayList<ECContacts>();
//        ECContacts contacts = new ECContacts("13522353298");
//        contacts.setNickname("李文超");
//        list.add(contacts);
//
//        ECContacts contactszj = new ECContacts("18600668603");
//        contactszj.setNickname("詹季春");
//        list.add(contactszj);
//
//        ECContacts contactsj = new ECContacts("18701360647");
//        contactsj.setNickname("贾政阳");
//        list.add(contactsj);
//
//        ECContacts contactsz = new ECContacts("13552886480");
//        contactsz.setNickname("张俊良");
//        list.add(contactsz);
//
//        ECContacts contactsp = new ECContacts("15120024616");
//        contactsp.setNickname("潘晓东");
//        list.add(contactsp);
//
//        ECContacts contactst = new ECContacts("13691417205");
//        contactst.setNickname("唐陆军");
//        list.add(contactst);
//        return list;
//    }

    // These are the Contacts rows that we will retrieve.
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID,
    };

    /**
     * 获取手机系统联系人信息
     * @return
     */
//    public static ArrayLists<ECContacts> getPhoneContacts(Context ctx) {
//        ArrayLists<ECContacts> contacts = new ArrayLists<ECContacts>();
//        // Now create and return a CursorLoader that will take care of
//        // creating a Cursor for the data being displayed.
//        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
//                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
//                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
//        try {
//            Cursor cursor = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
//                    CONTACTS_SUMMARY_PROJECTION,
//                    select,
//                    null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
//            while (cursor.moveToNext()) {
//                int indexId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
//                String contactId = cursor.getString(indexId);
//                int indexDisplayName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                String name = cursor.getString(indexDisplayName);
//
//                Cursor phones = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//                ECContacts data = new ECContacts();
//                data.setId(indexId);
//                data.setNickname(name);
//                data.setRemark(ContactLogic.CONVER_PHONTO[ContactSqlManager.getIntRandom(4, 0)]);
//                while (phones.moveToNext()) {
//                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    Phone phone = new Phone(0,phoneNumber);
//                    data.addPhone(phone);
//                    pyFormat(data);
//                }
//                contacts.add(data);
//                phones.close();
//            }
//            cursor.close();
//
//            if(contacts != null) {
//                  Collections.sort(contacts, new PyComparator());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return contacts;
//    }


    /**
     * 获取联系人配置
     *
     * @return
     */
//    public List<String[]> getSettingList() {
//        List<String[]> cl = null;
//        Cursor cursor = null;
//        try {
//            cursor = CCPAppManager.getContext().getContentResolver().query(ContactsContract.Settings.CONTENT_URI,
//                    projection_getSettingList, null, null, null);
//            if (cursor != null && cursor.getCount() > 0) {
//                cl = new ArrayList<String[]>();
//                while (cursor.moveToNext()) {
//                    String[] s = new String[cursor.getColumnCount()];
//                    for (int i = 0; i < s.length; i++) {
//                        s[i] = cursor.getString(i);
//                    }
//                    cl.add(s);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//                cursor = null;
//            }
//        }
//
//        return cl;
//    }

//    public static ArrayLists<ECContacts> getContractList(boolean showSim) throws Exception {
//        // TODO 可能需要添加，是否显示无号码联系人选项，显示哪些账户联系人选项
//       getInstance().getSettingList();
//        Cursor cursor = null;
//        ArrayLists<ECContacts> cl = null;
//        String where = "(" + ContactsContract.Data.MIMETYPE + " = ? or "
//                + ContactsContract.Data.MIMETYPE + " = ? or "
//                + ContactsContract.Data.MIMETYPE + " = ? or "
//                + ContactsContract.Data.MIMETYPE + " = ? or "
//                + ContactsContract.Data.MIMETYPE + " = ? or "
//                + ContactsContract.Data.MIMETYPE + " = ?)";
//        if (!showSim) {
//            where += " AND (" + ContactsContract.RawContacts.ACCOUNT_TYPE
//                    + " NOT LIKE " + "'%sim%'" + " or "
//                    + ContactsContract.RawContacts.ACCOUNT_TYPE + " is null)";
//        }
//        String[] WhereParams = {
//                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
//                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE,
//                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
//                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
//                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
//                ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
//        Uri uri = ContactsContract.Data.CONTENT_URI;
//        try {
//            cursor = CCPAppManager.getContext().getContentResolver().query(uri, projection_getContractList, where,
//                    WhereParams, ContactsContract.Data.DISPLAY_NAME + ","
//                            + ContactsContract.Data.RAW_CONTACT_ID);
//            if (cursor != null) {
//                cl = new ArrayLists<ECContacts>();
//                ECContacts cli = null;
//
//                long tmpid = -1;
//                while (cursor.moveToNext()) {
//                    long rawContactId = cursor.getLong(0);
//                    String mimetype = cursor.getString(1);
//                    String data1 = cursor.getString(2);
//                    int data2 = cursor.getInt(3);
//                    String data3 = cursor.getString(4);
//
//                    long Id = cursor.getLong(6);
//                    if (tmpid != rawContactId) {
//                        if (cursor.getPosition() != 0 && !TextUtils.isEmpty(cli.getContactid())) {
//                            if(TextUtils.isEmpty(cli.getNickname())) {
//                                cli.setNickname(cli.getContactid());
//                            }
//                            cl.add(cli);
//                        }
//                        cli = new ECContacts();
//                        cli.setId(rawContactId);
//                    }
//                    tmpid = rawContactId;
//                    if (mimetype.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
//                        Phone phone = null;
//                        if (data2 == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
//                            phone = new Phone(rawContactId, Id, data1, data3);
//                        } else {
//                            phone = new Phone(rawContactId, Id, data2, data1);
//                        }
//                        cli.addPhone(phone);
//                    }
//                    if (mimetype.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
//                        cli.setNickname(data1);
//                    }
//                    if (mimetype.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
//                        cli.setPhotoId(Id);
//                    }
//                    cli.setRemark(ContactLogic.CONVER_PHONTO[ContactSqlManager.getIntRandom(4, 0)]);
//                    pyFormat(cli);
//                    // 最后一条记录时添加最后一个
//                    if (cursor.getPosition() == (cursor.getCount() - 1)) {
//                        if(TextUtils.isEmpty(cli.getNickname())) {
//                            cli.setNickname(cli.getContactid());
//                        }
//                        cl.add(cli);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception(e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//                cursor = null;
//            }
//            if(format != null) {
//                format = null;
//            }
//        }
//        if (cl != null) {
//            Collections.sort(cl, new PyComparator());
//        }
////		PinyinHelper.release();
//        return cl;
//    }


    private static HanyuPinyinOutputFormat format = null;

    public static void pyFormat(ECContacts contact) {
        try {
            String name = contact.getNickname();
            if (name == null || name.trim().length() == 0) {
                contact.setJpName("#");
                return;
            }
            name = name.trim();
            // 拼音转换设置
            if (format == null) {
                format = new HanyuPinyinOutputFormat();// 定义转换格式
                format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不要声调
                format.setVCharType(HanyuPinyinVCharType.WITH_V);// 设置 女 nv
            }

            String qpName = ""; // 用于分隔全拼数组
            StringBuilder qpBuilder = new StringBuilder();

            String qpNameStr = ""; // 完整的全拼Str
            StringBuilder qpStrBuilder = new StringBuilder();

            String qpNumber = ""; // 全拼对应的拨号盘数字
            StringBuilder qpNumberBuilder = new StringBuilder();

            String jpName = ""; // 简拼
            StringBuilder jpNameBuilder = new StringBuilder();

            String jpNumber = ""; // 简拼对应的拨号盘数字
            StringBuilder jpNumberBuilder = new StringBuilder();
//			LogUtil.v(name);
            int length = 0;
            char c = 0;
            // 处理英文名

            if (name.getBytes().length == name.length()) {
                qpName = name;
                String[] splitName = name.split(" ");
                for (String s : splitName) {
                    length = s.length();
                    for (int i = 0; i < length; i++) {
                        qpNumberBuilder.append(DialNumberMap.numberMap.get(s.charAt(i)) == null ? String.valueOf(s.charAt(i)) : DialNumberMap.numberMap.get(s.charAt(i)));
                    }
                    c = s.charAt(0);
                    qpNumberBuilder.append(" ");
                    jpNumberBuilder.append(DialNumberMap.numberMap.get(c) == null ? String.valueOf(c) : DialNumberMap.numberMap.get(c));
                    jpNameBuilder.append(String.valueOf(c));
                    qpStrBuilder.append(String.valueOf(c).toUpperCase()).append(s.subSequence(1, s.length()));
                }
                length = splitName.length;
                for (int i = 0; i < length; i++) {
                    splitName[i] = splitName[i].toLowerCase();
                }
                // jpName = jpNameBuilder.toString();
            } else { // 含有中文
                int namelength = name.length();
                for (int i = 0; i < namelength; i++) {
                    try {
                        String[] pyArray = PinyinHelper.toHanyuPinyinStringArray(name.charAt(i), format);
                        if (pyArray == null) {
//							char c = name.charAt(i);
                            c = name.charAt(i);
                            if (' ' == c) {
                                continue;
                            }
                            qpStrBuilder.append(c);
                            Integer num = DialNumberMap.numberMap.get(c);
                            qpNumberBuilder.append(num == null ? String.valueOf(c) : num).append(" ");
                            jpNumberBuilder.append(num == null ? String.valueOf(c) : num);
                            qpBuilder.append(String.valueOf(c).toLowerCase()).append(" ");
                            jpNameBuilder.append(String.valueOf(c).toLowerCase());
                            continue;
                        } else {
                            String py = pyArray[0];
                            length = py.length();
                            for (int j = 0; j < length; j++) {
                                qpNumberBuilder.append(DialNumberMap.numberMap.get(py.charAt(j)));
                            }
                            c = py.charAt(0);
                            qpNumberBuilder.append(" ");
                            jpNameBuilder.append(c);
                            jpNumberBuilder.append(DialNumberMap.numberMap.get(c));
                            qpBuilder.append(py).append(" ");
                            qpStrBuilder.append(String.valueOf(c).toUpperCase()).append(py.subSequence(1, py.length()));// 将拼音第一个字母转成大写后拼接在一起。
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
                qpName = qpBuilder.toString();
            }
            jpName = jpNameBuilder.toString();
            jpNumber = jpNumberBuilder.toString();
            qpNumber = qpNumberBuilder.toString();
            qpNameStr = qpStrBuilder.toString();

            if (qpName.length() > 0) {
                contact.setQpName(qpName.trim().split(" "));
                contact.setQpNumber(qpNumber.trim().split(" "));
                contact.setJpNumber(jpNumber.trim());
                //如果首字母为数字，则将其编号置为"#"
                if (!new RegexChar().judge(jpName.substring(0, 1))) {
                    contact.setJpName("#");
                } else {
                    contact.setJpName(jpName.toUpperCase());
                }
                contact.setQpNameStr(qpNameStr.trim());
                contact.setQpNumber(qpNumber.trim().split(" "));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回讨论组的头像
     *
     * @return
     */
    public static Bitmap getChatroomPhoto(final String groupid) {
        try {
            if (photoCache.containsKey(groupid)) {
                return photoCache.get(groupid);
            }
            new Thread(new Runnable() {

                @Override
                public void run() {
                    processChatroomPhoto(groupid);
                }
            });
            processChatroomPhoto(groupid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (groupid.toUpperCase().startsWith("G")) {
            return BitmapFactory.decodeResource(CCPAppManager.getContext().getResources(), R.drawable.group_head);
        }
        return mDefaultBitmap;
    }

    /**
     * @param groupid
     */
    private static void processChatroomPhoto(String groupid) {
        ArrayList<String> groupMembers = GroupMemberSqlManager.getGroupMemberID(groupid);
        if (groupMembers != null) {
            ArrayList<String> contactName = ContactSqlManager.getContactRemark(groupMembers.toArray(new String[]{}));
            if (contactName != null) {
                Bitmap[] bitmaps = new Bitmap[contactName.size()];
                if (bitmaps.length > 9) {
                    bitmaps = new Bitmap[9];
                }
                List<BitmapUtil.InnerBitmapEntity> bitmapEntitys = getBitmapEntitys(bitmaps.length);
                for (int i = 0; i < bitmaps.length; i++) {
                    Bitmap photo = getPhoto(contactName.get(i));
                    photo = ThumbnailUtils.extractThumbnail(photo, (int) bitmapEntitys.get(0).width, (int) bitmapEntitys.get(0).width);
                    bitmaps[i] = photo;
                }
                Bitmap combineBitmap = BitmapUtil.getCombineBitmaps(bitmapEntitys, bitmaps);
                if (combineBitmap != null) {
                    photoCache.put(groupid, combineBitmap);
                    BitmapUtil.saveBitmapToLocal(groupid, combineBitmap);
                }
            }
        }
    }

    private static List<BitmapUtil.InnerBitmapEntity> getBitmapEntitys(int count) {
        List<BitmapUtil.InnerBitmapEntity> mList = new LinkedList<BitmapUtil.InnerBitmapEntity>();
        String value = ECPropertiesUtil.readData(CCPAppManager.getContext(), String.valueOf(count), R.raw.nine_rect);
        LogUtil.d("value=>" + value);
        String[] arr1 = value.split(";");
        int length = arr1.length;
        for (int i = 0; i < length; i++) {
            String content = arr1[i];
            String[] arr2 = content.split(",");
            BitmapUtil.InnerBitmapEntity entity = null;
            for (int j = 0; j < arr2.length; j++) {
                entity = new BitmapUtil.InnerBitmapEntity();
                entity.x = Float.valueOf(arr2[0]);
                entity.y = Float.valueOf(arr2[1]);
                entity.width = Float.valueOf(arr2[2]);
                entity.height = Float.valueOf(arr2[3]);
            }
            mList.add(entity);
        }
        return mList;
    }


    public static void getMobileContactPhoto(List<ECContacts> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (ECContacts contact : list) {
            if (contact.getPhotoId() > 0) {
                getMobileContactPhoto(contact);
            }
        }
    }

    public static void getMobileContactPhoto(ECContacts contact) {
        try {
            Bitmap bitmap = getContactPhoto(contact);
            if (bitmap == null) {
                return;
            }
            contact.setRemark("mobilePhoto://" + contact.getContactid());
            DemoUtils.saveBitmapToLocal(new File(FileAccessor.getAvatarPathName(), contact.getContactid()), bitmap);
            ContactSqlManager.updateContactPhoto(contact);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getContactPhoto(ECContacts contact) {
        long photoId = contact.getPhotoId();
        if (photoId != 0) {
            Cursor cursor = null;
            ContentResolver contentResolver = AppMain.getInstance().getApplicationContext().getContentResolver();
            try {
                cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Photo._ID, ContactsContract.CommonDataKinds.Photo.PHOTO}, ContactsContract.CommonDataKinds.Photo._ID + " = " + photoId, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    byte[] photo = cursor.getBlob(1);
                    if (photo != null) {
                        return BitmapFactory.decodeByteArray(photo, 0, photo.length);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        }
        return null;
    }

}
