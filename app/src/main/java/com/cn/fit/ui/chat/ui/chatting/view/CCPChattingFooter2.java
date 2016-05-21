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
package com.cn.fit.ui.chat.ui.chatting.view;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.base.CCPEditText;
import com.cn.fit.ui.chat.common.utils.DensityUtil;
import com.cn.fit.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.fit.ui.chat.common.utils.ECPreferences;
import com.cn.fit.ui.chat.common.utils.FileAccessor;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.ResourceHelper;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconEditText;
import com.cn.fit.customer.coach.ActivityCoachPage;
import com.cn.fit.util.CustomDialog;


/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class CCPChattingFooter2 extends LinearLayout {

    private int mRemainTime;
    private long mUserId;
    private String mUserImgUrl;
    private static final String TAG = LogUtil.getLogUtilsTag(CCPChattingFooter2.class);
    private static final int WHAT_ON_DIMISS_DIALOG = 0x1;

    // cancel recording sliding distance field.
    private static final int CANCLE_DANSTANCE = 60;
    /**
     * Chatting mode that input mode
     */
    public static final int CHATTING_MODE_KEYBORD = 1;

    /**
     * Chatting mode that Speech mode, voice record.
     */
    public static final int CHATTING_MODE_VOICE = 2;

    private InputMethodManager mInputMethodManager;

    private View mChattingFooterView;

    private View mVoiceHintRcding;
    private View mVoiceHintTooshort;
    private View mVoiceHintAnimArea;
    private View mVoiceHintLoading;

    /**
     * 'voice_rcd_hint_cancel_area
     */
    private View mVoiceRcdHitCancelView;

    private LinearLayout mTextPanel;

    private FrameLayout mChattingBottomPanel;

    private ImageButton mChattingAttach;

    private ImageButton mChattingModeButton;

    /**
     * The emoji send button in panel.
     */
    private ImageButton mBiaoqing;

    private ImageView mVoiceHintAnim;

    private ImageView mVoiceHintCancelIcon;

    private TextView mVoiceHintCancelText;

    private TextView mVoiceNormalWording;

    private Button mVoiceRecord;

    private Button mChattingSend;

    private EmojiconEditText mEditText;

    /**
     * Cloud communication panel, display all support ability
     */
    private AppPanel mAppPanel;

    /**
     * Panel tha display emoji.
     */
    private ChatFooterPanel mChatFooterPanel;

    /**
     *
     */
    private RecordPopupWindow popupWindow;

    /**
     * Interface definition for a callback to be invoked
     * when the {@link ChatFooterPanel} has been click
     */
    private OnChattingFooterLinstener mChattingFooterLinstener;

    private OnChattingPanelClickListener mChattingPanelClickListener;

    /**
     * Interface definition for a callback to be invoked
     * when Input Text
     */
    private ChatingInputTextWatcher mChatingInputTextWatcher;

    /**
     * Do not enable the enter button to send the message
     */
    private boolean mDonotEnableEnterkey;

    /**
     * Whether to display the keyboard
     */
    private boolean mShowKeyBord;

    /**
     * Whether to display emoji panel.
     */
    private boolean mBiaoqingEnabled;

    /**
     * Whether the voice recording button is touched.
     */
    private boolean mVoiceButtonTouched;

    /**
     * @see #CHATTING_MODE_KEYBORD
     * @see #CHATTING_MODE_VOICE
     */
    private int mChattingMode;
    private int mChattingFooterTopHeight;
    private int mAppPanleHeight = -1;
    private boolean mHasKeybordHeight;

    private static final int ampValue[] = {
            0, 15, 30, 45, 60, 75, 90, 100
    };
    private static final int ampIcon[] = {
            R.drawable.amp1,
            R.drawable.amp2,
            R.drawable.amp3,
            R.drawable.amp4,
            R.drawable.amp5,
            R.drawable.amp6,
            R.drawable.amp7
    };


    /**
     *
     */
    private int mTop;

    final private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            popupWindow.dismiss();
            mVoiceRecord.setBackgroundDrawable(ResourceHelper.getDrawableById(getContext(), R.drawable.voice_rcd_btn_talk_nor));
            mVoiceRecord.setEnabled(true);
        }

    };

    final private TextView.OnEditorActionListener mOnEditorActionListener
            = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (mRemainTime > 0) {
                if (((actionId == EditorInfo.IME_NULL) && mDonotEnableEnterkey) || actionId == EditorInfo.IME_ACTION_SEND) {
                    mChattingSend.performClick();
                    return true;
                }
//            return false;
            } else {
                showReorderServiceDialog();
            }
            return false;
        }
    };

    final private OnTouchListener mOnTouchListener
            = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hideBottomPanel();
