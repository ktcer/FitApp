//package com.cn.aihu.ui.patient.main.mynurse;
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
//import com.cn.aihu.util.coverflow.FancyCoverFlow;
//import com.cn.aihu.util.coverflow.FancyCoverFlowAdapter;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
///**
// * @author kuangtiecheng
// * 
// */
//public class ImageAdapterNurse extends FancyCoverFlowAdapter {
//	private Context context;
//	private ImageLoader imageLoader;
//	private DisplayImageOptions options;
//
//	public ImageAdapterNurse(Context context, ImageLoader imageLoader,
//			DisplayImageOptions options) {
//		super();
//		this.context = context;
//		this.imageLoader = imageLoader;
//		this.options = options;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return AppMain.allExpertList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return AppMain.allExpertList.get(position);
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
//        if(position==AppMain.allExpertList.size()-1){
//        	//最后一个未添加按钮
//        	customViewGroup.getImageView().setImageResource(R.drawable.add_user_icon);
//        	customViewGroup.getTitle().setText("点击添加\n保健秘书");
//        	customViewGroup.getTitle().setTextSize(12);
//        }else{
//        	imageLoader.displayImage(AbsParam.getBaseUrl()+AppMain.allExpertList.get(position).getTxlj(), customViewGroup.getImageView(), options);
//        	customViewGroup.getTitle().setText(AppMain.allExpertList.get(position).getExpertName());
//        }
//        return customViewGroup;
//	}
//	
//    private static class CustomViewGroup extends LinearLayout {
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
//        private CustomViewGroup(Context context) {
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
//        private CircleImageView getImageView() {
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