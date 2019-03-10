package com.yasoft.smsar.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.yasoft.smsar.DBHelper;
import com.yasoft.smsar.ShowProperty;
import com.yasoft.smsar.SmsarMainActivity;
import com.yasoft.smsar.models.Property;
import com.yasoft.smsar.R;
import com.yasoft.smsar.UserMainActivity;
import com.yasoft.smsar.models.SmsarModel;

import java.util.ArrayList;

public class DiscoverAdapter extends BaseAdapter {



    Context context;
    ArrayList<Property> arrayList;
    ArrayList<SmsarModel> smsarModels;
    DBHelper mDBHelper;
    Button mCall,mMessage;
    String pn;
    String textMessage="أود استأجار الشقة ";
    TextView txtDesc,mInfo;
     Property property;

    public DiscoverAdapter(Context context,ArrayList<Property> arrayList){

        this.context=context;
        this.arrayList=arrayList;


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {

        return arrayList.get(position);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =inflater.inflate(R.layout.property_view,null);

        TextView txtCity=(TextView)convertView.findViewById(R.id.city);

        txtDesc=(TextView)convertView.findViewById(R.id.description);

        TextView txtPrice=(TextView)convertView.findViewById(R.id.price);
        TextView txtUsername=(TextView)convertView.findViewById(R.id.usernameShow);
        TextView mCall=(TextView)convertView.findViewById(R.id.eCall);

        mDBHelper= new DBHelper(context);

         Property property=arrayList.get(position);
       final Property mProperty=arrayList.get(position);
        smsarModels=mDBHelper.getAllSmsar(property.getmUsername());

        txtCity.setText(property.getmCity());
        txtDesc.setText(property.getmDesc());
        txtPrice.setText(property.getmPrice()+" JD");
        final LikeButton likeButton =(LikeButton)convertView.findViewById(R.id.star_button);
        final MediaPlayer mp=MediaPlayer.create(context,R.raw.like);


        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                mp.start();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });


        txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDesc.setMaxLines(7);


            }
        });
        String text = "By <font color='blue'>"+property.getmUsername()+"</font>.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtUsername.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            txtUsername.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }





       mCall=(TextView) convertView.findViewById(R.id.eCall);
      //  mMessage=(Button)convertView.findViewById(R.id.eTextMessage);

        mInfo=(TextView)convertView.findViewById(R.id.description);
        ViewFlipper mImage=(ViewFlipper)convertView.findViewById(R.id.v_flipper);




            mInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { parseActivity(mProperty.getmID()); }
            });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseActivity(mProperty.getmID());
            }
        });






        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pn=mDBHelper.getPhone(mProperty.getmUsername());
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + pn));
                context.startActivity(intent);
            }

        });

 /*       mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pn=mDBHelper.getPhone(property.getmUsername());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + pn));
                intent.putExtra("sms_body", textMessage);
                context.startActivity(intent);
            }
        });*/



        return convertView;
    }


    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    public  void parseActivity (int id /*Property ID*/ ){
        FragmentManager fragmentManager =null;
        FragmentTransaction fragmentTransaction=null;
        ShowProperty mShowProperty = new ShowProperty();
        Bundle mBundle=new Bundle();
     if(context.toString().contains("User")) {
        final UserMainActivity user = (UserMainActivity) context;
         fragmentManager=user.getFragmentManager();
         fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.discoverView ,mShowProperty, "ShowProperty");


     }

     else if(context.toString().contains("Smsar"))
        {
            final SmsarMainActivity mSmsar = (SmsarMainActivity) context;
             fragmentManager=mSmsar.getFragmentManager();
             fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainView ,mShowProperty, "ShowProperty");

        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mBundle.putInt("propertyID",id);
        mShowProperty.setArguments(mBundle);
    }


}
