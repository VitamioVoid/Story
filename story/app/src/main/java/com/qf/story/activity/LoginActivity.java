package com.qf.story.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.qf.story.R;
import com.qf.story.entities.Data;
import com.qf.story.entities.ResponseResult;
import com.qf.story.utils.Utils;
import com.qf.story.view.Editor;
import com.qf.story.view.StoryTitle;

public class LoginActivity extends AppCompatActivity implements
        StoryTitle.OnBackClickListener, Utils.ResponseCallBack {

    private Editor userNameEt;
    private Editor userPwdEt;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private StoryTitle loginTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        initView();
        setListener();
        initData();

    }

    private void setListener() {

    }

    private void initData() {
        preferences = getSharedPreferences("login", MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void initView() {
        userNameEt = (Editor) findViewById(R.id.mUserName);
        userPwdEt = (Editor) findViewById(R.id.mUserPwd);
        loginTitle = (StoryTitle) findViewById(R.id.mLoginTitle);
        loginTitle.setBackClickListener(this);

        loginTitle.setTitle("用户登录");
        userNameEt.setTitle(getResources().getString(R.string.user_name));
        userPwdEt.setEditTextInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        userPwdEt.setTitle(getResources().getString(R.string.user_password));
    }

    public void intentToRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void clickLogin(View view) {
        String userName = userNameEt.getEditText();
        if (Utils.isChinese(userName)) {
            Utils.toast("账号不能为中文", this);
        } else {
            String password = userPwdEt.getEditText();
            alertNull(userName, password);
            Utils.setResponseCallBack(this);
            Utils.login(userName, password);
        }
    }

    private void alertNull(String... params) {
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals("")) {
                Utils.toast("不能为空", this);
                break;
            }
        }
    }

    @Override
    public void backClick(View view) {
        finish();
    }

    @Override
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            initSharePreference(data);
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        } else {
            Utils.toast(result.getMsg(), this);
            userNameEt.setEditText("");
            userPwdEt.setEditText("");
        }
    }

    private void initSharePreference(Data data) {
        editor.putBoolean("hasLogin", true);
        editor.putInt("id", data.getId());
        editor.putString("username", data.getUsername());
        editor.putString("userpass", data.getUserpass());
        editor.putInt("gender", data.getUsersex());
        editor.putString("email", data.getUseremail());
        editor.putString("alias", data.getNickname());
        editor.putString("birthday", data.getBirthday());
        editor.putString("portrait", data.getPortrait());
        editor.putString("signature", data.getSignature());
        editor.commit();
    }
}
