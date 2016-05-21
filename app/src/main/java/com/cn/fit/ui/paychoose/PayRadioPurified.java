package com.cn.fit.ui.paychoose;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.fit.R;

public class PayRadioPurified extends RelativeLayout implements Checkable {

    private ImageView payLogo;
    private TextView payTitle;
    private TextView payDesc;
    private RadioButton payChecked;

    private boolean mChecked;    ////閻樿埖锟戒焦妲搁崥锕傦拷澶夎厬
    private boolean mBroadcasting;
    private int id;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public PayRadioPurified(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.payment_list_item, this);

        payLogo = (ImageView) findViewById(R.id.pay_icon);
        payTitle = (TextView) findViewById(R.id.pay_name);
        payDesc = (TextView) findViewById(R.id.pay_desc);
        payChecked = (RadioButton) findViewById(R.id.pay_check);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PayRidioButton);

//		Drawable d = array.getDrawable(R.styleable.PayRidioButton_radio);
//		if (d != null) {
//			payChecked.setButtonDrawable(d);
//		}

        String title = array.getString(R.styleable.PayRidioButton_title);
        if (title != null) {
            setTextTitle(title);
        }

        String str = array.getString(R.styleable.PayRidioButton_desc);
        if (str != null) {
            setTextDesc(str);
        }

        Drawable logo = array.getDrawable(R.styleable.PayRidioButton_logo);
        if (logo != null) {
            setDrawableLogo(logo);
        }

        boolean checked = array.getBoolean(R.styleable.PayRidioButton_checked, false);
        payChecked.setChecked(checked);

        array.recycle();
        setClickable(true);

        id = getId();
    }

    @Override
    public boolean isChecked() {
        // TODO Auto-generated method stub
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        // TODO Auto-generated method stub
        if (mChecked != checked) {
            mChecked = checked;
            payChecked.refreshDrawableState();

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
            mBroadcasting = false;
        }
    }

    @Override
    public void toggle() {
        // TODO Auto-generated method stub
        if (!isChecked()) {
            setChecked(!mChecked);
        }
    }

    @Override
    public boolean performClick() {
        // TODO Auto-generated method stub
        /*
         * XXX: These are tiny, need some surrounding 'expanded touch area',
         * which will need to be implemented in Button if we only override
         * performClick()
         */

        /* When clicked, toggle the state */
        toggle();
        return super.performClick();
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(PayRadioPurified buttonView, boolean isChecked);
    }

    public void setTextDesc(String s) {
        if (s != null) {
            payDesc.setText(s);
        }
    }

    public void setTextTitle(String s) {
        if (s != null) {
            payTitle.setText(s);
        }
    }

    public String getTextTitle() {
        String s = payTitle.getText().toString();
        return s == null ? "" : s;
    }

    public void setDrawableLogo(Drawable d) {
        if (d != null) {
            payLogo.setImageDrawable(d);
        }
    }

    public void setChangeImg(int checkedId) {
        System.out.println(">>" + checkedId);
        System.out.println(">>" + id);
        if (checkedId == id) {
            payChecked.setChecked(true);
        } else {
            payChecked.setChecked(false);
        }
    }

}
