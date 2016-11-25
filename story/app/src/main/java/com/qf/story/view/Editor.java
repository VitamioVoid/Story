package com.qf.story.view;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qf.story.R;

/**
 * 自定义编辑控件
 */
public class Editor extends LinearLayout {
    private EditText editText;
    private TextView titleTv;

    public Editor(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.editor_layout, this);
        editText = (EditText) view.findViewById(R.id.editContent);
        titleTv = (TextView) view.findViewById(R.id.editTitle);
        titleTv.setTextColor(getResources().getColor(R.color.textTitleColor));
        editText.setTextColor(getResources().getColor(R.color.infoTextColor));
    }

    /**
     * 注册文本监视器
     * @param watcher
     */
    public void setTextWatcher(TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    public void setClickable(boolean clickable) {
        editText.setClickable(clickable);
    }

    @Override
    public void setFocusable(boolean focusable) {
        editText.setFocusable(focusable);
    }

    public void setEditTextInputType(int type) {
        editText.setInputType(type);
    }

    public void setEditText(String text) {
        editText.setText(text);
    }

    public String getEditText() {
        return editText.getText().toString();
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        editText.setOnClickListener(l);
    }
}
