package com.yasoft.smsar.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yasoft.smsar.DBHelper;
import com.yasoft.smsar.NewProperty;
import com.yasoft.smsar.Settings;
import com.yasoft.smsar.SmsarMainActivity;
import com.yasoft.smsar.models.Property;
import com.yasoft.smsar.R;


import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CustomAdapter extends BaseAdapter{

    Context context;
    ArrayList<Property> arrayList;
    DBHelper mDBHelper;
    SmsarMainActivity sm=new SmsarMainActivity();
    NewProperty newProperty= new NewProperty();
    public CustomAdapter(Context context,ArrayList<Property> arrayList){

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
            convertView =inflater.inflate(R.layout.single_property,null);
            TextView txtCity=(TextView)convertView.findViewById(R.id.city);
            final TextView txtDesc=(TextView)convertView.findViewById(R.id.description);
            TextView txtPrice=(TextView)convertView.findViewById(R.id.price);
            final TextView txtId=(TextView)convertView.findViewById(R.id.id);
            final Property property=arrayList.get(position);
            txtCity.setText(property.getmCity());
            txtDesc.setText(property.getmDesc());
            txtPrice.setText(property.getmPrice()+" JD");

        TextView mEdit=(TextView)convertView.findViewById(R.id.eEdit);
        TextView mDelete=(TextView)convertView.findViewById(R.id.eDelete);


        final SmsarMainActivity smsar=(SmsarMainActivity)context;


        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)throws SQLException {
                try{
                mDBHelper=new DBHelper(context.getApplicationContext());
                mDBHelper.deletePropperty(property.getmID());
                arrayList.remove(position);
                Toast.makeText(context,"Done",Toast.LENGTH_LONG).show();


            }
            catch (Exception e) {
                Toast.makeText(context,e.getMessage(), Toast.LENGTH_LONG).show();

            }
            }
        });

      mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((SmsarMainActivity)context).callSetData();
            }
        });


        return convertView;
    }




    @Override
    public int getCount() {
        return this.arrayList.size();
    }

}




