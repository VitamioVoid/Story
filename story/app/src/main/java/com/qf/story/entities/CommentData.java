package com.qf.story.entities;

/**
 * Created by Administrator on 2016/10/27.
 */
public class CommentData {
    private int id;
    private String comments;
    private long time;
    private int sid;
    private int uid;
    private int cid;
    private Data user;

    public CommentData() {
    }

    public CommentData(int cid, String comments, int id, int sid, long time, int uid, Data user) {
        this.cid = cid;
        this.comments = comments;
        this.id = id;
        this.sid = sid;
        this.time = time;
        this.uid = uid;
        this.user = user;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
