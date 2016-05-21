package com.cn.fit.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cn.fit.R;
import com.cn.fit.model.customer.BeanPayDone;
import com.cn.fit.model.nurse.BeanAddMoney;
import com.cn.fit.model.nurse.BeanGoldPayDone;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscynAddMoneyPayDone;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscynGoldcoinsPaydone;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscyncCoachPayDone;
import com.cn.fit.ui.patient.others.myorders.ActivityMyOrderInfo;
import com.cn.fit.util.Constant;
import com.cn.fit.weixin.pay.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends ActivityBasic implements
        IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            String msg = "";
            switch (code) {
                case 0:
                    if (AppMain.kindsPay == 0) {
                        payTravel();
                    }
                    if (AppMain.kindsPay == 1) {
                        payMyGold();
                    }
                    if (AppMain.kindsPay == 2) {
                        payMyMoney();
                    }
                    msg = "支付成功，请到单订单列表查看详情";
                    break;
                case -1:
                    msg = "支付失败，您可以在单订单列表重试支付";
                    String oldPayChooseActivity = TabActivityMain.class.getName();
                    backTo(oldPayChooseActivity);
                    break;
                case -2:
                    msg = "支付取消，您可以在单订单列表继续支付";
                    String oldPayChooseActivity1 = TabActivityMain.class.getName();
                    backTo(oldPayChooseActivity1);
                    break;

                default:
                    msg = "支付失败，您可以在单订单列表重试支付";
                    String oldPayChooseActivity2 = TabActivityMain.class.getName();
                    backTo(oldPayChooseActivity2);
                    break;
            }
            ToastUtil.showMessage(msg);

        }
    }

    private void payMyMoney() {
        showProgressBar();
        AscynAddMoneyPayDone ascypgTask = new AscynAddMoneyPayDone(this) {

            @Override
            protected void onPostExecute(BeanAddMoney result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result.getResult().equals("success")) {
                    hideProgressBar();
                    String old = TabActivityMain.class.getName();
                    backTo(old);
                } else {
                    ToastUtil.showMessage("失败" + result.getResult());
                }
            }

        };
        ascypgTask.execute();

    }

    private void payMyGold() {
        showProgressBar();
        AscynGoldcoinsPaydone ascypgTask = new AscynGoldcoinsPaydone(this) {

            @Override
            protected void onPostExecute(BeanGoldPayDone result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result.getResult().equals("success")) {
                    hideProgressBar();
                    String old = TabActivityMain.class.getName();
                    backTo(old);
                } else {
                    ToastUtil.showMessage("失败" + result.getNowcoins());
                }
            }

        };
        ascypgTask.execute();

    }

    private void payTravel() {

        showProgressBar();
        AscyncCoachPayDone apd = new AscyncCoachPayDone(WXPayEntryActivity.this,
                ActivityMyOrderInfo.orderNo) {

            @Override
            protected void onPostExecute(BeanPayDone result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result.getResult() == 1) {

                } else {
                    // Toast.makeText(act, result.getDetail(),
                    // 1000).show();
                }
                hideProgressBar();
                Intent intent = new Intent(WXPayEntryActivity.this,
                        ActivityMyOrderInfo.class);
                Bundle mBundle = new Bundle();
                mBundle.putString(Constant.ORDER_NO,
                        ActivityMyOrderInfo.orderNo);
                mBundle.putByte(Constant.ORDER_STATUS, (byte) 1);
                mBundle.putFloat(Constant.ORDER_PRICE,
                        ActivityMyOrderInfo.price);
                intent.putExtras(mBundle);
                WXPayEntryActivity.this.startActivity(intent);

            }

        };
        apd.execute();

    }

}