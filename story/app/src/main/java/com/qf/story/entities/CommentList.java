package com.qf.story.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */
public class CommentList {
    private int result;
    private String msg;
    private List<CommentData> data;

    public CommentList() {
        data = new ArrayList<>();
    }

    public CommentList(List<CommentData> data, String msg, int result) {
        this.data = data;
        this.msg = msg;
        this.result = result;
    }

    public List<CommentData> getData() {
        return data;
    }

    public void setData(List<CommentData> data) {
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
}
