package com.cn.fit.ui.basic;

import android.app.Dialog;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.util.CustomProgressDialog;

public class FragmentBasic extends Fragment implements IfaceDialog, OnClickListener {
    protected CustomProgressDialog dialogProgress;
    protected Animation animation;
    protected Dialog progressDialog;

    protected void setAnimation(float startX, float endX, float startY, float endY) {
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, startX,
                Animation.RELATIVE_TO_SELF, endX, Animation.RELATIVE_TO_SELF,
                startY, Animation.RELATIVE_TO_SELF, endY);
//		animation = new AlphaAnimation(0.0f,1.0f); 
        animation.setDuration(200);
    }

    /**
     * 隐藏软键盘
     */

    protected void inputHidden(IBinder binder) {
        if (null == imm) {
            imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        }
        imm.hideSoftInputFromWindow(binder, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private InputMethodManager imm;

    /**
     * 显示软键盘
     */
    protected void inputShow(View view) {
        if (null == imm) {
            imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    //显示载入弹窗
    public void showProgressBar() {
        if (getActivity() == null) {
            return;
        }
        hideProgressBar();
//		dialogProgress =new CustomProgressDialog(getActivity(), "正在加载中");
//		dialogProgress.show();
        progressDialog = new Dialog(getActivity(), R.style.progress_dialog);
        progressDialog.setContentView(R.layout.progress_dialog_ios);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("卖力加载中");
        progressDialog.show();
    }

    //隐藏载入弹窗
    public void hideProgressBar() {
//		if(dialogProgress!=null)
//		dialogProgress.dismiss();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    protected void showNetErrorInfo() {
        if (getActivity() == null) {
            return;
        }
        ToastUtil.showMessage("您的网络好像有点问题哦，请稍后重试！");
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
//		arg0.startAnimation(AnimationUtils.loadAnimation(getActivity(),
//				R.anim.icon_scale));
    }

    @Override
    public boolean closeActivity() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub

    }

    @Override
    public void showAlertDialog(String title, String message, int drawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showAlertDialog(String title, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showProgressDialog(String title, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showProgressDialog(String title, String message, Runnable run) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showProgressDialog(String message, Runnable run) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showProgressDialog(String title, String message, Runnable run,
                                   long delayedTime) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showToastDialog(String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showToastDialogLongTime(String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showNextExitDialog(String title, String msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showDialogAndReturnMainPageReLogin(String title, String msg) {
        // TODO Auto-generated method stub

    }


}