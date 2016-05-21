package com.cn.fit.ui.patient.main.healthpost.healthpost;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.healthpost.BeanHealthPost;
import com.cn.fit.ui.AppMain;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.refreshgridview.FooterView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据适配器
 *
 * @author kuangtiecheng
 */
public class AdapterHealthPostItem extends BaseAdapter {

    private List<BeanHealthPost> mList = new ArrayList<BeanHealthPost>();
    private Context mContext;
    private DisplayImageOptions options;
    private String footerviewItem;

    private FooterView footerView;

    private boolean footerViewEnable = false;
    private OnClickListener ml;

    public AdapterHealthPostItem(Context context, List<BeanHealthPost> list) {
        if (list != null) {
            this.mList = list;
        }
        this.mContext = context;
        options = AppMain.initImageOptions(R.drawable.default_life_icon, true);
    }

    public boolean isFooterViewEnable() {
        return footerViewEnable;
    }

    /**
     * 存放列表项控件句柄
     */
    private class ViewHolder {
        public ImageView imageView;
        public TextView decs;
        public TextView newPrice;
        public TextView oldPrice;
    }

    public void setFootreViewEnable(boolean enable) {
        footerViewEnable = enable;
    }

    public void setOnFooterViewClickListener(OnClickListener l) {
        ml = l;
    }

    private int getDisplayWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        return width;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        // 伪造的空项可以根据楼盘id来确定。
        if (footerViewEnable && i == mList.size() - 1) {
            if (footerView == null) {
                footerView = new FooterView(parent.getContext());

                GridView.LayoutParams pl = new GridView.LayoutParams(
                        getDisplayWidth((Activity) mContext),
                        LayoutParams.WRAP_CONTENT);
                footerView.setLayoutParams(pl);
                footerView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (ml != null) {
                            ml.onClick(v);
                        }

                    }
                });
            }
            setFooterViewStatus(FooterView.MORE);
            return footerView;
        }
        final ViewHolder holder;

        if (convertView == null
                || (convertView != null && convertView == footerView)) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gridview_content, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageView);
            holder.decs = (TextView) convertView
                    .findViewById(R.id.desc);
            holder.newPrice = (TextView) convertView
                    .findViewById(R.id.newprice);
            holder.oldPrice = (TextView) convertView
                    .findViewById(R.id.oldprice);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.decs.setSingleLine(false);
        holder.decs.setLines(2);
        holder.decs.setTextSize(14);
        holder.decs.setText("【" + mList.get(i).getTag() + "】" + mList.get(i).getActivetime() + " " + mList.get(i).getTitle());
        holder.newPrice.setText("¥" + mList.get(i).getFinalprice());
        holder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.oldPrice.setText("¥" + mList.get(i).getOriprice());
        ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + mList.get(i).getCover(), holder.imageView, options);
        return convertView;
    }

    public FooterView getFooterView() {
        return footerView;
    }

    public void setFooterViewStatus(int status) {
        if (footerView != null) {
            footerView.setStatus(status);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
