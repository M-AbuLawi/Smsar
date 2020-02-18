package com.yasoft.aqarkom.adapters;


import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yasoft.aqarkom.R;

import java.util.ArrayList;

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
            populateItemRows(holder, position);

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