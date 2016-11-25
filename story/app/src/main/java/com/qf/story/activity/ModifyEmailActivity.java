package com.qf.story.activity;

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

public class ModifyEmailActivity extends AppCompatActivity implements StoryTitle.OnBackClickListener, StoryTitle.OnSaveClickListener, Utils.ResponseCallBack {

    private StoryTitle mModifyEmailTitle;
    private Editor mUserEmailEt;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_email);
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
        mModifyEmailTitle.setBackClickListener(this);
        mModifyEmailTitle.setSaveVisible();
        mModifyEmailTitle.setSaveClickListener(this);
        Utils.setResponseCallBack(this);
    }

    private void initView() {
        mModifyEmailTitle = (StoryTitle) findViewById(R.id.mModifyEmailTitle);
        mModifyEmailTitle.setTitle("修改邮箱");

        mUserEmailEt = (Editor) findViewById(R.id.mUserEmail);
        mUserEmailEt.setTitle(getResources().getString(R.string.user_email));
        mUserEmailEt.setEditTextInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    @Override
    public void backClick(View view) {
        finish();
    }

    private String email;

    @Override
    public void saveClick(View view) {
        email = mUserEmailEt.getEditText();
        if (!checkEmail(email)) {
            Utils.toast("email输入有误", this);
            mUserEmailEt.setEditText("");
        } else {
            int uid = preferences.getInt("id", 0);
            String pass = preferences.getString("userpass", "");
            Utils.modifyEmail(uid, pass, email);
        }
    }

    /**
     * 检测email是否正确输入
     *
     * @param email
     */
    private boolean checkEmail(String email) {
        String reg = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
        if (!email.matches(reg)) {
            return false;
        }
        return true;
    }

    @Override
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            editor.putString("email", data.getUseremail());
            editor.commit();
            setResult(RESULT_OK);
            finish();
        } else {
            Utils.toast(result.getMsg(), this);
        }
    }
}
