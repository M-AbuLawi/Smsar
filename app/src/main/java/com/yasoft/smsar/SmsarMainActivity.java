package com.yasoft.smsar;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class SmsarMainActivity extends AppCompatActivity {

    private TextView mTextMessage;


    FragmentManager fragmentManager= getFragmentManager();
    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

    NewProperty _fragmentNewProperty =new NewProperty();
    Manage _fragmentManage=new Manage();
    Discover mDiscover=new Discover();
    Settings _fragmentSetting=new Settings();
    Fragment fragment;
    Inbox _mInbox=new Inbox();
    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                 fragmentLuncher(_fragmentManage);
                    return true;
                case R.id.navigation_discover:
                    fragmentLuncher(mDiscover);
                    return true;
                case R.id.navigation_notifications:
                    fragmentLuncher(_fragmentNewProperty);
                    return true;
                case R.id.navigation_inbox:
                    fragmentLuncher(_mInbox);
                    return true;
                case R.id.navigation_account:
                    fragmentLuncher(_fragmentSetting);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        setContentView(R.layout.activity_smsar_main);

        SharedPreferences prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        String restoredText = prefs.getString("username",null);



        Toast.makeText(this,restoredText,Toast.LENGTH_LONG).show();
        bundle.putString("username",restoredText);

        _fragmentManage.setArguments(bundle);
        _fragmentSetting.setArguments(bundle);
        _fragmentNewProperty.setArguments(bundle);
        mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentTransaction.replace(R.id.mainView,_fragmentManage);
        fragmentTransaction.commit();



    }

    private  <T> void fragmentLuncher(T transform){

        fragmentTransaction = fragmentManager.beginTransaction();
        if(_fragmentManage.isVisible())
            fragmentTransaction.remove(_fragmentManage);
        if (_fragmentSetting.isVisible())
            fragmentTransaction.remove(_fragmentSetting);
        if(_fragmentNewProperty.isVisible())
            fragmentTransaction.remove(_fragmentNewProperty);
        fragmentTransaction.replace(R.id.mainView, (android.app.Fragment) transform);
        fragmentTransaction.commit();

    }
    public void logout(){
        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);;
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();


    }

    public void openAddNew(){
        if(_fragmentNewProperty.isAdded())
        {
        //Do Nothing
        }
        else
        fragmentLuncher(_fragmentNewProperty);

    }



}
