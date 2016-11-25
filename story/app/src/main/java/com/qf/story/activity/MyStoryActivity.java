package com.qf.story.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qf.story.R;
import com.qf.story.entities.Data;
import com.qf.story.entities.MyStory;
import com.qf.story.entities.MyStoryData;
import com.qf.story.entities.StoryData;
import com.qf.story.utils.Utils;
import com.qf.story.view.CommentListView;
import com.qf.story.view.MyStoryTitle;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyStoryActivity extends AppCompatActivity
        implements MyStoryTitle.OnBackClickListener,
        Utils.GetMyStoriesCallBack,
        MyStoryTitle.OnEditClickListener,
        AdapterView.OnItemClickListener,
        View.OnScrollChangeListener {

    private MyStoryTitle myStoryTitle;
    private CommentListView myStoryList;
    private ImageView mPortrait;
    private TextView mSignature;
    private ImageView mGenderImg;
    private TextView mTag;
    private TextView mAlias;

    private ScrollView myScrollView;
    private LinearLayout mRootLayout;
    private LinearLayout mLayout;

    private RelativeLayout mStoryCover;
    private MyAdapter adapter;
    private MyStory myStory = new MyStory();
    private SharedPreferences preferences;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);
        init();
    }

    private void init() {
        initView();
        setListener();
        initData();
    }

    private int curPage = 1;

    private void initData() {
        preferences = getSharedPreferences("login", MODE_PRIVATE);

        String alias = preferences.getString("alias", "");
        mAlias.setText(alias);

        String portrait = preferences.getString("portrait", "");
        portrait = Utils.BASE_URL + Utils.BASE_PORTRAIT + portrait;
        Picasso.with(this).load(portrait).placeholder(R.drawable.icon_portrait).into(mPortrait);

        String signature = preferences.getString("signature", "");
        mSignature.setText(signature);

        //获取生日计算年代标签
        String birthday = preferences.getString("birthday", "");
        if (!birthday.equals("")) {
            String tag = Utils.getGenerationTag(birthday);
            mTag.setText(tag);
        } else {
            Utils.toast("请完善个人信息", this);
            Intent intent = new Intent(MyStoryActivity.this, PersonalCenterActivity.class);
            startActivity(intent);
        }

        int gender = preferences.getInt("gender", 0);
        mGenderImg.setImageResource(gender == 0 ? R.drawable.icon_man : R.drawable.icon_woman);


        adapter = new MyAdapter(this);
        myStoryList.setAdapter(adapter);
        id = preferences.getInt("id", 0);
        Utils.getMyStories(id, curPage);
    }

    private void setListener() {
        myStoryTitle.setBackClickListener(this);
        myStoryTitle.setEditClickListener(this);
        myStoryList.setOnItemClickListener(this);
        Utils.setGetMyStoriesCallBack(this);
    }

    private void initView() {
        myStoryTitle = (MyStoryTitle) findViewById(R.id.myStoryTitle);
        myStoryList = (CommentListView) findViewById(R.id.myStoryList);
        mAlias = (TextView) findViewById(R.id.mUserAlias);
        mGenderImg = (ImageView) findViewById(R.id.mGenderImg);
        mTag = (TextView) findViewById(R.id.mineTag);
        mSignature = (TextView) findViewById(R.id.mSignature);
        mPortrait = (ImageView) findViewById(R.id.mPortrait);
        mStoryCover = (RelativeLayout) findViewById(R.id.storyCover);

        myScrollView = (ScrollView) findViewById(R.id.myScrollView);
        myScrollView.setOnScrollChangeListener(this);
        //scrollTo方法要等到界面显示完毕才能有效，而view.post方法也是在界面刷新完毕之后才执行的。
        myScrollView.post(new Runnable() {
            @Override
            public void run() {
                myScrollView.smoothScrollTo(0, 0);
            }
        });

        mRootLayout = (LinearLayout) findViewById(R.id.mRootLayout);
        mLayout = (LinearLayout) findViewById(R.id.mLayout);

        mProgressBar = new ProgressBar(this);

        myStoryTitle.setTitle("我的故事");
    }

    @Override
    public void backClick(View view) {
        finish();
    }

    @Override
    public void getMyStories(MyStory myStory) {
        if (myStory.getResult() == 1) {
            Utils.toast(myStory.getMsg(), this);
            List<MyStoryData> data = myStory.getData();
            if (isAddedFooter) {
                myStoryList.removeFooterView(mProgressBar);
                isAddedFooter = false;
            }
            this.myStory.getData().addAll(data);
            if (this.myStory.getData().size() == 0) {//没有数据，使用Cover覆盖提示无数据
                mStoryCover.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void editClick(View view) {
        Intent intent = new Intent(MyStoryActivity.this, NewStoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MyStoryActivity.this, StoryDetailActivity.class);
        StoryData storyData = makeStoryData(myStory.getData().get(position));
        Bundle data = new Bundle();
        data.putSerializable("storyData", storyData);
        intent.putExtras(data);
        startActivity(intent);
    }

    private StoryData makeStoryData(MyStoryData myStoryData) {
        StoryData storyData = new StoryData();

        storyData.setComment(myStoryData.getComment());
        storyData.setId(myStoryData.getId());
        storyData.setPics(myStoryData.getPics());
        storyData.setStory_info(myStoryData.getStory_info());
        storyData.setStory_time(myStoryData.getStory_time() + "");
        storyData.setReadcount(myStoryData.getReadcount());

        Data data = new Data();
        data.setPortrait(preferences.getString("portrait", ""));
        data.setNickname(preferences.getString("alias", ""));
        data.setUsersex(preferences.getInt("gender", 0));

        storyData.setUser(data);
        return storyData;
    }

    private ProgressBar mProgressBar;
    private boolean isAddedFooter = false;

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (myScrollView.getScrollY() + mRootLayout.getMeasuredHeight() ==
                mLayout.getMeasuredHeight() + myStoryTitle.getMeasuredHeight()) {
            curPage++;
            if (!isAddedFooter) {
                myStoryList.addFooterView(mProgressBar);
                isAddedFooter = true;
            }
            Utils.getMyStories(id, curPage);
        }

    }

    private class MyAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder holder;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return myStory.getData().size();
        }

        @Override
        public Object getItem(int position) {
            return myStory.getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.mystory_list_item,
                        null);
                holder = new ViewHolder();
                View content = convertView.findViewById(R.id.mContent);
                holder.mTalkNum = (TextView) content.findViewById(R.id.talkNum);
                holder.mHeartNum = (TextView) content.findViewById(R.id.heartNum);
                holder.storyContent = (TextView) content.findViewById(R.id.storyContent);
                holder.storyContImg = (ImageView) content.findViewById(R.id.mContentImg);
                holder.mStoryDate = (TextView) convertView.findViewById(R.id.storyDate);
                holder.mStoryTime = (TextView) convertView.findViewById(R.id.storyTime);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MyStoryData data = (MyStoryData) getItem(position);
            String comment = data.getComment() + "";
            String readCount = data.getReadcount() + "";
            holder.mTalkNum.setText(comment);
            holder.mHeartNum.setText(readCount);
            String dateTime = Utils.getDateTime(data.getStory_time() * 1000);
            String[] split = dateTime.split("@");
            holder.mStoryDate.setText(split[0]);
            holder.mStoryTime.setText(split[1]);
            holder.storyContent.setText(data.getStory_info());
            holder.storyContImg.setImageResource(R.drawable.mine_bg);
            String[] pics = data.getPics();
            if (pics != null) {
                String picPath = pics[0];
                picPath = Utils.BASE_URL + Utils.BASE_IMAGE + picPath;
                Picasso.with(MyStoryActivity.this).load(picPath).
                        tag(MyStoryActivity.this).placeholder(R.drawable.mine_bg)
                        .into(holder.storyContImg);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView mStoryTime;
        public TextView mStoryDate;
        public TextView storyContent;
        public ImageView storyContImg;
        public TextView mTalkNum;
        public TextView mHeartNum;
    }

    private static final int SIGN_MODIFY = 5;

    public void modifySignature(View view) {
        Intent intent = new Intent(MyStoryActivity.this, ModifySignatureActivity.class);
        startActivityForResult(intent, SIGN_MODIFY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_MODIFY && resultCode == RESULT_OK) {
            System.out.println(preferences.getString("signature", ""));
            mSignature.setText(preferences.getString("signature", ""));
        }
    }

    /**
     * 点击头像
     *
     * @param view
     */
    public void clickPortrait(View view) {
        Intent intent = new Intent(MyStoryActivity.this, PersonalCenterActivity.class);
        startActivity(intent);
    }
}
