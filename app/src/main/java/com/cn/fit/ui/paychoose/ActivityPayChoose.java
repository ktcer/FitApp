package com.cn.fit.ui.paychoose;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.com.alipay.pay.Pays;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.paychoose.PayRadioGroup.OnCheckedChangeListener;
import com.cn.fit.util.FButton;
import com.cn.fit.weixin.pay.Constants;
import com.cn.fit.weixin.pay.MD5;
import com.cn.fit.weixin.pay.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class ActivityPayChoose extends ActivityBasic {
    private FButton payTectView;
    private TextView all_price;
    private TextView all_price_tv;
    private String flag = "微信支付";
    private float money;
    private String description;

    // 微信支付
    private static final String TAG = "MicroMsg.SDKSample.PayActivity";

    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    // TextView show;
    Map<String, String> resultunifiedorder;
    StringBuffer sb;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.go_pay:
                payTectView.setEnabled(false);
                payTectView.setButtonColor(getResources().getColor(
                        R.color.fbutton_color_concrete));

                pay();
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        payTectView.setEnabled(true);
        payTectView
                .setButtonColor(getResources().getColor(R.color.blue_second));
    }

    private void pay() {
        // TODO Auto-generated method stub
        if (flag.equals("微信支付")) {
            req = new PayReq();
            sb = new StringBuffer();

            msgApi.registerApp(Constants.APP_ID);

            String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
                    .toUpperCase();

            GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
            getPrepayId.execute();
        } else if (flag.equals("支付宝支付")) {
            Pays pays = new Pays(ActivityPayChoose.this, description,
                    description, money, AppMain.kindsPay);// mHandler
            pays.check();
            pays.pay();

        } else if (flag.equals("银联支付")) {
            Toast.makeText(ActivityPayChoose.this, "银联支付暂时还没开通，嘿嘿",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_choose);
        money = getIntent().getFloatExtra("money", 0);
        description = getIntent().getStringExtra("description");
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("支付");
        payTectView = (FButton) findViewById(R.id.go_pay);
        payTectView.setOnClickListener(this);
        payTectView.setCornerRadius(3);
        all_price = (TextView) findViewById(R.id.all_price);
        all_price.setText("总价￥" + money);
        all_price_tv = (TextView) findViewById(R.id.all_price_tv);
        all_price_tv.setText(description);
        PayRadioGroup group = (PayRadioGroup) findViewById(R.id.genderGroup);
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(PayRadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int radioButtonId = group.getCheckedRadioButtonId();
                // PayRadioButton rb =
                // (PayRadioButton)MainActivity.this.findViewById(radioButtonId);
                // Toast.makeText(MainActivity.this, rb.getText(),
                // Toast.LENGTH_SHORT).show();

                PayRadioPurified rl = (PayRadioPurified) ActivityPayChoose.this
                        .findViewById(radioButtonId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    ((PayRadioPurified) group.getChildAt(i))
                            .setChangeImg(checkedId);
                }
                flag = rl.getTextTitle().toString();
                if (flag == null) {

                } else {
                    payTectView.setEnabled(true);
                    payTectView.setButtonColor(getResources().getColor(
                            R.color.blue_second));
                }

                // Toast.makeText(ActivityPayChoose.this, rl.getTextTitle(),
                // Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 下面是微信支付
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", sb.toString());
        return sb.toString();

    }

    private class GetPrepayIdTask extends
            AsyncTask<Void, Void, Map<String, String>> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ActivityPayChoose.this,
                    getString(R.string.app_tip),
                    getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

            resultunifiedorder = result;

            genPayReq();
            sendPayReq();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String
                    .format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            Log.e("orion", entity);

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("orion", content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 瀹炰緥鍖杝tudent瀵硅薄
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    //
    private String genProductArgs() {
        String Allprice = new String();
        Allprice = (int) (money * 100) + "";
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams
                    .add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", description));// 鍟嗗搧璇︽儏
            packageParams
                    .add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url",
                    "http://121.40.35.3/test"));
            packageParams.add(new BasicNameValuePair("out_trade_no",
                    genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip",
                    "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", Allprice));// 璁㈠崟鎬婚噾棰濓紝鍗曚綅涓哄垎锛屼笉鑳藉甫灏忔暟鐐�
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);

            return new String(xmlstring.getBytes(), "ISO8859-1");

            // return xmlstring;

        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }

    }

    private void genPayReq() {

        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

        // show.setText(sb.toString());

        Log.e("orion", signParams.toString());

    }

    private void sendPayReq() {

        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

}