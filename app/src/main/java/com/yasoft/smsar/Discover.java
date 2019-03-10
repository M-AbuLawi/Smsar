package com.yasoft.smsar;



import android.content.Context;

import android.os.Bundle;
import android.app.Fragment;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.ListView;


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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        root= inflater.inflate(R.layout.fragment_discover, container, false);
        String context=root.getContext().toString();
       final Context mContext=root.getContext();

        if(context.contains("Smsar")) {
            ((SmsarMainActivity) getActivity()).navPointer(R.id.navigation_discover);
     //       UserMainActivity mUser=(UserMainActivity)mContext;
        }
        else if(context.contains("User")) {
            ((UserMainActivity) getActivity()).navPointer(R.id.navigation_discover);
        }

       //mUser.getSupportActionBar().setTitle(R.string.title_Discover);

        final Toolbar toolbar1 = (Toolbar) root.findViewById(R.id.discoverToolBar);
        toolbar1.inflateMenu(R.menu.menu_dashboard_titlebar);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });









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
