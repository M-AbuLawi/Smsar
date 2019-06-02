package com.yasoft.smsar.adapters;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.yasoft.smsar.models.Property;
import com.yasoft.smsar.R;
//  private List<Property> arrayList;



public class DiscoverAdapter extends FirestoreRecyclerAdapter<Property, DiscoverAdapter.PropertyHolder> {


    private OnItemClickListener listener;
  //  public List<String> arrayList;

    public DiscoverAdapter(@NonNull FirestoreRecyclerOptions<Property> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull PropertyHolder holder, int position, @NonNull Property model) {


        holder.txtCity.setText(model.getmCity());
        holder.txtDesc.setText(model.getmDesc());
        holder.txtPrice.setText(model.getmPrice()+" JD");
        Picasso.get().load(model.getmImageUrl()).
                fit().placeholder(R.drawable.placeholder_image).
                error(R.drawable.no_img).into(holder.mImage);
    //    holder
        Bundle mb=new Bundle();
        mb.putInt("id",model.getmID());
        mb.putString("desc",model.getmDesc());


        // Toast.makeText(context,mb.getInt("id")+""+mb.get("desc"),Toast.LENGTH_LONG).show();

        String text = "By <font color='blue'>"+model.getmUsername()+"</font>.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtUsername.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            holder.txtUsername.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }

    @NonNull
    @Override
    public PropertyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_card,
                parent, false);
        return new PropertyHolder(v);
    }

    class PropertyHolder extends RecyclerView.ViewHolder {
        TextView txtCity, txtPrice, txtUsername,txtDesc;
        ImageView mImage;

        public PropertyHolder(View itemView) {
            super(itemView);

            txtDesc=itemView.findViewById(R.id.description);
            txtCity=itemView.findViewById(R.id.city);
            txtPrice=itemView.findViewById(R.id.price);
            txtUsername=itemView.findViewById(R.id.usernameShow);
            mImage=itemView.findViewById(R.id.mainImage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}