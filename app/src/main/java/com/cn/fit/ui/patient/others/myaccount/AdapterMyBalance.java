package com.cn.fit.ui.patient.others.myaccount;

import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.model.nurse.BeanPayMyWalletMoney;
import com.cn.fit.model.personinfo.BeanMyWallet;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.ui.chat.common.dialog.ECListDialog;
import com.cn.fit.ui.patient.main.mynurse.asynctask.AscyncPaymoney;
import com.cn.fit.ui.paychoose.ActivityPayChoose;
import com.cn.fit.util.FButton;

public class AdapterMyBalance extends BaseAdapter {
    private List<BeanMyWallet> list;
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
    private float[] money1 = {(float) 10.0, (float) 100.0};
    private String[] money = {"10元", "100元"};
    public static int moneys = 0;
    private int positions;
    private int iposition;

    public AdapterMyBalance() {
        super();
        // TODO Auto-generated constructor stub
    }

    public AdapterMyBalance(List<BeanMyWallet> list,
                            ActivityBasicListView mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
        // beanOverageHead = new BeanOverageHead();
        // list = new ArrayList<BeanOverage>();
    }

    @Override
    public int getItemViewType(int position) {
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = LayoutInflater.from(mContext);

        viewHolderHead head = null;
        viewHolder holderList = null;
        int type = getItemViewType(position);

        // if (convertView == null) {
        Log.e("convertView = ", " NULL");

        switch (type) {
            case TYPE_2:

                head = new viewHolderHead();
                convertView = inflater.inflate(R.layout.my_overage_head, parent,
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
                head.fillMoney.setVisibility(View.GONE);
                convertView.setTag(head);
                break;
            case TYPE_1:
                convertView = inflater.inflate(R.layout.my_overage, parent, false);

                holderList = new viewHolder();
                holderList.consumptionName = (TextView) convertView
                        .findViewById(R.id.consumption_name);
                holderList.consumptionTime = (TextView) convertView
                        .findViewById(R.id.consumption_time);
                holderList.overages = (TextView) convertView
                        .findViewById(R.id.overage);
                holderList.detailedMoney = (TextView) convertView
                        .findViewById(R.id.detailed_money);
                Log.e("convertView = ", "NULL TYPE_SEEKBAR");
                convertView.setTag(holderList);
                break;
        }
        // } else {
        // 有convertView，按样式，取得不用的布局
        switch (type) {
            case TYPE_2:
                head = (viewHolderHead) convertView.getTag();
                head.currentMoneyNum.setText(ActivityMyAccountCenter.money + "元");
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
                head.fillMoney.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        AppMain.kindsPay = 2;// 我的余额充值
                        ECListDialog dialog = new ECListDialog(mContext, money);
                        dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
                            @Override
                            public void onDialogItemClick(Dialog d, int position) {
                                iposition = position;
                                // handleContentMenuClick(itemPosition
                                // ,position);
                                // selectServeType = position;
                                // AscyncSetExpert acsy = new AscyncSetExpert();
                                // acsy.execute();

                                if (position == 0) {
                                    moneys = (int) money1[position] + 10;
                                } else {
                                    moneys = (int) money1[position] + 100;
                                }

                                mContext.showProgressBar();
                                AscyncPaymoney ascypmTask = new AscyncPaymoney(
                                        mContext, AdapterMyBalance.moneys + "") {

                                    @Override
                                    protected void onPostExecute(
                                            BeanPayMyWalletMoney result) {
                                        // TODO Auto-generated method stub
                                        super.onPostExecute(result);
                                        if (result.getResult() == 1) {
                                            AppMain.orderNumberwWllet = result
                                                    .getDdh();
                                            mContext.hideProgressBar();
                                            Intent intent = new Intent(mContext,
                                                    ActivityPayChoose.class);
                                            intent.putExtra("money",
                                                    money1[iposition]);
                                            intent.putExtra("description", "充值"
                                                    + money[iposition]);
                                            mContext.startActivity(intent);
                                        } else {
                                            Toast.makeText(mContext,
                                                    "失败" + result.getNowmoney(),
                                                    1000).show();
                                        }
                                    }

                                };
                                ascypmTask.execute();
                            }
                        });
                        dialog.setTitle("请选择要充值额度");
                        dialog.show();
                    }
                });
                break;
            case TYPE_1:
                holderList = (viewHolder) convertView.getTag();
                BeanMyWallet beanOverage = list.get(position);
                holderList.consumptionName.setText(beanOverage.getItem());
                holderList.consumptionTime
                        .setText(beanOverage.getCreatetime() + "");
                holderList.overages.setText(beanOverage.getDetail());
                float count = beanOverage.getAmount();
                if (count > 0) {
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
                // holderList.detailedMoney.setText(beanOverage.getAmount() + "");
                Log.e("convertView !!!!!!= ", "NULL TYPE_SEEKBAR");
                break;
        }
        // }
        return convertView;
    }
}

class viewHolderHead {
    FButton takeMoney;
    FButton fillMoney;
    TextView currentMoneyNum;
}

class viewHolder {
    TextView consumptionName;
    TextView consumptionTime;
    TextView overages;
    TextView detailedMoney;
}
