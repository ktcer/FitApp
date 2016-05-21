//package com.cn.aihu.ui.chat.common;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.database.ContentObserver;
//import android.os.Handler;
//import android.provider.ContactsContract;
//
//import com.cn.aihu.ui.AppMain;
//import com.cn.aihu.ui.chat.core.ContactsCache;
//
///**
// * com.cn.aihu.ui.chat.common in ECDemo_Android
// * Created by Jorstin on 2015/5/22.
// */
//public class ECContentObservers {
//
//    private static final int CONTACTS_CHANGED = 300;
//    private static ECContentObservers ourInstance = new ECContentObservers();
//
//    public static ECContentObservers getInstance() {
//        return ourInstance;
//    }
//
//    private Context mContext;
//    private ECContentObservers() {
//
//        mContext = AppMain.getInstance().getApplicationContext();
//    }
//
//
//    public void initContentObserver() {
//        ContentResolver resolver = mContext.getContentResolver();
//        resolver.registerContentObserver(ContactsContract.Data.CONTENT_URI, true, new MyContactObserver(null));
//    }
//
//    private class MyContactObserver extends ContentObserver {
//        public MyContactObserver(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        public void onChange(boolean selfChange) {
//            super.onChange(selfChange);
//            observerHandler.removeMessages(CONTACTS_CHANGED);
//            observerHandler.sendEmptyMessageDelayed(CONTACTS_CHANGED, 1000);
//        }
//    }
//
//
//    private Handler observerHandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case CONTACTS_CHANGED:
//                    ContactsCache.getInstance().reload();
//                    break;
//            }
//        };
//    };
//}
