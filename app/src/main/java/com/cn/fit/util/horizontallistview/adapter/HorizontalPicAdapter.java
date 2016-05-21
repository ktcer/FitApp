package com.cn.fit.util.horizontallistview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cn.fit.R;
import com.cn.fit.util.AbsParam;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

public class HorizontalPicAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private DisplayImageOptions options;
//	private ImageLoader imageLoader;

    public HorizontalPicAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.gerenfengcai) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.gerenfengcai)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.gerenfengcai)  //设置图片加载/解码过程中错误时候显示的图片
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
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//		ImageView imageView = null;
//		View view  = convertView;
//		
//		
//		options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.gerenfengcai)
//		.showImageForEmptyUri(R.drawable.gerenfengcai)
//		.showImageOnFail(R.drawable.gerenfengcai).cacheInMemory(true)
//		.cacheOnDisc(true)
//		.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//		.bitmapConfig(Bitmap.Config.RGB_565).build();
//		
//		if(view == null ){
//			imageView = new ImageView(mContext);
//			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 110); 
//			imageView.setLayoutParams(lp);
//			imageView.setPadding(5, 0, 5, 0);
//			imageView.setScaleType(ScaleType.FIT_XY);
//			view = imageView;
//			view.setTag(imageView);
//		}else{
//			imageView = (ImageView) view.getTag();
//		}
//		ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl()+mList.get(position), imageView, options);		
//		return view;

//		final ImageView imageView = new ImageView(mContext);
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 110); 
//		imageView.setLayoutParams(lp);
//		imageView.setPadding(5, 0, 5, 0);
//		imageView.setScaleType(ScaleType.FIT_XY);
//		ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl()+mList.get(position), imageView,options);
//		return imageView;

        HolderView holderView = null;
        View view = convertView;

        if (view == null) {
            holderView = new HolderView();
            view = LayoutInflater.from(mContext).inflate(R.layout.match_league_round_item, parent, false);
            holderView.imageView = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + mList.get(position), holderView.imageView, options);
        return view;

    }

    private class HolderView {
        ImageView imageView;
    }

}
