package com.yasoft.smsar;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewProperty extends Fragment {

    Spinner spin;
    EditText DESC,PRICE,mAddress;
    ImageButton ib;
    float price;
    ImageView iv;
    DBHelper mDBHelper;
    FloatingActionButton fab;
     View  root;
     TextView mError , mArea;
     TextView mListed;
     Context context;
    Spinner _mNumberOfRooms;
    Spinner _mNumberOfBathRooms;
    RadioButton rd;
    String date;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
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
        _mNumberOfBathRooms=(Spinner)root.findViewById(R.id.numofRooms);
        _mNumberOfRooms=(Spinner)root.findViewById(R.id.numofbathroom);
        mArea=(TextView)root.findViewById(R.id.area);
        rd=(RadioButton)root.findViewById(R.id.parking);
        mAddress=(EditText)root.findViewById(R.id.address);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(new Date());

     //   Toast.makeText(root.getContext(),date,Toast.LENGTH_LONG).show(); //   FOE TESTING PURPOSE



        ((SmsarMainActivity)getActivity()).navPointer(R.id.navigation_newApartment);

        return root;
      //  return inflater.inflate(R.layout.fragment_addproperty, container, false);

    }
    public void captureImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(root.getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(root.getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }

    }


        public void addProperty()throws SQLException{
         mDBHelper=new DBHelper(root.getContext());
       // mListed=(TextView)root.findViewById(R.id.dett);

    if (validationVariable()) {

        boolean parking=false;
        if(rd.isChecked())
            parking=true;

        boolean flag;
        try {
            flag = mDBHelper.insertProperty(getArguments().getString("username"),spin.getSelectedItem().toString(), DESC.getText()
                    .toString(), Float.valueOf(PRICE.getText().toString()),
                    Integer.parseInt(_mNumberOfRooms.getSelectedItem().toString()),
                    Integer.parseInt(_mNumberOfBathRooms.getSelectedItem().toString()),parking,mAddress.getText().toString(),date,mArea.getText().toString());
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
                TextUtils.isEmpty(PRICE.getText().toString())||
                TextUtils.isEmpty(_mNumberOfBathRooms.toString())||TextUtils.isEmpty(_mNumberOfRooms.toString()))
            return false;

        return true;

    }


    final SmsarMainActivity smsar=(SmsarMainActivity)context;




}
