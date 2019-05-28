package com.yasoft.smsar.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;
import com.yasoft.smsar.DBHelper;
import com.yasoft.smsar.DeleteAlertDialog;
import com.yasoft.smsar.NewProperty;
import com.yasoft.smsar.SmsarMainActivity;
import com.yasoft.smsar.models.Property;
import com.yasoft.smsar.R;
import com.yasoft.smsar.models.Smsar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CustomAdapter extends FirestoreRecyclerAdapter<Property, CustomAdapter.PropertyHolder> {


    private Context mContext;
  //  private View mView;
    public CustomAdapter(@NonNull FirestoreRecyclerOptions<Property> options) {
        super(options);
    }

    Bundle mb;
    @Override
    protected void onBindViewHolder(@NonNull PropertyHolder holder, int position, @NonNull Property model) {

        holder.txtCity.setText(model.getmCity());
        holder.txtDesc.setText(model.getmDesc());
        holder.txtPrice.setText(model.getmPrice()+"JD");
      //  Toast.makeText(mContext,model.getmImageDrawable(),Toast.LENGTH_LONG).show();
        Picasso.get().load(model.getmImageUrl()).
                fit().placeholder(R.drawable.placeholder_image).
                error(R.drawable.no_img).into(holder.mImage);

          mb=new Bundle();
        mb.putInt("id",model.getmID());
        mb.putString("desc",model.getmDesc());


        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDetails();
           //   openDialog(position);
            }
        });
        //edit delete clicks


        holder.mDelete.setOnClickListener(v ->{
            Toast.makeText(mContext, "delete me", Toast.LENGTH_SHORT).show();
           openDialog(position);
                //deleteItem(position);

        });

    }

    @NonNull
    @Override
        public PropertyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_property_card,
                    parent, false);
         //   mView=v;
        mContext=v.getContext();
        return new PropertyHolder(v);
    }
    private void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
        notifyDataSetChanged();
    }
    class PropertyHolder extends RecyclerView.ViewHolder {
        TextView txtCity, txtPrice ,txtDesc ,mDelete,mEdit;
        ImageView mImage;
        private PropertyHolder(View itemView) {
            super(itemView);

            txtDesc=itemView.findViewById(R.id.description);
            txtCity=itemView.findViewById(R.id.city);
            txtPrice=itemView.findViewById(R.id.price);
            mDelete=itemView.findViewById(R.id.eDelete);
            mEdit=itemView.findViewById(R.id.edit);
            mImage=itemView.findViewById(R.id.mainImage);

        }
    }

    //private Boolean flag=false;
    private void openDialog(int position){

        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You will not be able to retrieve this property again!")
                .setConfirmText("Yes, delete it")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    deleteItem(position);
                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();


    }

    public void editDetails(){
        ((SmsarMainActivity)mContext).launchEditor(mb);

    }
}