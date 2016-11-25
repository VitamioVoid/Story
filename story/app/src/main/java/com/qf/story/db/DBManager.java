package com.qf.story.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.qf.story.entities.Data;
import com.qf.story.entities.StoryData;
import com.qf.story.entities.StoryList;

/**
 * Created by ZombieFan on 2016/10/30.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
    }

    public void insert(StoryData storyData) {
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("portrait", storyData.getUser().getPortrait());
        cv.put("alias", storyData.getUser().getNickname());
        cv.put("uptime", storyData.getStory_time());
        cv.put("story", storyData.getStory_info());
        cv.put("gender", storyData.getUser().getUsersex());
        cv.put("place", storyData.getCity());
        cv.put("commentCount", storyData.getComment());
        cv.put("readCount", storyData.getReadcount());
        String[] pics = storyData.getPics();
        if (pics != null) {
            cv.put("image", pics[0]);
        }

        long insert = db.insert(DBHelper.TABLENAME, null, cv);
        if (insert > 0) {
            Log.e("DBManager", "数据插入成功");
        } else {
            Log.e("DBManager", "数据插入失败");
        }
    }

    public StoryList getList() {
        db = helper.getReadableDatabase();
        StoryList list = new StoryList();
        String deleteSameStr = "delete from " + DBHelper.TABLENAME +
                " where _id not in (select * from ((select min(_id) from " + DBHelper.TABLENAME
                + " group by(image)) as tmptable))";
        db.execSQL(deleteSameStr);
        Cursor cursor = db.query(true, DBHelper.TABLENAME, null, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                StoryData data = new StoryData();
                String portrait = cursor.getString(cursor.getColumnIndex("portrait"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String alias = cursor.getString(cursor.getColumnIndex("alias"));
                String uptime = cursor.getString(cursor.getColumnIndex("uptime"));
                String story = cursor.getString(cursor.getColumnIndex("story"));
                String place = cursor.getString(cursor.getColumnIndex("place"));
                int gender = cursor.getInt(cursor.getColumnIndex("gender"));
                int readCount = cursor.getInt(cursor.getColumnIndex("readCount"));
                int commentCount = cursor.getInt(cursor.getColumnIndex("commentCount"));

                Data user = new Data();
                user.setNickname(alias);
                user.setUsersex(gender);
                user.setPortrait(portrait);

                data.setUser(user);
                data.setCity(place);
                data.setComment(commentCount);
                data.setReadcount(readCount);
                data.setPics(new String[]{image});
                data.setStory_time(uptime);
                data.setStory_info(story);

                list.getData().add(data);
            } while (cursor.moveToNext());
        } else {
            Log.e("查询失败", "Error");
        }
        return list;
    }

    public void closeDB() {
        if (db.isOpen()) {
            db.close();
        }
    }
}
