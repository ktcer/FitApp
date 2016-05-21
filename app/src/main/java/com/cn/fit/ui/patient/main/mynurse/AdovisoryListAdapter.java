package com.cn.fit.ui.patient.main.mynurse;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.nurse.BeanAdovisoryListBeen;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

public class AdovisoryListAdapter extends BaseAdapter {

    private Context mContext;
    private List<BeanAdovisoryListBeen> mList;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    public AdovisoryListAdapter(Context context, List<BeanAdovisoryListBeen> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BeanAdovisoryListBeen getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView = null;
        View view = convertView;


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_user_icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_user_icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_user_icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                        //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                        //设置图片加入缓存前，对bitmap进行设置
                        //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
        imageLoader = ImageLoader.getInstance();

        if (view == null) {
            holderView = new HolderView();
            view = LayoutInflater.from(mContext).inflate(R.layout.advisory_list_item, parent, false);

            holderView.imageView = (CircleImageView) view.findViewById(R.id.imageView_cv);
            holderView.textViewName = (TextView) view.findViewById(R.id.name_advisory_tv);
            holderView.textViewStaff = (TextView) view.findViewById(R.id.staff_advisory_tv);
            holderView.textViewQuestion = (TextView) view.findViewById(R.id.content_advisory_tv);
            holderView.textViewAnswer = (TextView) view.findViewById(R.id.reply_content_advisory_tv);
            holderView.textViewHospitalTime = (TextView) view.findViewById(R.id.hospitail_part_time_advisory_tv);

            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        imageLoader.displayImage(AbsParam.getBaseUrl() + mList.get(position).getImagUrl(), holderView.imageView, options);
        holderView.textViewName.setText((String) mList.get(position).getName());//get("page")
        holderView.textViewStaff.setText((String) mList.get(position).getStaff());
        holderView.textViewQuestion.setText((String) mList.get(position).getQuestion());
        holderView.textViewAnswer.setText((String) mList.get(position).getAnswer());
        holderView.textViewHospitalTime.setText((String) mList.get(position).getHospital() + "  " + mList.get(position).getDepartment() + "      " + mList.get(position).getTime());

        return view;
    }


    class HolderView {
        CircleImageView imageView;
        TextView textViewName;
        TextView textViewStaff;
        TextView textViewQuestion;
        TextView textViewAnswer;
        TextView textViewHospitalTime;
    }
}
