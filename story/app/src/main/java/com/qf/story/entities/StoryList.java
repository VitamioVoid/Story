package com.qf.story.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class StoryList {
    private int result;
    private String msg;
    private List<StoryData> data;

    public StoryList() {
        this.data = new ArrayList<>();
    }

    public StoryList(List<StoryData> data, String msg, int result) {
        this.data = data;
        this.msg = msg;
        this.result = result;
    }

    public List<StoryData> getData() {
        return data;
    }

    public void setData(List<StoryData> data) {
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

    @Override
    public String toString() {
        return "StoryList{" +
                "data=" + data +
                ", result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }
}
