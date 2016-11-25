package com.qf.story.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qf.story.R;


public class HomePageTitle extends LinearLayout implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Button mBackBtn;
    private Button mEditBtn;
    private RadioGroup mRadioGroup;

    private View view;

    public HomePageTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.home_page_title_layout, this);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.mGroup);
        mRadioGroup.setOnCheckedChangeListener(this);
        mBackBtn = (Button) view.findViewById(R.id.mBack);
        mEditBtn = (Button) view.findViewById(R.id.mEdit);
        mBackBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);
    }


    public void setBackClickListener(OnBackClickListener listener) {
        this.backClickListener = listener;
    }

    public void setEditClickListener(OnEditClickListener editClickListener) {
        this.editClickListener = editClickListener;
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

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = null;

    public void setCheckedChangeListener(RadioGroup.OnCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    public interface OnCheckChangedListener {
        void onCheckedChanged(RadioGroup group, int checkedId);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        checkedChangeListener.onCheckedChanged(group, checkedId);
    }

    public interface OnEditClickListener {
        void editClick(View view);
    }

    public interface OnBackClickListener {
        void backClick(View view);
    }
}
