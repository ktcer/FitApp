package com.cn.fit.customer.discovery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.customer.BeanDiscovery;
import com.cn.fit.ui.AppMain;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.SlideShowView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ktcer on 2015/12/29.
 */
public class DiscoveryAdapter extends BaseAdapter {
    private Context context;
    private List<BeanDiscovery> list;
    private LayoutInflater inflater;
    /**
     * 第一个后面的position
     */
    private final int TYPE_1 = 0;//
    private final int TYPE_2 = 1;//
    private final int VIEW_TYPE = 2;//总布局数
    private int positions;

    public DiscoveryAdapter(Context context, List<BeanDiscovery> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        int type = getItemViewType(positions);
        if (type == 1) {
            return list.size();
        } else {
            return list.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
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
        super.getViewTypeCount();
        return VIEW_TYPE;
    }

    @Override
    public Object getItem(int position) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        ViewHolder holder = null;
        ViewHolderad holderad = null;
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_2:
                holderad = new ViewHolderad();
                convertView = inflater.inflate(R.layout.coach_slideshowview_ad, parent, false);
                holderad.adSshow = (SlideShowView) convertView.findViewById(R.id.slideshowViewad);
                convertView.setTag(holderad);
                break;
            case TYPE_1:
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.coach_list_item_discovery, parent, false);
                holder.titleText = (TextView) convertView
                        .findViewById(R.id.coach_text_apply);
                holder.functionText = (TextView) convertView
                        .findViewById(R.id.function_textView3_apply);
                holder.functionMuchText = (TextView) convertView
                        .findViewById(R.id.function_much_textView);
                holder.activityImage = (ImageView) convertView
                        .findViewById(R.id.half_artist_image);
                holder.functionNumber = (TextView) convertView
                        .findViewById(R.id.function_number);
                holder.halfTitleText = (TextView) convertView
                        .findViewById(R.id.half_title_text);
                holder.applynum = (TextView) convertView
                        .findViewById(R.id.applynum);
                holder.money = (TextView) convertView
                        .findViewById(R.id.money);
                holder.timeAndNumber = (TextView) convertView
                        .findViewById(R.id._text);
                holder.addressText = (TextView) convertView
                        .findViewById(R.id.address_text);

                convertView.setTag(holder);
                break;

        }
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = LayoutInflater.from(context).inflate(
//                    R.layout.coach_list_item_discovery, null);
//            holder.titleText = (TextView) convertView
//                    .findViewById(R.id.coach_text);
//            holder.functionText = (TextView) convertView
//                    .findViewById(R.id.function_textView3);
//            holder.functionMuchText = (TextView) convertView
//                    .findViewById(R.id.function_much_textView);
//            holder.activityImage = (ImageView) convertView
//                    .findViewById(R.id.half_artist_image);
//            holder.functionNumber = (TextView) convertView
//                    .findViewById(R.id.function_number);
//            holder.halfTitleText = (TextView) convertView
//                    .findViewById(R.id.half_title_text);
//            holder.applynum = (TextView) convertView
//                    .findViewById(R.id.applynum);
//            holder.money = (TextView) convertView
//                    .findViewById(R.id.money);
//            holder.timeAndNumber = (TextView) convertView
//                    .findViewById(R.id._text);
//            holder.addressText = (TextView) convertView
//                    .findViewById(R.id.address_text);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        switch (type) {
            case TYPE_2:
                holderad = (ViewHolderad) convertView.getTag();
//                holderad.adSshow.slideShowView(context);
                break;
            case TYPE_1:
                holder = (ViewHolder) convertView.getTag();
                holder.addressText.setText(list.get(position).getLocation());// infoList.get(position)
                holder.timeAndNumber.setText(list.get(position).getStartTime());
                holder.money.setText("¥" + list.get(position).getFinalPrice() + "");
                holder.applynum.setText("已报名:" + list.get(position).getNums() + "/" + list.get(position).getHaveNums());
                holder.functionMuchText.setVisibility(View.INVISIBLE);
                holder.functionNumber.setVisibility(View.INVISIBLE);
                holder.halfTitleText.setText(list.get(position).getServeType());
                holder.titleText.setText(list.get(position).getTitle());// infoList.get(position)
                String diastans = "";
                if (list.get(position).getDistance() < 1000) {
                    diastans = list.get(position).getDistance() + "m";
                } else {
                    diastans = list.get(position).getDistance() / 1000 + "km";
                }
                holder.addressText.setText(list.get(position).getLocation() + "  " + diastans);// infoList.get(position)
                holder.functionText.setText(list.get(position).getTag());// infoList.get(position)


                ImageLoader.getInstance().displayImage(
                        AbsParam.getBaseUrl() + list.get(position).getCover(),
                        holder.activityImage,
                        AppMain.initImageOptions(R.drawable.ic_user_default,
                                false));
                break;
        }

        return convertView;
    }
}

class ViewHolderad {
    SlideShowView adSshow;
}

class ViewHolder {
    /**
     * coach_text
     */
    TextView titleText;
    /**
     * function_textView3
     */
    TextView functionText;
    /**
     * function_much_textView
     */
    TextView functionMuchText;
    /**
     * function_number
     */
    TextView functionNumber;
    /**
     * half_title_text
     */
    TextView halfTitleText;
    /**
     * applynum
     */
    TextView applynum;
    /**
     * money
     */
    TextView money;
    /**
     * _text
     */
    TextView timeAndNumber;
    /**
     * address_text
     */
    TextView addressText;
    /**
     * half_artist_image
     */
    ImageView activityImage;//;

}
