package com.yasoft.smsar.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yasoft.smsar.DBHelper;
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
        TextView txtDesc=(TextView)convertView.findViewById(R.id.description);
        TextView txtPrice=(TextView)convertView.findViewById(R.id.price);
      //  TextView txtUsername=(TextView)convertView.findViewById(R.id.username);
        TextView mCall=(TextView)convertView.findViewById(R.id.eCall);

        mDBHelper= new DBHelper(context);



        final Property property=arrayList.get(position);

        smsarModels=mDBHelper.getAllSmsar(property.getmUsername());

        txtCity.setText(property.getmCity());
        txtDesc.setText(property.getmDesc());
        txtPrice.setText(property.getmPrice()+" JD");
      //  txtUsername.setText(property.getmUsername());

       mCall=(TextView) convertView.findViewById(R.id.eCall);
      //  mMessage=(Button)convertView.findViewById(R.id.eTextMessage);

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pn=mDBHelper.getPhone(property.getmUsername());

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
}
