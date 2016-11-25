package com.qf.story.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.qf.story.R;
import com.qf.story.activity.StoryDetailActivity;
import com.qf.story.adapter.StoryListAdapter;
import com.qf.story.db.DBManager;
import com.qf.story.entities.StoryData;
import com.qf.story.entities.StoryList;
import com.qf.story.utils.Utils;
import com.squareup.picasso.Picasso;

public class NewFragment extends Fragment implements Utils.GetStoriesCallBack,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener, View.OnClickListener {


    private ListView mStoryList;
    private StoryListAdapter adapter;
    private ProgressBar mProgressBar;
    private FloatingActionButton refresh;
    private StoryList list = new StoryList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(curPage);
    }

    private int curPage = 1;//当前浏览页数

    private void initData(int page) {
        dbManager = new DBManager(getActivity());
        Utils.getStories("new", page);
    }

    private void initView(View view) {
        mStoryList = (ListView) view.findViewById(R.id.mStoryList);
        mProgressBar = new ProgressBar(getActivity());
        refresh = (FloatingActionButton) view.findViewById(R.id.refresh);
        mStoryList.setOnScrollListener(this);
        mStoryList.setOnItemClickListener(this);
        Utils.setGetStoriesCallBack(this);
        adapter = new StoryListAdapter(getActivity(), list);
        mStoryList.setAdapter(adapter);
        refresh.setOnClickListener(this);
    }


    //重写回调方法 获取故事信息 通知ListView刷新
    @Override
    public void getStories(StoryList list) {
        if (isFresh) {
            this.list.getData().clear();
            isFresh = false;
        }
        this.list.getData().addAll(list.getData());
        if (isAddFooter) {
            mStoryList.removeFooterView(mProgressBar);
            isAddFooter = false;
        }
        if (isAddHeader) {
            mStoryList.removeHeaderView(mProgressBar);
            isAddHeader = false;
        }
        adapter.notifyDataSetChanged();
    }

    private boolean isBottom = false;//标记是否滑到了底部
    private boolean isAddFooter = false;//标记是否已添加底部View
    private boolean isAddHeader = false;//标记是否已添加顶部View

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isBottom && scrollState == SCROLL_STATE_IDLE) {//如果滑到底部且停止滑动
            if (!isAddFooter) {
                mStoryList.addFooterView(mProgressBar);
                isAddFooter = true;
            }
            curPage++;//当前浏览页数加一
            initData(curPage);//获取新的一页的数据
        }

        if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_IDLE) {
            Picasso.with(getActivity()).pauseTag(getActivity());
        } else {
            Picasso.with(getActivity()).resumeTag(getActivity());
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isBottom = firstVisibleItem + visibleItemCount == totalItemCount;
    }

    private DBManager dbManager;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent readIntent = new Intent(getActivity(), StoryDetailActivity.class);
        StoryData storyData = list.getData().get(position);
        Bundle data = new Bundle();
        dbManager.insert(storyData);
        dbManager.closeDB();
        data.putSerializable("storyData", storyData);
        readIntent.putExtras(data);
        startActivity(readIntent);
    }

    private boolean isFresh = false;

    @Override
    public void onClick(View v) {
        initData(1);
        isFresh = true;
    }
}
