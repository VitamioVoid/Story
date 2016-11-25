package com.qf.story.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qf.story.R;


public class DetailTitle extends LinearLayout implements View.OnClickListener {
    private Button mBackBtn;
    private Button mMoreBtn;
    private TextView mTitleTv;

    private View view;

    public DetailTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.detail_title_layout, this);
        mTitleTv = (TextView) view.findViewById(R.id.mTitle);
        mBackBtn = (Button) view.findViewById(R.id.mBack);
        mMoreBtn = (Button) view.findViewById(R.id.mMore);
        mBackBtn.setOnClickListener(this);
        mMoreBtn.setOnClickListener(this);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setBackClickListener(OnBackClickListener listener) {
        this.backClickListener = listener;
    }

    public void setMoreClickListener(OnMoreClickListener moreClickListener) {
        this.moreClickListener = moreClickListener;
    }

    public void setMoreVisible() {
        mMoreBtn.setVisibility(View.VISIBLE);
    }

    private OnBackClickListener backClickListener = null;

    private OnMoreClickListener moreClickListener = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBack:
                backClickListener.backClick(v);
                break;
            case R.id.mMore:
                moreClickListener.moreClick(v);
                break;
        }
    }

    public interface OnMoreClickListener {
        void moreClick(View view);
    }

    public interface OnBackClickListener {
        void backClick(View view);
    }
}
