package com.yasoft.smsar;



import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;


import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class ShowProperty extends Fragment {

    private DBHelper mDBHelper;

    public ShowProperty() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                onDetach();
                Toast.makeText(root.getContext(),"hello",Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_setting:
                onDetach();
                Toast.makeText(root.getContext(),"hello",Toast.LENGTH_LONG).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    View root;
    Context context;
     ViewGroup actionBarLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_show_property, container, false);


        //TextView Declaration
        //START
        final TextView mPrice=(TextView)root.findViewById(R.id.price);
        final TextView mDescription=(TextView)root.findViewById(R.id.description);
        final TextView mCity=(TextView)root.findViewById(R.id.address);



        //END

        if(root.getContext().toString().contains("User")) {
            ((UserMainActivity)getActivity()).hideBottomNavigationView();

        }
        else if(root.getContext().toString().contains("Smsar"))
        {
            ((SmsarMainActivity)getActivity()).hideBottomNavigationView();
        }
        final Toolbar toolbar1 = (Toolbar) root.findViewById(R.id.discoverToolBar);
        toolbar1.inflateMenu(R.menu.menu_dashboard_titlebar);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                onDetach();
                if(root.getContext().toString().contains("User")) {
                    ((UserMainActivity)getActivity()).onBackPressed();

                }
                else if(root.getContext().toString().contains("Smsar"))
                {
                    ((SmsarMainActivity)getActivity()).onBackPressed();
                }


                mDBHelper=new DBHelper(root.getContext());
                try {

                    Cursor rs = mDBHelper.getProperty(Integer.parseInt(getArguments().get("propertyID")+""));
                    rs.moveToFirst();

                       mDescription.setText(rs.getString(rs.getColumnIndex(DBHelper.PROPERTY_COLUMN__DESCRIPTION)));
                       mPrice.setText(rs.getString(rs.getColumnIndex(DBHelper.PROPERTY_COLUMN__PRICE)));
                       mCity.setText( rs.getString(rs.getColumnIndex(DBHelper.PROPERTY_COLUMN__CITY)));

                        if (!rs.isClosed())
                            rs.close();





                }
                catch(SQLException e){
                    e.printStackTrace();
                    Toast.makeText(root.getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }



           //     Toast.makeText(root.getContext(), getArguments().get("propertyID")+"",Toast.LENGTH_LONG).show();//For Testing Purpose

            }
        });

    return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(root.getContext().toString().contains("User")) {
            ((UserMainActivity)getActivity()).showBottomNavigationView();

        }
        else if(root.getContext().toString().contains("Smsar"))
        {
            ((SmsarMainActivity)getActivity()).showBottomNavigationView();
        }

    }




}
