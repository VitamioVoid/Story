package com.qf.story.entities;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ResponseResult{
    private String msg;
    private boolean result;

    public ResponseResult() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}
