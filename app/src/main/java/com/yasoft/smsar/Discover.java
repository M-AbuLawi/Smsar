package com.yasoft.smsar;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.yasoft.smsar.models.Property;
import com.yasoft.smsar.adapters.DiscoverAdapter;
import com.yasoft.smsar.models.SmsarModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Discover extends Fragment {


    public Discover() {
        // Required empty public constructor
    }
    DBHelper mDBHelper;


    ArrayList<Property> listItem;
    ArrayList<SmsarModel> smsarModels;
    ListView _propertList;
    ListAdapter iAdapter;
    Property propertyModel;
    View root;
    ViewFlipper v_flipper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_discover, container, false);
        _propertList=(ListView)root.findViewById(R.id.discoverLV);


    /*    int images[]={R.drawable.house1,R.drawable.house2, R.drawable.house3};

        v_flipper=root.findViewById(R.id.v_flipper);

    for(int image:images)
        flipperImages(image);*/
        listData();


        return root;
    }


/*    public void flipperImages(int image){
        ImageView imageView=new ImageView(root.getContext());
        imageView.setBackgroundResource(image);
    v_flipper.addView(imageView);
    v_flipper.setFlipInterval(4000);
    v_flipper.setAutoStart(true);


    v_flipper.setInAnimation(root.getContext(),android.R.anim.slide_in_left);
    v_flipper.setOutAnimation(root.getContext(),android.R.anim.slide_out_right);
    }*/
    public void listData(){

      //  String username = getArguments().getString("username");
        mDBHelper=new DBHelper(root.getContext());
        listItem=mDBHelper.getAllProperty();
        iAdapter = new DiscoverAdapter(root.getContext(),listItem);
        _propertList.setAdapter(iAdapter);


    }
}
