package com.qf.story.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qf.story.R;


public class RecordTitle extends LinearLayout implements View.OnClickListener {
    private Button mBackBtn;
    private Button mEditBtn;
    private TextView mTitleTv;

    private View view;

    public RecordTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.record_title_layout, this);
        mTitleTv = (TextView) view.findViewById(R.id.mTitle);
        mBackBtn = (Button) view.findViewById(R.id.mBack);
        mEditBtn = (Button) view.findViewById(R.id.mEdit);
        mBackBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setBackClickListener(OnBackClickListener listener) {
        this.backClickListener = listener;
    }

    public void setEditClickListener(OnEditClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }

    public void setEditVisible() {
        mEditBtn.setVisibility(View.VISIBLE);
    }

    private OnBackClickListener backClickListener = null;

    private OnEditClickListener editClickListener = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBack:
                backClickListener.backClick(v);
                break;
            case R.id.mEdit:
                editClickListener.editClick(v);
                break;
        }
    }

    public interface OnEditClickListener {
        void editClick(View view);
    }

    public interface OnBackClickListener {
        void backClick(View view);
    }
}