//            if(mRemainTime>0){
            if (mChattingFooterLinstener != null) {
                mChattingFooterLinstener.OnInEditMode();
            }
//            }else{
//            	showReorderServiceDialog();
//            }
            return false;
        }

    };

    long currentTimeMillis = 0;
    final private OnTouchListener mOnVoiceRecTouchListener
            = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mRemainTime > 0) {
                if (getAvailaleSize() < 10) {
                    LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "sdcard no memory ");
                    ToastUtil.showMessage(R.string.media_no_memory);
                    return false;
                }
                long time = System.currentTimeMillis() - currentTimeMillis;
                if (time <= 300) {
                    LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "Invalid click ");
                    currentTimeMillis = System.currentTimeMillis();
                    return false;
                }

                if (!FileAccessor.isExistExternalStore()) {
                    ToastUtil.showMessage(R.string.media_ejected);
                    return false;

                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mVoiceButtonTouched = true;
                        //mVoiceRecord.setEnabled(false);
                        onPause();
                        LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "CCPChatFooter voice recording action down");
                        if (mChattingFooterLinstener != null) {
                            mChattingFooterLinstener.OnVoiceRcdInitReuqest();
                        }

                        mVoiceRecord.setBackgroundDrawable(ResourceHelper.getDrawableById(getContext(), R.drawable.voice_rcd_btn_talk_press));
                        mVoiceRecord.setText(R.string.chatfooter_releasetofinish);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (popupWindow == null) {
                            return false;
                        }
                        if (event.getX() <= 0.0F || event.getY() <= -CANCLE_DANSTANCE || event.getX() >= mVoiceRecord.getWidth()) {
                            LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "show cancel Tips");
                            mVoiceHintCancelText.setText(R.string.chatfooter_cancel_rcd_release);
                            mVoiceRecord.setText(R.string.chatfooter_cancel_rcd_release);
                            mVoiceRcdHitCancelView.setVisibility(View.VISIBLE);
                            mVoiceHintAnimArea.setVisibility(View.GONE);
                        } else {
                            LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "show rcd animation Tips");
                            mVoiceHintCancelText.setText(R.string.chatfooter_cancel_rcd);
                            mVoiceRecord.setText(R.string.chatfooter_releasetofinish);
                            mVoiceRcdHitCancelView.setVisibility(View.GONE);
                            mVoiceHintAnimArea.setVisibility(View.VISIBLE);
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "CCPChatFooter voice recording action up ");
                        resetVoiceRecordingButton();
                        break;
                }

