package com.cn.fit.ui.patient.others.myaccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.model.nurse.BeanPayGold;
import com.cn.fit.model.personinfo.BeanCoinsPrice;
import com.cn.fit.model.personinfo.BeanOverage;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.ui.chat.common.dialog.ECListDialog;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscyncPayGold;
import com.cn.fit.ui.paychoose.ActivityPayChoose;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.FButton;
import com.cn.fit.util.PickerView;
import com.cn.fit.util.PickerView.onSelectListener;

public class AdapterMyCoins extends BaseAdapter {
    private List<BeanOverage> list;
    private ActivityBasicListView mContext;
    private LayoutInflater inflater;
    /**
     * 第一个后面positiong
     */
    private final int TYPE_1 = 0; // 类型1
    /**
     * 第一个positiong
     */
    private final int TYPE_2 = 1;// 类型第一个positiong
    private final int VIEW_TYPE = 2; // 总布局数
    private String gold;
    private int positions;
    private float unitPrice;
    private String money = "";
    private List<BeanCoinsPrice> beanCoinsPriceList;

    public AdapterMyCoins() {
        super();
        // TODO Auto-generated constructor stub
    }

    public AdapterMyCoins(List<BeanOverage> list,
                          ActivityBasicListView mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub

        super.getItemViewType(position);
        positions = position;
        if (position < 1) {
            return TYPE_2;
        } else {
            return TYPE_1;
        }
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        super.getViewTypeCount();
        return VIEW_TYPE;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int type = getItemViewType(positions);
        if (type == 1) {
            return list.size();
        } else {
            return list.size();
        }

    }

