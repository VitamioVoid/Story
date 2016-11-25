package com.qf.story.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/26.
 */
public class Data implements Serializable{
    private int id;
    private String username;
    private String userpass;
    private int usersex;
    private String useremail;
    private String nickname;
    private String birthday;
    private String portrait;
    private String signature;

    public Data() {
    }

    public Data(String birthday, int id, String nickname, String portrait, String signature, String useremail, String username, String userpass, int usersex) {
        this.birthday = birthday;
        this.id = id;
        this.nickname = nickname;
        this.portrait = portrait;
        this.signature = signature;
        this.useremail = useremail;
        this.username = username;
        this.userpass = userpass;
        this.usersex = usersex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public int getUsersex() {
        return usersex;
    }

    public void setUsersex(int usersex) {
        this.usersex = usersex;
    }

    @Override
    public String toString() {
        return "Data{" +
                "birthday='" + birthday + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", userpass='" + userpass + '\'' +
                ", usersex='" + usersex + '\'' +
                ", useremail='" + useremail + '\'' +
                ", nickname='" + nickname + '\'' +
                ", portrait='" + portrait + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
