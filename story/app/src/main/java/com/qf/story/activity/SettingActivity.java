package com.qf.story.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qf.story.R;
import com.qf.story.view.InfoText;
import com.qf.story.view.StoryTitle;

public class SettingActivity extends AppCompatActivity implements StoryTitle.OnBackClickListener {

    private StoryTitle mSettingTitle;
    private InfoText encourage;
    private InfoText donation;
    private InfoText message;
    private InfoText update;
    private InfoText question;
    private InfoText about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        initView();
        setListener();
    }

    private void setListener() {
        mSettingTitle.setBackClickListener(this);
    }

    private void initView() {
        mSettingTitle = (StoryTitle) findViewById(R.id.mSettingTitle);
        encourage = (InfoText) findViewById(R.id.encourage);
        about = (InfoText) findViewById(R.id.about);
        message = (InfoText) findViewById(R.id.message);
        update = (InfoText) findViewById(R.id.update);
        question = (InfoText) findViewById(R.id.question);
        donation = (InfoText) findViewById(R.id.donation);

        mSettingTitle.setTitle("设   置");
        encourage.setTitle("鼓励我们");
        donation.setTitle("捐助我们");
        message.setTitle("系统消息");
        update.setTitle("检查更新");
        question.setTitle("常见问题");
        about.setTitle("关于我们");
    }

    @Override
    public void backClick(View view) {
        finish();
    }
}
