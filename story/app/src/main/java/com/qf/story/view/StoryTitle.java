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
 * 用于大多数页面的头部
 */

public class StoryTitle extends LinearLayout implements View.OnClickListener {
    private Button mBackBtn;
    private Button mSaveBtn;
    private TextView mTitleTv;

    private View view;

    public StoryTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.title_layout, this);
        mTitleTv = (TextView) view.findViewById(R.id.mTitle);
        mBackBtn = (Button) view.findViewById(R.id.mBack);
        mSaveBtn = (Button) view.findViewById(R.id.mSave);
        mBackBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setBackClickListener(OnBackClickListener listener) {
        this.backClickListener = listener;
    }

    public void setSaveClickListener(OnSaveClickListener saveClickListener) {
        this.saveClickListener = saveClickListener;
    }

    public void setSaveVisible() {
        mSaveBtn.setVisibility(View.VISIBLE);
    }

    private OnBackClickListener backClickListener = null;

    private OnSaveClickListener saveClickListener = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBack:
                backClickListener.backClick(v);
                break;
            case R.id.mSave:
                saveClickListener.saveClick(v);
                break;
        }
    }

    public interface OnSaveClickListener {
        void saveClick(View view);
    }

    public interface OnBackClickListener {
        void backClick(View view);
    }
}
