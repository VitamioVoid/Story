package com.qf.story.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qf.story.R;
import com.qf.story.adapter.MyGridViewAdapter;
import com.qf.story.entities.CommentData;
import com.qf.story.entities.CommentList;
import com.qf.story.entities.ResponseResult;
import com.qf.story.entities.StoryData;
import com.qf.story.utils.Utils;
import com.qf.story.view.CommentListView;
import com.qf.story.view.DetailTitle;
import com.qf.story.view.MyGridView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StoryDetailActivity extends AppCompatActivity
        implements DetailTitle.OnBackClickListener,
        Utils.GetCommentCallBack,
        Utils.GetMyCommentCallBack,
        View.OnScrollChangeListener {

    private DetailTitle detailTitle;
    private CommentListView listView;
    private ScrollView mScrollView;
    private LinearLayout mLayout;
    private FrameLayout mMatchLayout;

    private ImageView mPortrait;
    private ImageView mGenderIv;
    private TextView mContentTv;
    private TextView mPlace;
    private TextView mUpTime;
    private TextView mTalkNum;
    private TextView mHeartNum;
    private TextView mCommentNum;
    private TextView mAlias;
    private EditText mCommentEt;
    private LinearLayout mCommentBar;
    private GridView mEmojiView;
    private MyGridView mGirdView;

    private MyAdapter adapter;
    private StoryData storyData = new StoryData();
    private CommentList list = new CommentList();

    private SharedPreferences preferences;
    //mCommentBar 的布局参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
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

        //获取传递过来的数据
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        storyData = (StoryData) data.getSerializable("storyData");

        Utils.readStory(storyData.getId());

        initStory();

        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        Utils.getComments(storyData.getId(), curPage);
    }

    /**
     * 初始化故事详情页数据
     */
    private void initStory() {
        String portrait = storyData.getUser().getPortrait();
        portrait = Utils.BASE_URL + Utils.BASE_PORTRAIT + portrait;
        Picasso.with(this).load(portrait).into(mPortrait);

        int usersex = storyData.getUser().getUsersex();
        mGenderIv.setImageResource(usersex == 0 ? R.drawable.icon_man : R.drawable.icon_woman);

        String timeVal = storyData.getStory_time();
        mUpTime.setText(Utils.getTime(Long.parseLong(timeVal + "000")));
        mContentTv.setText(storyData.getStory_info());
        mPlace.setText(storyData.getCity());

        String comment = storyData.getComment() + "";
        String readCount = storyData.getReadcount() + "";
        mTalkNum.setText(comment);
        mHeartNum.setText(readCount);

        String commentNumStr = "包含" + comment + "条评论";
        mCommentNum.setText(commentNumStr);
        mAlias.setText(storyData.getUser().getNickname());

        String[] pics = storyData.getPics();
        if (pics != null) {
            initContentImg(pics);
        }
    }

    private void initContentImg(String[] pics) {
        List<String> picList = new ArrayList<>();
        for (int i = 0; i < pics.length; i++) {
            picList.add(pics[i]);
        }
        mGirdView.setNumColumns(pics.length);
        MyGridViewAdapter adapter = new MyGridViewAdapter(this, picList);
        mGirdView.setAdapter(adapter);
    }

    private void setListener() {
        detailTitle.setBackClickListener(this);
        Utils.setGetCommentCallBack(this);
        Utils.setGetMyCommentCallBack(this);
        mScrollView.setOnScrollChangeListener(this);
    }

    private void initView() {
        detailTitle = (DetailTitle) findViewById(R.id.mDetailTitle);
        listView = (CommentListView) findViewById(R.id.commentList);
        detailTitle.setTitle("故事详情");

        mPortrait = (ImageView) findViewById(R.id.mPortrait);
        mGenderIv = (ImageView) findViewById(R.id.mGenderImg);
        mPortrait = (ImageView) findViewById(R.id.mPortrait);
        mTalkNum = (TextView) findViewById(R.id.talkNum);
        mHeartNum = (TextView) findViewById(R.id.heartNum);
        mContentTv = (TextView) findViewById(R.id.storyContent);
        mAlias = (TextView) findViewById(R.id.mUserAlias);
        mPlace = (TextView) findViewById(R.id.mPlace);
        mUpTime = (TextView) findViewById(R.id.mUptime);
        mCommentNum = (TextView) findViewById(R.id.commentNum);
        mCommentEt = (EditText) findViewById(R.id.commentEdit);
        mProgressBar = new ProgressBar(this);
        mScrollView = (ScrollView) findViewById(R.id.myScrollView);
        mLayout = (LinearLayout) findViewById(R.id.mLayout);
        mMatchLayout = (FrameLayout) findViewById(R.id.mMatchLayout);
        mCommentBar = (LinearLayout) findViewById(R.id.mCommentBar);
        mEmojiView = (GridView) findViewById(R.id.mEmojiView);
        mGirdView = (MyGridView) findViewById(R.id.mGridImg);
    }

    @Override
    public void backClick(View view) {
        finish();
    }

    @Override
    public void getComment(CommentList list) {//获取评论
        this.list.getData().addAll(list.getData());
        if (isAddedFooter) {
            listView.removeFooterView(mProgressBar);
            isAddedFooter = false;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getMyComment(ResponseResult result, CommentData data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            this.list.getData().add(data);
            adapter.notifyDataSetChanged();
            mCommentEt.setText("");
        } else {
            Utils.toast(result.getMsg(), this);
        }
    }

    private ProgressBar mProgressBar;
    private boolean isAddedFooter = false;


    /**
     * 当滑动到了底部的时候加载下一页评论
     *
     * @param v
     * @param scrollX
     * @param scrollY
     * @param oldScrollX
     * @param oldScrollY
     */
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //根据当前scrollView滑出的长度与页面高度的和与页面实际高度的比较来确定是否滑到了底部
        if (mLayout.getMeasuredHeight() + detailTitle.getMeasuredHeight() == mScrollView.getScrollY()
                + mMatchLayout.getMeasuredHeight() && listView.getCount() < storyData.getComment()) {
            curPage++;
            if (!isAddedFooter) {
                listView.addFooterView(mProgressBar);
                isAddedFooter = true;
            }
            Utils.getComments(storyData.getId(), curPage);
        }
    }

    private class MyAdapter extends BaseAdapter {

        private ViewHolder holder;
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.getData().size();
        }

        @Override
        public Object getItem(int position) {
            return list.getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.comment_list_item,
                        null);
                holder = new ViewHolder();
                holder.mCommentTimeTv = (TextView) convertView.findViewById(R.id.commentTime);
                holder.mCommentTv = (TextView) convertView.findViewById(R.id.mCommentCotent);
                holder.mVisitorIv = (ImageView) convertView.findViewById(R.id.mVisitorImg);
                holder.mVisitorNameTv = (TextView) convertView.findViewById(R.id.mVisitorName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CommentData data = (CommentData) getItem(position);
            holder.mVisitorNameTv.setText(data.getUser().getNickname());
            holder.mCommentTv.setText(data.getComments());
            long time = data.getTime();
            holder.mCommentTimeTv.setText(Utils.getTime(time * 1000));
            String portrait = data.getUser().getPortrait();
            portrait = Utils.BASE_URL + Utils.BASE_PORTRAIT + portrait;
            Picasso.with(StoryDetailActivity.this).load(portrait).into(holder.mVisitorIv);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView mVisitorIv;
        public TextView mVisitorNameTv;
        public TextView mCommentTv;
        public TextView mCommentTimeTv;

    }

    public void clickFace(View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                mCommentBar.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.mEmojiView);
        mEmojiView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 300));
        mCommentBar.setLayoutParams(params);

    }

    public void clickSend(View view) {//发送评论
        int uid = preferences.getInt("id", 0);
        String userpass = preferences.getString("userpass", "");
        int cid = 0;
        String comments = mCommentEt.getText().toString();
        String alias = preferences.getString("alias", "");
        String path = preferences.getString("portrait", "");
        Utils.sendComments(uid, storyData.getId(), userpass, comments, cid, alias, path);
    }

}
