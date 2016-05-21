package com.cn.fit.customer.my;

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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ktcer on 2016/1/12.
 */
public class AdaperMyCollectss extends BaseAdapter {

    private Context context;
    private List<BeanDiscovery> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    private InterfaceIsfavoriteListener l;
    private boolean flag;

    public AdaperMyCollectss(Context context, List<BeanDiscovery> list, boolean flag) {
        this.context = context;
        this.list = list;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setIsfavoriteListener(InterfaceIsfavoriteListener l) {
        this.l = l;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.coach_list_item_collect, parent, false);
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

            holder.favorite = (ImageView) convertView.findViewById(R.id.favorite_image);
            holder.favorite.setVisibility(View.VISIBLE);


            convertView.setTag(holder);

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
        holder = (ViewHolder) convertView.getTag();
//            if( flag){
//                holder.favorite.setVisibility(View.INVISIBLE);
//            }else {

//            }
        if (!flag) {
            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.getDone(position);
//                    AscyncFollowclass asc = new AscyncFollowclass(list.get(position).getClassID()+"");
//                    asc.execute();


                }
            });
        }
        holder.addressText.setText(list.get(position).getLocation());// infoList.get(position)
        holder.timeAndNumber.setText(list.get(position).getStartTime());
        holder.money.setText("¥" + list.get(position).getFinalPrice() + "");
        holder.applynum.setText("已报名:" + list.get(position).getHaveNums() + "/" + list.get(position).getNums());
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

        return convertView;

    }

    //public  void  setfavoriteclickListener(){
//
//}
    public interface InterfaceIsfavoriteListener {

        void getDone(int positione);

    }

    public class ViewHolder {
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
        ImageView favorite;

    }
}
