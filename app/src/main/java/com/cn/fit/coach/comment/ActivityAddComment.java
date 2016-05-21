package com.cn.fit.coach.comment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.customer.BeanAddComment;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.paychoose.PayRadioGroup;
import com.cn.fit.ui.paychoose.PayRadioPurified;
import com.cn.fit.util.CircleImageView;

/**
 * Created by ktcer on 2016/1/20.
 */
public class ActivityAddComment extends ActivityBasic {
    private EditText commentext;
    private CircleImageView coachImage;
    private RatingBar rbCourseStar, rbCoachStar;
    private PayRadioGroup group;
    private String commentStr;
    private int commentInt = 2;
    private int starLevelClass;
    private int starLevelCoach;
    private long classID;
    private long coachID;
    private String ddh;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableBanner = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comments);
        setTitle("");
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        classID = getIntent().getLongExtra("classID", 0);
        coachID = getIntent().getLongExtra("coachID", 0);
        ddh = getIntent().getStringExtra("ddh");
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("评价");
        TextView rigth = (TextView) findViewById(R.id.right_tv);
        rigth.setOnClickListener(this);
        rigth.setText("提交");
        rigth.setVisibility(View.VISIBLE);
        commentext = (EditText) findViewById(R.id.comment_text);
        commentext.addTextChangedListener(new CustomTextWacher(0));
        commentext.setOnClickListener(this);
        rbCourseStar = (RatingBar) findViewById(R.id.rb_course_star);
        rbCourseStar.setNumStars(5);
        rbCourseStar.setRating(0);
        rbCourseStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                starLevelClass = (int) rating;
            }
        });
        rbCoachStar = (RatingBar) findViewById(R.id.rb_coach_star);
        rbCoachStar.setNumStars(5);
        rbCoachStar.setRating(0);
        rbCoachStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                starLevelCoach = (int) rating;

            }
        });

        group = (PayRadioGroup) findViewById(R.id.genderGroup);
        group.setOnCheckedChangeListener(new PayRadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(PayRadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int radioButtonId = group.getCheckedRadioButtonId();
                PayRadioPurified rl = (PayRadioPurified) ActivityAddComment.this
                        .findViewById(radioButtonId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    ((PayRadioPurified) group.getChildAt(i))
                            .setChangeImg(checkedId);
                    switch (checkedId) {
                        case 2131492941:
                            ((PayRadioPurified) group.getChildAt(0)).setDrawableLogo(getResources().getDrawable(R.drawable.haoping1));
                            ((PayRadioPurified) group.getChildAt(1)).setDrawableLogo(getResources().getDrawable(R.drawable.zhongping));
                            ((PayRadioPurified) group.getChildAt(2)).setDrawableLogo(getResources().getDrawable(R.drawable.chaping));
                            break;

                        case 2131492942:
                            ((PayRadioPurified) group.getChildAt(0)).setDrawableLogo(getResources().getDrawable(R.drawable.haoping));
                            ((PayRadioPurified) group.getChildAt(1)).setDrawableLogo(getResources().getDrawable(R.drawable.zhongping1));
                            ((PayRadioPurified) group.getChildAt(2)).setDrawableLogo(getResources().getDrawable(R.drawable.chaping));
                            break;
                        case 2131492943:
                            ((PayRadioPurified) group.getChildAt(0)).setDrawableLogo(getResources().getDrawable(R.drawable.haoping));
                            ((PayRadioPurified) group.getChildAt(1)).setDrawableLogo(getResources().getDrawable(R.drawable.zhongping));
                            ((PayRadioPurified) group.getChildAt(2)).setDrawableLogo(getResources().getDrawable(R.drawable.chaping1));
                            break;
                    }
                }

                commentStr = rl.getTextTitle().toString();
                if (commentStr == null) {

                } else {
                    switch (commentStr) {
                        case "好评":
                            commentInt = 2;
                            break;
                        case "中评":
                            commentInt = 1;
                            break;
                        case "差评":
                            commentInt = 0;
                            break;


                    }
                    // payTectView.setVisibility(View.VISIBLE);
                }

                // Toast.makeText(ActivityPayChoose.this, rl.getTextTitle(),
                // Toast.LENGTH_SHORT).show();
            }
        });


//        ImageLoader.getInstance().displayImage(
//                AbsParam.getBaseUrl() + beanDiscovery.getCover(), bigTitleImage,
//                AppMain.initImageOptions(R.drawable.ic_user_default,
//                        false));


    }

    private class CustomTextWacher implements TextWatcher {
        int type;


        public CustomTextWacher(int type) {
            this.type = type;

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub
            switch (type) {
                case 0:
                    commentStr = arg0.toString();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv://评论类容
                showProgressBar();
                AscynAddComment task = new AscynAddComment(commentInt + "", this, classID + "", coachID + "", commentStr, starLevelClass + "", starLevelCoach + "", ddh) {
                    @Override
                    protected void onPostExecute(BeanAddComment beanAddComment) {
                        super.onPostExecute(beanAddComment);
                        hideProgressBar();
                        if (beanAddComment == null) {
                            return;
                        }
                        if (beanAddComment.getResult() == 1) {
//                            String old = TabActivityMain.class.getName();
//                            backTo(old);
                            finish();

                        } else {
                            showToastDialog("提交失败" + beanAddComment.getDetail());
                        }
                    }
                };
                task.execute();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
