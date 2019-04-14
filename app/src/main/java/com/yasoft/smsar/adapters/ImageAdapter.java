package com.yasoft.smsar.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yasoft.smsar.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.HorizontalViewHolder> {

    private ArrayList<Bitmap> items;

    public ImageAdapter(ArrayList<Bitmap> items) {

        this.items = items;

    }


    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.browse_images, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder holder, int position) {
        //holder.image.setImageResource(items.get(position));
            populateItemRows((HorizontalViewHolder) holder, position);

    }

    @Override
    public int getItemCount() {
        return 1;
    }
    ImageView image;
    public class HorizontalViewHolder extends RecyclerView.ViewHolder {


        public HorizontalViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.preview);
        } }

    private void populateItemRows(HorizontalViewHolder viewHolder, int position) {

        image.setImageBitmap(Bitmap.createScaledBitmap(items.iterator().next(), 150, 150, true));



    }
}