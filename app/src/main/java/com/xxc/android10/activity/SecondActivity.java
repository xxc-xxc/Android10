package com.xxc.android10.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xxc.android10.R;
import com.xxc.android10.utils.DeviceUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = SecondActivity.class.getSimpleName();
    private static final int SELECT_IMAGE = 0;
    final RxPermissions mPermissions = new RxPermissions(this);
    @BindView(R.id.iv_select_image)
    ImageView ivSelectImage;
    @BindView(R.id.btn_select_image)
    Button btnSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
//        getAudioData();
        getVideoData();

        String imei = DeviceUtil.getIMEI(this, SecondActivity.this);

//        getContacts();
    }

    /**
     * 查询联系人信息
     */
    @SuppressLint("CheckResult")
    private void getContacts() {
        new RxPermissions(this)
                .request(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                .subscribe(granted -> {
                    if (granted) {
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                ContentResolver resolver = getContentResolver();
                                Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        null, null, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        Log.d(TAG, "run: name = " + name);
                                        Log.d(TAG, "run: number = " + number);
                                    }
                                }
                            }
                        }.start();
                    } else {

                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getVideoData() {
        Observable.create((ObservableOnSubscribe<Cursor>) emitter -> {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] mediaMessage = {
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.ARTIST
            };
            Cursor cursor = contentResolver.query(uri, mediaMessage, null, null, null);
            emitter.onNext(cursor);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Cursor>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Cursor cursor) {
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        Log.d(TAG, "getAudioData: " + cursor.getString(0));
                        Log.d(TAG, "getAudioData: " + cursor.getString(1));
                        Log.d(TAG, "getAudioData: " + cursor.getString(2));
                        Log.d(TAG, "getAudioData: " + cursor.getString(3));
                        Log.d(TAG, "getAudioData: " + cursor.getString(4));
                    }
                    cursor.close();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                ContentResolver contentResolver = getContentResolver();
//                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                String[] mediaMessage = {
//                        MediaStore.Video.Media.DISPLAY_NAME,
//                        MediaStore.Video.Media.DURATION,
//                        MediaStore.Video.Media.SIZE,
//                        MediaStore.Video.Media.DATA,
//                        MediaStore.Video.Media.ARTIST
//                };
//                Cursor cursor = contentResolver.query(uri, mediaMessage, null, null, null);
//                if (cursor != null) {
//                    while (cursor.moveToNext()) {
//                        Log.d(TAG, "getAudioData: " + cursor.getString(0));
//                        Log.d(TAG, "getAudioData: " + cursor.getString(1));
//                        Log.d(TAG, "getAudioData: " + cursor.getString(2));
//                        Log.d(TAG, "getAudioData: " + cursor.getString(3));
//                        Log.d(TAG, "getAudioData: " + cursor.getString(4));
//                    }
//                    cursor.close();
//                }
//            }
//        }.start();
    }

    /**
     * 扫描手机的音频文件
     */
    private void getAudioData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ContentResolver contentResolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] mediaMess = {
                        MediaStore.Audio.Media.DISPLAY_NAME, // 视频名称
                        MediaStore.Audio.Media.DURATION, // 视频长度
                        MediaStore.Audio.Media.SIZE, // 视频大小
                        MediaStore.Audio.Media.DATA // 视频的播放地址
                };
                Cursor cursor = contentResolver.query(uri, mediaMess, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        Log.d(TAG, "getAudioData: " + cursor.getString(0));
                        Log.d(TAG, "getAudioData: " + cursor.getString(1));
                        Log.d(TAG, "getAudioData: " + cursor.getString(2));
                        Log.d(TAG, "getAudioData: " + cursor.getString(3));
                    }
                    cursor.close();
                }
            }
        }.start();
    }

    @OnClick(R.id.btn_select_image)
    public void onViewClicked() {
        checkPermission();
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

    /**
     * 打开相册
     */
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        String path = getRealPathFromUriAboveApiAndroidQ(this, data.getData());
                        displayImage(path);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        String path = getRealPathFromUriAboveApiAndroidK(this, data.getData());
                        displayImage(path);
                    } else {
                        String path = getRealPathFromUriBelowApiAndroidK(this, data.getData());
                        displayImage(path);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 适配 Android 10以上相册选取照片操作
     *
     * @param context 上下文
     * @param uri     图片uri
     * @return 图片地址
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getRealPathFromUriAboveApiAndroidQ(Context context, Uri uri) {
        Cursor cursor = null;
        String path = getRealPathFromUriAboveApiAndroidK(context, uri);
        try {
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                    new String[]{path}, null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                return String.valueOf(Uri.withAppendedPath(baseUri, "" + id));
            } else {
                // 如果图片不在手机的共享图片数据库，就先把它插入。
                if (new File(path).exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, path);
                    return String.valueOf(context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values));
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 适配Android 4.4及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getRealPathFromUriAboveApiAndroidK(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) {
                // 使用':'分割
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 适配Android 4.4以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private String getRealPathFromUriBelowApiAndroidK(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     */
    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    //展示图片
    private void displayImage(String imagePath) {
        if (imagePath != null) {
//            Bitmap bitImage = BitmapFactory.decodeFile(imagePath);//格式化图片
//            ivSelectImage.setImageBitmap(bitImage);//为imageView设置图片
//            Glide.with(this).load(imagePath).into(ivSelectImage);
            try {
                Bitmap bitmap;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(Uri.parse(imagePath), "r");
                    bitmap = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor());
                    fd.close();
                } else {
                    bitmap = BitmapFactory.decodeFile(imagePath);
                }
                ivSelectImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

}
