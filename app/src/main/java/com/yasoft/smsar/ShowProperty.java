package com.yasoft.smsar;



import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    String username="",pn="";
     Button mCall;
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








           //     Toast.makeText(root.getContext(), getArguments().get("propertyID")+"",Toast.LENGTH_LONG).show();//For Testing Purpose

            }
        });
        mDBHelper=new DBHelper(root.getContext());
        try {

            Cursor rs = mDBHelper.getProperty(Integer.parseInt(getArguments().get("propertyID")+""));

            rs.moveToFirst();

            mDescription.setText(rs.getString(rs.getColumnIndex(DBHelper.PROPERTY_COLUMN__DESCRIPTION)));
            mPrice.setText(rs.getString(rs.getColumnIndex(DBHelper.PROPERTY_COLUMN__PRICE)));
            mCity.setText( rs.getString(rs.getColumnIndex(DBHelper.PROPERTY_COLUMN__CITY)));
            username=rs.getString(rs.getColumnIndex(DBHelper.PROPERTY_COLUMN_SMSARUSERNAME));
            pn=mDBHelper.getPhone(username);

            if (!rs.isClosed())
                rs.close();





        }
        catch(SQLException e){
            e.printStackTrace();
            Toast.makeText(root.getContext(), e.toString(), Toast.LENGTH_LONG).show();

        }

       mCall=(Button)root.findViewById(R.id.call);
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCall.setTextColor(getResources().getColor(R.color.ms_white));
                mCall.setBackgroundColor(getResources().getColor(R.color.buttons));

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + pn));
                root.getContext().startActivity(intent);

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

    @Override
    public void onResume() {
        super.onResume();
        mCall.setTextColor(getResources().getColor(R.color.ms_black));
        mCall.setBackgroundColor(getResources().getColor(R.color.float_transparent));

    }
}
