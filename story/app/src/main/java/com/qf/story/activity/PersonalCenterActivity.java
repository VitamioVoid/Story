package com.qf.story.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.qf.story.R;
import com.qf.story.entities.Data;
import com.qf.story.entities.ResponseResult;
import com.qf.story.utils.Utils;
import com.qf.story.view.InfoText;
import com.qf.story.view.StoryTitle;
import com.squareup.picasso.Picasso;

public class PersonalCenterActivity extends AppCompatActivity implements View.OnClickListener,
        InfoText.OnJumpClickListener, StoryTitle.OnBackClickListener, Utils.ResponseCallBack {

    private StoryTitle centerTitle;
    private InfoText userNameInfo;
    private InfoText userGenderInfo;
    private InfoText userAliasInfo;
    private InfoText userEmailInfo;
    private InfoText userBirthDayInfo;

    private Button pwdModifyBtn;
    private ImageView mPortrait;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
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

        userNameInfo.setInfoText(preferences.getString("username", "无名氏"));
        userNameInfo.setBtnInVisible();
        userAliasInfo.setInfoText(preferences.getString("alias", ""));

        int gender = preferences.getInt("gender", -1);
        if (gender != -1) {
            userGenderInfo.setInfoText(gender == 0 ? "男" : "女");
            userGenderInfo.setGenderIv(gender == 0 ? R.drawable.icon_man : R.drawable.icon_woman);
        }

        String email = preferences.getString("email", "");
        if (!email.equals("")) {
            userEmailInfo.setInfoText(email);
        }

        String birthday = preferences.getString("birthday", "");
        if (!birthday.equals("")) {
            userBirthDayInfo.setInfoText(birthday);
        }

        String portraitPath = preferences.getString("portrait", "");
        portraitPath = Utils.BASE_URL + Utils.BASE_PORTRAIT + portraitPath;
        Picasso.with(this).load(portraitPath)
                .placeholder(R.drawable.icon_portrait)
                .config(Bitmap.Config.RGB_565)
                .into(mPortrait);
    }

    private void setListener() {
        pwdModifyBtn.setOnClickListener(this);
        userAliasInfo.setJumpClickListener(this);
        userGenderInfo.setJumpClickListener(this);
        userEmailInfo.setJumpClickListener(this);
        userBirthDayInfo.setJumpClickListener(this);
        centerTitle.setBackClickListener(this);
        Utils.setResponseCallBack(this);
    }

    private void initView() {
        centerTitle = (StoryTitle) findViewById(R.id.mCenterTitle);
        userNameInfo = (InfoText) findViewById(R.id.mUserName);
        userAliasInfo = (InfoText) findViewById(R.id.mUserAlias);
        userGenderInfo = (InfoText) findViewById(R.id.mUserGender);
        userEmailInfo = (InfoText) findViewById(R.id.mUserEmail);
        userBirthDayInfo = (InfoText) findViewById(R.id.mUserBirthDay);
        pwdModifyBtn = (Button) findViewById(R.id.pwdModify);
        mPortrait = (ImageView) findViewById(R.id.mPortrait);

        centerTitle.setTitle("个人中心");
        userNameInfo.setTitle(getResources().getString(R.string.user_name));
        userAliasInfo.setTitle(getResources().getString(R.string.user_alias));
        userGenderInfo.setTitle(getResources().getString(R.string.user_gender));
        userEmailInfo.setTitle(getResources().getString(R.string.user_email));
        userBirthDayInfo.setTitle(getResources().getString(R.string.user_birthday));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PersonalCenterActivity.this,
                ModifyPwdActivity.class);
        startActivity(intent);
    }

    private static final int MODIFY_ALIAS = 3;
    private static final int MODIFY_BIRTHDAY = 4;
    private static final int MODIFY_GENDER = 5;
    private static final int MODIFY_EMAIL = 6;

    @Override
    public void click(View view) {
        Intent intent = null;
        int requestCode = 0;
        switch (view.getId()) {
            case R.id.mUserAlias:
                intent = new Intent(PersonalCenterActivity.this,
                        ModifyAliasActivity.class);
                requestCode = MODIFY_ALIAS;
                break;
            case R.id.mUserBirthDay:
                intent = new Intent(PersonalCenterActivity.this,
                        ModifyBirthDayActivity.class);
                requestCode = MODIFY_BIRTHDAY;
                break;
            case R.id.mUserEmail:
                intent = new Intent(PersonalCenterActivity.this,
                        ModifyEmailActivity.class);
                requestCode = MODIFY_EMAIL;
                break;
            case R.id.mUserGender:
                intent = new Intent(PersonalCenterActivity.this,
                        ModifyGenderActivity.class);
                requestCode = MODIFY_GENDER;
                break;
        }
        startActivityForResult(intent, requestCode);
    }

    private String portraitImgPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MODIFY_ALIAS:
                    String alias = preferences.getString("alias", "");
                    userAliasInfo.setInfoText(alias);
                    break;
                case MODIFY_GENDER:
                    int gender = preferences.getInt("gender", 0);
                    userGenderInfo.setInfoText(gender == 0 ? "男" : "女");
                    userGenderInfo.setGenderIv(gender == 0 ? R.drawable.icon_man : R.drawable.icon_woman);
                    break;
                case MODIFY_BIRTHDAY:
                    String birthday = preferences.getString("birthday", "");
                    userBirthDayInfo.setInfoText(birthday);
                    break;
                case MODIFY_EMAIL:
                    String email = preferences.getString("email", "");
                    userEmailInfo.setInfoText(email);
                    break;
                default:
                    break;
            }
        }
        if (requestCode == PICK_RESULT && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().
                    query(selectedImage, filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                portraitImgPath = cursor.getString(columnIndex);
                cursor.close();
                String pass = preferences.getString("userpass", "");
                int uid = preferences.getInt("id", 0);
                Utils.modifyPortrait(uid, pass, portraitImgPath);
            }
        }
    }

    private static final int PICK_RESULT = 3;

    @Override
    public void backClick(View view) {
        finish();
    }

    public void modifyPortrait(View view) {
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
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            Bitmap bitmap = BitmapFactory.decodeFile(portraitImgPath);
            mPortrait.setImageBitmap(bitmap);
            editor.putString("portrait", data.getPortrait());
            editor.commit();
        } else {
            Utils.toast(result.getMsg(), this);
        }
    }
}
