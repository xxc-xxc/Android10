package com.xxc.android10.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xxc.android10.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    final RxPermissions mPermissions = new RxPermissions(this);
    @BindView(R.id.btn_select_photo)
    Button btnSelectPhoto;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.btn_go_activity)
    Button btnGoActivity;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPermissions
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {

                    } else {
                        finish();
                    }
                });

    }

    @OnClick({R.id.btn_select_photo, R.id.btn_go_activity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select_photo:
                checkPermission();
                break;
            case R.id.btn_go_activity:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }

    @SuppressLint("CheckResult")
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mPermissions.request(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            openAlbum();
                        } else {
                            Toast.makeText(this, "您取消了授权...", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            openAlbum();//有权限就打开相册
        }
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);//高于4.4版本使用此方法处理图片
                    } else {
                        handleImageBeforeKitKat(data);//低于4.4版本使用此方法处理图片
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android,providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    //获得图片路径
    public String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);   //内容提供器
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));   //获取路径
            }
        }
        cursor.close();
        return path;
    }

    //展示图片
    private void displayImage(String imagePath) {
        if (imagePath != null) {
//            Uri uri;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(imagePath));
//            } else {
//                uri = Uri.fromFile(new File(imagePath));
//            }
//            Log.d(TAG, "displayImage: " + uri.getPath());
            Bitmap bitImage = BitmapFactory.decodeFile(imagePath);//格式化图片

            ivImage.setImageBitmap(bitImage);//为imageView设置图片

        } else {
            Toast.makeText(MainActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

}
