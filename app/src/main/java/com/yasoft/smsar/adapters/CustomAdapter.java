package com.yasoft.smsar.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.yasoft.smsar.DBHelper;
import com.yasoft.smsar.DeleteAlertDialog;
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


    @Override
    protected void onBindViewHolder(@NonNull PropertyHolder holder, int position, @NonNull Property model) {

        holder.txtCity.setText(model.getmCity());
        holder.txtDesc.setText(model.getmDesc());
        holder.txtPrice.setText(model.getmPrice()+" JD");

        Bundle mb=new Bundle();
        mb.putInt("id",model.getmID());
        mb.putString("desc",model.getmDesc());



        //edit delete clicks

        holder.mEdit.setOnClickListener(v -> {

            Toast.makeText(mContext ,"edit me", Toast.LENGTH_SHORT).show();
        });

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

        private PropertyHolder(View itemView) {
            super(itemView);

            txtDesc=itemView.findViewById(R.id.description);
            txtCity=itemView.findViewById(R.id.city);
            txtPrice=itemView.findViewById(R.id.price);
            mDelete=itemView.findViewById(R.id.eDelete);
            mEdit=itemView.findViewById(R.id.eEdit);

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
}