    @Override
    public Object getItem(int position) {
        Log.i("input", position + "");
        int type = getItemViewType(positions);
        if (type == 1) {
            return null;
        } else {

            return list.get(position);

        }

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        inflater = LayoutInflater.from(mContext);

        viewHolderHead1 head = null;
        viewHolder1 holderList = null;
        int type = getItemViewType(position);

        // if (convertView == null) {//去掉if后损失性能，heihei
        Log.e("convertView = ", " NULL");
        Log.i("input", "type=" + type + ">>>>>>>>>" + "position=" + position);
        switch (type) {
            case TYPE_2:

                head = new viewHolderHead1();
                convertView = inflater.inflate(R.layout.my_integral_head, parent,
                        false);
                //
                head.takeMoney = (FButton) convertView
                        .findViewById(R.id.take_money);
                head.takeMoney.setVisibility(View.GONE);
                head.fillMoney = (FButton) convertView
                        .findViewById(R.id.fill_money);
                head.takeMoney.setCornerRadius(3);
                head.fillMoney.setCornerRadius(3);
                head.currentMoneyNum = (TextView) convertView
                        .findViewById(R.id.current_money_num);
                head.tv_more = (TextView) convertView.findViewById(R.id.tv_more);
                convertView.setTag(head);
                break;
            case TYPE_1:
                convertView = inflater.inflate(R.layout.my_overage, null);

                holderList = new viewHolder1();
                holderList.consumptionName = (TextView) convertView
                        .findViewById(R.id.consumption_name);
                holderList.overages = (TextView) convertView
                        .findViewById(R.id.overage);
                holderList.detailedMoney = (TextView) convertView
                        .findViewById(R.id.detailed_money);
                holderList.consumptionTime = (TextView) convertView
                        .findViewById(R.id.consumption_time);
                Log.e("convertView = ", "NULL TYPE_SEEKBAR");
                convertView.setTag(holderList);
                break;
        }
        // } else {
        // 有convertView，按样式，取得不用的布局
        switch (type) {
            case TYPE_2:
                head = (viewHolderHead1) convertView.getTag();

                head.currentMoneyNum.setText((int) ActivityMyAccountCenter.myCoins
                        + "个");
                head.takeMoney.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // Intent intent =new
                        // Intent(mContext,ActivityHealthPostDetail.class);
                        // Bundle mBundle = new Bundle();
                        // mBundle.putSerializable(Constant.TOUR_BEAN,mList.get(i));
                        // intent.putExtras(mBundle);
                        // mContext.startActivity(intent);
                    }

                });
                head.tv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (historyShow != null) {
                            historyShow.showHistory();
                        }
                    }
                });
                head.fillMoney.setOnClickListener(new OnClickListener() {
                    int iposition;

                    @Override
                    public void onClick(View arg0) {
                        AppMain.kindsPay = 1;
                        ((ActivityBasic) mContext).showProgressBar();
                        // 先获取最新的金币与充值费用表
                        AscyncGetCoinsPrice mTask = new AscyncGetCoinsPrice(
                                mContext) {

                            @Override
                            protected void onPostExecute(List<BeanCoinsPrice> result) {
                                // TODO Auto-generated method stub
                                super.onPostExecute(result);
                                beanCoinsPriceList = result;
                                List<String> contentList = new ArrayList<String>();
                                if (result != null) {
                                    for (BeanCoinsPrice bean : result) {
                                        contentList.add(bean.getDetail() + "   " + bean.getNow_price() + "元");
                                    }
                                }
                                ECListDialog dialog = new ECListDialog(mContext,
                                        contentList);
                                dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
                                    @Override
                                    public void onDialogItemClick(Dialog d,
                                                                  int position) {

                                        iposition = position;

                                        mContext.showProgressBar();
                                        AscyncPayGold ascypgTask = new AscyncPayGold(
                                                mContext, beanCoinsPriceList.get(
                                                iposition).getCount()
                                                + "") {

                                            @Override
                                            protected void onPostExecute(
                                                    BeanPayGold result) {
                                                // TODO Auto-generated method
                                                // stub
                                                super.onPostExecute(result);
                                                if (result.getResult() == 1) {
                                                    AppMain.orderNumber = result
                                                            .getDdh();
                                                    mContext.hideProgressBar();
                                                    Intent intent = new Intent(
                                                            mContext,
                                                            ActivityPayChoose.class);
                                                    intent.putExtra(
                                                            "money",
                                                            beanCoinsPriceList.get(
                                                                    iposition)
                                                                    .getNow_price()
                                                    );
                                                    intent.putExtra(
                                                            "description",
                                                            beanCoinsPriceList.get(
                                                                    iposition)
                                                                    .getDetail());
                                                    mContext.startActivity(intent);
                                                } else {
                                                    ((ActivityBasic) mContext).hideProgressBar();
                                                    ToastUtil.showMessage("购买金币失败");
                                                }
                                            }

                                        };
                                        ascypgTask.execute();
                                        // }

                                    }
                                });
                                dialog.setTitle("请选择您要充值的金币数");
                                dialog.show();
                            }

                        };
                        mTask.execute();

                    }
                });
                break;
            case TYPE_1:
                holderList = (viewHolder1) convertView.getTag();
                BeanOverage beanOverage = list.get(position);
                holderList.consumptionName.setText(beanOverage.getItem());
                holderList.overages.setText(beanOverage.getDetail());
                holderList.consumptionTime.setText(beanOverage.getCreatetime());
                String detail = beanOverage.getDetail();
                // 匹配找到“支出“字符串
                Pattern p = Pattern.compile("支出");
                Matcher m = p.matcher(detail);
                if (!m.find()) {
                    holderList.detailedMoney.setTextColor(mContext.getResources()
                            .getColor(R.color.lightgreen));
                    holderList.detailedMoney.setText("+" + beanOverage.getAmount()
                            + "");
                } else {
                    holderList.detailedMoney.setTextColor(mContext.getResources()
                            .getColor(R.color.orange));
                    holderList.detailedMoney.setText("-" + beanOverage.getAmount()
                            + "");
                }

                Log.e("convertView !!!!!!= ", "NULL TYPE_SEEKBAR");
                break;
        }
        // }
        return convertView;
    }

    // 弹出选择弹窗
    private void showChooseDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setContentView(formChooseDialog());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mContext.showProgressBar();
                AscyncPayGold ascypgTask = new AscyncPayGold(mContext,
                        unitPrice * Integer.parseInt(gold) + "") {

                    @Override
                    protected void onPostExecute(BeanPayGold result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        if (result.getResult() == 1) {
                            AppMain.orderNumber = result.getDdh();
                            mContext.hideProgressBar();
                            Intent intent = new Intent(mContext,
                                    ActivityPayChoose.class);
                            intent.putExtra("money",
                                    unitPrice * Integer.parseInt(gold));
                            intent.putExtra("description", "充值" + gold + "个金币");
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext,
                                    "失败" + result.getNowcoins(), 1000).show();
                        }
                    }

                };
                ascypgTask.execute();
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

    TextView title;

    // 构建选择弹窗
    private LinearLayout formChooseDialog() {

        LayoutInflater inflaterDl = LayoutInflater.from(mContext);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                R.layout.choosepicker_single_layout, null);
        title = (TextView) layout.findViewById(R.id.indicate_Info);
        title.setText("请选择购买的金币数量");
        PickerView pickerView = (PickerView) layout
                .findViewById(R.id.minute_pv);
        pickerView.setVisibility(View.VISIBLE);
        List<String> data3 = new ArrayList<String>();
        for (int a = 0; a < 200; a++) {
            data3.add(a + "");
        }
        pickerView.setData(data3);

        pickerView.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text) {
                // TODO Auto-generated method stub
                gold = text;
                title.setText("您选择了" + text + "个金币\n共需支付：" + unitPrice
                        * Integer.parseInt(gold) + "元");
            }
        });
        gold = pickerView.getText();
        return layout;
    }

    private HistoryShow historyShow = null;

    public static interface HistoryShow {
        void showHistory();
    }

    public HistoryShow getHistoryShow() {
        return historyShow;
    }

    public void setHistoryShow(HistoryShow historyShow) {
        this.historyShow = historyShow;
    }
}

class viewHolderHead1 implements Serializable {
    FButton takeMoney;
    FButton fillMoney;
    TextView currentMoneyNum;
    TextView tv_more;
}

class viewHolder1 implements Serializable {
    TextView consumptionName;
    TextView consumptionTime;
    TextView overages;
    TextView detailedMoney;
}
