package com.cn.fit.ui.patient.main.healthpost.healthpost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.model.customer.BeanSubmitOrder;
import com.cn.fit.model.customer.BeanPayDone;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscynCoachSubmitOrder;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscyncCoachPayDone;
import com.cn.fit.ui.patient.others.myorders.ActivityMyOrderInfo;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.FButton;
import com.cn.fit.util.UtilsSharedData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/*
 * 选择人数
 */
public class ActivityCocahSubmitOrder extends ActivityBasic {

    private Button btmajor01, btmajor02;
    private TextView etmajor;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private ImageView myImage;
    private TextView titleTravel, money, allMoney;
    private TextView adultPrice;
    private FButton buy;
    int majornum = 1;// 数量
    private float allPrice;
    private String img;
    private String title;
    private float price;
    private long classID;

    private String name;
    private String address;
    private String time;
    private TextView nameTextLeft, nameTextRight;
    private TextView addressTextLeft, addressTextRight;
    private TextView timeTextLeft, timeTextRight;
    private TextView phoneTextLeft, phoneTextRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBanner = false;
        UtilsSharedData.initDataShare(this);
        setContentView(R.layout.activity_cocah_submit_order);

        ((TextView) findViewById(R.id.middle_tv)).setText("选择人数");
        img = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        title = getIntent().getStringExtra("classname");
        address = getIntent().getStringExtra("address");
        time = getIntent().getStringExtra("time");
        price = getIntent().getFloatExtra("unitprice", (float) 0.1);
        allPrice = price;
        classID = getIntent().getLongExtra("classID", 0);

        options = AppMain.initImageOptions(R.drawable.default_life_icon, true);
        imageLoader = ImageLoader.getInstance();

        btmajor01 = (Button) findViewById(R.id.addbt_bt);
        btmajor02 = (Button) findViewById(R.id.subbt_bt);
        buy = (FButton) findViewById(R.id.buy_fbt_hh);
        buy.setCornerRadius(3);
        etmajor = (TextView) findViewById(R.id.edt_et);
        titleTravel = (TextView) findViewById(R.id.title_travel);
        money = (TextView) findViewById(R.id.money);
        allMoney = (TextView) findViewById(R.id.all_money_tv);
        adultPrice = (TextView) findViewById(R.id.adult_money_tv);
        myImage = (ImageView) findViewById(R.id.image_travel_iv);
        nameTextLeft = (TextView) findViewById(R.id.name_text_left);
        nameTextRight = (TextView) findViewById(R.id.name_text_right);
        addressTextLeft = (TextView) findViewById(R.id.address_text_left);
        addressTextRight = (TextView) findViewById(R.id.address_text_right);
        timeTextLeft = (TextView) findViewById(R.id.time_text_left);
        timeTextRight = (TextView) findViewById(R.id.time_text_right);
        phoneTextLeft = (TextView) findViewById(R.id.phone_text_left);
        phoneTextRight = (TextView) findViewById(R.id.phone_text_right);
        adultPrice.setText("单价  (" + price + "元/人):");
        nameTextLeft.setText("教练");
        nameTextRight.setText(name);
        addressTextLeft.setText("课程地点");
        addressTextRight.setText(address);
        timeTextRight.setText(time);
        phoneTextRight.setText(UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT));

        btmajor01.setTag("major+");
        btmajor02.setTag("major-");
//		btold01.setTag("old+");
//		btold02.setTag("old-");
//		btchild01.setTag("chil+");
//		btchild02.setTag("chil-");

        // 设置输入类型为数字
        etmajor.setText(String.valueOf(majornum));
        setViewListener();
        initDate();
    }


    private void initDate() {
        imageLoader.displayImage(AbsParam.getBaseUrl() + img, myImage, options);
        titleTravel.setText(title);
        money.setText("¥" + price);
        allMoney.setText("¥" + price);
    }

    /**
     * 设置文本变化相关监听事件
     */
    private void setViewListener() {
        btmajor01.setOnClickListener(new OnButtonClickListener());
        btmajor02.setOnClickListener(new OnButtonClickListener());
        buy.setOnClickListener(this);
    }

    /**
     * 判断用户数量是否大于0
     *
     * @return
     */
    private void judgeHasPeople() {
        int allNumber = majornum;
        if (allNumber != 0) {
            buy.setEnabled(true);
            buy.setButtonColor(getResources().getColor(R.color.blue_second));
        } else {
            buy.setEnabled(false);
            buy.setButtonColor(getResources().getColor(R.color.fbutton_color_concrete));
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.buy_fbt_hh:
                showProgressBar();
                AscynCoachSubmitOrder acm = new AscynCoachSubmitOrder(
                        ActivityCocahSubmitOrder.this, classID, allPrice, majornum) {

                    @Override
                    protected void onPostExecute(BeanSubmitOrder result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        if (result == null) {
                            return;
                        }
                        ;
                        if (result.getResult() == 1) {
                            final String ddh = result.getDdh();
                            if (allPrice == 0) {
                                // 直接支付完成

                                AscyncCoachPayDone apd = new AscyncCoachPayDone(
                                        ActivityCocahSubmitOrder.this,
                                        result.getDdh()) {

                                    @Override
                                    protected void onPostExecute(BeanPayDone result) {
                                        // TODO Auto-generated method stub
                                        super.onPostExecute(result);
                                        if (result.getResult() == 1) {

                                        } else {
                                            // Toast.makeText(act,
                                            // result.getDetail(), 1000).show();
                                        }
                                        hideProgressBar();
                                        // 应该直接去订单详情页面？？？
                                        jumpToOrderInfoPage((byte) 1, ddh, allPrice);

                                    }

                                };
                                apd.execute();

                            } else {
                                // 进行支付
                                hideProgressBar();
                                jumpToOrderInfoPage((byte) 0, ddh, allPrice);
                            }
                        } else {
                            Toast.makeText(ActivityCocahSubmitOrder.this,
                                    result.getDetail(), Toast.LENGTH_SHORT).show();
                            ;
                        }

                    }
                };
                acm.execute();

                break;

            default:
                break;
        }
    }

    private void jumpToOrderInfoPage(byte status, String orderNum, float price) {
        Intent intent = new Intent(this, ActivityMyOrderInfo.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(Constant.ORDER_NO, orderNum);
        mBundle.putByte(Constant.ORDER_STATUS, status);
        mBundle.putFloat(Constant.ORDER_PRICE, price);
//        mBundle.putString("image",img);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    /**
     * 加减按钮事件监听器
     */
    class OnButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            String nummajorString = etmajor.getText().toString().trim();
            if (nummajorString == null || nummajorString.equals("")) {
                majornum = 1;
                etmajor.setText("0");
                allPrice = price * (float) majornum;
                allMoney.setText("¥" + allPrice + "");
            } else {
                if (v.getTag().equals("major-")) {
                    if (++majornum < 0) // 先加，再判断
                    {
                        majornum--;
                    } else {
                        etmajor.setText(String.valueOf(majornum));
                        allPrice = price * (float) majornum;
                        allMoney.setText("¥" + allPrice + "");
                    }
                } else if (v.getTag().equals("major+")) {
                    if (--majornum < 0) // 先减，再判断
                    {
                        majornum++;
                    } else {
                        etmajor.setText(String.valueOf(majornum));
                        allPrice = price * (float) majornum;
                        allMoney.setText("¥" + allPrice + "");

                    }
                }
            }

            judgeHasPeople();
        }
    }


}
