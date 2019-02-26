package com.yasoft.smsar;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.yasoft.smsar.adapters.CustomAdapter;
import com.yasoft.smsar.models.Property;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Manage extends Fragment {


    public Manage() {
        // Required empty public constructor
    }
    SharedPreferences prf;
    FragmentManager fragmentManager=getFragmentManager();
        DBHelper mDBHelper;


    ArrayList<Property> listItem;

    ListAdapter iAdapter;
    FloatingActionButton fab,_refresh;
    ListView _propertList;
    View root;
    TextView userName ;
    Property propertyModel;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//Need to get id from db and then delete record from the listview and the db;

      root = inflater.inflate(R.layout.fragment_manage, container, false);

        _propertList=(ListView)root.findViewById(R.id.propertyList);


 /*       _Fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((SmsarMainActivity)getActivity()).openAddNew();
            }
        });*/

        listItem=new ArrayList<>();
        userName=(TextView)root.findViewById(R.id.user) ;

        listData();

        ((SmsarMainActivity)getActivity()).navPointer(R.id.navigation_dashboard);
        return root;
    }




    public void listData(){

        String username = getArguments().getString("username");
       userName.setText("Hello "+username);
        mDBHelper=new DBHelper(root.getContext());
        listItem=mDBHelper.getAllProperty(username);
        iAdapter = new CustomAdapter(root.getContext(),listItem);
        _propertList.setAdapter(iAdapter);


    }


}
