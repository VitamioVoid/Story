package com.qf.story.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ListView;

import com.qf.story.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文混排编辑控件
 * Created by Administrator on 2016/10/28.
 */
public class ImageTextEditor extends EditText {
    private Context mContext;//上下文
    private List<String> mContentList;//文本内容

    public static final String mBitmapTag = "@";//位图标记
    private String mNewLineTag = "\n";//占行字符串

    public ImageTextEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mContentList = getMContentList();
        insertData();
    }

    /**
     * 根据编辑内容选择设置文本还是图片
     */
    private void insertData() {
        if (mContentList.size() > 0) {
            for (String str :
                    mContentList) {
                if (str.contains(mBitmapTag)) {//判断是否为图片地址
                    String path = str.replace(mBitmapTag, "");//获取图片地址
                    Bitmap bitmap = Utils.createImageThumbnail(path, 480, 480);//二次采样获取图片
                    insertBitmap(path, bitmap);//插入图片
                } else {//插入文字
                    SpannableString spannableString = new SpannableString(str);
                    append(spannableString);
                }
            }
        }
    }

    /**
     * 插入图片
     *
     * @param path
     * @param bitmap
     * @return
     */
    private SpannableString insertBitmap(String path, Bitmap bitmap) {
        Editable editText = getEditableText();
        int index = getSelectionStart();//获取光标所在位置
        SpannableString newLine = new SpannableString("\n");//插入换行符，使图片单独占一行
        editText.insert(index, newLine);//插入图片前换行
        path = mBitmapTag + path + mBitmapTag;//创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        SpannableString spannableString = new SpannableString(path);
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap);//根据Bitmap对象创建ImageSpan
        //用ImageSpan对象替换指定的字符串
        spannableString.setSpan(imageSpan, 0, path.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //将选择的图片追加到EditText中光标的所在位置
        if (index < 0 || index >= editText.length()) {
            editText.append(spannableString);
        } else {
            editText.insert(index, spannableString);
        }
        editText.insert(index, newLine);//插入图片后换行
        return spannableString;
    }


    /**
     * 插入图片
     *
     * @param path
     */
    public void insertBitmap(String path) {
        Bitmap bitmap = Utils.createImageThumbnail(path, 480, 480);
        insertBitmap(path, bitmap);
    }

    /**
     * 以集合的方式获取控件里的内容
     *
     * @return
     */
    public List<String> getMContentList() {
        if (mContentList == null) {
            mContentList = new ArrayList<>();
        }
        String content = getText().toString().replaceAll(mNewLineTag, "");
        if (content.length() > 0 && content.contains(mBitmapTag)) {
            String[] split = content.split("@");
            mContentList.clear();
            for (String str : split) {
                if (str.endsWith(".jpg") || str.endsWith(".png") || str.endsWith(".gif")) {
                    str = "@" + str + "@";
                }
                mContentList.add(str);

            }
        } else {
            mContentList.add(content);
        }
        return mContentList;
    }

    /**
     * 设置显示的集合内容
     *
     * @param contentList
     */
    private void setMContentList(List<String> contentList) {
        if (mContentList == null) {
            mContentList = new ArrayList<>();
        }
        this.mContentList.clear();
        this.mContentList.addAll(contentList);
        insertData();
    }


    private float oldY = 0;

    /**
     * 按下时获取焦点
     * 移动距离超过20的时候，取消焦点
     * 防止长图片时，自动对焦点到图片底部，无法精准定位
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getY();
                requestFocus();
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = event.getY();
                if (Math.abs(oldY - newY) > 20) {
                    clearFocus();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}

