package com.qf.story.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.qf.story.R;
import com.qf.story.entities.Data;
import com.qf.story.entities.GeoData;
import com.qf.story.entities.ResponseResult;
import com.qf.story.utils.Utils;
import com.qf.story.view.ImageTextEditor;
import com.qf.story.view.NewStoryTitle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NewStoryActivity extends AppCompatActivity implements
        NewStoryTitle.OnBackClickListener,
        View.OnClickListener,
        NewStoryTitle.OnSendClickListener,
        Utils.GetGeoDataCallBack, Utils.ResponseCallBack {

    private NewStoryTitle newStoryTitle;
    private ImageTextEditor imageTextEditor;
    private Button mGetPicBtn;
    private Button mImageCapture;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private List<File> pics;//发送故事的图片文件
    private StringBuffer myStoryContent;//发送的故事
    // 所需的全部权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_story);
        init();
    }

    public void getPermissions(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission}, 1);
        } else {
            getLatLng();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLatLng();
            } else {
                Utils.toast("Permission Denied", this);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getLatLng() {
        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 200, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    Utils.getGeoCity(lat, lng);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        initView();
        getPermissions(PERMISSIONS[0]);
        setListener();
        initData();
    }

    private void initData() {
        preferences = getSharedPreferences("login", MODE_PRIVATE);
        editor = preferences.edit();
        pics = new ArrayList<>();
        myStoryContent = new StringBuffer();
    }

    private void setListener() {
        newStoryTitle.setBackClickListener(this);
        newStoryTitle.setSendClickListener(this);
        mGetPicBtn.setOnClickListener(this);
        mImageCapture.setOnClickListener(this);
        Utils.setGetGeoDataCallBack(this);
        Utils.setResponseCallBack(this);
    }

    private void initView() {
        newStoryTitle = (NewStoryTitle) findViewById(R.id.newStoryTitle);
        newStoryTitle.setTitle("新的故事");
        imageTextEditor = (ImageTextEditor) findViewById(R.id.mImageTextEt);
        mGetPicBtn = (Button) findViewById(R.id.mGetPicBtn);
        mImageCapture = (Button) findViewById(R.id.imageCapture);
    }

    @Override
    public void backClick(View view) {
        finish();
    }

    private static final int PICK_RESULT = 1;
    private static final int CAPTURE_RESULT = 2;


    /**
     * 从相册选择图片
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mGetPicBtn:
                Intent getPicIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(getPicIntent, PICK_RESULT);
                break;
            case R.id.imageCapture:
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, CAPTURE_RESULT);
                } else {
                    Utils.toast("sdCard不可用", this);
                }
                break;
        }
    }

    /**
     * 获取从相册选择的图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_RESULT && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                String picPath = cursor.
                        getString(cursor.getColumnIndex(filePathColumn[0]));
                pics.add(new File(picPath));//添加选择的图片文件
                cursor.close();
                imageTextEditor.insertBitmap(picPath);
            }
        }
        if (requestCode == CAPTURE_RESULT && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null || data.getExtras() != null) {
                Uri uri = data.getData();
                if (uri == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        getCaptureImg(bundle);
                    }
                }
            }
        }
    }

    private void getCaptureImg(Bundle bundle) {
        Bitmap bitmap = (Bitmap) bundle.get("data");
        if (bitmap != null) {
            String filePath = Environment.getExternalStorageDirectory()
                    + "/story/img/";
            String imgName = System.currentTimeMillis() / 1000000 + ".jpg";
            String picPath = filePath + imgName;
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(picPath);
            try {
                FileOutputStream fout = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                pics.add(new File(picPath));
                imageTextEditor.insertBitmap(picPath);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送故事
     *
     * @param view
     */
    @Override
    public void sendClick(View view) {
        setMyStoryContent();
        String userpass = preferences.getString("userpass", "");
        int id = preferences.getInt("id", 0);
        String latStr = preferences.getString("lat", "");
        String lngStr = preferences.getString("lng", "");
        double lat;
        double lng;
        if (latStr.equals("") && lngStr.equals("")) {
            lat = 0;
            lng = 0;
        } else {
            lat = Double.parseDouble(latStr);
            lng = Double.parseDouble(lngStr);
        }
        String city = preferences.getString("city", "");
        Utils.sendStory(id, myStoryContent.toString(), pics, userpass, lat, lng, city);
    }

    private void setMyStoryContent() {
        List<String> mContentList = imageTextEditor.getMContentList();
        for (String str : mContentList) {//获取编辑控件中的文本内容
            if (mContentList.size() > 0 || mContentList.contains(ImageTextEditor.mBitmapTag)) {
                if (!str.contains("@")) {//过滤掉图片地址
                    myStoryContent.append(str);
                }
            }
        }
    }

    @Override
    public void getGeoData(GeoData data) {
        editor.putString("lat", data.getLat() + "");
        editor.putString("lng", data.getLng() + "");
        editor.putString("city", data.getCity());
        editor.commit();
    }

    @Override
    public void getResponse(ResponseResult result, Data data) {
        if (result.isResult()) {
            Utils.toast(result.getMsg(), this);
            finish();
        } else {
            Utils.toast(result.getMsg(), this);
        }
    }
}
