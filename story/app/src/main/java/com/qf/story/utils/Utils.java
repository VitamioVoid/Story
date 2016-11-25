package com.qf.story.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.callback.StringCallback;
import com.qf.story.entities.CommentData;
import com.qf.story.entities.CommentList;
import com.qf.story.entities.Data;
import com.qf.story.entities.GeoData;
import com.qf.story.entities.MyStory;
import com.qf.story.entities.ResponseResult;
import com.qf.story.entities.StoryList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class Utils {
    //Story的数据和请求接口
    public static final String BASE_URL = "http://139.129.19.51/story";
    public static final String BASE_INTERFACE = "/index.php/home/Interface/";
    //获取图片
    public static final String BASE_IMAGE = "/Uploads/";
    //获取头像
    public static final String BASE_PORTRAIT = "/Uploads/portrait/";
    public static final String INTERFACE_LOGIN = "login";
    public static final String INTERFACE_REGIST = "regist";
    public static final String INTERFACE_SENDSTORY = "sendStory";
    public static final String INTERFACE_GETSTORIES = "getStorys";
    public static final String INTERFACE_READSTORIES = "readStorys";
    public static final String INTERFACE_MYSTORIES = "myStorys";
    public static final String INTERFACE_MODIFYNICKNAME = "changeNickName";
    public static final String INTERFACE_MODIFYSEX = "changeSex";
    public static final String INTERFACE_MODIFYEMAIL = "changeEmail";
    public static final String INTERFACE_MODIFYPWD = "changePassword";
    public static final String INTERFACE_MODIFYBIRTHDAY = "changeBirthday";
    public static final String INTERFACE_MODIFYSIGN = "changeSignature";
    public static final String INTERFACE_MODIFYPORTRAIT = "changePortrait";
    public static final String INTERFACE_SENDCOMMENTS = "sendComment";
    public static final String INTERFACE_GETCOMMENTS = "getComments";

    //百度提供的地理编码API
    public static final String BAIDU_GEOCODING = "http://api.map.baidu.com/geocoder/v2/";
    //密钥
    public static final String API_KEY = "NxosjwYAlBcGeGnLGPNIMK1llvTQlGC2";

    /**
     * 图片的二次采样
     * @param filePath
     * @param newHeight
     * @param newWidth
     * @return
     */
    public static Bitmap createImageThumbnail(String filePath,
                                              int newHeight, int newWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int oldHeight = options.outHeight;
        int oldWidth = options.outWidth;

        int ratioHeight = oldHeight / newHeight;
        int ratioWidth = oldWidth / newWidth;

        options.inSampleSize = ratioHeight > ratioWidth ? ratioWidth : ratioHeight;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    /**
     * 判断字符串是否为中文
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符是否为中文
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 根据经纬度逆地理编码获取地址
     *
     * @param lat 纬度
     * @param lng 经度
     */
    public static void getGeoCity(double lat, double lng) {
        OkHttpUtils.get(BAIDU_GEOCODING)
                .params("ak", API_KEY)
                .params("location", lat + "," + lng)
                .params("output", "json")
                .params("coordtype", "wgs84ll")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s.trim());
                            if (json.getInt("status") == 0) {
                                JSONObject result = json.getJSONObject("result");
                                String city = result.getJSONObject("addressComponent").getString("city");
                                double lat = result.optJSONObject("location").optDouble("lat");
                                double lng = result.optJSONObject("location").optDouble("lng");
                                GeoData data = new GeoData(city, lat, lng);
                                getGeoDataCallBack.getGeoData(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 注册请求
     * @param userName
     * @param password
     * @param alias
     * @param portrait
     */
    public static void regist(String userName, String password
            , String alias, String portrait) {
        File portraitFile = new File(portrait);
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_REGIST)
                .params("nikename", alias)
                .params("username", userName)
                .params("password", password)
                .params("portrait", portraitFile)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            ResponseResult result = new ResponseResult();
                            JSONObject json = new JSONObject(s.trim());
                            result.setMsg(json.getString("msg"));
                            result.setResult(json.getInt("result") == 1);
                            responseCallBack.getResponse(result, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 登录请求
     * @param userName
     * @param password
     */
    public static void login(String userName, String password) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_LOGIN)
                .params("username", userName)
                .params("password", password)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            ResponseResult result = new ResponseResult();
                            JSONObject json = new JSONObject(s.trim());
                            result.setMsg(json.getString("msg"));
                            if (json.getInt("result") == 1) {
                                result.setResult(true);
                                Data data = new Data();
                                System.out.println(s);
                                JSONObject dataJson = json.getJSONObject("data");
                                data.setId(dataJson.optInt("id"));
                                data.setUsername(dataJson.optString("username"));
                                data.setUserpass(dataJson.optString("userpass"));
                                data.setUsersex(dataJson.optInt("usersex"));
                                data.setUseremail(dataJson.optString("useremail"));
                                data.setNickname(dataJson.optString("nickname"));
                                data.setBirthday(dataJson.optString("birthday"));
                                data.setPortrait(dataJson.optString("portrait"));
                                data.setSignature(dataJson.optString("signature"));
                                responseCallBack.getResponse(result, data);
                            } else {
                                result.setResult(false);
                                responseCallBack.getResponse(result, null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 修改昵称
     * @param uid
     * @param pass
     * @param alias
     */
    public static void modifyAlias(int uid, String pass, String alias) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_MODIFYNICKNAME)
                .params("uid", uid)
                .params("userpass", pass)
                .params("nickname", alias)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s.trim());
                            ResponseResult result = new ResponseResult();
                            Data data = new Data();
                            result.setResult(json.optInt("result") == 1);
                            result.setMsg(json.optString("msg"));
                            data.setNickname(json.optString("data"));
                            responseCallBack.getResponse(result, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 修改性别
     * @param uid
     * @param pass
     * @param gender
     */
    public static void modifyGender(int uid, String pass, int gender) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_MODIFYSEX)
                .params("uid", uid)
                .params("userpass", pass)
                .params("usersex", gender)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s.trim());
                            ResponseResult result = new ResponseResult();
                            Data data = new Data();
                            result.setResult(json.optInt("result") == 1);
                            result.setMsg(json.optString("msg"));
                            data.setUsersex(Integer.parseInt(json.optString("data")));
                            responseCallBack.getResponse(result, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 修改生日
     * @param uid
     * @param pass
     * @param birthday
     */
    public static void modifyBirthday(int uid, String pass, String birthday) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_MODIFYBIRTHDAY)
                .params("uid", uid)
                .params("userpass", pass)
                .params("birthday", birthday)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s.trim());
                            ResponseResult result = new ResponseResult();
                            Data data = new Data();
                            result.setResult(json.optInt("result") == 1);
                            result.setMsg(json.optString("msg"));
                            data.setBirthday(json.optString("data"));
                            responseCallBack.getResponse(result, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 修改Email
     * @param uid
     * @param pass
     * @param email
     */
    public static void modifyEmail(int uid, String pass, String email) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_MODIFYEMAIL)
                .params("uid", uid)
                .params("userpass", pass)
                .params("useremail", email)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s.trim());
                            ResponseResult result = new ResponseResult();
                            Data data = new Data();
                            result.setResult(json.optInt("result") == 1);
                            result.setMsg(json.optString("msg"));
                            data.setUseremail(json.optString("data"));
                            responseCallBack.getResponse(result, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 修改密码
     * @param uid
     * @param oldpwd
     * @param newpwd
     */
    public static void modifyPassword(int uid, String oldpwd, String newpwd) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_MODIFYPWD)
                .params("uid", uid)
                .params("oldpass", oldpwd)
                .params("newpass", newpwd)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s.trim());
                            ResponseResult result = new ResponseResult();
                            Data data = new Data();
                            result.setResult(json.optInt("result") == 1);
                            result.setMsg(json.optString("msg"));
                            data.setUserpass(json.optString("data"));
                            responseCallBack.getResponse(result, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 修改头像
     * @param uid
     * @param userpass
     * @param portraitPath
     */
    public static void modifyPortrait(int uid, String userpass, String portraitPath) {
        File file = new File(portraitPath);
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_MODIFYPORTRAIT)
                .params("uid", uid)
                .params("userpass", userpass)
                .params("portrait", file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s.trim());
                            ResponseResult result = new ResponseResult();
                            Data data = new Data();
                            result.setResult(json.optInt("result") == 1);
                            result.setMsg(json.optString("msg"));
                            data.setPortrait(json.optString("data"));
                            responseCallBack.getResponse(result, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 修改签名
     * @param uid
     * @param userpass
     * @param signature
     */
    public static void modifySignature(int uid, String userpass, String signature) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_MODIFYSIGN)
                .params("uid", uid)
                .params("userpass", userpass)
                .params("signature", signature)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s.trim());
                            ResponseResult result = new ResponseResult();
                            result.setMsg(json.optString("msg"));
                            result.setResult(json.optInt("result") == 1);
                            Data data = new Data();
                            data.setSignature(json.optString("data"));
                            responseCallBack.getResponse(result, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 获取故事列表
     * @param type
     * @param page
     */
    public static void getStories(String type, int page) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_GETSTORIES)
                .params("type", type)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        StoryList storyList = gson.fromJson(s, StoryList.class);
                        getStoriesCallBack.getStories(storyList);
                    }
                });
    }

    /**
     * 阅读故事
     * @param sid
     */
    public static void readStory(int sid) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_READSTORIES)
                .params("sid", sid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                    }
                });
    }

    /**
     * 获取评论
     * @param sid
     * @param page
     */
    public static void getComments(int sid, int page) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_GETCOMMENTS)
                .params("sid", sid)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        CommentList commentList = new Gson().fromJson(s.trim(), CommentList.class);
                        getCommentCallBack.getComment(commentList);
                    }
                });
    }

    /**
     * 发送评论
     * @param uid
     * @param sid
     * @param userpass
     * @param comments
     * @param cid
     * @param alias
     * @param portrait
     */
    public static void sendComments(int uid, int sid, String userpass, String
            comments, int cid, final String alias, final String portrait) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_SENDCOMMENTS)
                .params("uid", uid)
                .params("sid", sid)
                .params("userpass", userpass)
                .params("comments", comments)
                .params("cid", cid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s.trim());
                            CommentData data = new CommentData();
                            ResponseResult result = new ResponseResult();
                            result.setResult(json.optInt("result") == 1);
                            result.setMsg(json.optString("msg"));
                            data.setSid(json.getJSONObject("data").optInt("sid"));
                            data.setCid(json.getJSONObject("data").optInt("cid"));
                            data.setTime(json.getJSONObject("data").optLong("time"));
                            data.setId(json.getJSONObject("data").optInt("id"));
                            data.setComments(json.getJSONObject("data").optString("comments"));
                            data.setTime(json.getJSONObject("data").optLong("time"));

                            Data user = new Data();
                            user.setNickname(alias);
                            user.setPortrait(portrait);

                            data.setUser(user);

                            getMyCommentCallBack.getMyComment(result, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 发送故事
     * @param uid
     * @param storyInfo
     * @param pics
     * @param userpass
     * @param lat
     * @param lng
     * @param city
     */
    public static void sendStory(int uid, String storyInfo, List<File> pics, String userpass,
                                 double lat, double lng, String city) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_SENDSTORY)
                .params("uid", uid)
                .params("story_info", storyInfo)
                .addFileParams("photo[]", pics)
                .params("userpass", userpass)
                .params("lat", lat)
                .params("lng", lng)
                .params("city", city)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            System.out.println(s);
                            JSONObject json = new JSONObject(s.trim());
                            ResponseResult result = new ResponseResult();
                            result.setResult(json.getInt("result") == 1);
                            result.setMsg(json.getString("msg"));
                            responseCallBack.getResponse(result, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取我的故事
     * @param uid
     * @param page
     */
    public static void getMyStories(int uid, int page) {
        OkHttpUtils.post(BASE_URL + BASE_INTERFACE + INTERFACE_MYSTORIES)
                .params("uid", uid)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyStory myStory = new Gson().fromJson(s.trim(), MyStory.class);
                        getMyStoriesCallBack.getMyStories(myStory);
                    }
                });
    }

    /**
     * 将毫秒数转换为上传 需求与当前时间的差
     * 根据实际返回上传时间的
     * @param timeVal
     * @return
     */
    public static String getTime(long timeVal) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = new Date(timeVal);
        String dateTime = simpleDateFormat.format(date);

        long curMilliSec = System.currentTimeMillis();
        long val = curMilliSec - timeVal;
        long second = val / 1000 % 60;
        long minute = val / 1000 / 60 % 60;
        long hour = val / 1000 / 3600 % 24;
        long day = val / 1000 / 3600 / 24;
        if (hour > 0 && day == 0) {
            return "于" + hour + "小时前上传";
        } else if (day > 0) {
            return "于" + dateTime + "上传";
        } else if (minute > 0 && hour == 0) {
            return "于" + minute + "分钟前上传";
        } else if (minute == 0 && second > 0) {
            return "于" + second + "秒前上传";
        }
        return null;
    }

    /**
     * 获取完整日期
     * @param timeVal
     * @return
     */
    public static String getDateTime(long timeVal) {
        SimpleDateFormat simpleDateFormat = new
                SimpleDateFormat("yyyy.MM月@dd日 hh:mm", Locale.CHINA);
        Date date = new Date(timeVal);
        return simpleDateFormat.format(date);
    }

    /**
     * 根据生日信息 得到叉叉后标签
     * @param birthday
     * @return
     */
    public static String getGenerationTag(String birthday) {
        String[] split = birthday.split("-");
        int year = Integer.parseInt(split[0]);
        if (year >= 1980 && year < 1990) {
            return "80后";
        } else if (year < 2000 && year >= 1990) {
            return "90后";
        } else if (year > 2000) {
            return "00后";
        }
        return "老司机";
    }

    //以下全是传递解析后json数据的接口

    private static GetGeoDataCallBack getGeoDataCallBack = null;

    public static void setGetGeoDataCallBack(GetGeoDataCallBack callBack) {
        getGeoDataCallBack = callBack;
    }

    public interface GetGeoDataCallBack {
        void getGeoData(GeoData data);
    }

    public static void setGetMyStoriesCallBack(GetMyStoriesCallBack callBack) {
        getMyStoriesCallBack = callBack;
    }

    private static GetMyStoriesCallBack getMyStoriesCallBack = null;

    public interface GetMyStoriesCallBack {
        void getMyStories(MyStory myStory);
    }

    public static void setGetMyCommentCallBack(GetMyCommentCallBack callBack) {
        getMyCommentCallBack = callBack;
    }

    private static GetMyCommentCallBack getMyCommentCallBack = null;

    public interface GetMyCommentCallBack {
        void getMyComment(ResponseResult result, CommentData data);
    }

    public static void setGetCommentCallBack(GetCommentCallBack CallBack) {
        getCommentCallBack = CallBack;
    }

    private static GetCommentCallBack getCommentCallBack = null;

    public interface GetCommentCallBack {
        void getComment(CommentList list);
    }

    private static ResponseCallBack responseCallBack = null;

    public static void setResponseCallBack(ResponseCallBack callBack) {
        responseCallBack = callBack;
    }

    private static GetStoriesCallBack getStoriesCallBack = null;

    public static void setGetStoriesCallBack(GetStoriesCallBack getStoriesCallBack) {
        Utils.getStoriesCallBack = getStoriesCallBack;
    }

    public interface GetStoriesCallBack {
        void getStories(StoryList list);
    }

    public interface ResponseCallBack {
        void getResponse(ResponseResult result, Data data);
    }

    public static void toast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
