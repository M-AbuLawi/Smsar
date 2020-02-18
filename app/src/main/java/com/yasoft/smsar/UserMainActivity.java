package com.yasoft.aqarkom;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UserMainActivity extends AppCompatActivity {




    private TextView mTextMessage;


        FragmentManager fg=getSupportFragmentManager();
        FragmentTransaction ft=fg.beginTransaction();
        Fragment _discoverFrag=new Discover();
    Fragment likedListFrag=new LikedList();
    Fragment notificationFrag=new InboxList();
    Fragment accountFrag=new Settings();
        BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.like:
                    fragmentLauncher(likedListFrag);
                    return true;

                case R.id.navigation_discover:
                    fragmentLauncher(_discoverFrag);
                    return true;

                case R.id.navigation_inbox:
                    fragmentLauncher(notificationFrag);
                    return true;

                case R.id.navigation_seeker_account:

                    fragmentLauncher(accountFrag);
                    return true;
            }
            return false;


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        SharedPreferences preferences;
        preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        navigation =  findViewById(R.id.user_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        if (!preferences.contains("username") && !preferences.contains("password"))
                  hideBottomNavigationView();
        if(preferences.getString("userTYpe","none").contains("Seeker")) {
            final Menu menu = navigation.getMenu();
            MenuItem item = menu.getItem(3);
            item.setVisible(false);
        }

        if(!_discoverFrag.isVisible()) {
        //    navPointer(R.id.navigation_discover);

            ft.replace(R.id.discoverView,_discoverFrag);
            ft.addToBackStack(null);
            ft.commit();
        }

    }



    private   void fragmentLauncher(Fragment transform){

        ft = fg.beginTransaction();

        if(_discoverFrag.isVisible())
            ft.remove(_discoverFrag);
        if (likedListFrag.isVisible())
            ft.remove(likedListFrag);
        if (accountFrag.isVisible())
ft.remove(accountFrag);
        ft.replace(R.id.discoverView, transform);
        ft.addToBackStack(null);
        ft.commit();
    }

        public void callPhoneNumber(String number){
         /*   Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            startActivity(intent);*/

        }

    public void navPointer(int id){
        navigation =  findViewById(R.id.user_navigation);
        if(id!=0)
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
