package com.cn.fit.com.alipay.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cn.fit.model.customer.BeanPayDone;
import com.cn.fit.model.nurse.BeanAddMoney;
import com.cn.fit.model.nurse.BeanGoldPayDone;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscynAddMoneyPayDone;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscynGoldcoinsPaydone;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscyncCoachPayDone;
import com.cn.fit.ui.patient.others.myorders.ActivityMyOrderInfo;
import com.cn.fit.util.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Pays {

    // 商户PID
    private static final String PARTNER = "2088021100200031";
    // 商户收款账号
    private static final String SELLER = "2088021100200031";
    // 商户私钥，pkcs8格式
    private static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPqoy7wVeEQTFFRWKVbQrSUD+r4V36/O+gz1JTjg1PVN7v39KpfuHwgGsMv1kD2Uer+x9W1rVuymBhnsMwFq+rKtZBoPdP2iPVyk7DY/pG/xt9zdoT899EKgOM/RO5gBhTwBFTN+ZDROimGsxb1sA/T+BlpP6UFjO0aORWg9+ib9AgMBAAECgYBLfPRGg4zBq6FOuNggTh4m7KjiAEJqdwH1TwrTs1pPOhZEpa7jLJJp32H1OtIiyYmiG50XQ+Fcya9CtVCzF53CA0N8vF1ezc5wsy8SN7mEd03G8MMrdpCYil3pKpjq266Q5g1egQ4s4ICuKXO0wRc+JpsmbHK8mVdLzNd4B8VxvQJBAP6NKUqnbeBzadcJDaEdPJ9nIm8U3xC1wkWDErw4K3xk94UX3qUtY0MuWZtoTrMGnciYjhP4fI/GF1HRqx4JItsCQQD8Ffbn37581YzwNd3isR5tpCOGekQPW/THUc50v328tl5JqQqgG+CqRIHcFWeAKmWy46XfV3F2k7B/vWnJKYkHAkBtV0dwWOD6s7i39JyVI1DiC9QfsPBsZhav8zB+Bwau5VPpKKqrewaTWfqFpdWAUww9mUIWU/t1apqju5NsUHJXAkEAwK1ZJyoRQwFG1GOX0SloBI2syaCyXLsAgUi58OpchN0vfEXEZVRpiDsMttd1YLUHPpBZvDft9aA1C64PKT6iCQJAWV7rFfG20qbJlggWqSAcs8PrJF4pEw9+pH+uKBHbzolEkuvvLh3TwhG26Pcva/iyvzGjhRF1nzxMqxCeGJ5lcQ==";
    // 支付宝公钥
    private static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD6qMu8FXhEExRUVilW0K0lA/q+Fd+vzvoM9SU44NT1Te79/SqX7h8IBrDL9ZA9lHq/sfVta1bspgYZ7DMBavqyrWQaD3T9oj1cpOw2P6Rv8bfc3aE/PfRCoDjP0TuYAYU8ARUzfmQ0TophrMW9bAP0/gZaT+lBYztGjkVoPfom/QIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private ActivityBasic act;
    private String commodityName;// 商品名
    private String commodityDescribe;// 商品描述
    private String commodityPrice;// 商品价格
    private String resultStatus;// 判断resultStatus
    // 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
    private Handler myHandler;
    private String orderNo;
    private float price;
    /**
     * 判断是什么类别支付，0：健康驿站旅游支付，1我的金币支付.2我的余额充值
     */
    private int payKinds;

    public Pays(ActivityBasic act) {
        super();
        this.act = act;
    }

    public Pays() {
        super();
    }

    public Pays(ActivityBasic act, String commodityName,
                String commodityDescribe, String commodityPrice, String orderNo,
                float price, int payKinds) {
        super();
        this.act = act;
        this.commodityName = commodityName;
        this.commodityDescribe = commodityDescribe;
        this.commodityPrice = commodityPrice;
        this.orderNo = orderNo;
        this.price = price;
        this.payKinds = payKinds;
    }

    public Pays(ActivityBasic act, String commodityName,
                String commodityDescribe, float price, int payKinds) {
        super();
        this.act = act;
        this.commodityName = commodityName;
        this.commodityDescribe = commodityDescribe;
        this.price = price;
        this.payKinds = payKinds;
    }

    // private static final int SDK_CHECK_FLAG = 2;
    // private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtil.showMessage("支付成功");
                        // switch (payflag) {
                        // case 0://支付后拨打视频通话
                        // ActivityTag = "VIDEO";
                        // call();
                        // act.finish();
                        switch (payKinds) {
                            case 0:// l旅游支付
                                payTravel();
                                break;
                            case 1:// 我的金币支付
                                payMyGold();
                                break;
                            case 2:// 我的余额
                                payMyMoney();
                                break;
                            default:
                                break;
                        }

                        break;
                        // case 1://支付后显示保健护士
                        // //支付成功之后向后台提交同意保健专家的请求
                        // showProgressBar();
                        // ConfirmExpectAsyncTask confirmExpectAsyncTask = new
                        // ConfirmExpectAsyncTask();
                        // confirmExpectAsyncTask.execute();
                        // break;
                        //
                        // default:
                        // break;
                        // }

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(act, "支付结果确认中,我们会尽快为您处理",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(act, "支付失败，您可以在单订单列表重试支付",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    // Toast.makeText(act, "检查结果为：" + msg.obj,
                    // Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        private void payMyMoney() {
            act.showProgressBar();
            AscynAddMoneyPayDone ascyamTask = new AscynAddMoneyPayDone(act) {

                @Override
                protected void onPostExecute(BeanAddMoney result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);
                    if (result.getResult().equals("success")) {
                        act.hideProgressBar();
                        String old = TabActivityMain.class.getName();
                        act.backTo(old);
                    } else {
                        ToastUtil.showMessage("失败");
                    }
                }

            };
            ascyamTask.execute();

        }

        private void payMyGold() {
            act.showProgressBar();
            AscynGoldcoinsPaydone ascypgTask = new AscynGoldcoinsPaydone(act) {

                @Override
                protected void onPostExecute(BeanGoldPayDone result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);
                    if (result.getResult().equals("success")) {
                        act.hideProgressBar();
                        String old = TabActivityMain.class.getName();
                        act.backTo(old);
                    } else {
                        ToastUtil.showMessage("失败");
                    }
                }

            };
            ascypgTask.execute();

        }

        private void payTravel() {
            act.showProgressBar();
            AscyncCoachPayDone apd = new AscyncCoachPayDone(act, orderNo) {

                @Override
                protected void onPostExecute(BeanPayDone result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);
                    if (result.getResult() == 1) {

                    } else {
                        // Toast.makeText(act, result.getDetail(), 1000).show();
                    }
                    act.hideProgressBar();
//					if(AppPool.existsPage(pageName))
//					//应该直接去订单详情页面？？？
//					String old = TabActivityMain.class.getName();
//					act.backTo(old);
                    Intent intent = new Intent(act, ActivityMyOrderInfo.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString(Constant.ORDER_NO, orderNo);
                    mBundle.putByte(Constant.ORDER_STATUS, (byte) 1);
                    mBundle.putFloat(Constant.ORDER_PRICE, price);
                    intent.putExtras(mBundle);
                    act.startActivity(intent);

                }

            };
            apd.execute();

        }

        // private void call() {
        // int callResult = MakeCall.makecall(act,
        // UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT),
        // "40045635");
        // if (callResult != 0) {
        // //重新登录
        // Toast.makeText(act, MakeCall.getReturnMeaning(callResult),
        // 1000).show();
        // }
        //
        // };
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        String allPrice = "";
        allPrice = String.valueOf(price);
        // 订单
        String orderInfo = getOrderInfo(commodityName, commodityDescribe,
                allPrice);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(act);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check() {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(act);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(act);
        String version = payTask.getVersion();
        Toast.makeText(act, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityDescribe() {
        return commodityDescribe;
    }

    public void setCommodityDescribe(String commodityDescribe) {
        this.commodityDescribe = commodityDescribe;
    }

    public String getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(String commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public String getResultStatus() {
        return resultStatus;
    }

}
