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

public class ModifySignatureActivity extends AppCompatActivity implements
        StoryTitle.OnSaveClickListener, StoryTitle.OnBackClickListener, Utils.ResponseCallBack {

    private StoryTitle mModifySignTitle;
    private Editor mUserSignEt;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_signature);
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
        mModifySignTitle.setSaveClickListener(this);
        mModifySignTitle.setBackClickListener(this);
        Utils.setResponseCallBack(this);
    }

    private void initView() {
        mModifySignTitle = (StoryTitle) findViewById(R.id.mModifySignTitle);
        mUserSignEt = (Editor) findViewById(R.id.mUserSignature);

        mModifySignTitle.setTitle("修改签名");
        mModifySignTitle.setSaveVisible();

        mUserSignEt.setTitle(getResources().getString(R.string.user_signature));
    }

    @Override
    public void saveClick(View view) {
        String signature = mUserSignEt.getEditText();
        int uid = preferences.getInt("id", 0);
        String pass = preferences.getString("userpass", "");
        Utils.modifySignature(uid, pass, signature);
    }

    @Override
    public void backClick(View view) {
        finish();
    }


    @Override
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            editor.putString("signature", data.getSignature());
            editor.commit();
            setResult(RESULT_OK);
            finish();
        } else {
            Utils.toast(result.getMsg(), this);
        }
    }

}
