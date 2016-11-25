package com.qf.story.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qf.story.R;

public class LaunchActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        launch();
    }

    /**
     * 启动主页或者登陆页面
     */
    private void launch() {
        preferences = getSharedPreferences("login", MODE_PRIVATE);
        final Intent homePageIntent = new Intent
                (LaunchActivity.this, HomePageActivity.class);
        final Intent loginIntent = new Intent(LaunchActivity.this,
                LoginActivity.class);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            if (preferences.getBoolean("hasLogin", false)) {
                                startActivity(homePageIntent);//如果已经登录过，启动主页
                            } else {
                                startActivity(loginIntent); //没有登录，启动登录页面
                            }
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }
}
