package com.cn.fit.ui.chat.ui.group;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.base.CCPClearEditText;
import com.cn.fit.ui.chat.common.dialog.ECProgressDialog;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.storage.GroupSqlManager;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.SDKCoreHelper;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECGroupMatch;

public class SearchGroupActivity extends ECSuperActivity implements View.OnClickListener {

    private int mSearchType;
    private ListView mResultView;
    private CCPClearEditText mEdittext;
    private ECProgressDialog mPostingdialog;
    private GroupAdapter mGroupAdapter;

    final private TextWatcher textWatcher = new TextWatcher() {

        private int fliteCounts = 20;
        ;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            LogUtil.d(LogUtil.getLogUtilsTag(textWatcher.getClass()), "fliteCounts=" + fliteCounts);
            fliteCounts = fliteCounts(s);
            if (fliteCounts < 0) {
                fliteCounts = 0;
            }
            if (checkNameEmpty()) {
                getTopBarView().setRightBtnEnable(true);
                return;
            }
            getTopBarView().setRightBtnEnable(false);
            mGroupAdapter.clear();
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mGroupAdapter != null) {
                ECGroup dGroup = mGroupAdapter.getItem(position);
                if (GroupSqlManager.isNeedApply(dGroup.getGroupId())) {
                    Intent intent = new Intent(SearchGroupActivity.this, ApplyWithGroupPermissionActivity.class);
                    intent.putExtra(GroupInfoActivity.GROUP_ID, dGroup.getGroupId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SearchGroupActivity.this, ChattingActivity.class);
                    intent.putExtra(ChattingActivity.RECIPIENTS, dGroup.getGroupId());
                    intent.putExtra(ChattingActivity.CONTACT_USER, dGroup.getName());
                    startActivity(intent);
                }


                clearSearch();
            }
        }
    };

    private void clearSearch() {
        if (mEdittext != null) {
            mEdittext.setText("");
        }
        if (mGroupAdapter != null) {
            mGroupAdapter.setData(null);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSearchType = getIntent().getIntExtra(BaseSearch.EXTRA_SEARCH_TYPE, BaseSearch.SEARCH_BY_ID);
        initView();
        String title = getString(mSearchType == BaseSearch.SEARCH_BY_ID ? R.string.searcha_by_id_tip : R.string.searcha_by_indistinct_name_tip);
        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted,
                R.drawable.btn_style_green, null,
                getString(R.string.dialog_ok_button),
                title, null, this);
        getTopBarView().setRightBtnEnable(false);
    }

    private void initView() {
        mEdittext = (CCPClearEditText) findViewById(R.id.search_flite);
        mEdittext.setHint(mSearchType == BaseSearch.SEARCH_BY_ID ? R.string.str_sear_group_id_hint : R.string.str_sear_group_name_hint);
        mResultView = (ListView) findViewById(R.id.searcha_result_lv);
        View mEmptyView = findViewById(R.id.empty_search_tv);
        mResultView.setEmptyView(mEmptyView);
        mGroupAdapter = new GroupAdapter(this);
        mResultView.setAdapter(mGroupAdapter);
        mResultView.setOnItemClickListener(onItemClickListener);

        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = filter;
        mEdittext.setFilters(inputFilters);
        mEdittext.addTextChangedListener(textWatcher);
    }

    /**
     * @return
     */
    private boolean checkNameEmpty() {
        return mEdittext != null && mEdittext.getText().toString().trim().length() > 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_group;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.text_right:
                hideSoftKeyboard();
                ECGroupManager ecGroupManager = SDKCoreHelper.getECGroupManager();
                if (!checkNameEmpty() || ecGroupManager == null) {
                    return;
                }
                mPostingdialog = new ECProgressDialog(this, R.string.search_group_posting);
                mPostingdialog.show();
                String keywords = mEdittext.getText().toString().trim();
                ECGroupMatch match = new ECGroupMatch(ECGroupMatch.SearchType.GROUPID);
                if (mSearchType == BaseSearch.SEARCH_BY_INDISTINCT_NAME) {
                    match.setSearchType(ECGroupMatch.SearchType.GROUPNAME);
                }
                match.setkeywords(keywords);
                // 调用API创建群组、处理创建群组接口回调
                ecGroupManager.searchPublicGroups(match, new ECGroupManager.OnSearchPublicGroupsListener() {
                    @Override
                    public void onSearchPublicGroupsComplete(ECError error, List<ECGroup> groups) {
                        if (mPostingdialog != null && mPostingdialog.isShowing()) {
                            mPostingdialog.dismiss();
                            ;
                            mPostingdialog = null;
                        }
                        if (error.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
                            GroupSqlManager.insertGroupInfos(groups, -1);
                            mGroupAdapter.setData(groups);
                            return;
                        }
                        ToastUtil.showMessage("查询失败[" + error.errorCode + "]");
                    }

                });
                break;
            default:
                break;
        }
    }

    public class GroupAdapter extends ArrayAdapter<ECGroup> {
        int padding;

        /**
         * @param ctx
         */
        public GroupAdapter(Context ctx) {
            super(ctx, 0);
            padding = ctx.getResources().getDimensionPixelSize(R.dimen.OneDPPadding);
        }

        public void setData(List<ECGroup> data) {
            clear();
            if (data != null) {
                for (ECGroup group : data) {
                    add(group);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder mViewHolder;
            if (convertView == null || convertView.getTag() == null) {
                view = View.inflate(SearchGroupActivity.this, R.layout.search_group_result_item, null);

                mViewHolder = new ViewHolder();
                mViewHolder.group_id = (TextView) view.findViewById(R.id.group_id);
                mViewHolder.group_name = (TextView) view.findViewById(R.id.group_name);
                mViewHolder.join_state = (TextView) view.findViewById(R.id.join_state);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            ECGroup group = getItem(position);
            if (group != null) {
                mViewHolder.group_id.setText(getString(R.string.str_group_id_fmt, DemoUtils.getGroupShortId(group.getGroupId())));
                mViewHolder.group_name.setText(group.getName());
            }

            return view;
        }

        class ViewHolder {
            TextView group_id;
            TextView group_name;
            TextView join_state;
        }

    }

    final InputFilter filter = new InputFilter() {

        private int limit = 30;

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            LogUtil.i(LogUtil.getLogUtilsTag(SearchGroupActivity.class), source
                    + " start:" + start + " end:" + end + " " + dest
                    + " dstart:" + dstart + " dend:" + dend);
            float count = calculateCounts(dest);
            int overplus = limit - Math.round(count) - (dend - dstart);
            if (overplus <= 0) {
                if ((Float.compare(count, (float) (limit - 0.5D)) == 0)
                        && (source.length() > 0)
                        && (!(DemoUtils.characterChinese(source.charAt(0))))) {
                    return source.subSequence(0, 1);
                }
                return "";
            }

            if (overplus >= (end - start)) {
                return null;
            }
            int tepmCont = overplus + start;
            if ((Character.isHighSurrogate(source.charAt(tepmCont - 1))) && (--tepmCont == start)) {
                return "";
            }
            return source.subSequence(start, tepmCont);
        }

    };


    /**
     * @param text
     * @return
     */
    public static int fliteCounts(CharSequence text) {
        int count = (30 - Math.round(calculateCounts(text)));
        LogUtil.v(LogUtil.getLogUtilsTag(SearchGroupActivity.class), "count " + count);
        return count;
    }

    /**
     * @param text
     * @return
     */
    public static float calculateCounts(CharSequence text) {

        float lengh = 0.0F;
        for (int i = 0; i < text.length(); i++) {
            if (!DemoUtils.characterChinese(text.charAt(i))) {
                lengh += 1.0F;
            } else {
                lengh += 0.5F;
            }
        }

        return lengh;
    }
}
