package com.qf.story.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.qf.story.R;
import com.qf.story.entities.Data;
import com.qf.story.entities.ResponseResult;
import com.qf.story.utils.Utils;
import com.qf.story.view.Editor;
import com.qf.story.view.MyRadioButton;
import com.qf.story.view.StoryTitle;

public class ModifyGenderActivity extends AppCompatActivity implements
        StoryTitle.OnBackClickListener, StoryTitle.OnSaveClickListener, MyRadioButton.OnRadioClickListener, Utils.ResponseCallBack {

    private StoryTitle mModifyGenderTitle;
    private MyRadioButton maleRadio;
    private MyRadioButton femaleRadio;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_gender);
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
        mModifyGenderTitle.setBackClickListener(this);
        mModifyGenderTitle.setSaveVisible();
        mModifyGenderTitle.setSaveClickListener(this);
        femaleRadio.setRadioClickListener(this);
        maleRadio.setRadioClickListener(this);
        Utils.setResponseCallBack(this);
    }

    private void initView() {
        mModifyGenderTitle = (StoryTitle) findViewById(R.id.mModifyGenderTitle);
        mModifyGenderTitle.setTitle("修改性别");

        maleRadio = (MyRadioButton) findViewById(R.id.maleRadio);
        femaleRadio = (MyRadioButton) findViewById(R.id.femaleRadio);
        maleRadio.setRadioText(getResources().getString(R.string.male));
        femaleRadio.setRadioText(getResources().getString(R.string.female));
        maleRadio.setChecked(true);
    }

    @Override
    public void backClick(View view) {
        finish();
    }

    @Override
    public void saveClick(View view) {
        int uid = preferences.getInt("id", 0);
        String pass = preferences.getString("userpass", "");
        Utils.modifyGender(uid, pass, gender);
    }

    private int gender = 0;

    @Override
    public void radioClick(View view) {
        switch (view.getId()) {
            case R.id.femaleRadio:
                gender = 1;
                femaleRadio.setChecked(true);
                maleRadio.setChecked(false);
                break;
            case R.id.maleRadio:
                gender = 0;
                maleRadio.setChecked(true);
                femaleRadio.setChecked(false);
                break;
        }
    }

    @Override
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            editor.putInt("gender", data.getUsersex());
            editor.commit();
            setResult(RESULT_OK);
            finish();
        } else {
            Utils.toast(result.getMsg(), this);
        }
    }

}
