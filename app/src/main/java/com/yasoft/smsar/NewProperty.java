package com.yasoft.smsar;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yasoft.smsar.models.Property;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewProperty extends Fragment {


    Spinner spin;
    EditText DESC,PRICE;
    ImageButton ib;
    float price;
    ImageView iv;
    DBHelper mDBHelper;
    FloatingActionButton fab;
     View  root;
     TextView mError;
     TextView mListed;
     Context context;
    public NewProperty() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_addproperty, container, false);
        DESC=(EditText) root.findViewById(R.id.description);
        PRICE=(EditText) root.findViewById(R.id.price);
        ib=(ImageButton)root.findViewById(R.id.addImage);
        iv=(ImageView)root.findViewById(R.id.showImage);
        spin=(Spinner)root.findViewById(R.id.cities);
        fab=(FloatingActionButton)root.findViewById(R.id.addProperty);
        mError=(TextView)root.findViewById(R.id.mError);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                addProperty();

            }
        });
        ib.setOnClickListener(new View.OnClickListener() {
    @Override
               public void onClick(View v) {
                 captureImage();
                    }
            });



        return root;
      //  return inflater.inflate(R.layout.fragment_addproperty, container, false);

    }
    public void captureImage(){
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 2020 );

            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, 2020);
        }
        catch (Exception e)
        {
            Toast.makeText(root.getContext(),"No Image",Toast.LENGTH_LONG).show();

        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode==2020)
        {
            Bundle ext=data.getExtras();
            if(ext!=null){
            Bitmap imv=(Bitmap) ext.get("data");iv.setImageBitmap(imv);
            iv.getLayoutParams().height = 650;
            iv.getLayoutParams().width = 650;
        }
        }
    }

    public void addProperty()throws SQLException{
         mDBHelper=new DBHelper(root.getContext());
       // mListed=(TextView)root.findViewById(R.id.dett);

    if (validationVariable()) {
        boolean flag;
        try {
            flag = mDBHelper.insertProperty(getArguments().getString("username"),spin.getSelectedItem().toString(), DESC.getText()
                    .toString(), Float.valueOf(PRICE.getText().toString()));
            if (flag) {
                Toast.makeText(root.getContext(), "done",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(root.getContext(), "not done",
                        Toast.LENGTH_SHORT).show();


            }


                // printData(spin.getSelectedItem().toString());
            //
            //mListed.setText(mDBHelper.getDataCity(spin.getSelectedItem().toString()).toString());
        } catch (SQLException e) {
            e.printStackTrace();


           }

        }
        else{
        mError.setText("Empty Fields");
        mError.setVisibility(View.VISIBLE);
    }
    }

    public boolean validationVariable () {
        if (TextUtils.isEmpty(spin.getSelectedItem().toString()) || TextUtils.isEmpty(DESC.getText().toString()) ||
                TextUtils.isEmpty(PRICE.getText().toString()))
            return false;

        return true;

    }
/*
        public void printData(String location){
            ArrayList<Property> listed=new ArrayList();
            listed=mDBHelper.getAllProperty(location);

     *//*   for (int i=0;i<5;i++){

            mListed.setText(listed.get(i)+"\t");

        }*//*


        }*/
}
