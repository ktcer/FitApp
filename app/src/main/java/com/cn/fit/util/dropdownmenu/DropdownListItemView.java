package com.cn.fit.util.dropdownmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cn.fit.R;

/**
 * Created by Administrator on 2015/5/28.
 */
public class DropdownListItemView extends TextView {
    public DropdownListItemView(Context context) {
        this(context, null);
    }

    public DropdownListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropdownListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(CharSequence text, boolean checked) {
        setText(text);
        if (checked) {
            Drawable icon = getResources().getDrawable(R.drawable.round_selector_checked);
            setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }


}
