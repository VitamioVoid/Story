package com.qf.story.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */
public class MyStory {
    private int result;
    private String msg;
    private List<MyStoryData> data;
    private Data user;

    public MyStory() {
        data = new ArrayList<>();
    }

    public MyStory(List<MyStoryData> data, String msg, int result, Data user) {
        this.data = data;
        this.msg = msg;
        this.result = result;
        this.user = user;
    }

    public List<MyStoryData> getData() {
        return data;
    }

    public void setData(List<MyStoryData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Data getUser() {
        return user;
    }

    public void setUser(Data user) {
        this.user = user;
    }
}
