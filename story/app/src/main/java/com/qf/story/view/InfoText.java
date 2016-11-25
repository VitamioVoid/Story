package com.qf.story.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qf.story.R;

/**
 * 自定义文本显示控件 用于个人中心页面和设置页面
 */
public class InfoText extends LinearLayout implements View.OnClickListener {
    private TextView infoTv;
    private TextView titleTv;
    private Button jumpBtn;
    private ImageView genderIv;

    public InfoText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.info_text_layout, this);
        infoTv = (TextView) view.findViewById(R.id.infoContent);
        titleTv = (TextView) view.findViewById(R.id.editTitle);
        titleTv.setTextColor(getResources().getColor(R.color.textTitleColor));
        jumpBtn = (Button) view.findViewById(R.id.jumpBtn);
        genderIv = (ImageView) view.findViewById(R.id.genderImg);
        jumpBtn.setOnClickListener(this);
    }

    public void setInfoText(String content) {
        infoTv.setText(content);
    }

    public void setBtnVisible() {
        jumpBtn.setVisibility(View.VISIBLE);
    }

    public void setBtnInVisible() {
        jumpBtn.setVisibility(View.INVISIBLE);
    }

    public void setInfoTestColor(int color) {
        infoTv.setTextColor(color);
    }

    public void setGenderIv(int id) {
        genderIv.setVisibility(VISIBLE);
        genderIv.setImageResource(id);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public void setJumpClickListener(OnJumpClickListener listener) {
        this.listener = listener;
    }

    private OnJumpClickListener listener = null;

    public interface OnJumpClickListener {
        void click(View view);
    }

    @Override
    public void onClick(View v) {
        listener.click(this);
    }
}
