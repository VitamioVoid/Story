package com.qf.story.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qf.story.R;

/**
 * 用于新的故事的页面的头部
 */
public class NewStoryTitle extends LinearLayout implements View.OnClickListener {
    private Button mBackBtn;
    private Button mSendBtn;
    private TextView mTitleTv;

    private View view;

    public NewStoryTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.new_title_layout, this);
        mTitleTv = (TextView) view.findViewById(R.id.mTitle);
        mBackBtn = (Button) view.findViewById(R.id.mBack);
        mSendBtn = (Button) view.findViewById(R.id.mSend);
        mBackBtn.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setBackClickListener(OnBackClickListener listener) {
        this.backClickListener = listener;
    }

    public void setSendClickListener(OnSendClickListener sendClickListener) {
        this.sendClickListener = sendClickListener;
    }

    private OnBackClickListener backClickListener = null;

    private OnSendClickListener sendClickListener = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBack:
                backClickListener.backClick(v);
                break;
            case R.id.mSend:
                sendClickListener.sendClick(v);
                break;
        }
    }

    public interface OnSendClickListener {
        void sendClick(View view);
    }

    public interface OnBackClickListener {
        void backClick(View view);
    }
}
