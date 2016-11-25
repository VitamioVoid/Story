package com.qf.story.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;

import com.qf.story.R;
import com.qf.story.entities.Data;
import com.qf.story.entities.ResponseResult;
import com.qf.story.utils.Utils;
import com.qf.story.view.Editor;
import com.qf.story.view.StoryTitle;

public class ModifyPwdActivity extends AppCompatActivity implements StoryTitle.OnBackClickListener, Utils.ResponseCallBack, TextWatcher {

    private StoryTitle modifyPwdTitle;
    private Editor oldPwdEt;
    private Editor newPwdEt;
    private Editor confirmPwdEt;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
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
        modifyPwdTitle.setBackClickListener(this);
        Utils.setResponseCallBack(this);
    }

    private void initView() {
        modifyPwdTitle = (StoryTitle) findViewById(R.id.mModifyPwdTitle);
        oldPwdEt = (Editor) findViewById(R.id.oldPwd);
        newPwdEt = (Editor) findViewById(R.id.newPwd);
        confirmPwdEt = (Editor) findViewById(R.id.confirmPwd);

        modifyPwdTitle.setTitle("修改密码");
        oldPwdEt.setEditTextInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPwdEt.setEditTextInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPwdEt.setEditTextInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        oldPwdEt.setTitle("原 密 码：");
        newPwdEt.setTitle("新 密 码：");
        confirmPwdEt.setTitle("重 复 密 码：");
        confirmPwdEt.setTextWatcher(this);
    }

    private String oldPassword;
    private String newPassword;

    public void clickPwd(View view) {
        oldPassword = oldPwdEt.getEditText();
        newPassword = newPwdEt.getEditText();
        int uid = preferences.getInt("id", 0);
        Utils.modifyPassword(uid, oldPassword, newPassword);
    }

    @Override
    public void backClick(View view) {
        finish();
    }

    @Override
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            editor.putString("userpass", data.getUserpass());
            editor.commit();
            setResult(RESULT_OK);
            finish();
        } else {
            Utils.toast(result.getMsg(), this);
            newPwdEt.setEditText("");
            oldPwdEt.setEditText("");
            confirmPwdEt.setEditText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (newPwdEt.getEditText().equals("")) {
            newPwdEt.setFocusable(true);
            Utils.toast("输入不能为空", this);
        } else {
            if (s.toString().length() == newPwdEt.getEditText().length()
                    && !s.toString().equals(newPwdEt.getEditText())) {
                Utils.toast("密码不一致", this);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
