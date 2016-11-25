package com.qf.story.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qf.story.R;
import com.qf.story.entities.Data;
import com.qf.story.entities.ResponseResult;
import com.qf.story.utils.Utils;
import com.qf.story.view.Editor;
import com.qf.story.view.StoryTitle;

public class ModifyAliasActivity extends AppCompatActivity implements
        StoryTitle.OnSaveClickListener, StoryTitle.OnBackClickListener, Utils.ResponseCallBack {

    private StoryTitle mModifyAliasTitle;
    private Editor mUserAliasEt;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_alias);
        init();
    }

    private void init() {
        initView();
        setListener();
        initData();
    }

    private void initData() {
        //获取sp
        preferences = getSharedPreferences("login", MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void setListener() {
        mModifyAliasTitle.setSaveClickListener(this);
        mModifyAliasTitle.setBackClickListener(this);
        Utils.setResponseCallBack(this);
    }

    private void initView() {
        mModifyAliasTitle = (StoryTitle) findViewById(R.id.mModifyAliasTitle);
        mUserAliasEt = (Editor) findViewById(R.id.mUserAlias);

        mModifyAliasTitle.setTitle("修改昵称");
        mModifyAliasTitle.setSaveVisible();

        mUserAliasEt.setTitle(getResources().getString(R.string.user_alias));
    }

    @Override
    public void saveClick(View view) {
        String alias = mUserAliasEt.getEditText();
        int uid = preferences.getInt("id", 0);
        String pass = preferences.getString("userpass", "");
        Utils.modifyAlias(uid, pass, alias);
    }

    @Override
    public void backClick(View view) {
        finish();
    }


    @Override
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            editor.putString("alias", data.getNickname());//更新当前用户的信息
            editor.commit();
            setResult(RESULT_OK);
            finish();
        } else {
            Utils.toast(result.getMsg(), this);
        }
    }

}
