package com.cn.fit.ui.patient.main.mynurse;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.nurse.BeanNurseInfo;
import com.cn.fit.ui.AppMain;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.cn.fit.util.refreshgridview.FooterView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据适配器
 *
 * @author kuangtiecheng
 */
public class AdapterExpertItem extends BaseAdapter {
    private List<BeanNurseInfo> mList = new ArrayList<BeanNurseInfo>();
    private Context mContext;
    //	private DisplayImageOptions options;
    private String footerviewItem;

    private FooterView footerView;

    private boolean footerViewEnable = false;
    private OnClickListener ml;

    public AdapterExpertItem(Context context, List<BeanNurseInfo> list) {
        if (list != null) {
            this.mList = list;
        }
        this.mContext = context;

    }

    public boolean isFooterViewEnable() {
        return footerViewEnable;
    }

    /**
     * 存放列表项控件句柄
     */
    private class ViewHolder {
        CircleImageView image;
        TextView name;
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
                    R.layout.doctor_list_item_vertical, parent, false);
            holder = new ViewHolder();
            holder.image = (CircleImageView) convertView
                    .findViewById(R.id.doctor_avatar);
            holder.name = (TextView) convertView
                    .findViewById(R.id.doctor_name_tv);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + mList.get(i).getUrl(), holder.image, AppMain.initImageOptions(R.drawable.default_user_icon, false));
        holder.name.setText(mList.get(i).getNurseName());
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
