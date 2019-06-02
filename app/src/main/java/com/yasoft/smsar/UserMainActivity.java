package com.yasoft.smsar;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.view.MenuItem;
import android.widget.TextView;

public class UserMainActivity extends AppCompatActivity {



    public void UserMainActivity(){

    }

    private TextView mTextMessage;


        FragmentManager fg=getSupportFragmentManager();
        FragmentTransaction ft=fg.beginTransaction();
        Fragment _discoverFrag=new Discover();
        BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    if(_discoverFrag.isVisible());
                    else
                    {
                        ft.replace(R.id.discoverView,_discoverFrag);
                    ft.commit();
                    }
                    return true;
                case R.id.navigation_inbox:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;

                case R.id.navigation_notifications:
                   // mTextMessage.setText(R.string.title_notifications);
                    return true;

                case R.id.navigation_discover:
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.like:
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);



         navigation = (BottomNavigationView) findViewById(R.id.user_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ft.replace(R.id.discoverView,_discoverFrag);
        ft.commit();

    }


/*
    private  <T> void fragmentLuncher(T transform){

        ft = fg.beginTransaction();
        if(_fragmentManage.isVisible())
            fragmentTransaction.remove(_fragmentManage);
        if (_fragmentSetting.isVisible())
            fragmentTransaction.remove(_fragmentSetting);
        if(_fragmentNewProperty.isVisible())
            fragmentTransaction.remove(_fragmentNewProperty);
        fragmentTransaction.replace(R.id.mainView, (android.app.Fragment) transform);
        fragmentTransaction.commit();

    }*/

        public void callPhoneNumber(String number){
         /*   Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            startActivity(intent);*/

        }

    public void navPointer(int id){
        navigation = (BottomNavigationView) findViewById(R.id.user_navigation);
        navigation.setSelectedItemId(id);

    }


    public void hideBottomNavigationView() {
        navigation.clearAnimation();
        navigation.animate().translationY(navigation.getHeight()).setDuration(300);

    }


     public void showBottomNavigationView() {
    navigation.clearAnimation();
    navigation.animate().translationY(0).setDuration(300);
}


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }




    void parseDataFilter(int rooms,int baths,boolean parking){
            Bundle mBundle=new Bundle();
        mBundle.putBoolean("parking",parking);
        mBundle.putInt("NOBathrooms",baths);
        mBundle.putInt("NORooms",rooms);

            _discoverFrag.setArguments(mBundle);

    }
}
