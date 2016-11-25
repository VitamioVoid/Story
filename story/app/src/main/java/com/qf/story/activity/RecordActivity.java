package com.qf.story.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.qf.story.R;
import com.qf.story.adapter.StoryListAdapter;
import com.qf.story.db.DBManager;
import com.qf.story.entities.StoryData;
import com.qf.story.entities.StoryList;
import com.qf.story.view.RecordTitle;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements RecordTitle.OnBackClickListener, RecordTitle.OnEditClickListener, AdapterView.OnItemClickListener {

    private RecordTitle mRecordTitle;
    private ListView mContentList;
    private StoryListAdapter adapter;
    private StoryList list;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        init();
    }

    private void init() {
        initView();
        setListener();
        initData();
    }

    private void initData() {
        dbManager = new DBManager(this);
        list = dbManager.getList();
        getReverse();
        adapter = new StoryListAdapter(this, this.list);
        mContentList.setAdapter(adapter);
        dbManager.closeDB();
    }

    private void getReverse() {
        List<StoryData> data = list.getData();
        List<StoryData> reverse = new ArrayList<>();
        for (int i = data.size() - 1; i >= 0; i--) {
            reverse.add(data.get(i));
        }
        list.getData().clear();
        list.getData().addAll(reverse);
    }

    private void setListener() {
        mRecordTitle.setBackClickListener(this);
        mRecordTitle.setEditClickListener(this);
        mContentList.setOnItemClickListener(this);
    }

    private void initView() {
        mRecordTitle = (RecordTitle) findViewById(R.id.mRecordTitle);
        mContentList = (ListView) findViewById(R.id.mContentList);

        mRecordTitle.setTitle("浏览记录");
    }

    @Override
    public void backClick(View view) {
        finish();
    }

    @Override
    public void editClick(View view) {
        Intent intent = new Intent(RecordActivity.this, NewStoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent readIntent = new Intent(RecordActivity.this, StoryDetailActivity.class);
        StoryData storyData = list.getData().get(position);
        Bundle data = new Bundle();
        data.putSerializable("storyData", storyData);
        readIntent.putExtras(data);
        startActivity(readIntent);
    }
}
