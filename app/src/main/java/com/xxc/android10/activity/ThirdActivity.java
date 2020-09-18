package com.xxc.android10.activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xxc.android10.R;
import com.xxc.android10.adapter.ThirdAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = ThirdActivity.class.getSimpleName();
    @BindView(R.id.rv_content)
    RecyclerView rvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ButterKnife.bind(this);

        List<Uri> uriList = new ArrayList<>();

        // 获取相册中的图片
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.MediaColumns.DATE_ADDED + " desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                // 将id拼接成完成的URI
                Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                uriList.add(uri);
                Log.d(TAG, "onCreate: " + uri);
            }
            cursor.close();
        }

        ThirdAdapter adapter = new ThirdAdapter(this, uriList);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        rvContent.setAdapter(adapter);
    }
}