//	            return false;
            } else {
                showReorderServiceDialog();
            }
            return false;
        }
    };

    final private OnClickListener mChattingSendClickListener
            = new OnClickListener() {

        @Override
        public void onClick(View v) {
            LogUtil.d(TAG, "send msg onClick");
            if (mRemainTime > 0) {
                String message = mEditText.getText().toString();

                if ((message.trim().length() == 0) && message.length() != 0) {
                    LogUtil.d(TAG, "empty message cant be sent");
                    return;
                }

                // send.
                if (mChattingFooterLinstener != null) {
                    mChattingFooterLinstener.OnSendTextMessageRequest(message);
                }

                mEditText.clearComposingText();
                mEditText.setText("");
            } else {
                showReorderServiceDialog();
            }

        }
    };


    public void showReorderServiceDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
        builder.setTitle("提示");
        String content = "该专家的服务已经到期，请尽快续约";
        builder.setMessage(content);
        builder.setPositiveButton("续约", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(getContext(), ActivityCoachPage.class);
                intent.putExtra("ExpertId", mUserId);
                intent.putExtra("image", mUserImgUrl);
                intent.putExtra("pageType", 1 + "");
                getContext().startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

//	private void chargeCoinsDiaolog(){
//		List<String> chargeCoinsList = new ArrayList<String>();
//		chargeCoinsList.add("10个金币");
//		chargeCoinsList.add("100个金币");
//		ECListDialog dialog = new ECListDialog(
//				this,
//				(String[]) chargeCoinsList.toArray(new String[chargeCoinsList.size()]));
//		dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
//			@Override
//			public void onDialogItemClick(Dialog d, int position) {
//				// handleContentMenuClick(itemPosition ,position);
//				selectServeType = position;
//				float chargeCoins = (float)(position==0?0.01:0.1);
//				Intent intent = new Intent(getBaseContext(),ActivityPayChoose.class);
//				intent.putExtra("money", chargeCoins);
//				intent.putExtra("description", "充值"+chargeCoins);
//				startActivity(intent);
//			}
//		});
//		dialog.setTitle("请您选择充值金币数量");
//		dialog.show();
//	}

    final private OnKeyListener mVoiceButtonKeyListener
            = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (mRemainTime > 0) {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN:

                        if ((keyCode != KeyEvent.KEYCODE_DPAD_CENTER && keyCode != KeyEvent.KEYCODE_ENTER)) {
                            mVoiceRecord.setText(R.string.chatfooter_releasetofinish);
                            mVoiceRecord.setBackgroundDrawable(ResourceHelper.getDrawableById(getContext(), R.drawable.voice_rcd_btn_talk_press));
                        }
                        break;
                    case KeyEvent.ACTION_UP:

                        if ((keyCode != KeyEvent.KEYCODE_DPAD_CENTER && keyCode != KeyEvent.KEYCODE_ENTER)) {
                            mVoiceRecord.setText(R.string.chatfooter_presstorcd);
                            mVoiceRecord.setBackgroundDrawable(ResourceHelper.getDrawableById(getContext(), R.drawable.voice_rcd_btn_talk_press));
                        }
                        break;
                    default:
                        break;
                }
            } else {
                showReorderServiceDialog();
            }
            return false;
        }
    };

    final private OnClickListener mChattingModeClickListener
            = new OnClickListener() {

        @Override
        public void onClick(View v) {
            hideInputMethod();
            hideChatFooterPanel();
            if (mRemainTime > 0) {
                if (mChattingMode != CHATTING_MODE_VOICE) {
                    switchChattingMode(CHATTING_MODE_VOICE);
                } else {
                    switchChattingMode(CHATTING_MODE_KEYBORD);
                }
            } else {
                showReorderServiceDialog();
            }

        }
    };

    final private OnClickListener mChattingSmileyClickListener
            = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mRemainTime > 0) {
                displaySmileyPanel();
            } else {
                showReorderServiceDialog();
            }
        }
    };

    final private OnClickListener mChattingAttachClickListener
            = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mRemainTime > 0) {
                if (isButtomPanelNotVisibility()) {

                    hideInputMethod();
                    //setMode(0, -1, true);
                    if (mAppPanel == null) {
                        initAppPanel();
                    }
                    mAppPanel.initFlipperRotateMe();

                    if (mChatFooterPanel != null) {
                        mChatFooterPanel.setVisibility(View.GONE);
                    }
                    mAppPanel.setVisibility(View.VISIBLE);
                    requestFocusEditText(false);
                    if (mChattingMode == CHATTING_MODE_VOICE) {
                        switchChattingMode(CHATTING_MODE_KEYBORD);
                    }
                    mChattingBottomPanel.setVisibility(View.VISIBLE);
                    mAppPanel.setVisibility(View.VISIBLE);
                    //mAppPanel.refreshAppPanel();
                } else {
                    //setMode(CHATTING_MODE_VOICE, 22, true);
                    if (mChatFooterPanel.getVisibility() == View.VISIBLE) {
                        mChatFooterPanel.setVisibility(View.GONE);
                        mAppPanel.setVisibility(View.VISIBLE);
                        setMode(0, 22, false);
                    } else {
                        hideChatFooterPanel();
                        requestFocusEditText(true);
                        mInputMethodManager.showSoftInput(mEditText, 0);
                    }
                }
            } else {
                showReorderServiceDialog();
            }
        }
    };


    final private AppPanel.OnAppPanelItemClickListener mAppPanelItemClickListener
            = new AppPanel.OnAppPanelItemClickListener() {

        @Override
        public void OnTakingPictureClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnTakingPictureRequest();
            }
        }

        @Override
        public void OnSelectImageClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnSelectImageReuqest();
            }
        }

        @Override
        public void OnSelectFileClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnSelectFileRequest();
            }
        }

    };

    final private EmojiGrid.OnEmojiItemClickListener mEmojiItemClickListener
            = new EmojiGrid.OnEmojiItemClickListener() {

        @Override
        public void onEmojiItemClick(int emojiid, String emojiName) {
            input(mEditText, emojiName);
        }

        @Override
        public void onEmojiDelClick() {
            mEditText.getInputConnection().sendKeyEvent(
                    new KeyEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            mEditText.getInputConnection().sendKeyEvent(
                    new KeyEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
        }
    };

    /**
     * @param context
     */
    public CCPChattingFooter2(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public CCPChattingFooter2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CCPChattingFooter2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        initChatFooter(context);
    }

    /**
     *
     */
    private void initChatFooter(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        mChattingFooterView = inflate(context, R.layout.ccp_chatting_footer2, this);
        mEditText = ((EmojiconEditText) findViewById(R.id.chatting_content_et));
        mTextPanel = ((LinearLayout) findViewById(R.id.text_panel_ll));
        mChattingBottomPanel = ((FrameLayout) findViewById(R.id.chatting_bottom_panel));
        mChattingAttach = ((ImageButton) findViewById(R.id.chatting_attach_btn));
        mChattingSend = ((Button) findViewById(R.id.chatting_send_btn));
        mVoiceRecord = ((Button) findViewById(R.id.voice_record_bt));
        mChattingModeButton = ((ImageButton) findViewById(R.id.chatting_mode_btn));
        mRemainTime = ((ChattingActivity) context).getmRemainTime();
        mUserImgUrl = ((ChattingActivity) context).getmUserImgUrl();
        String mRecipients = ((ChattingActivity) context).getmRecipients();
        mUserId = Long.parseLong((String) mRecipients.subSequence(0, mRecipients.length() - 1));
        enableChattingSend(false);
        resetEnableEnterkey();

        LogUtil.e(TAG, "send edittext ime option " + mEditText.getImeOptions());
        mEditText.setOnEditorActionListener(mOnEditorActionListener);
        mEditText.setOnTouchListener(mOnTouchListener);
        mChattingSend.setOnClickListener(mChattingSendClickListener);
        mVoiceRecord.setOnTouchListener(mOnVoiceRecTouchListener);
        mVoiceRecord.setOnKeyListener(mVoiceButtonKeyListener);
        mChattingModeButton.setOnClickListener(mChattingModeClickListener);

        initAppPanel();

        mChattingAttach.setVisibility(View.VISIBLE);
        mChattingAttach.setOnClickListener(mChattingAttachClickListener);

        setBottomPanelHeight(LayoutParams.MATCH_PARENT);

        LogUtil.i(TAG, "init time:" + (System.currentTimeMillis() - currentTimeMillis));
    }

    /**
     *
     */
    private void initAppPanel() {
        mAppPanel = (AppPanel) findViewById(R.id.chatting_app_panel);

        int height = ECPreferences.getSharedPreferences().getInt(
                ECPreferenceSettings.SETTINGS_KEYBORD_HEIGHT.getId(),
                ResourceHelper.fromDPToPix(getContext(), 320));
        mAppPanel.setOnAppPanelItemClickListener(mAppPanelItemClickListener);
        mAppPanel.setPanelHeight(height);
    }

    public final void initSmileyPanel() {
        mBiaoqing = (ImageButton) findViewById(R.id.chatting_smiley_btn);
        mBiaoqing.setVisibility(View.VISIBLE);
        mBiaoqing.setOnClickListener(mChattingSmileyClickListener);
    }

    /**
     *
     */
    public final void displaySmileyPanel() {
        mChattingMode = CHATTING_MODE_KEYBORD;
        mTextPanel.setVisibility(View.VISIBLE);
        mVoiceRecord.setVisibility(View.GONE);

        if (mChatFooterPanel != null) {
            mChatFooterPanel.reset();
        }

        setMode(CHATTING_MODE_VOICE, 21, true);
    }

    /**
     * Hide keyboard, keyboard does not show
     */
    private void hideInputMethod() {
        hideSoftInputFromWindow(this);
        setKeyBordShow(false);
    }

    /**
     * @param tab
     */
    public final void switchChattingPanel(String tab) {
        if (TextUtils.isEmpty(tab)) {
            return;
        }

        if (mChatFooterPanel == null) {
            initChattingFooterPanel();
        }
    }

    public void hideSoftInputFromWindow(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) view
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * init {@link ChatFooterPanel} if not null.
     */
    private void initChattingFooterPanel() {
        if (mChatFooterPanel == null) {
            if (CCPAppManager.getChatFooterPanel(getContext()) == null) {
                mChatFooterPanel = new SmileyPanel(getContext(), null);
            } else {
                mChatFooterPanel = CCPAppManager.getChatFooterPanel(getContext());
            }
        }
        mChatFooterPanel.setOnEmojiItemClickListener(mEmojiItemClickListener);
        if (mChatFooterPanel != null) {
            mChatFooterPanel.setVisibility(View.GONE);
        }

        if (mChattingBottomPanel != null) {
            mChattingBottomPanel.addView(mChatFooterPanel,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
        }

        if (mEditText.getText().length() <= 0) {
            return;
        }


    }

    /**
     * Hide {@link ChatFooterPanel} if not null.
     */
    private void hideChatFooterPanel() {
        mChattingBottomPanel.setVisibility(View.GONE);
        mAppPanel.setVisibility(View.GONE);
        if (mChatFooterPanel != null) {
            mChatFooterPanel.setVisibility(View.GONE);
        }

        setBiaoqingEnabled(false);
    }

    /**
     * Whether display emoji panel
     */
    private void setBiaoqingEnabled(boolean enabled) {
        if (mBiaoqing == null) {
            return;
        }

        if ((mBiaoqingEnabled && enabled) || (!mBiaoqingEnabled && !enabled)) {
            LogUtil.d(TAG, "biao qing panel has " + enabled);
            return;
        }
        mBiaoqingEnabled = enabled;
        if (enabled) {
            mBiaoqing.setImageDrawable(getContext().getResources().getDrawable(R.drawable.chatting_biaoqing_operation_enabled));
            return;
        }
        mBiaoqing.setImageDrawable(getContext().getResources().getDrawable(R.drawable.chatting_setmode_biaoqing_btn));
    }

    private void resetVoiceRecordingButton() {
        mVoiceButtonTouched = false;
        mVoiceRecord.setBackgroundDrawable(ResourceHelper.getDrawableById(getContext(), R.drawable.voice_rcd_btn_talk_nor));
        mVoiceRecord.setText(R.string.chatfooter_presstorcd);

        if (mVoiceRcdHitCancelView != null && mVoiceRcdHitCancelView.getVisibility() == View.VISIBLE) {
            // Start to cancel sending events when recording over
            if (mChattingFooterLinstener != null) {
                mChattingFooterLinstener.OnVoiceRcdCancelRequest();
            }
            return;
        }

        if (mChattingFooterLinstener != null) {
            mChattingFooterLinstener.OnVoiceRcdStopRequest();
        }
    }

    /**
     * If it is possible to enable the send button
     *
     * @param canSend
     */
    private void enableChattingSend(boolean canSend) {
        if (mChattingAttach == null || mChattingSend == null) {
            return;
        }

        // If the current attachment button visible, and the Enter key to send the message model
        if ((!mDonotEnableEnterkey && mChattingAttach.getVisibility() == View.VISIBLE)
                || mDonotEnableEnterkey && mChattingSend.getVisibility() == View.VISIBLE) {
            return;
        }

        if (mDonotEnableEnterkey) {
            mChattingSend.setVisibility(View.VISIBLE);
            mChattingAttach.setVisibility(View.GONE);
        } else {
            mChattingSend.setVisibility(View.GONE);
            mChattingAttach.setVisibility(View.VISIBLE);
        }
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "mDonotEnableEnterkey " + mDonotEnableEnterkey);
        mChattingSend.getParent().requestLayout();
    }

    private void requestFocusEditText(boolean focus) {
        if (focus) {
            mEditText.requestFocus();
            mTextPanel.setEnabled(true);
            return;
        }
        mEditText.clearFocus();
        mTextPanel.setEnabled(false);

    }

    /**
     *
     */
    public final void resetEnableEnterkey() {
        mDonotEnableEnterkey = ECPreferences.getSharedPreferences()
                .getBoolean(ECPreferenceSettings.SETTINGS_ENABLE_ENTER_KEY.getId(),
                        (Boolean) ECPreferenceSettings.SETTINGS_ENABLE_ENTER_KEY.getDefaultValue());
    }

    /**
     * Clear input box
     */
    public final void clearEditText() {
        if (mEditText == null) {
            return;
        }

        mEditText.setText("");
    }

    /**
     * 获得草稿
     *
     * @return
     */
    public final String getEditText() {
        if (mEditText == null) {
            return "";
        }

        return mEditText.getText().toString();
    }

    /**
     * @param showKeyBord
     */
    private void setKeyBordShow(boolean showKeyBord) {
        if (mShowKeyBord = showKeyBord) {
            return;
        }
        LogUtil.d(TAG, "set Show KeyBord " + showKeyBord);
        mShowKeyBord = showKeyBord;
    }

    /**
     * change chatting mode for Speech mode, input mode
     *
     * @param resId
     */
    private void setChattingModeImageResource(int resId) {
        if (mChattingModeButton == null) {
            return;
        }

        if (resId == R.drawable.chatting_setmode_voice_btn) {
            mChattingModeButton.setContentDescription(getContext().getString(R.string.chat_footer_switch_mode_voice_btn));
        } else {
            mChattingModeButton.setContentDescription(getContext().getString(R.string.chat_footer_switch_mode_keybord_btn));
        }
        mChattingModeButton.setImageResource(resId);
        mChattingModeButton.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.ChattingFootPaddingBottom));
    }

    /**
     * switch chatting mode for Speech mode, input mode
     *
     * @param mode
     */
    private void switchChattingMode(int mode) {
        mChattingMode = mode;
        switch (mode) {
            case CHATTING_MODE_KEYBORD:

                mTextPanel.setVisibility(View.VISIBLE);
                mVoiceRecord.setVisibility(View.GONE);
                setChattingModeImageResource(R.drawable.chatting_setmode_voice_btn);
                break;
            case CHATTING_MODE_VOICE:

                mTextPanel.setVisibility(View.GONE);
                mVoiceRecord.setVisibility(View.VISIBLE);
                setChattingModeImageResource(R.drawable.chatting_setmode_keyboard_btn);
                break;
            default:
                break;
        }
    }

    /**
     * @return
     */
    public boolean isButtomPanelNotVisibility() {
        return mChattingBottomPanel.getVisibility() != View.VISIBLE;
    }

    /**
     * set the {@link AppPanel} default height
     *
     * @param height
     */
    private void setBottomPanelHeight(int height) {

        int widthPixels = 0;
        if (height <= 0) {
            int[] displayScreenMetrics = getDisplayScreenMetrics();
            if (displayScreenMetrics[0] >= displayScreenMetrics[1]) {
                height = ResourceHelper.fromDPToPix(getContext(), 230);
            } else {
                height = ECPreferences.getSharedPreferences().getInt(
                        ECPreferenceSettings.SETTINGS_KEYBORD_HEIGHT.getId(),
                        ResourceHelper.fromDPToPix(getContext(), 230));
            }

            widthPixels = displayScreenMetrics[0];
        }

        if (height > 0 && mChattingBottomPanel != null) {
            LogUtil.d(TAG, "set bottom panel height: " + height);
            ViewGroup.LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, height);
            if (mChattingBottomPanel.getLayoutParams() != null) {
                layoutParams = mChattingBottomPanel.getLayoutParams();
            }
            layoutParams.height = height;
        }

        mAppPanel.setPanelHeight(height);
    }

    /**
     * Access to mobile phone screen resolution and the width and height
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    private int[] getDisplayScreenMetrics() {
        int[] metrics = new int[2];
        if (getContext() instanceof Activity) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            metrics[0] = displayMetrics.widthPixels;
            metrics[1] = displayMetrics.heightPixels;
            return metrics;
        }
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        metrics[0] = display.getWidth();
        metrics[1] = display.getHeight();

        return null;
    }


    public final void showVoiceRecordWindow(int offsert) {

        int yLocation = 0;
        int maxHeightDensity = ResourceHelper.fromDPToPix(getContext(), 180);
        int density = DensityUtil.getMetricsDensity(getContext(), 50.0F);

        if (offsert + density < maxHeightDensity) {
            yLocation = -1;
        } else {
            yLocation = density + (offsert - maxHeightDensity) / 2;
        }

        if (popupWindow == null) {
            popupWindow = new RecordPopupWindow(View.inflate(getContext(),
                    R.layout.voice_rcd_hint_window2, null),
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            mVoiceHintAnim = ((ImageView) popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_anim));
            mVoiceHintAnimArea = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_anim_area);
            mVoiceRcdHitCancelView = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_cancel_area);
            mVoiceHintCancelText = ((TextView) popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_cancel_text));
            mVoiceHintCancelIcon = ((ImageView) popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_cancel_icon));
            mVoiceHintLoading = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_loading);
            mVoiceHintRcding = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_rcding);
            mVoiceHintTooshort = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_tooshort);
            mVoiceNormalWording = ((TextView) popupWindow.getContentView().findViewById(R.id.voice_rcd_normal_wording));

        }

        if (yLocation != -1) {
            mVoiceHintTooshort.setVisibility(View.GONE);
            mVoiceHintRcding.setVisibility(View.GONE);
            mVoiceHintLoading.setVisibility(View.VISIBLE);
            popupWindow.showAtLocation(this, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, yLocation);
        }
    }

    public void displayAmplitude(double amplitude) {
        for (int i = 0; i < ampIcon.length; i++) {
            if (amplitude < ampValue[i] || amplitude >= ampValue[i + 1]) {
                continue;
            }
            LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "Voice rcd amplitude " + amplitude);
            mVoiceHintAnim.setBackgroundDrawable(ResourceHelper.getDrawableById(getContext(), ampIcon[i]));
            if ((amplitude == -1) && (this.popupWindow != null)) {
                popupWindow.dismiss();
                mVoiceHintLoading.setVisibility(View.VISIBLE);
                mVoiceHintRcding.setVisibility(View.GONE);
                mVoiceHintTooshort.setVisibility(View.GONE);
            }
            return;
        }
    }

    /**
     *
     */
    public void showVoiceRecording() {
        if (mChattingFooterLinstener != null) {
            mChattingFooterLinstener.OnVoiceRcdStartRequest();
        }
        mVoiceHintLoading.setVisibility(View.GONE);
        mVoiceHintRcding.setVisibility(View.VISIBLE);
    }

    /**
     *
     */
    public final void dismissPopuWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            mVoiceHintRcding.setVisibility(View.VISIBLE);
            mVoiceHintLoading.setVisibility(View.GONE);
            mVoiceHintTooshort.setVisibility(View.GONE);
            mVoiceRcdHitCancelView.setVisibility(View.GONE);
            mVoiceHintAnimArea.setVisibility(View.VISIBLE);
        }
        mVoiceRecord.setBackgroundDrawable(ResourceHelper.getDrawableById(
                getContext(), R.drawable.voice_rcd_btn_talk_nor));
        mVoiceRecord.setText(R.string.chatfooter_presstorcd);
        mVoiceButtonTouched = false;
    }

    public synchronized void tooShortPopuWindow() {
        LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "CCPChatFooter voice to short , then set enable false");
        mVoiceRecord.setEnabled(false);
        mVoiceRecord.setBackgroundDrawable(ResourceHelper.getDrawableById(getContext(), R.drawable.voice_rcd_btn_talk_press));
        if (popupWindow != null) {
            mVoiceHintTooshort.setVisibility(View.VISIBLE);
            mVoiceHintRcding.setVisibility(View.GONE);
            mVoiceHintLoading.setVisibility(View.GONE);
            //popupWindow.update();
        }
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(WHAT_ON_DIMISS_DIALOG, 500L);
        }
    }

    /**
     * Register a drag event listener callback object for {@link CCPEditText}. The parameter is
     * an implementation of {@link android.view.View.OnDragListener}. To send a drag event to a
     * View, the system calls the
     * {@link android.view.View.OnDragListener#onDrag(android.view.View, android.view.DragEvent)} method.
     *
     * @param l An implementation of {@link android.view.View.OnDragListener}.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public final void setOnEditTextDragListener(OnDragListener l) {
        mEditText.setOnDragListener(l);
    }

    /**
     * @param l
     */
    public final void setOnChattingFooterLinstener(OnChattingFooterLinstener l) {
        mChattingFooterLinstener = l;
    }

    /**
     * @param l
     */
    public final void setOnChattingPanelClickListener(OnChattingPanelClickListener l) {
        mChattingPanelClickListener = l;
    }

    /**
     * Register a drag event listener callback object for {@link CCPEditText}
     *
     * @param textWatcher
     */
    public final void addTextChangedListener(TextWatcher textWatcher) {
        mChatingInputTextWatcher = new ChatingInputTextWatcher(textWatcher);
        mEditText.addTextChangedListener(mChatingInputTextWatcher);
    }

    /**
     *
     */
    public final void setEditTextNull() {
        mEditText.setText(null);
    }

    public final void setEditText(CharSequence text) {
        mEditText.setText(text);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * @return
     */
    public long getAvailaleSize() {

        File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / 1024 / 1024;//  MIB单位
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public final void hideBottomPanel() {
        setMode(CHATTING_MODE_VOICE, 20, false);
    }

    /**
     * @param mode
     */
    public void setMode(int mode) {
        setMode(mode, true);
    }

    /**
     * @param mode
     * @param focus
     */
    public final void setMode(int mode, boolean focus) {
        switchChattingMode(mode);
        switch (mode) {
            case CHATTING_MODE_KEYBORD:
                requestFocusEditText(true);
                hideChatFooterPanel();

                if (focus) {
                    if (mEditText.length() > 0) {
                        enableChattingSend(true);
                        return;
                    }
                }
                enableChattingSend(false);
                break;
            case CHATTING_MODE_VOICE:
                enableChattingSend(false);
                setMode(0, -1, false);
            default:
                setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * @param mode
     * @param messageMode
     * @param focus
     */
    private void setMode(int mode, int messageMode, boolean focus) {
        if (focus) {
            switch (mode) {
                case CHATTING_MODE_KEYBORD:

                    requestFocusEditText(true);
                    mInputMethodManager.showSoftInput(this.mEditText, 0);
                    break;

                case CHATTING_MODE_VOICE:
                    if (messageMode == 22) {
                        if (mAppPanel == null) {
                            initAppPanel();
                        }
                        mAppPanel.initFlipperRotateMe();

                        if (mChatFooterPanel != null) {
                            mChatFooterPanel.setVisibility(View.GONE);
                        }
                        mAppPanel.setVisibility(View.VISIBLE);
                        requestFocusEditText(false);
                        if (mChattingMode == CHATTING_MODE_VOICE) {
                            switchChattingMode(CHATTING_MODE_KEYBORD);
                        }
                    } else if (messageMode == 21) {
                        if (mAppPanel != null) {
                            mAppPanel.setVisibility(View.GONE);
                        }
                        if (mChatFooterPanel == null) {
                            initChattingFooterPanel();
                        }
                        mChatFooterPanel.onResume();
                        if (mChatFooterPanel != null) {
                            mChatFooterPanel.setVisibility(View.VISIBLE);
                        }
                        setBiaoqingEnabled(true);
                        requestFocusEditText(true);

                        hideInputMethod();
                        this.mChattingBottomPanel.setVisibility(View.VISIBLE);
                    }

                    break;
                default:
                    if (focus && messageMode != 21 && mBiaoqing != null) {
                        setBiaoqingEnabled(false);
                    }
                    if (!focus && mode == 0) {
                        setBiaoqingEnabled(false);
                    }
                    mChattingBottomPanel.setVisibility(View.VISIBLE);
                    mAppPanel.setVisibility(View.VISIBLE);

                    break;
            }
        } else {
            if (messageMode == 20) {
                hideChatFooterPanel();
            }

            if (messageMode != 21 && mBiaoqing != null) {
                setBiaoqingEnabled(false);
            }
        }
    }

    /**
     *
     */
    public final void refreshAppPanel() {
        mAppPanel.refreshAppPanel();
    }

    /**
     *
     */
    public final void onPause() {
        if (mChatFooterPanel != null) {
            mChatFooterPanel.onPause();
        }

        mChattingFooterLinstener.onPause();
    }

    public void onDestory() {
        mAppPanel = null;
        if (mChatFooterPanel != null) {
            mChatFooterPanel.onDestroy();
            mChatFooterPanel = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mEditText != null) {
            mEditText.miInputConnection = null;
            mEditText.setOnEditorActionListener(null);
            mEditText.setOnTouchListener(null);
            mEditText.removeTextChangedListener(null);
            mEditText.clearComposingText();
            mEditText = null;
        }
        mChattingSend.setOnClickListener(null);
        mVoiceRecord.setOnTouchListener(null);
        mVoiceRecord.setOnKeyListener(null);
        mVoiceRecord.removeTextChangedListener(null);
        mChattingModeButton.setOnClickListener(null);
        //initAppPanel();
        mChattingAttach.setOnClickListener(null);
        mVoiceRecord = null;
        mChattingModeButton = null;
        mChattingAttach = null;
        popupWindow = null;
        mChattingFooterLinstener = null;
        mChattingPanelClickListener = null;
        mChatingInputTextWatcher = null;
    }

    /**
     * Interface definition for a callback to be invoked when the {@link ChatFooterPanel} has been click
     * such as .emoji click , voice rcd onTouch
     */
    public interface OnChattingFooterLinstener {

        void OnVoiceRcdInitReuqest();

        void OnVoiceRcdStartRequest();

        /**
         * Called when the voce record button nomal and cancel send voice.
         */
        void OnVoiceRcdCancelRequest();

        /**
         * Called when the voce record button nomal and send voice.
         */
        void OnVoiceRcdStopRequest();

        void OnSendTextMessageRequest(CharSequence text);

        void OnUpdateTextOutBoxRequest(CharSequence text);

        void OnSendCustomEmojiRequest(int emojiid, String emojiName);

        void OnEmojiDelRequest();

        void OnInEditMode();

        void onPause();

        void onResume();

        void release();
    }

    /**
     * Interface definition for a callback to be invoked when the {@link ChatFooterPanel} has been click
     * such as .emoji click , voice rcd onTouch
     */
    public interface OnChattingPanelClickListener {
        void OnTakingPictureRequest();

        void OnSelectImageReuqest();

        void OnSelectFileRequest();

    }


    private class ChatingInputTextWatcher implements TextWatcher {

        private TextWatcher mTextWatcher;

        /**
         *
         */
        public ChatingInputTextWatcher(TextWatcher textWatcher) {
            mTextWatcher = textWatcher;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            mTextWatcher.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            mTextWatcher.onTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            mTextWatcher.afterTextChanged(s);
            if ((s.length() > 0) && (s.toString().trim().length() > 0)) {
                mDonotEnableEnterkey = true;
                enableChattingSend(true);
            } else {
                mDonotEnableEnterkey = false;
                enableChattingSend(false);
            }
        }

    }


    /**
     * @return
     */
    public int getMode() {
        return 0;
    }


    /**
     * @param b
     */
    public void setCancle(boolean b) {

    }

    /**
     * @return
     */
    public boolean isVoiceRecordCancle() {
        return false;
    }

    public static void input(EditText editText, String emojiName) {
        if (editText == null || emojiName == null) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emojiName);
        } else {
            editText.getText().replace(Math.min(start, end), Math.max(start, end), emojiName, 0, emojiName.length());
        }
    }
}
