package com.xxc.android10.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xxc.android10.R;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Create By xxc
 * Date: 2020/9/18 15:42
 * Desc:
 */
public class ThirdAdapter extends RecyclerView.Adapter<ThirdAdapter.ThirdViewHolder> {

    private Context mContext;
    private List<Uri> mUriList;

    public ThirdAdapter(Context context, List<Uri> uriList) {
        mContext = context;
        mUriList = uriList;
    }

    @NonNull
    @Override
    public ThirdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_img, parent, false);
        return new ThirdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThirdViewHolder holder, int position) {
//        try {
//            ParcelFileDescriptor descriptor = mContext.getContentResolver().openFileDescriptor(mUriList.get(position), "r");
//            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(descriptor.getFileDescriptor());
//            holder.itemImg.setImageBitmap(bitmap);
            Glide.with(mContext)
                    .load(mUriList.get(position))
                    .into(holder.itemImg);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return mUriList.size();
    }

    public static class ThirdViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImg;

        public ThirdViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_img);
        }
    }

}
