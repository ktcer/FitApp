package com.cn.fit.ui.chat.ui.contact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.customer.coach.ActivityCoachPage;
import com.cn.fit.customer.coach.ActivityCoachsList;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.core.ContactsCache;
import com.cn.fit.ui.chat.core.ContactsCache.InterfaceGetContactsListener;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.ui.ContactListFragment;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.TabFragment;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.fit.ui.scancode.ActivityCapture;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CustomDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jorstin on 2015/3/20.
 */
public class MobileContactActivity extends ECSuperActivity implements
        View.OnClickListener {

    private static Long expertId = 0l;
    private static String url = "";

    @Override
    protected int getLayoutId() {
        return R.layout.mobile_contacts_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        // Create the list fragment and add it as our sole content.
        if (savedInstanceState == null) {
            // Do first time initialization -- add initial fragment.
            MobileContactFragment list = new MobileContactFragment();
            fm.beginTransaction().add(R.id.mobile_content, list).commit();
        }

        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1,
                getString(R.string.mobile_contact), this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;

            default:
                break;
        }
    }

    public static class MobileContactFragment extends TabFragment {
        private static final String TAG = "ECDemo.MobileContactFragment";

        /**
         * 当前联系人列表类型（查看、联系人选择）
         */
        public static final int TYPE_NORMAL = 1;
        public static final int TYPE_SELECT = 2;
        /**
         * 列表类型
         */
        private int mType;
        private String[] sections = {"A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z", "#"};
        private static final String ALL_CHARACTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
        /**
         * 每个字母最开始的位置
         */
        private HashMap<String, Integer> mFirstLetters;
        /**
         * 当前选择联系人位置
         */
        public static ArrayList<Integer> positions = new ArrayList<Integer>();
        /**
         * 每个首字母对应的position
         */
        private String[] mLetterPos;
        private ContactListFragment.OnContactClickListener mClickListener;
        /**
         * 每个姓氏第一次出现对应的position
         */
        private int[] counts;
        private String mSortKey = "#";
        private PinnedHeaderListView mListView;
        private CustomSectionIndexer mCustomSectionIndexer;
        private ContactsAdapter mAdapter;
        /**
         * 选择联系人
         */
        private View mSelectCardItem;
        private final int SCANER_CODE = 1;
        final private View.OnClickListener mSelectClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.card_item_tv:
                        // 进入健康秘书列表
                        Intent intent = new Intent(
                                MobileContactFragment.this.getActivity(),
                                ActivityCoachsList.class);
                        startActivity(intent);
                        break;
                    case R.id.card_scan_code:
                        // 进入扫码界面
                        Intent intent1 = new Intent(
                                MobileContactFragment.this.getActivity(),
                                ActivityCapture.class);
                        startActivityForResult(intent1, SCANER_CODE);
                        break;
                    default:
                        break;
                }
                // Intent intent = new
                // Intent(MobileContactFragment.this.getActivity() ,
                // EditConfigureActivity.class);
                // intent.putExtra(EditConfigureActivity.EXTRA_EDIT_TITLE ,
                // getString(R.string.edit_add_contacts));
                // intent.putExtra(EditConfigureActivity.EXTRA_EDIT_HINT ,
                // getString(R.string.edit_add_contacts));
                // startActivityForResult(intent , 0xa);

            }
        };

        // 设置联系人点击事件通知
        private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int headerViewsCount = mListView.getHeaderViewsCount();
                if (position < headerViewsCount) {
                    return;
                }
                int _position = position - headerViewsCount;

                if (mAdapter == null || mAdapter.getItem(_position) == null) {
                    return;
                }
                if (mType != TYPE_NORMAL) {
                    // 如果是选择联系人模式
                    Integer object = Integer.valueOf(_position);
                    if (positions.contains(object)) {
                        positions.remove(object);
                    } else {
                        positions.add(object);
                    }
                    notifyClick(positions.size());
                    mAdapter.notifyDataSetChanged();
                    return;
                }

                ECContacts contacts = mAdapter.getItem(_position);
                if (contacts == null || contacts.getId() == -1) {
                    ToastUtil.showMessage(R.string.contact_none);
                    return;
                }

                if (contacts.getRemainServeTime() <= 0) {
                    showReorderServiceDialog();
                    // intent = new Intent(getActivity(),
                    // ActivityCoachPage.class);
                    String contactId = contacts.getContactid();
                    contactId = contactId.substring(0, contactId.length() - 1);
                    expertId = Long.parseLong(contactId);
                    url = contacts.getImgUrl();
                } else {
                    Intent intent;
                    intent = new Intent(getActivity(),
                            ContactDetailActivity.class);
                    intent.putExtra(ContactDetailActivity.MOBILE,
                            contacts.getContactid());
                    intent.putExtra(ContactDetailActivity.DISPLAY_NAME,
                            contacts.getNickname());
                    intent.putExtra(ContactDetailActivity.IMAGEURL,
                            contacts.getImgUrl());
                    intent.putExtra(ContactDetailActivity.REMAIN_TIME,
                            contacts.getRemainServeTime());
//					intent.putExtra(ContactDetailActivity.ISMASTER, contacts.getIsmaster());
                    startActivity(intent);
                }

                // startActivity(new Intent(getActivity() ,
                // MobileContactActivity.class));
            }
        };

        public void showReorderServiceDialog() {

            CustomDialog.Builder builder = new CustomDialog.Builder(
                    getActivity());
            builder.setTitle("提示");
            String content = "该专家的服务已经到期，请尽快续约";
            builder.setMessage(content);
            builder.setPositiveButton("续约",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(getActivity(),
                                    ActivityCoachPage.class);
                            intent.putExtra("ExpertId",
                                    MobileContactActivity.expertId);
                            intent.putExtra("image", url);
                            intent.putExtra("pageType", "1");
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        }

        /**
         * Create a new instance of ContactListFragment, providing "type" as an
         * argument.
         */
        public static MobileContactFragment newInstance(int type) {
            MobileContactFragment f = new MobileContactFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("type", type);
            f.setArguments(args);

            return f;
        }

        @Override
        protected int getLayoutId() {
            return R.layout.mobile_contacts_activity;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mType = getArguments() != null ? getArguments().getInt("type")
                    : TYPE_NORMAL;
            if (positions == null) {
                positions = new ArrayList<Integer>();
            }
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            if (!(activity instanceof MobileContactSelectActivity)
                    || mType == TYPE_NORMAL) {
                return;
            }
            try {
                mClickListener = (ContactListFragment.OnContactClickListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnContactClickListener");
            }
        }

        private void notifyClick(int count) {
            if (mClickListener != null) {
                mClickListener.onContactClick(count);
            }
        }

        /**
         * 选择的联系人
         */
        public String getChatuser() {
            StringBuilder selectUser = new StringBuilder();
            for (Integer position : positions) {
                ECContacts item = mAdapter.getItem(position);
                ContactSqlManager.insertContact(item);
                if (item != null) {
                    selectUser.append(item.getContactid()).append(",");
                }
            }

            if (selectUser.length() > 0) {
                selectUser.substring(0, selectUser.length() - 1);
            }
            return selectUser.toString();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            registerReceiver(new String[]{ContactsCache.ACTION_ACCOUT_INIT_CONTACTS});
            if (mListView != null) {
                mListView.setAdapter(null);
            }
            mListView = (PinnedHeaderListView) findViewById(R.id.address_contactlist);
            mListView.setOnItemClickListener(onItemClickListener);
            // //先请求患者所有的保健秘书
            // getExperts();
            initContactListView();
            findView();
        }

        @Override
        public void onResume() {
            // TODO Auto-generated method stub
            super.onResume();
            if (ContactsCache.getInstance().getContacts() == null
                    || ContactsCache.getInstance().getContacts().isEmpty()) {
                ContactsCache.getInstance().load();
                ContactsCache.getInstance().setOnGetContactsDoneListener(new InterfaceGetContactsListener() {

                    @Override
                    public void getContactsDone() {
                        // TODO Auto-generated method stub
                        Log.i("test", "被回收了，重新加载回来，哈哈");
                        refreshContacts();
                    }
                });
            }

        }

        /**
         * 初始化联系人列表
         */
        private void initContactListView() {
            if (mListView != null && mSelectCardItem != null) {
                mListView.removeHeaderView(mSelectCardItem);
                mListView.setAdapter(null);
            }
            if (ContactsCache.getInstance().getContacts() == null) {
                return;
            }
            counts = new int[sections.length];

            for (ECContacts c : ContactsCache.getInstance().getContacts()) {

                String firstCharacter = c.getSortKey();
                int index = ALL_CHARACTER.indexOf(firstCharacter);
                counts[index]++;
            }
            if (ContactsCache.getInstance().getContacts() != null
                    && !ContactsCache.getInstance().getContacts().isEmpty()) {
                // for(int i = 0 ; i < counts.length; i++){
                // if(counts[i]!=0){
                // mSortKey = sections[i];
                // break;
                // }
                // }
                mSortKey = ContactsCache.getInstance().getContacts().get(0)
                        .getSortKey();
            }
            mCustomSectionIndexer = new CustomSectionIndexer(sections, counts);
            if (mType == TYPE_NORMAL) {
                mSelectCardItem = View.inflate(getActivity(),
                        R.layout.group_card_item, null);
                TextView startCard = (TextView) mSelectCardItem
                        .findViewById(R.id.card_item_tv);
                // startCard.setGravity(Gravity.CENTER);
                startCard.setText(R.string.edit_add_contacts);
                if (startCard != null) {
                    startCard.setOnClickListener(mSelectClickListener);
                }
                ImageView scanCodeCard = (ImageView) mSelectCardItem
                        .findViewById(R.id.card_scan_code);
                if (scanCodeCard != null) {
                    scanCodeCard.setOnClickListener(mSelectClickListener);
                }

                mListView.addHeaderView(mSelectCardItem);
            }
            mAdapter = new ContactsAdapter(getActivity());
            mAdapter.setData(ContactsCache.getInstance().getContacts(),
                    mCustomSectionIndexer);
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(mAdapter);
            // 設置頂部固定頭部
            mListView.setPinnedHeaderView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.header_item_cator, mListView, false));
            findViewById(R.id.loading_tips_area).setVisibility(View.GONE);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode,
                                     Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
                    + ", resultCode=" + resultCode + ", data=" + data);

            // If there's no data (because the user didn't select a picture and
            // just hit BACK, for example), there's nothing to do.
            if (requestCode == 0xa) {
                if (data == null) {
                    return;
                }
            } else if (resultCode != RESULT_OK) {
                LogUtil.d("onActivityResult: bail due to resultCode="
                        + resultCode);
                return;
            }
            if (requestCode == 0xa) {
                String result_data = data.getStringExtra("result_data");
                if (TextUtils.isEmpty(result_data)
                        || result_data.trim().length() == 0) {
                    ToastUtil.showMessage(R.string.mobile_list_empty);
                    return;
                }
                /*
				 * if(!CheckUtil.isMobileNO(result_data)) {
				 * ToastUtil.showMessage(R.string.input_mobile_error); return ;
				 * }
				 */
                Intent intent = new Intent(
                        MobileContactFragment.this.getActivity(),
                        ChattingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(ChattingActivity.RECIPIENTS, result_data);
                intent.putExtra(ChattingActivity.CONTACT_USER, result_data);
                startActivity(intent);
            }

        }

        private void findView() {

            BladeView mLetterListView = (BladeView) findViewById(R.id.mLetterListView);

            mLetterListView
                    .setOnItemClickListener(new BladeView.OnItemClickListener() {

                        @Override
                        public void onItemClick(String s) {
                            if (s != null && ALL_CHARACTER != null
                                    && mCustomSectionIndexer != null) {
                                int section = ALL_CHARACTER.indexOf(s);
                                int position = mCustomSectionIndexer
                                        .getPositionForSection(section);
                                if (position != -1) {
                                    mListView.setSelection(position);
                                }
                            }

                        }
                    });
        }

        @Override
        public void onTabFragmentClick() {

        }

        @Override
        protected void onReleaseTabUI() {

        }

        @Override
        public void onDetach() {
            super.onDetach();
            if (positions != null) {
                positions.clear();
                positions = null;
            }
        }

        @Override
        protected void handleReceiver(Context context, Intent intent) {
            super.handleReceiver(context, intent);
            if (ContactsCache.ACTION_ACCOUT_INIT_CONTACTS.equals(intent
                    .getAction())) {
                LogUtil.d("handleReceiver ACTION_ACCOUT_INIT_CONTACTS");
                initContactListView();
            }
        }

        class ContactsAdapter extends ArrayAdapter<ECContacts> implements
                PinnedHeaderListView.PinnedHeaderAdapter,
                AbsListView.OnScrollListener {
            CustomSectionIndexer mIndexer;
            Context mContext;
            private int mLocationPosition = -1;

            public ContactsAdapter(Context context) {
                super(context, 0);
                mContext = context;
            }

            public void setData(List<ECContacts> data,
                                CustomSectionIndexer indexer) {
                mIndexer = indexer;
                setNotifyOnChange(false);
                clear();
                setNotifyOnChange(true);
                if (data != null) {
                    for (ECContacts appEntry : data) {
                        add(appEntry);
                    }
                }
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view;
                ViewHolder mViewHolder;
                if (convertView == null || convertView.getTag() == null) {
                    view = View.inflate(mContext,
                            R.layout.mobile_contact_list_item, null);

                    mViewHolder = new ViewHolder();
                    mViewHolder.item = (LinearLayout) view
                            .findViewById(R.id.contact_item_ll);
                    mViewHolder.mAvatar = (ImageView) view
                            .findViewById(R.id.avatar_iv);
                    mViewHolder.name_tv = (EmojiconTextView) view
                            .findViewById(R.id.name_tv);
                    mViewHolder.account = (TextView) view
                            .findViewById(R.id.account);
                    mViewHolder.period = (TextView) view
                            .findViewById(R.id.tv_remain_period);
                    mViewHolder.checkBox = (CheckBox) view
                            .findViewById(R.id.contactitem_select_cb);
                    mViewHolder.tvCatalog = (TextView) view
                            .findViewById(R.id.contactitem_catalog);
                    view.setTag(mViewHolder);
                } else {
                    view = convertView;
                    mViewHolder = (ViewHolder) view.getTag();
                }

                ECContacts contacts = getItem(position);
                if (contacts != null) {
                    int section = mIndexer.getSectionForPosition(position);
                    if (mIndexer.getPositionForSection(section) == position) {
                        mViewHolder.tvCatalog.setVisibility(View.VISIBLE);
                        if (position == 0 && contacts.getSortKey().equals("*")) {
                            mViewHolder.tvCatalog.setText("主管健康秘书");
                        } else {
                            mViewHolder.tvCatalog.setText(contacts.getSortKey());
                        }
                    } else {
                        mViewHolder.tvCatalog.setVisibility(View.GONE);
                    }
                    String remark = contacts.getRemark();
                    // String remark =
                    // ContactLogic.CONVER_PHONTO[ECSDKUtils.getIntRandom(4,
                    // 0)];
                    ImageLoader.getInstance().displayImage(
                            AbsParam.getBaseUrl() + contacts.getImgUrl(),
                            mViewHolder.mAvatar,
                            AppMain.initImageOptions(
                                    R.drawable.icon_touxiang_persion_gray,
                                    false));
                    // mViewHolder.mAvatar.setImageBitmap(ContactLogic.getPhoto(/*contacts.getRemark()*/remark));
                    mViewHolder.name_tv.setText(contacts.getNickname());
//					 mViewHolder.account.setText("秘书工号："+contacts.getContactid());
//					mViewHolder.account.setText("擅长"
//							+ contacts.getDiseaseType());
                    mViewHolder.account.setVisibility(View.INVISIBLE);
//					mViewHolder.account.setText("年龄:"+contacts.getAge());
                    String remainServeTime;
//					ColorMatrix cm = new ColorMatrix();
//					if(contacts.getRemainServeTime() > 0){
//						mViewHolder.name_tv.setTextColor(getResources().getColor(R.color.black));
//						cm.setSaturation(1);
//					}
//					if (contacts.getRemainServeTime() <= 0) {
//						remainServeTime = "已过期";
//						mViewHolder.period.setTextColor(getResources()
//								.getColor(R.color.font_gray));
//						cm.setSaturation(0);
//
//						mViewHolder.name_tv.setTextColor(getResources().getColor(R.color.font_gray));
//					} else if (contacts.getRemainServeTime() <= 5) {
//						// 显示红色
//						remainServeTime = contacts.getRemainServeTime() + "天";
//						mViewHolder.period.setTextColor(getResources()
//								.getColor(R.color.red));
//					} else if ((contacts.getRemainServeTime() > 5)
//							&& (contacts.getRemainServeTime() <= 10)) {
//						// 显示橙色
//						remainServeTime = contacts.getRemainServeTime() + "天";
//						mViewHolder.period.setTextColor(getResources()
//								.getColor(R.color.orange));
//					} else {// if(contacts.getRemainServeTime()>10){
//						// 显示绿色
//						remainServeTime = contacts.getRemainServeTime() + "天";
//						mViewHolder.period.setTextColor(getResources()
//								.getColor(R.color.lightgreen));
//					}
//					//头像灰化处理
//					ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
//					mViewHolder.mAvatar.setColorFilter(cf);
//
//
                    mViewHolder.period.setText("年龄:" + contacts.getAge());
//					mViewHolder.period.setVisibility(View.INVISIBLE);

                    if (mType != TYPE_NORMAL) {
                        mViewHolder.checkBox.setVisibility(View.VISIBLE);
                        if (mViewHolder.checkBox.isEnabled()
                                && positions != null) {
                            mViewHolder.checkBox.setChecked(positions
                                    .contains(position));
                        } else {
                            mViewHolder.checkBox.setChecked(false);
                        }
                    } else {
                        mViewHolder.checkBox.setVisibility(View.GONE);
                    }
                }

                return view;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (view instanceof PinnedHeaderListView) {
                    ((PinnedHeaderListView) view)
                            .configureHeaderView(firstVisibleItem);
                }
            }

            @Override
            public int getPinnedHeaderState(int position) {
                int realPosition = position - 1;
                if (realPosition < 0
                        || (mLocationPosition != -1 && mLocationPosition == realPosition)) {
                    return PINNED_HEADER_GONE;
                }
                mLocationPosition = -1;
                int section = mIndexer.getSectionForPosition(realPosition);
                int nextSectionPosition = mIndexer
                        .getPositionForSection(section + 1);
                if (nextSectionPosition != -1
                        && realPosition == nextSectionPosition - 1) {
                    return PINNED_HEADER_PUSHED_UP;
                }
                return PINNED_HEADER_VISIBLE;
            }

            @Override
            public void configurePinnedHeader(View header, int position,
                                              int alpha) {
                int realPosition = position;
                int _position = position - 1;
                if (_position < 0)
                    return;
                TextView headView = ((TextView) header
                        .findViewById(R.id.contactitem_catalog));
                if (_position == 0) {
                    headView.setText(mSortKey);
                    return;
                }
                ECContacts item = getItem(_position);
                if (item != null) {
                    headView.setText(item.getSortKey());
                }
				/*
				 * int section = mIndexer.getSectionForPosition(realPosition);
				 * String title = (String) mIndexer.getSections()[section];
				 */
            }

            class ViewHolder {
                LinearLayout item;
                /**
                 * 头像
                 */
                ImageView mAvatar;
                /**
                 * 名称
                 */
                EmojiconTextView name_tv;
                /**
                 * 账号
                 */
                TextView account;
                /**
                 * 服务剩余时长
                 */
                TextView period;
                /**
                 * 选择状态
                 */
                CheckBox checkBox;
                TextView tvCatalog;

            }
        }

        public void refreshContacts() {
            initContactListView();
        }

    }

}
