//package com.cn.aihu.ui.patient.main.healthdiary;
//
//import java.util.List;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.cn.aihu.R;
//import com.cn.aihu.model.healthdiary.BeanHealthProgram;
//import com.cn.aihu.ui.AppMain;
//import com.cn.aihu.util.AbsParam;
//import com.cn.aihu.util.CircleImageView;
//import com.cn.aihu.util.coverflow.FancyCoverFlow;
//import com.cn.aihu.util.coverflow.FancyCoverFlowAdapter;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//
//
//
//public class AdapterHealthProgram extends FancyCoverFlowAdapter {
//	private Context context;
//	private ImageLoader imageLoader;// = ImageLoader.getInstance();;
//	private DisplayImageOptions options=new DisplayImageOptions.Builder()
//	.showImageOnLoading(R.drawable.default_healthdiary) //设置图片在下载期间显示的图片  
//	.showImageForEmptyUri(R.drawable.default_healthdiary)//设置图片Uri为空或是错误的时候显示的图片  
//	.showImageOnFail(R.drawable.default_healthdiary) 
//	.showImageOnLoading(R.drawable.default_healthdiary)
//	.cacheInMemory(true)
//	.cacheOnDisc(true)
////	.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
//	.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//	.bitmapConfig(Bitmap.Config.RGB_565).build();
//	private List<BeanHealthProgram> list;
//	public AdapterHealthProgram(Context context,ImageLoader imageLoader, List<BeanHealthProgram> list) {
//		this.context = context;
////		this.options = options;
//		this.imageLoader = imageLoader;
//		this.list=list;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getCoverFlowItem(int position, View reusableView,ViewGroup parent) {
//		CustomViewGroup customViewGroup = null;
//
//        if (reusableView != null) {
//            customViewGroup = (CustomViewGroup) reusableView;
//        } else {
//            customViewGroup = new CustomViewGroup(parent.getContext());
//       
//            customViewGroup.setLayoutParams(new FancyCoverFlow.LayoutParams(140*AppMain.screenWidths/720, 195*AppMain.screenHeight/1280));
//        }
//        if(list.get(position).getTemplateId()==0){
////        	customViewGroup.getImageView().setColorFilter(Color.GRAY,PorterDuff.Mode.XOR);
//			ColorMatrix matrix = new ColorMatrix();
//			matrix.setSaturation(0);
//		    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
//		    customViewGroup.getImageView().setColorFilter(filter);
////        	imageLoader.displayImage(AbsParam.getBaseUrl()+list.get(position).getImgUrl(), customViewGroup.getImageView(), options);
//        }//else{
//        	imageLoader.displayImage(AbsParam.getBaseUrl()+list.get(position).getImgUrl(), customViewGroup.getImageView(), options);
////        }
////		customViewGroup.getTitle().setText(list.get(position).getPlanName());
//        	customViewGroup.getTitle().setVisibility(View.GONE);
//        return customViewGroup;
//	}
//	
//    public static class CustomViewGroup extends LinearLayout {
//
//        // =============================================================================
//        // Child views
//        // =============================================================================
//
//        private CircleImageView imageView;
//
//        private TextView title;
//
//        // =============================================================================
//        // Constructor
//        // =============================================================================
//
//        public CustomViewGroup(Context context) {
//            super(context);
//
//            this.setOrientation(VERTICAL);
//            this.setWeightSum(5);
//
//            this.imageView = new CircleImageView(context);
//            this.title = new TextView(context);
//
//            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            LayoutParams layoutParamsPic = new LayoutParams(140*AppMain.screenWidths/720, 140*AppMain.screenWidths/720);
//            this.imageView.setLayoutParams(layoutParamsPic);
//            this.title.setLayoutParams(layoutParams);
////            this.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
////            this.imageView.setAdjustViewBounds(false);
//
//            this.title.setGravity(Gravity.CENTER);
//            this.title.setTextColor(getResources().getColor(R.color.black));
//            this.title.setTextSize(11);
//            this.addView(this.imageView);
//            this.addView(this.title);
//        }
//
//        // =============================================================================
//        // Getters
//        // =============================================================================
//
//        public CircleImageView getImageView() {
//            return imageView;
//        }
//
//		public TextView getTitle() {
//			return title;
//		}
//
//    }
//}
//
//
