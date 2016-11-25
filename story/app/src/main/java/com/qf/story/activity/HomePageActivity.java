package com.qf.story.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.qf.story.R;
import com.qf.story.fragment.HotFragment;
import com.qf.story.fragment.NewFragment;
import com.qf.story.view.HomePageTitle;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements
        HomePageTitle.OnBackClickListener, RadioGroup.OnCheckedChangeListener,
        SlidingPaneLayout.PanelSlideListener, HomePageTitle.OnEditClickListener {

    private HomePageTitle mHomePageTitle;
    private List<Fragment> fragmentList;
    private SlidingPaneLayout mSlidingPaneLayout;
    private boolean openOrNot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        init();
    }

    private void init() {
        initView();
        initData();
        setListener();
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new NewFragment());
        fragmentList.add(new HotFragment());
        showFragment(0);
    }

    private void setListener() {
        mHomePageTitle.setBackClickListener(this);
        mHomePageTitle.setCheckedChangeListener(this);
        mHomePageTitle.setEditClickListener(this);
        mSlidingPaneLayout.setPanelSlideListener(this);
    }

    private void initView() {
        mSlidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.mSlidingPaneLayout);
        mHomePageTitle = (HomePageTitle) findViewById(R.id.mHomePageTitle);
    }

    @Override
    public void backClick(View view) {
        if (openOrNot == false) {
            mSlidingPaneLayout.openPane();
        } else {
            mSlidingPaneLayout.closePane();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton rb = (RadioButton) group.getChildAt(i);
            if (rb.isChecked()) {
                showFragment(i);
                break;
            }
        }
    }

    private int lastShowIndex = -1;

    private void showFragment(int i) {
        Fragment fragment = fragmentList.get(i);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (-1 != lastShowIndex) {
            Fragment lastFragment = fragmentList.get(lastShowIndex);
            fragmentTransaction.hide(lastFragment);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.container, fragment);
        }

        fragmentTransaction.commit();
        lastShowIndex = i;

    }

    public void clickPane(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.oldStory:
                mSlidingPaneLayout.closePane();
                return;
            case R.id.mRecord:
                intent = new Intent(HomePageActivity.this, RecordActivity.class);
                break;
            case R.id.myStory:
                intent = new Intent(HomePageActivity.this, MyStoryActivity.class);
                break;
            case R.id.sysSettings:
                intent = new Intent(HomePageActivity.this, SettingActivity.class);
                break;
            case R.id.personalInfo:
                intent = new Intent(HomePageActivity.this, PersonalCenterActivity.class);
                break;
        }
        startActivity(intent);
    }

    public void exit(View view) {
        finish();
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {
        openOrNot = true;
    }

    @Override
    public void onPanelClosed(View panel) {
        openOrNot = false;
    }

    @Override
    public void editClick(View view) {
        Intent intent = new Intent(HomePageActivity.this, NewStoryActivity.class);
        startActivity(intent);
    }
}
