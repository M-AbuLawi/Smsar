package com.yasoft.smsar;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
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
    BottomNavigationView navigation;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard_titlebar,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                fragmentLauncher(_fragmentNewProperty);
            //    setTitle(R.string.title_newApartment);
                return true;

            case R.id.action_setting:
                fragmentLauncher(_fragmentSetting);
           //     setTitle(R.string.title_setting);
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_dashboard:
                 fragmentLauncher(_fragmentManage);
                   // setTitle(R.string.title_dashboard);
                    return true;
                case R.id.navigation_discover:
                    fragmentLauncher(mDiscover);
                 //   setTitle(R.string.title_Discover);
                    return true;
                case R.id.navigation_newApartment:
                    fragmentLauncher(_fragmentNewProperty);
                //    setTitle(R.string.title_newApartment);
                    return true;
                case R.id.navigation_inbox:
                    fragmentLauncher(_mInbox);
                //    setTitle(R.string.title_inbox);
                    return true;
                case R.id.navigation_account:
                    fragmentLauncher(_fragmentSetting);
               //     setTitle(R.string.title_account);
                    return true;
            }
            return false;
        }
    };
    String restoredText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsar_main);




        Bundle bundle = new Bundle();

        //get username

        // START
        SharedPreferences prefs = getSharedPreferences("user_details", MODE_PRIVATE);
         restoredText = prefs.getString("username",null);
        //END

        //send username to fragments
        //START
        Toast.makeText(this,restoredText,Toast.LENGTH_LONG).show(); // FOR TESTING PURPOSES
        bundle.putString("username",restoredText);

        _fragmentManage.setArguments(bundle);
        _fragmentSetting.setArguments(bundle);
        _fragmentNewProperty.setArguments(bundle);

        //END

        mTextMessage = (TextView) findViewById(R.id.message);


         navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentTransaction.replace(R.id.mainView,_fragmentManage);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();




    }



    private  <T> void fragmentLauncher(T transform){

        fragmentTransaction = fragmentManager.beginTransaction();

        if(_fragmentManage.isVisible())
            fragmentTransaction.remove(_fragmentManage);
        if (_fragmentSetting.isVisible())
            fragmentTransaction.remove(_fragmentSetting);
        if(_fragmentNewProperty.isVisible()){
         //   setListener(_fragmentNewProperty);
            fragmentTransaction.remove(_fragmentNewProperty);}

        fragmentTransaction.replace(R.id.mainView, (android.app.Fragment) transform);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void logout(){
        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);;
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

        this.finish();


    }

    public void openAddNew(){
        if(_fragmentNewProperty.isAdded())
        {
        //Do Nothing
        }
        else
        fragmentLauncher(_fragmentNewProperty);

    }


    public void navPointer(int id){
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
}
