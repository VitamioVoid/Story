package com.qf.story.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qf.story.R;
import com.qf.story.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class MyGridViewAdapter extends BaseAdapter {
    Context context;
    List<String> picList;

    public MyGridViewAdapter(Context context, List<String> picList) {
        this.context = context;
        this.picList = picList;
    }


    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public Object getItem(int position) {
        return picList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
        }
        ImageView iv = (ImageView) convertView.findViewById(R.id.mImage);
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setImageResource(R.drawable.mine_bg);
        String picPath = (String) getItem(position);
        if (picPath != null) {
            picPath = Utils.BASE_URL + Utils.BASE_IMAGE + picPath;
            Picasso.with(context).load(picPath).placeholder(R.drawable.mine_bg).into(iv);
        }
        return convertView;
    }
}
