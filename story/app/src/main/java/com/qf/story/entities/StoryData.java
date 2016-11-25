package com.qf.story.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/26.
 */
public class StoryData implements Serializable{
    private int id;
    private String story_time;
    private String story_info;
    private String[] pics;
    private int uid;
    private String lng;
    private String lat;
    private String city;
    private int readcount;
    private int comment;
    private Data user;

    public StoryData() {
    }

    public StoryData(String city, int comment, int id, String lat, String lng, String[] pics, int readcount, String story_info, String story_time, int uid, Data user) {
        this.city = city;
        this.comment = comment;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.pics = pics;
        this.readcount = readcount;
        this.story_info = story_info;
        this.story_time = story_time;
        this.uid = uid;
        this.user = user;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }

    public int getReadcount() {
        return readcount;
    }

    public void setReadcount(int readcount) {
        this.readcount = readcount;
    }

    public String getStory_info() {
        return story_info;
    }

    public void setStory_info(String story_info) {
        this.story_info = story_info;
    }

    public String getStory_time() {
        return story_time;
    }

    public void setStory_time(String story_time) {
        this.story_time = story_time;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Data getUser() {
        return user;
    }

    public void setUser(Data user) {
        this.user = user;
    }
}
