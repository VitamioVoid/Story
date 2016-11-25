package com.qf.story.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qf.story.R;
import com.qf.story.entities.StoryData;
import com.qf.story.entities.StoryList;
import com.qf.story.utils.Utils;
import com.squareup.picasso.Picasso;

public class StoryListAdapter extends BaseAdapter {
    private ViewHolder holder;
    private Context context;
    private StoryList list;

    public StoryListAdapter(Context context, StoryList list) {
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.story_list_item, null);
            View content = convertView.findViewById(R.id.mContent);
            holder = new ViewHolder();
            holder.mContentIv = (ImageView) content.findViewById(R.id.mContentImg);
            holder.mPortraitIv = (ImageView) convertView.findViewById(R.id.mPortrait);
            holder.mUpPlaceTv = (TextView) convertView.findViewById(R.id.mPlace);
            holder.mGenderIv = (ImageView) convertView.findViewById(R.id.mGenderImg);
            holder.mUptimeTv = (TextView) convertView.findViewById(R.id.mUptime);
            holder.mUserNameTv = (TextView) convertView.findViewById(R.id.mUserAlias);
            holder.mStoryContentTv = (TextView) content.findViewById(R.id.storyContent);
            holder.mTalkNumTv = (TextView) content.findViewById(R.id.talkNum);
            holder.mHeartNumTv = (TextView) content.findViewById(R.id.heartNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoryData data = (StoryData) getItem(position);
        String portrait = data.getUser().getPortrait();
        portrait = Utils.BASE_URL + Utils.BASE_PORTRAIT + portrait;
        Picasso.with(context).load(portrait).tag(context).into(holder.mPortraitIv);
        holder.mUpPlaceTv.setText(data.getCity());
        holder.mStoryContentTv.setText(data.getStory_info());
        holder.mUserNameTv.setText(data.getUser().getNickname());
        String timeVal = data.getStory_time();
        holder.mUptimeTv.setText(Utils.getTime(Long.parseLong(timeVal + "000")));
        int usersex = data.getUser().getUsersex();
        holder.mGenderIv.setImageResource(usersex == 0 ? R.drawable.icon_man : R.drawable
                .icon_woman);
        //TextView要填入数字时得先转换为字符串
        String comment = data.getComment() + "";
        String readCount = data.getReadcount() + "";
        holder.mTalkNumTv.setText(comment);
        holder.mHeartNumTv.setText(readCount);
        holder.mContentIv.setImageResource(R.drawable.mine_bg);
        if (data.getPics() != null) {
            String[] pics = data.getPics();
            if (pics[0] != null) {
                String picPath = pics[0];
                picPath = Utils.BASE_URL + Utils.BASE_IMAGE + picPath;
                Picasso.with(context).load(picPath).tag(context).placeholder(R.drawable.mine_bg)
                        .into(holder.mContentIv);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        public ImageView mPortraitIv;
        public ImageView mGenderIv;
        public ImageView mContentIv;
        public TextView mUserNameTv;
        public TextView mUptimeTv;
        public TextView mUpPlaceTv;
        public TextView mStoryContentTv;
        public TextView mTalkNumTv;
        public TextView mHeartNumTv;
    }

}
