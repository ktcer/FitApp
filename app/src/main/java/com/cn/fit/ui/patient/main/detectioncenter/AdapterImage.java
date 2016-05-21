//package com.cn.aihu.ui.patient.main.detectioncenter;
//
//import android.content.Context;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.cn.aihu.R;
//import com.cn.aihu.ui.AppMain;
//import com.cn.aihu.util.AbsParam;
//import com.cn.aihu.util.CircleImageView;
//import com.cn.aihu.util.StringUtil;
//import com.cn.aihu.util.coverflow.FancyCoverFlow;
//import com.cn.aihu.util.coverflow.FancyCoverFlowAdapter;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
///**
// * @author kuangtiecheng
// * 
// */
//public class AdapterImage extends FancyCoverFlowAdapter {
//	private Context context;
//	private ImageLoader imageLoader;
//	private DisplayImageOptions options;
//
//	public AdapterImage(Context context,DisplayImageOptions options,ImageLoader imageLoader) {
//		this.context = context;
//		this.options = options;
//		this.imageLoader = imageLoader;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
////		return AppMain.allFamilyMemberList.size();
//		return 1;
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return AppMain.allFamilyMemberList.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getCoverFlowItem(int position, View reusableView,
//			ViewGroup parent) {
//		CustomViewGroup customViewGroup = null;
//
//        if (reusableView != null) {
//            customViewGroup = (CustomViewGroup) reusableView;
//        } else {
//            customViewGroup = new CustomViewGroup(parent.getContext());
//       
//            customViewGroup.setLayoutParams(new FancyCoverFlow.LayoutParams(140*AppMain.screenWidths/720, 195*AppMain.screenHeight/1280));
//        }
////        Glide.with(context)
////        .load(AbsParam.getBaseUrl()+AppMain.allFamilyMemberList.get(position).getImageUrl())
////        .placeholder(R.drawable.default_user_icon)
////        .crossFade()
////        .into(new SimpleTarget<Bitmap>(width, height) {
////        @Override
////        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
////            // setImageBitmap(bitmap) on CircleImageView
////			customViewGroup.getImageView().setImageBitmap(bitmap);
////        }
////        };
//
////        .into(customViewGroup.getImageView());
//        imageLoader.displayImage(AbsParam.getBaseUrl()+AppMain.allFamilyMemberList.get(position).getImageUrl(), customViewGroup.getImageView(), options);
////		customViewGroup.getTitle().setText(StringUtil.displayName(AppMain.allFamilyMemberList.get(position)));
//        customViewGroup.getTitle().setText("我");
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
//            this.title.setTextSize(16);
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
//
//}