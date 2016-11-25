package com.qf.story.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.qf.story.R;
import com.qf.story.entities.Data;
import com.qf.story.entities.ResponseResult;
import com.qf.story.utils.Utils;
import com.qf.story.view.Editor;
import com.qf.story.view.StoryTitle;

import java.io.File;

public class RegisterActivity extends AppCompatActivity
        implements StoryTitle.OnBackClickListener, TextWatcher, Utils.ResponseCallBack {

    private StoryTitle mTitle;
    private Editor userNameEt;
    private Editor userPwdEt;
    private Editor userAliasEt;

    private ImageView mPortrait;
    private static final int PICK_RESULT = 1;
    private String portraitImgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
    }

    private void init() {
        initView();
        setListener();
    }

    private void setListener() {
        userNameEt.setTextWatcher(this);
    }

    private void initView() {
        mTitle = (StoryTitle) findViewById(R.id.mRegisterTitle);
        mTitle.setTitle("用户注册");
        mTitle.setBackClickListener(this);

        userNameEt = (Editor) findViewById(R.id.mUserName);
        userAliasEt = (Editor) findViewById(R.id.mUserAlias);
        userPwdEt = (Editor) findViewById(R.id.mUserPwd);

        mPortrait = (ImageView) findViewById(R.id.mPortrait);

        userNameEt.setTitle(getResources().getString(R.string.user_name));
        userAliasEt.setTitle(getResources().getString(R.string.user_alias));
        userPwdEt.setTitle(getResources().getString(R.string.user_password));
        userPwdEt.setEditTextInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void upLoadPortrait(View v) {
        new AlertDialog.Builder(this).setTitle("从图库里选择图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PICK_RESULT);
                    }
                }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_RESULT && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().
                    query(selectedImage, filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                portraitImgPath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmap = Utils.createImageThumbnail(portraitImgPath, 100, 100);
                mPortrait.setImageBitmap(bitmap);
            }
        }
    }

    public void clickRegister(View view) {
        String userName = userNameEt.getEditText();
        String password = userPwdEt.getEditText();
        String alias = userAliasEt.getEditText();
        alertNull(userName, password, alias);

        Utils.setResponseCallBack(this);
        Utils.regist(userName, password, alias, portraitImgPath);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (Utils.isChinese(s.toString())) {
            Utils.toast("用户名不能有中文！", this);
            userNameEt.setEditText("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            finish();
        } else {
            Utils.toast(result.getMsg(), this);
        }
    }
}
