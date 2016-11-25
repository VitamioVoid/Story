package com.qf.story.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;

import com.qf.story.R;
import com.qf.story.entities.Data;
import com.qf.story.entities.ResponseResult;
import com.qf.story.utils.Utils;
import com.qf.story.view.Editor;
import com.qf.story.view.StoryTitle;

import java.util.Calendar;

public class ModifyBirthDayActivity extends AppCompatActivity implements
        StoryTitle.OnBackClickListener,
        StoryTitle.OnSaveClickListener,
        Utils.ResponseCallBack,
        View.OnClickListener {

    private StoryTitle mModifyBirthTitle;
    private Editor mUserBirthEt;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_birth_day);
        init();
    }

    private void init() {
        initView();
        setListener();
        initData();
    }

    private void initData() {
        preferences = getSharedPreferences("login", MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void setListener() {
        mModifyBirthTitle.setBackClickListener(this);
        mModifyBirthTitle.setSaveVisible();
        mModifyBirthTitle.setSaveClickListener(this);
        mUserBirthEt.setOnClickListener(this);
        Utils.setResponseCallBack(this);
    }

    private void initView() {
        mModifyBirthTitle = (StoryTitle) findViewById(R.id.mModifyBirthTitle);
        mModifyBirthTitle.setTitle("修改年龄");
        mUserBirthEt = (Editor) findViewById(R.id.mUserBirthDay);
        mUserBirthEt.setTitle(getResources().getString(R.string.user_birthday));
        mUserBirthEt.setEditTextInputType(InputType.TYPE_NULL);
        mUserBirthEt.setClickable(true);
    }

    @Override
    public void backClick(View view) {
        finish();
    }

    private String birthday;

    @Override
    public void saveClick(View view) {
        birthday = mUserBirthEt.getEditText();
        int uid = preferences.getInt("id", 0);
        String pass = preferences.getString("userpass", "");
        Utils.modifyBirthday(uid, pass, birthday);
    }

    @Override
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            editor.putString("birthday", data.getBirthday());
            editor.commit();
            setResult(RESULT_OK);
            finish();
        } else {
            Utils.toast(result.getMsg(), this);
        }
    }

    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mUserBirthEt.setEditText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }
        }, c.get(Calendar.YEAR)
                , c.get(Calendar.MONTH)
                , c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
