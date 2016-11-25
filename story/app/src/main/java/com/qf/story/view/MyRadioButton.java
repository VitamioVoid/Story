package com.qf.story.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.qf.story.R;

/**
 * 自定义RadioButton 用于主页面
 */
public class MyRadioButton extends FrameLayout implements View.OnClickListener {
    private ImageView checkImg;
    private RadioButton mRadioBtn;

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.radio_layout, this);
        checkImg = (ImageView) view.findViewById(R.id.checkImg);
        mRadioBtn = (RadioButton) view.findViewById(R.id.mRadioBtn);
        mRadioBtn.setOnClickListener(this);
    }

    public void setRadioText(String text) {
        mRadioBtn.setText(text);
    }

    public void setChecked(boolean isChecked) {
        mRadioBtn.setChecked(isChecked);
        if (isChecked) {
            checkImg.setVisibility(VISIBLE);
        } else {
            checkImg.setVisibility(INVISIBLE);
        }
    }


    public void setRadioClickListener(OnRadioClickListener radioClickListener) {
        this.radioClickListener = radioClickListener;
    }

    private OnRadioClickListener radioClickListener = null;

    public interface OnRadioClickListener {
        void radioClick(View view);
    }

    @Override
    public void onClick(View v) {
        radioClickListener.radioClick(this);
    }
}
